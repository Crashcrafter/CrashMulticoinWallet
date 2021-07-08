package de.crash.crashwallet.wallet

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import de.crash.crashwallet.bscscanApiKey
import de.crash.crashwallet.etherscanioApiKey
import de.crash.crashwallet.exceptions.UnknownAddressTypeException
import de.crash.crashwallet.get
import de.crash.crashwallet.json.BCHBalanceObj
import de.crash.crashwallet.json.EtherscanBalanceResponse
import de.crash.crashwallet.json.ThetaResponse
import de.crash.crashwallet.json.TronscanAccountObject
import dev.crash.address.AddressType
import java.net.URL

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
			AddressType.DASH -> getDashBalance(address)
			AddressType.DOGE -> getDogeBalance(address)
			AddressType.DGB -> getDigibyteBalance(address)
			AddressType.RDD -> getReddcoinBalance(address)
			else -> throw UnknownAddressTypeException()
		}
		return BalanceAndAddress(resultval, address)
	}else{
		return BalanceAndAddress(0.0, "")
	}
}

private fun getBitcoinBalance(btcaddress: String): Double {
	return URL("https://blockchain.info/q/addressbalance/$btcaddress?confirmations=3").readText().toDouble()/100000000.0
}

private fun getEthereumBalance(ethaddress: String): Double {
	val response = URL("https://api.etherscan.io/api?module=account&action=balance&address=$ethaddress&tag=latest&apikey=$etherscanioApiKey").readText()
	val mapper = jacksonObjectMapper()
	return mapper.readValue<EtherscanBalanceResponse>(response).result.toDouble()
}
//Not in use, will be used later
private fun getERC20TokenBalance(ethaddress: String, contract: String) : Double {
	val response = URL("https://api.etherscan.io/api?module=account&action=tokenbalance&contractaddress=$contract&address=$ethaddress&tag=latest&apikey=$etherscanioApiKey").readText()
	val mapper = jacksonObjectMapper()
	return mapper.readValue<EtherscanBalanceResponse>(response).result.toDouble()
}

private fun getSmartChainBalance(bscaddress: String): Double {
	val response = URL("https://api.bscscan.com/api?module=account&action=balance&address=$bscaddress&tag=latest&apikey=$bscscanApiKey").readText()
	val mapper = jacksonObjectMapper()
	return mapper.readValue<EtherscanBalanceResponse>(response).result.toDouble()
}

private fun getTronBalance(tronaddress: String): Double {
	val response = URL("https://apilist.tronscan.org/api/account?address=$tronaddress").readText()
	val mapper = jacksonObjectMapper()
	return mapper.readValue<TronscanAccountObject>(response).balance.toDouble() / 100000
}

private fun getLitecoinBalance(ltcaddress: String): Double {
	return URL("https://insight.litecore.io/api/addr/$ltcaddress/balance").get().toDouble() / 100000000
}

private fun getBitcoinCashBalance(bchaddress: String): Double {
	val response = URL("https://api.fullstack.cash/v4/electrumx/balance/$bchaddress").readText()
	val mapper = jacksonObjectMapper()
	return mapper.readValue<BCHBalanceObj>(response).balance.confirmed.toDouble()
}

private fun getZcashBalance(zecaddress: String): Double {
	val response = URL("https://zecblockexplorer.com/api/v2/address/$zecaddress").get()
	val obj = jacksonObjectMapper().readTree(response)
	return obj["balance"].asDouble() / 100000000
}

private fun getDashBalance(dashaddress: String): Double {
	return URL("https://explorer.dash.org/insight-api/addr/$dashaddress/balance").readText().toDouble() / 100000000
}

private fun getDogeBalance(dogeaddress: String): Double {
	return URL("https://dogechain.info/chain/Dogecoin/q/addressbalance/$dogeaddress").readText().toDouble()
}

private fun getDigibyteBalance(dgbaddress: String): Double {
	return URL("https://digiexplorer.info/api/addr/$dgbaddress/balance").readText().toDouble() / 100000000
}

private fun getNanoBalance(nanoaddress: String): Double {
	return 0.0
}

private fun getReddcoinBalance(rddaddress: String): Double {
	return URL("https://live.reddcoin.com/api/addr/$rddaddress/balance").get().toDouble() / 100000000
}

private fun getTFuelBalance(tfueladdress: String): Double {
	val response = URL("https://explorer.thetatoken.org:9000/api/account/$tfueladdress").readText()
	val mapper = jacksonObjectMapper()
	val obj = mapper.readValue<ThetaResponse>(response)
	return obj.body.balance.tfuelwei.toDouble() / 1000000000000000000
}

private fun getThetaBalance(thetaaddress: String): Double {
	val response = URL("https://explorer.thetatoken.org:9000/api/account/$thetaaddress").readText()
	val mapper = jacksonObjectMapper()
	val obj = mapper.readValue<ThetaResponse>(response)
	return obj.body.balance.thetawei.toDouble() / 1000000000000000000
}
