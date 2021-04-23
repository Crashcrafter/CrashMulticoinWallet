package dev.einsjannis.crashwallet.server.json

data class BuildTransactionObj (
    val hash: String,
    val ver: Long,
    val vin_sz: Long,
    val vout_sz: Long,
    val lock_time: Long,
    val size: Long,
    //@Json(name = "in")
    val InRenameLaterToin: List<In>,
    val out: List<Out>
)

data class Out (
    val value: String,
    val scriptPubKey: String
)

data class In (
    val prev_out: PrevOut,
    val scriptSig: String
)

data class PrevOut (
    val hash: String,
    val n: Long
)