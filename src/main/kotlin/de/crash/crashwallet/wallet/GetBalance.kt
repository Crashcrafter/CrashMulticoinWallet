package de.crash.crashwallet.wallet

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import de.crash.crashwallet.bscscanApiKey
import de.crash.crashwallet.etherscanioApiKey
import de.crash.crashwallet.exceptions.UnknownAddressTypeException
import de.crash.crashwallet.get
import de.crash.crashwallet.json.BCHBalanceObj
import de.crash.crashwallet.json.EtherscanBalanceResponse
import de.crash.crashwallet.json.TronscanAccountObject
import dev.crash.address.AddressType
import dev.crash.bscscan.BscscanClient
import dev.crash.etherscan.EtherscanClient
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
	return URL("https://blockchain.info/q/addressbalance/$btcaddress?confirmations=3").get().toDouble()/100000000.0
}

private fun getEthereumBalance(ethaddress: String): Double {
	return EtherscanClient(etherscanioApiKey).getAccountBalance(ethaddress).toDouble()
}

fun getERC20TokenBalance(ethaddress: String, contract: String): Double {
	return EtherscanClient(etherscanioApiKey).getERC20TokenBalance(ethaddress, contract).toDouble()
}

private fun getSmartChainBalance(bscaddress: String): Double {
	return BscscanClient(bscscanApiKey).getAccountBalance(bscaddress).toDouble()
}

fun getBEP20TokenBalance(bscaddress: String, contract: String): Double {
	return BscscanClient(bscscanApiKey).getBEP20TokenBalance(bscaddress, contract).toDouble()
}

private fun getTronBalance(tronaddress: String): Double {
	val response = URL("https://apilist.tronscan.org/api/account?address=$tronaddress").get()
	val mapper = jacksonObjectMapper()
	return mapper.readValue<TronscanAccountObject>(response).balance.toDouble() / 100000
}

private fun getLitecoinBalance(ltcaddress: String): Double {
	return URL("https://insight.litecore.io/api/addr/$ltcaddress/balance").get().toDouble() / 100000000
}

private fun getBitcoinCashBalance(bchaddress: String): Double {
	val response = URL("https://api.fullstack.cash/v4/electrumx/balance/$bchaddress").get()
	val mapper = jacksonObjectMapper()
	return mapper.readValue<BCHBalanceObj>(response).balance.confirmed.toDouble()
}

private fun getDashBalance(dashaddress: String): Double {
	return URL("https://explorer.dash.org/insight-api/addr/$dashaddress/balance").get().toDouble() / 100000000
}

private fun getDogeBalance(dogeaddress: String): Double {
	return URL("https://dogechain.info/chain/Dogecoin/q/addressbalance/$dogeaddress").get().toDouble()
}

private fun getDigibyteBalance(dgbaddress: String): Double {
	return URL("https://digiexplorer.info/api/addr/$dgbaddress/balance").get().toDouble() / 100000000
}

private fun getReddcoinBalance(rddaddress: String): Double {
	return URL("https://live.reddcoin.com/api/addr/$rddaddress/balance").get().toDouble() / 100000000
}
