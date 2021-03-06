package de.crash.crashwallet.website.publicpages.wallet

import de.crash.crashwallet.website.*
import de.crash.crashwallet.website.publicpages.defaultFooter
import de.crash.crashwallet.website.publicpages.defaultHeader
import de.crash.crashwallet.website.publicpages.defaultHeads
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