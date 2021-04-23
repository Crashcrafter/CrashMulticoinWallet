package dev.einsjannis.crashwallet.server.wallet.transactions

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import dev.einsjannis.crashwallet.server.json.transactionobj.DigibyteTransactionObj
import dev.einsjannis.crashwallet.server.wallet.AddressType
import java.net.URL

data class TransactionObj(val addressTo: String, val addressFrom: String, val amount: Double)

fun getTransactionData(type: AddressType, txid: String): TransactionObj {
    return when(type) {
        AddressType.DGB -> getDGBTransactionData(txid)
        else -> throw UnsupportedOperationException()
    }
}

private fun getDGBTransactionData(txid: String): TransactionObj {
    val response = URL("https://digiexplorer.info/api/tx/$txid").readText()
    println(response)
    val dgbTransactionObj = jacksonObjectMapper().readValue<DigibyteTransactionObj>(response)
    return TransactionObj(dgbTransactionObj.vin[0].addr, "", 0.0)
}