package dev.einsjannis.crashwallet.server.wallet.balance

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import dev.einsjannis.crashwallet.server.json.AjaxObj
import dev.einsjannis.crashwallet.server.json.AjaxWalletResponse
import dev.einsjannis.crashwallet.server.wallet.*
import dev.einsjannis.crashwallet.server.website.user
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
