package dev.einsjannis.crashwallet.server.wallet

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.mysql.cj.exceptions.CJCommunicationsException
import dev.einsjannis.crashwallet.server.*
import dev.einsjannis.crashwallet.server.json.CoingeckoPriceInfo
import dev.einsjannis.crashwallet.server.logger.log
import dev.einsjannis.crashwallet.server.logger.mainLogger
import dev.einsjannis.crashwallet.server.wallet.currencies.currencies
import java.io.FileNotFoundException
import java.io.IOException
import java.sql.Connection
import kotlin.math.round

data class BalanceAndAddress(val balance: Double, val address: String)

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
				ex.printStackTrace()
				mainLogger.log("$short Balance query failed (IOException)")
			}
		}else{
			result[short] = BalanceAndAddress(0.0, "")
		}
	}
	con.close()
	return result
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

fun getPrices(jsonResponse: String): CoingeckoPriceInfo {
	val mapper = jacksonObjectMapper()
	return mapper.readValue(jsonResponse)
}
