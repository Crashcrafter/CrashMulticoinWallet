package dev.einsjannis.crashwallet.server.json

data class InsightAddressInfo (
	val addrStr: String,
	val balance: Double,
	val balanceSat: Long,
	val totalReceived: Double,
	val totalReceivedSat: Long,
	val totalSent: Long,
	val totalSentSat: Long,
	val unconfirmedBalance: Long,
	val unconfirmedBalanceSat: Long,
	val unconfirmedTxApperances: Long,
	val txApperances: Long,
	val transactions: List<String>
)
