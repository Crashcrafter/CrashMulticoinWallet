package dev.einsjannis.crashwallet.server.wallet.transactions

import dev.einsjannis.crashwallet.server.wallet.AddressType

data class TransactionHistoryObj(val txid: String, val send: Boolean, val otherAddress: String, val amount: Double)

fun getTransactions(address: String, type: AddressType): List<TransactionHistoryObj> {
    return when(type) {
        AddressType.DGB -> getDGBTransactionHistory(address)
        else -> listOf()
    }
}

fun getDGBTransactionHistory(address: String): List<TransactionHistoryObj> {
    return listOf()
}