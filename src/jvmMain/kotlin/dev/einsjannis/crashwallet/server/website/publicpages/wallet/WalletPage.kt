package dev.einsjannis.crashwallet.server.website.publicpages.wallet

import dev.einsjannis.crashwallet.server.*
import dev.einsjannis.crashwallet.server.wallet.*
import dev.einsjannis.crashwallet.server.website.getDefaultUserData
import dev.einsjannis.crashwallet.server.website.isGetSet
import dev.einsjannis.crashwallet.server.website.publicpages.defaultFooter
import dev.einsjannis.crashwallet.server.website.publicpages.defaultHeader
import dev.einsjannis.crashwallet.server.website.publicpages.defaultHeads
import dev.einsjannis.crashwallet.server.website.user
import dev.einsjannis.crashwallet.server.website.userOnly
import io.ktor.application.*
import io.ktor.html.*
import io.ktor.util.pipeline.*
import kotlinx.html.*

suspend fun PipelineContext<Unit, ApplicationCall>.wallet() = run{
	val userData : DefaultUserData = getDefaultUserData(user)
	userOnly(userData.name, userData.loggedIn)
	if(call.isGetSet("getBal")){
		getBalResponse()
	}else if(call.isGetSet("getListing")){
		walletListing()
	}
	call.respondHtml {
		head {
			defaultHeads()
			link(rel = "stylesheet", href = "/assets/wallet.css")
			title("Your Wallet - Crash Wallet")
		}
		body {
			defaultHeader(userData.name, userData.role, userData.loggedIn)

			section {
				hr()
				div(classes = "currenciesbox") {

				}

				div(classes = "hidden backup"){

				}
			}
			defaultFooter()
			script(src = "/js/wallet.js"){}
		}
	}
}