package de.crash.crashwallet.wallet.transactions

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import de.crash.crashwallet.json.utxo.DGBUTXOObj
import dev.crash.address.AddressType
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
    val utxos = mutableListOf<UTXO>()
    UTXOObj.forEach {
        val obj = getTransactionData(AddressType.DGB, it.txid)
        utxos.add(UTXO(it.txid, obj.amount))
    }
    return utxos
}