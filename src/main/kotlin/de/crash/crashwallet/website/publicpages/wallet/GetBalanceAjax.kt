package de.crash.crashwallet.website.publicpages.wallet

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import de.crash.crashwallet.json.ajax.AjaxObj
import de.crash.crashwallet.json.ajax.AjaxWalletResponse
import de.crash.crashwallet.wallet.BalanceAndAddress
import de.crash.crashwallet.wallet.currencies.CurrencyInfo
import de.crash.crashwallet.wallet.currencies.currencylist
import de.crash.crashwallet.wallet.currencies.order
import de.crash.crashwallet.wallet.getBalances
import de.crash.crashwallet.wallet.round
import de.crash.crashwallet.wallet.toReadableString
import de.crash.crashwallet.website.user
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.util.pipeline.*

suspend fun PipelineContext<Unit, ApplicationCall>.getBalResponse() {
	val hashmap = HashMap<String, AjaxObj>()
	val userbalances = getBalances(user!!.id)
	var totalbalance = 0.0
	val portfoliopercent = HashMap<String, Double>()
	userbalances.forEach{
		if(it.value.balance > 0.0){
			totalbalance += it.value.balance* currencylist[it.key]!!.currentprice
		}
	}
	if(totalbalance > 0.0){
		userbalances.forEach{
			portfoliopercent[it.key] = ((it.value.balance* currencylist[it.key]!!.currentprice)/totalbalance).round(6) * 100
		}
	}else{
		userbalances.forEach {
			portfoliopercent[it.key] = 0.0
		}
	}
	order.forEach {
		if(userbalances.containsKey(it)){
			val obj = currencylist[it]!!
			val balobj = userbalances[obj.short]!!

			hashmap[obj.short] = buildAjaxBalanceObj(obj, balobj, portfoliopercent[obj.short]!!)
		}
	}
	val responseobj = AjaxWalletResponse(hashmap)
	val mapper = jacksonObjectMapper()
	val response = mapper.writeValueAsString(responseobj)
	call.respondText(response)
}

fun buildAjaxBalanceObj(obj: CurrencyInfo, balobj: BalanceAndAddress, portfoliopercent: Double) : AjaxObj = AjaxObj(balobj.balance.toString(), "${balobj.balance.times(obj.currentprice).round(3)}"
	, portfoliopercent, obj.getExplorerLink(balobj.address), obj.marketcap.toReadableString(),
	obj.dayvolume.toReadableString(), obj.daychange, "${obj.currentprice}")


fun CurrencyInfo.getExplorerLink(address: String) : String{
	var explorerLink = "${this.explorerLink}/address/${address}"
	if(this.short == "theta" || this.short == "tfuel") explorerLink = "${this.explorerLink}/account/${address}"
	return explorerLink
}
