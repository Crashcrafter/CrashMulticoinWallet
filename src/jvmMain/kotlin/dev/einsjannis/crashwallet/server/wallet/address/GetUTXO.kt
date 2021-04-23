package dev.einsjannis.crashwallet.server.wallet.address

import dev.einsjannis.crashwallet.server.wallet.AddressType

data class UTXO(val txid: String, val amount: Double)

fun getUXTOfromAddress(type: AddressType, address: String): List<UTXO>{
    return when(type){
        else -> listOf()
    }
}