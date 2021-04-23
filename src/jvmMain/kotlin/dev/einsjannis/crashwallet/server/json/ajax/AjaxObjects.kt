package dev.einsjannis.crashwallet.server.json.ajax

class AjaxWalletResponse(elements: Map<String, AjaxObj>) : HashMap<String, AjaxObj>(elements)

data class AjaxObj (
	val bal: String,
	val value: String,
	val portpercent: Double,
	val explorerlink: String,
	val marketcap: String,
	val dayvolume: String,
	val daychange: Double,
	val currentprice: String
)
