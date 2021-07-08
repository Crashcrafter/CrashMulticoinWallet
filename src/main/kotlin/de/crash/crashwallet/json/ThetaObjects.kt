package de.crash.crashwallet.json

data class ThetaResponse (
	val type: String,
	val body: ThetaBody
)

data class ThetaBody (
	val address: String,
	val balance: ThetaBalance,
	val sequence: String,
	val reserved_funds: List<Any?>,
	val txs_counter: Map<String, Long>
)

data class ThetaBalance (
	val thetawei: String,
	val tfuelwei: String
)
