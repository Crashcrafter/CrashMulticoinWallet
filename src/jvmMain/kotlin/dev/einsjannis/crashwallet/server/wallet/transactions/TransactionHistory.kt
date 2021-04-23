package dev.einsjannis.crashwallet.server.wallet.transactions

import dev.einsjannis.crashwallet.server.wallet.AddressType
import java.net.URL

data class TransactionHistoryObj(val txid: String, val send: Boolean, val otherAddress: String, val amount: Double, val confirmed: Boolean)

fun getTransactions(address: String, type: AddressType): List<TransactionHistoryObj> {
    return when(type) {
        AddressType.DGB -> getDGBTransactionHistory(address)
        else -> listOf()
    }
}

fun getDGBTransactionHistory(address: String): List<TransactionHistoryObj> {
    val response = URL("https://digiexplorer.info/api/addr/$address/utxo").readText()
    println(response)
    return listOf()
}