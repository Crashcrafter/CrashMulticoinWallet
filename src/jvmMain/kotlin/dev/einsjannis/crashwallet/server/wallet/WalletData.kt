package dev.einsjannis.crashwallet.server.wallet

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.mysql.cj.exceptions.CJCommunicationsException
import dev.einsjannis.crashwallet.server.*
import dev.einsjannis.crashwallet.server.exceptions.UnknownAddressTypeException
import dev.einsjannis.crashwallet.server.json.CoingeckoPriceInfo
import dev.einsjannis.crashwallet.server.logger.log
import dev.einsjannis.crashwallet.server.logger.mainLogger
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.FileNotFoundException
import java.io.IOException
import java.net.URL
import java.sql.Connection
import kotlin.math.round

data class StaticCurrencyInfo(val name: String, val short: String, val img: String, val explorerLink: String)

data class CurrencyInfo(val name: String, val short: String, val img: String, val explorerLink: String, val currentprice: Double, val daychange: Double,
						val dayvolume: Double, val marketcap: Double)

data class BalanceAndAddress(val balance: Double, val address: String)

val currencies = mutableListOf<StaticCurrencyInfo>()
val currencylist = HashMap<String, CurrencyInfo>()
val order = mutableListOf<String>()

fun updatePrices(){
	updateStaticInfo()
	GlobalScope.launch {
		while(true) {
			val finalcurrencyinfos = HashMap<String, CurrencyInfo>()
			var queryString = ""
			currencies.forEach {
				queryString += it.name.toQueryName() + ","
			}
			queryString.dropLast(1)
			val response = URL("https://api.coingecko.com/api/v3/simple/price?ids=$queryString&vs_currencies=usd&include_market_cap=true&include_24hr_vol=true&include_24hr_change=true").readText()
			val readObj = getPrices(response)
			currencies.forEach {
				val valueobj = readObj[it.name.toQueryName()]
				if (valueobj != null) {
					finalcurrencyinfos[it.short] = CurrencyInfo(it.name, it.short, it.img, it.explorerLink, valueobj.usd.round(6),
						valueobj.usd_24h_change.round(2), valueobj.usd_24h_vol.round(0), valueobj.usd_market_cap.round(0))
				}else {
					println(it.name.toQueryName() + " is null")
				}
			}
			currencylist.clear()
			currencylist.putAll(finalcurrencyinfos)
			delay(10000)
		}
	}
}

fun updateStaticInfo(){
	val staticCurrencyInfos = mutableListOf<StaticCurrencyInfo>()
	order.clear()
	transaction {
		CurrencyTable.selectAll().forEach {
			//TODO: Enable Theta when bug is found
			if(it[CurrencyTable.short].toString() != "theta"){
				order.add(it[CurrencyTable.short])
				staticCurrencyInfos.add(StaticCurrencyInfo(it[CurrencyTable.name], it[CurrencyTable.short], it[CurrencyTable.img], it[CurrencyTable.explorerLink]))
			}
		}
	}
	currencies.clear()
	currencies.addAll(staticCurrencyInfos)
}

fun getBalances(userid: Int) : HashMap<String, BalanceAndAddress>{
	val result = HashMap<String, BalanceAndAddress>()
	val con = getnewDBConnection()
	currencies.forEach {
		val short = it.short
		val type = AddressType.valueOf(short.toAddressTypeString())
		val address = getAddress(userid, type, con)
		if(address != ""){
			try {
				result[short] = getBalance(address, type)
			}catch (ex: FileNotFoundException){
				mainLogger.log("$short Balance query failed (FileNotFoundException, api endpoint not found!)")
			}catch (ex: IOException) {
				mainLogger.log("$short Balance query failed (IOException: Http 502, Bad Gateway)")
			}
		}else{
			result[short] = BalanceAndAddress(0.0, "")
		}
	}
	con.close()
	return result
}

fun String.toAddressTypeString() : String {
	var string = this.toUpperCase()
	//For Theta later
	return string
}

fun getBalance(userid: Int, type: AddressType) : BalanceAndAddress = getBalance(getAddress(userid, type), type)

fun getBalance(address: String, type: AddressType) : BalanceAndAddress{
	if(address != ""){
		val resultval: Double = when(type){
			AddressType.BTC -> getBitcoinBalance(address)
			AddressType.ETH -> getEthereumBalance(address)
			AddressType.BNB -> getSmartChainBalance(address)
			AddressType.TRX -> getTronBalance(address)
			AddressType.LTC -> getLitecoinBalance(address)
			AddressType.BCH -> getBitcoinCashBalance(address)
			AddressType.ZEC -> getZcashBalance(address)
			AddressType.DASH -> getDashBalance(address)
			AddressType.DOGE -> getDogeBalance(address)
			AddressType.DGB -> getDigibyteBalance(address)
			AddressType.NANO -> getNanoBalance(address)
			AddressType.RDD -> getReddcoinBalance(address)
			AddressType.THETA -> getThetaBalance(address)
			AddressType.TFUEL -> getTFuelBalance(address)
			else -> throw UnknownAddressTypeException()
		}
		return BalanceAndAddress(resultval, address)
	}else{
		return BalanceAndAddress(0.0, "")
	}
}

fun getAddress(userid: Int, type: AddressType) : String{
	val con = getnewDBConnection()
	val result = getAddress(userid, type, con)
	con.close()
	return result
}

fun getAddress(userid:Int, type: AddressType, connection: Connection) : String{
	try {
		var typename = type.name
		if(typename == "TFUEL") typename = "THETA"
		val queryString = "SELECT $typename FROM wallets WHERE id=$userid"
		val statement = connection.prepareStatement(queryString)
		statement.execute()
		val set = statement.resultSet!!
		if(set.next()) {
			return set.getString(type.name) ?: ""
		}
		return ""
	}catch (ex: CJCommunicationsException){
		connection.close()
		return getAddress(userid, type)
	}
}

fun Double.round(decimals: Int): Double {
	var multiplier = 1.0
	repeat(decimals) { multiplier *= 10 }
	return round(this * multiplier) / multiplier
}

fun Double.toReadableString() : String {
	var value = round(0)
	var count = 0
	while (value.round(0).toLong().toString().length > 3){
		count++
		value = (value / 1000).round(2)
	}
	var suffix = ""
	when(count){
		0 -> suffix = ""
		1 -> suffix = "K"
		2 -> suffix = "M"
		3 -> suffix = "B"
		4 -> suffix = "T"
	}
	return "$value$suffix"
}

fun String.toQueryName() : String {
	var result = this.toLowerCase().replace(' ', '-')
	if(result.contains("smart-chain")){
		result = "binancecoin"
	}
	return result
}

fun getPrices(jsonResponse: String): CoingeckoPriceInfo {
	val mapper = jacksonObjectMapper()
	return mapper.readValue(jsonResponse)
}
