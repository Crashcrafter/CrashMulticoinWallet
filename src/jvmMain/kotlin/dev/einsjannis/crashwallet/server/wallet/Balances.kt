package dev.einsjannis.crashwallet.server.wallet

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import dev.einsjannis.crashwallet.server.bscscanApiKey
import dev.einsjannis.crashwallet.server.etherscanioApiKey
import dev.einsjannis.crashwallet.server.json.BCHBalanceObj
import dev.einsjannis.crashwallet.server.json.EtherscanBalanceResponse
import dev.einsjannis.crashwallet.server.json.ThetaResponse
import dev.einsjannis.crashwallet.server.json.TronscanBalanceObject
import java.net.URL

fun getBitcoinBalance(btcaddress: String): Double {
	return URL("https://blockchain.info/q/addressbalance/$btcaddress?confirmations=3").readText().toDouble()/100000000.0
}

fun getEthereumBalance(ethaddress: String): Double {
	val response = URL("https://api.etherscan.io/api?module=account&action=balance&address=$ethaddress&tag=latest&apikey=$etherscanioApiKey").readText()
	val mapper = jacksonObjectMapper()
	return mapper.readValue<EtherscanBalanceResponse>(response).result.toDouble()
}
//Not in use, will be used later
fun getERC20TokenBalance(ethaddress: String, contract: String) : Double {
	val response = URL("https://api.etherscan.io/api?module=account&action=tokenbalance&contractaddress=$contract&address=$ethaddress&tag=latest&apikey=$etherscanioApiKey").readText()
	val mapper = jacksonObjectMapper()
	return mapper.readValue<EtherscanBalanceResponse>(response).result.toDouble()
}

fun getSmartChainBalance(bscaddress: String): Double {
	val response = URL("https://api.bscscan.com/api?module=account&action=balance&address=$bscaddress&tag=latest&apikey=$bscscanApiKey").readText()
	val mapper = jacksonObjectMapper()
	return mapper.readValue<EtherscanBalanceResponse>(response).result.toDouble()
}

fun getTronBalance(tronaddress: String): Double {
	val response = URL("https://apilist.tronscan.org/api/account?address=$tronaddress").readText()
	val mapper = jacksonObjectMapper()
	return mapper.readValue<TronscanBalanceObject>(response).balance.toDouble() / 100000
}
//TODO: Litecoin Balance
fun getLitecoinBalance(ltcaddress: String): Double {
	//return URL("https://insight.litecore.io/api/addr/$ltcaddress/balance").readText().toDouble() / 100000000
	return 0.0
}

fun getBitcoinCashBalance(bchaddress: String): Double {
	val response = URL("https://api.fullstack.cash/v4/electrumx/balance/$bchaddress").readText()
	val mapper = jacksonObjectMapper()
	return mapper.readValue<BCHBalanceObj>(response).balance.confirmed.toDouble()
}
//TODO: Zcash Balance
fun getZcashBalance(zecaddress: String): Double {
	//val response = URL("https://zecblockexplorer.com/api/v2/address/$zecaddress").readText()
	//println(response)
	return 0.0
}

fun getDashBalance(dashaddress: String): Double {
	return URL("https://explorer.dash.org/insight-api/addr/$dashaddress/balance").readText().toDouble() / 100000000
}

fun getDogeBalance(dogeaddress: String): Double {
	return URL("https://dogechain.info/chain/Dogecoin/q/addressbalance/$dogeaddress").readText().toDouble()
}

fun getDigibyteBalance(dgbaddress: String): Double {
	return URL("https://digiexplorer.info/api/addr/$dgbaddress/balance").readText().toDouble() / 100000000
}
//TODO: Nano Balance
fun getNanoBalance(nanoaddress: String): Double {
	return 0.0
}
//TODO: Reddcoin Balance
fun getReddcoinBalance(rddaddress: String): Double {
	//return URL("https://live.reddcoin.com/api/addr/$rddaddress/balance").readText().toDouble() / 100000000
	return 0.0
}

fun getTFuelBalance(tfueladdress: String): Double {
	val response = URL("https://explorer.thetatoken.org:9000/api/account/$tfueladdress").readText()
	val mapper = jacksonObjectMapper()
	val obj = mapper.readValue<ThetaResponse>(response)
	return obj.body.balance.tfuelwei.toDouble() / 1000000000000000000
}

fun getThetaBalance(thetaaddress: String): Double {
	val response = URL("https://explorer.thetatoken.org:9000/api/account/$thetaaddress").readText()
	val mapper = jacksonObjectMapper()
	val obj = mapper.readValue<ThetaResponse>(response)
	return obj.body.balance.thetawei.toDouble() / 1000000000000000000
}
