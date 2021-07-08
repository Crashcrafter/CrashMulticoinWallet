package de.crash.crashwallet.wallet

import de.crash.crashwallet.FaucetTable
import dev.crash.address.AddressType
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*
import kotlin.collections.HashMap

data class FaucetObj(val name: String, val shortdesc: String, val payoutrate: String, val link: String)

val faucetlist = HashMap<AddressType, MutableList<FaucetObj>>()

fun initFaucets(){
    faucetlist.clear()
    transaction {
        FaucetTable.selectAll().forEach {
            val type = AddressType.valueOf(it[FaucetTable.currency].uppercase(Locale.getDefault()))
            val faucetObj = FaucetObj(it[FaucetTable.name], it[FaucetTable.shortdesc], it[FaucetTable.payoutrate], it[FaucetTable.link])
            faucetlist[type]!!.add(faucetObj)
        }
    }
}