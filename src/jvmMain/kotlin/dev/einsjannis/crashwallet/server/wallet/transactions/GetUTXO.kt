package dev.einsjannis.crashwallet.server.wallet.transactions

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import dev.einsjannis.crashwallet.server.json.utxo.DGBUTXOObj
import dev.einsjannis.crashwallet.server.wallet.AddressType
import java.net.URL

data class UTXO(val txid: String, val amount: Double)

fun getUXTOfromAddress(type: AddressType, address: String): List<UTXO>{
    return when(type){
        AddressType.DGB -> getDGBUTXO(address)
        else -> listOf()
    }
}

private fun getDGBUTXO(address: String): List<UTXO> {
    val response = URL("https://digiexplorer.info/api/addr/$address/utxo").readText()
    println(response)
    val UTXOObj = jacksonObjectMapper().readValue<DGBUTXOObj>(response)
    UTXOObj.forEach {
        getTransactionData(AddressType.DGB, it.txid)
    }
    return listOf()
}