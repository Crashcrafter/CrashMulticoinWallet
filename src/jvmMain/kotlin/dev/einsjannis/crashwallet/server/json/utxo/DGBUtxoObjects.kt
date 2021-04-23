package dev.einsjannis.crashwallet.server.json.utxo

class DGBUTXOObj(elements: Collection<UTXOElement>) : ArrayList<UTXOElement>(elements)

data class UTXOElement (
    val address: String,
    val txid: String,
    val vout: Long,
    val ts: Long,
    val scriptPubKey: String,
    val amount: Any? = null,
    val confirmationsFromCache: Boolean
)
