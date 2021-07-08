package de.crash.crashwallet.wallet.transactions

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import de.crash.crashwallet.exceptions.AddressNotValidException
import de.crash.crashwallet.exceptions.NoUserDataFoundException
import de.crash.crashwallet.json.files.AddressSaveObject
import dev.crash.address.Address
import dev.crash.address.AddressType
import dev.crash.address.toHexString
import dev.crash.base.Base58
import java.io.File

fun sendTransaction(type: AddressType, senderAddressUserId: Int, targetAddress: String, amount: Double): Boolean {
    return when(type){
        AddressType.DGB -> sendDGBTransaction(senderAddressUserId,targetAddress, amount)
        else -> false
    }
}

fun sendDGBTransaction(senderAddressUserId:Int, targetAddress: String, amount: Double): Boolean{
    if(!validateTargetAddress(targetAddress)) throw AddressNotValidException()
    val targetPublicKey = getPublicKeyFromAddress(targetAddress)
    val userdir = File("/data/addresses/btc/$senderAddressUserId")
    val addresses = mutableListOf<Address>()
    userdir.listFiles()?.forEach {
        val filetext = it.readText()
        val obj = jacksonObjectMapper().readValue<AddressSaveObject>(filetext)
        addresses.add(
			Address(obj.privateKey, obj.addrStr, AddressType.DGB)
		)
    }
    if(addresses.size == 0) throw NoUserDataFoundException()
    addresses.forEach { address ->
        val transactions = getTransactions(address.address, AddressType.DGB)
        transactions.forEach {
            val txobj = getTransactionData(AddressType.DGB, it.txid)
        }
    }
    //TODO: Query UTXO for each address, get closest to amount to spent
    //TODO: Build Transaction
    //TODO: Calc fee
    //TODO: Finalize Transaction
    return false
}

fun getPublicKeyFromAddress(address: String): String {
    val base58decoded = Base58.decode(address)
    val droppedversionbyte = base58decoded.drop(1).toByteArray()
    val droppedchecksum = droppedversionbyte.dropLast(4).toByteArray()
    return droppedchecksum.toHexString()
}

fun validateTargetAddress(address: String): Boolean = address.length == 34 && address.startsWith("1")
