package de.crash.crashwallet.json

data class BlockBookAddressInfo (
    val page: Long,
    val totalPages: Long,
    val itemsOnPage: Long,
    val address: String,
    val balance: String,
    val totalReceived: String,
    val totalSent: String,
    val unconfirmedBalance: String,
    val unconfirmedTxs: Long,
    val txs: Long,
    val txids: List<String>
)