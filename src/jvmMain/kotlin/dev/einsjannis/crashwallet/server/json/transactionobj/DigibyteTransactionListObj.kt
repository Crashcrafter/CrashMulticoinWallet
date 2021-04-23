package dev.einsjannis.crashwallet.server.json.transactionobj

class DigibyteTransactionArray(elements: Collection<DigibyteTransactionListObj>) : ArrayList<DigibyteTransactionListObj>(elements)

//data class DigibyteTransactionArray(val elements: Collection<DigibyteTransactionObj>)

data class DigibyteTransactionListObj (
    val address: String,
    val txid: String,
    val vout: Long,
    val ts: Long,
    val scriptPubKey: String,
    val amount: Any?,
    val confirmations: Long?,
    val confirmationsFromCache: Boolean
)

data class DigibyteTransactionObj (
    val txid: String,
    val hash: String,
    val version: Long,
    val size: Long,
    val vsize: Long,
    val weight: Long,
    val locktime: Long,
    val vin: List<Vin>,
    val vout: List<Vout>,
    val blockhash: String,
    val confirmations: Long,
    val time: Long,
    val blocktime: Long,
    val valueOut: Double,
    val valueIn: Double,
    val fees: Double
)

data class Vin (
    val txid: String,
    val vout: Long,
    val scriptSig: ScriptSig,
    val sequence: Long,
    val n: Long,
    val addr: String,
    val valueSat: Long,
    val value: Double,
    val doubleSpentTxID: Any? = null
)

data class ScriptSig (
    val asm: String,
    val hex: String
)

data class Vout (
    val value: Double,
    val n: Long,
    val scriptPubKey: ScriptPubKey,
    val spentTxId: String?,
    val spentIndex: Long?,
    val spentTs: Long?
)

data class ScriptPubKey (
    val asm: String,
    val hex: String,
    val reqSigs: Long,
    val type: String,
    val addresses: List<String>
)