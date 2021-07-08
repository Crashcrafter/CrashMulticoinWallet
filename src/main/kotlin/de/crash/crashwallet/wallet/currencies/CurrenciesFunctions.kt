package de.crash.crashwallet.wallet.currencies

import de.crash.crashwallet.CurrencyTable
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.transaction

fun String.toQueryName() : String {
    var result = this.lowercase().replace(' ', '-')
    if(result.contains("smart-chain")){
        result = "binancecoin"
    }else if(result.contains("theta")){
        result = "theta-token"
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