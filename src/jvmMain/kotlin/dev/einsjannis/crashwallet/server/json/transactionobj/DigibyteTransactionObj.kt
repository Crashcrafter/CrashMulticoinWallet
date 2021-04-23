package dev.einsjannis.crashwallet.server.json.transactionobj

class DigibyteTransactionArray(elements: Collection<DigibyteTransactionObj>) : ArrayList<DigibyteTransactionObj>(elements)

//data class DigibyteTransactionArray(val elements: Collection<DigibyteTransactionObj>)

data class DigibyteTransactionObj (
    val address: String,
    val txid: String,
    val vout: Long,
    val ts: Long,
    val scriptPubKey: String,
    val amount: Any?,
    val confirmations: Long?,
    val confirmationsFromCache: Boolean
)