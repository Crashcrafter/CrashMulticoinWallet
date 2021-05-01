package dev.einsjannis.crashwallet.server.wallet

import dev.einsjannis.crashwallet.server.getnewDBConnection
import dev.einsjannis.crashwallet.server.logger.log
import dev.einsjannis.crashwallet.server.logger.mainLogger
import dev.einsjannis.crashwallet.server.saveAddress
import dev.einsjannis.crashwallet.server.wallet.address.Address
import dev.einsjannis.crashwallet.server.wallet.address.genAddress

fun registerWallets(userid: Int, save: Boolean){
	val checkqueryString = "SELECT id FROM wallets WHERE id=$userid"
	val con = getnewDBConnection()
	val checkstatement = con.prepareStatement(checkqueryString)
	checkstatement.execute()
	if(!checkstatement.resultSet.next()){
		val addresslist = mutableListOf<Address>()
		AddressType.values().forEach {
			try {
				if(!noOwnAddress.contains(it) && !disabledCurrencies.contains(it)) addresslist.add(genAddress(it))
			}catch (ex: Exception){
				addresslist.add(Address("", "", it))
			}
		}

		var queryString = "INSERT INTO wallets("
		addresslist.forEach {
			if(save){
				saveAddress(it, userid)
			}
			queryString += "${it.type.name.toLowerCase()},"
		}
		queryString += "id) VALUES("
		addresslist.forEach {
			queryString += "'${it.address}',"
		}
		queryString += "$userid)"
		val statement = con.prepareStatement(queryString)
		statement.execute()
	}else mainLogger.log("ID $userid already has a wallet! (Tried to override existing wallet)")
	con.close()
}
