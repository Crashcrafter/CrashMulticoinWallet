package de.crash.crashwallet.json.files

data class AddressSaveObject(
	val addrStr: String,
	val privateKey: String,
	val creationTime: String,
	val balance: Double,
	val transactions: ArrayList<TransactionSaveObject>
)

data class TransactionSaveObject(
	val txid: String,
	val send: Boolean,
	val otherAddress: String,
	val amount: Double,
	val timestamp: String
)

