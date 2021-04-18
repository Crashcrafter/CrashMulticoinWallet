package dev.einsjannis.crashwallet.server.wallet.currencies

import dev.einsjannis.crashwallet.server.CurrencyTable
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.transaction

fun String.toQueryName() : String {
    var result = this.toLowerCase().replace(' ', '-')
    if(result.contains("smart-chain")){
        result = "binancecoin"
    }
    return result
}

fun deleteCurrency(id: Int): Boolean {
    var result = 0
    transaction {
        result = CurrencyTable.deleteWhere(1, 0) { CurrencyTable.id eq id }
    }
    return result > 0
}