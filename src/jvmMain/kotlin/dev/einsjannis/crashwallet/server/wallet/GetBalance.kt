package dev.einsjannis.crashwallet.server.wallet

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import dev.einsjannis.crashwallet.server.bscscanApiKey
import dev.einsjannis.crashwallet.server.etherscanioApiKey
import dev.einsjannis.crashwallet.server.exceptions.UnknownAddressTypeException
import dev.einsjannis.crashwallet.server.json.BCHBalanceObj
import dev.einsjannis.crashwallet.server.json.EtherscanBalanceResponse
import dev.einsjannis.crashwallet.server.json.ThetaResponse
import dev.einsjannis.crashwallet.server.json.TronscanAccountObject
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
//TODO: Litecoin Balance
private fun getLitecoinBalance(ltcaddress: String): Double {
	//return URL("https://insight.litecore.io/api/addr/$ltcaddress/balance").readText().toDouble() / 100000000
	return 0.0
}

private fun getBitcoinCashBalance(bchaddress: String): Double {
	val response = URL("https://api.fullstack.cash/v4/electrumx/balance/$bchaddress").readText()
	val mapper = jacksonObjectMapper()
	return mapper.readValue<BCHBalanceObj>(response).balance.confirmed.toDouble()
}
//TODO: Zcash Balance
private fun getZcashBalance(zecaddress: String): Double {
	//val response = URL("https://zecblockexplorer.com/api/v2/address/$zecaddress").readText()
	//println(response)
	return 0.0
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
//TODO: Nano Balance
private fun getNanoBalance(nanoaddress: String): Double {
	return 0.0
}
//TODO: Reddcoin Balance
private fun getReddcoinBalance(rddaddress: String): Double {
	//return URL("https://live.reddcoin.com/api/addr/$rddaddress/balance").readText().toDouble() / 100000000
	return 0.0
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
