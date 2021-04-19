package dev.einsjannis.crashwallet.server.wallet

import dev.einsjannis.crashwallet.server.FaucetTable
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

data class FaucetObj(val name: String, val shortdesc: String, val payoutrate: String, val link: String)

val faucetlist = HashMap<AddressType, FaucetObj>()

fun initFaucets(){
    transaction {
        FaucetTable.selectAll().forEach {

        }
    }
}