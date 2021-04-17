package dev.einsjannis.crashwallet.server.json

data class AddressSaveObject(
	val addrStr: String,
	val privateKey: String,
	val creationTime: String,
	val transactions: ArrayList<TransactionSaveObject>
)

data class TransactionSaveObject(
	val txid: String,
	val amount: String,
	val from: String,
	val timestamp: String
)

