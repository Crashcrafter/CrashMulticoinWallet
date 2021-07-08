package de.crash.crashwallet.wallet.transactions

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import de.crash.crashwallet.getCurrentTimeStamp
import de.crash.crashwallet.json.transactionobj.DigibyteTransactionHistoryObj
import dev.crash.address.AddressType
import java.net.URL

data class TransactionHistoryObj(val txid: String, val send: Boolean, val otherAddress: String, val amount: Double, val timeStamp: String)

fun getTransactions(address: String, type: AddressType): List<TransactionHistoryObj> {
    return when(type) {
        AddressType.DGB -> getDGBTransactionHistory(address)
        else -> listOf()
    }
}

private fun getDGBTransactionHistory(address: String): List<TransactionHistoryObj> {
    val txList = mutableListOf<TransactionHistoryObj>()
    val response = URL("https://digiexplorer.info/api/txs/?address=$address").readText()
    val obj = jacksonObjectMapper().readValue<DigibyteTransactionHistoryObj>(response)
    obj.txs.forEach {
        val senderAddress = it.vin[0].addr
        val receiverAddress = it.vout[0].scriptPubKey.addresses[0]
        val send = address == senderAddress
        var otherAddress = senderAddress
        if(send) otherAddress = receiverAddress
        var value = 0.0
        it.vout.forEach { vout ->
            vout.scriptPubKey.addresses.forEach { addresses ->
                if(addresses == address) println()
            }
        }
        txList.add(TransactionHistoryObj(it.txid, send, otherAddress, 0.0, getCurrentTimeStamp()))
    }
    println(response)
    return txList
}