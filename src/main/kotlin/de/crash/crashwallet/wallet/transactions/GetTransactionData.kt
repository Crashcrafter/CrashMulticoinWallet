package de.crash.crashwallet.wallet.transactions

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import de.crash.crashwallet.json.transactionobj.DigibyteTransactionObj
import dev.crash.address.AddressType
import java.net.URL

data class TransactionObj(val addressFrom: String, val addressTo: String, val amount: Double)

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
    return TransactionObj(dgbTransactionObj.vin[0].addr, dgbTransactionObj.vout[0].scriptPubKey.addresses[0], 0.0)
}