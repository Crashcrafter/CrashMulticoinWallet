package dev.einsjannis.crashwallet.server.wallet

import dev.einsjannis.crashwallet.server.FaucetTable
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

data class FaucetObj(val name: String, val shortdesc: String, val payoutrate: String, val link: String)

val faucetlist = HashMap<AddressType, MutableList<FaucetObj>>()

fun initFaucets(){
    faucetlist.clear()
    transaction {
        FaucetTable.selectAll().forEach {
            val type = AddressType.valueOf(it[FaucetTable.currency].toUpperCase())
            val faucetObj = FaucetObj(it[FaucetTable.name], type.name, it[FaucetTable.payoutrate], it[FaucetTable.link])
            faucetlist[type]!!.add(faucetObj)
        }
    }
}