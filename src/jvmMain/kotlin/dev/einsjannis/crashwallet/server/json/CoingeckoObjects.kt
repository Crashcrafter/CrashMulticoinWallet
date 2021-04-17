package dev.einsjannis.crashwallet.server.json

class CoingeckoPriceInfo(elements: Map<String, CoingeckoCurrencyValue>) : HashMap<String, CoingeckoCurrencyValue>(elements)

data class CoingeckoCurrencyValue (
	val usd: Double,
	val usd_market_cap: Double,
	val usd_24h_vol: Double,
	val usd_24h_change: Double
)
