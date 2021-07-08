package de.crash.crashwallet.json

data class BCHBalanceObj (
	val success: Boolean,
	val balance: BCHBalance
)
data class BCHBalance (
	val confirmed: Long,
	val unconfirmed: Long
)
