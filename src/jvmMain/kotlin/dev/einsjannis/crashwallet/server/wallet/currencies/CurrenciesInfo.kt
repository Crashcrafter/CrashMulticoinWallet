package dev.einsjannis.crashwallet.server.wallet.currencies

import dev.einsjannis.crashwallet.server.CurrencyTable
import dev.einsjannis.crashwallet.server.wallet.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.net.URL

data class StaticCurrencyInfo(val name: String, val short: String, val img: String, val explorerLink: String)

data class CurrencyInfo(val name: String, val short: String, val img: String, val explorerLink: String, val currentprice: Double, val daychange: Double,
                        val dayvolume: Double, val marketcap: Double)

val currencies = mutableListOf<StaticCurrencyInfo>()
val currencylist = HashMap<String, CurrencyInfo>()
val order = mutableListOf<String>()

fun updatePrices(){
    updateStaticInfo()
    GlobalScope.launch {
        while(true) {
            val finalcurrencyinfos = HashMap<String, CurrencyInfo>()
            var queryString = ""
            currencies.forEach {
                if(!disabledCurrencies.contains(AddressType.valueOf(it.short.toUpperCase()))){
                    queryString += it.name.toQueryName() + ","
                }
            }
            queryString.dropLast(1)
            val response = URL("https://api.coingecko.com/api/v3/simple/price?ids=$queryString&vs_currencies=usd&include_market_cap=true&include_24hr_vol=true&include_24hr_change=true").readText()
            val readObj = getPrices(response)
            currencies.forEach {
                val valueobj = readObj[it.name.toQueryName()]
                if (valueobj != null) {
                    finalcurrencyinfos[it.short] = CurrencyInfo(it.name, it.short, it.img, it.explorerLink, valueobj.usd.round(6),
                        valueobj.usd_24h_change.round(2), valueobj.usd_24h_vol.round(0), valueobj.usd_market_cap.round(0))
                }else {
                    println(it.name.toQueryName() + " is null")
                }
            }
            currencylist.clear()
            currencylist.putAll(finalcurrencyinfos)
            delay(10000)
        }
    }
}

fun updateStaticInfo(){
    val staticCurrencyInfos = mutableListOf<StaticCurrencyInfo>()
    order.clear()
    transaction {
        CurrencyTable.selectAll().forEach {
            if(!disabledCurrencies.contains(AddressType.valueOf(it[CurrencyTable.short].toString().toUpperCase()))){
                val short = it[CurrencyTable.short]
                order.add(short)
                staticCurrencyInfos.add(StaticCurrencyInfo(it[CurrencyTable.name], short, "/logo/$short.png", it[CurrencyTable.explorerLink]))
            }
        }
    }
    currencies.clear()
    currencies.addAll(staticCurrencyInfos)
}