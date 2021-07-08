package de.crash.crashwallet.wallet

import de.crash.crashwallet.getnewDBConnection
import de.crash.crashwallet.logger.log
import de.crash.crashwallet.logger.mainLogger
import de.crash.crashwallet.saveAddress
import dev.crash.address.Address
import dev.crash.address.AddressGen.genAddress
import dev.crash.address.AddressType

fun registerWallets(userid: Int, save: Boolean){
	val checkqueryString = "SELECT id FROM wallets WHERE id=$userid"
	val con = getnewDBConnection()
	val checkstatement = con.prepareStatement(checkqueryString)
	checkstatement.execute()
	if(!checkstatement.resultSet.next()){
		val addresslist = mutableListOf<Address>()
		AddressType.values().forEach {
			try {
				addresslist.add(genAddress(it))
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
