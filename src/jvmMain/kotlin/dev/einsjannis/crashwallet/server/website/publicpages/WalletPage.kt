package dev.einsjannis.crashwallet.server.website.publicpages

import dev.einsjannis.crashwallet.server.*
import dev.einsjannis.crashwallet.server.wallet.*
import dev.einsjannis.crashwallet.server.wallet.balance.getBalResponse
import dev.einsjannis.crashwallet.server.website.getDefaultUserData
import dev.einsjannis.crashwallet.server.website.user
import dev.einsjannis.crashwallet.server.website.userOnly
import io.ktor.application.*
import io.ktor.html.*
import io.ktor.util.pipeline.*
import kotlinx.html.*

suspend fun PipelineContext<Unit, ApplicationCall>.wallet() = run{
	val userData : DefaultUserData = getDefaultUserData(user)
	userOnly(userData.name, userData.loggedIn)
	if(call.request.queryParameters["getBal"] != null){
		getBalResponse()
	}else if(call.request.queryParameters["getListing"] != null){
		call.respondHtml {
			body {
				div {
					walletListing()
				}
			}
		}
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

fun DIV.walletListing() = run {
	h1 {
		+"Your Wallet"
		button(classes = "refresh-button hidden") {
			onClick = "loadListing()"
			+"⟳"
		}
	}
	div(classes = "header") {
		div(classes = "column1") {
			p {
				+"Logo"
			}
		}
		div(classes = "column2") {
			p {
				+"Name"
			}
		}
		div(classes = "column3") {
			p{
				+"Short"
			}
		}
		div(classes = "column3") {
			p {
				+"Current Price"
			}
		}
		div(classes = "column3") {
			p {
				+"24h Change"
			}
		}
		div(classes = "column3") {
			p {
				+"24h Volume"
			}
		}
		div(classes = "column3") {
			p {
				+"Market Cap"
			}
		}
		div(classes = "column4") {
			p {
				+"Balance"
			}
		}
		div(classes = "column3") {
			p {
				+"Value"
			}
		}
		div(classes = "column3") {
			p {
				+"Portfolio %"
			}
		}
		div(classes = "column3 noborder"){
			p {
				+"Other Info"
			}
		}
	}
	for(j in order){
		val i = currencylist[j]!!
		val uppercaseShort = i.short.toUpperCase()
		div(classes = "${i.short} currencybox") {
			div(classes = "column1") {
				img {
					src = i.img
					alt = i.name
				}
			}
			div(classes = "column2") {
				h3 {
					+i.name
				}
			}
			div(classes = "column3") {
				p {
					+uppercaseShort
				}
			}
			div(classes = "column3") {
				p(classes = "currentprice-${i.short}") {
					+"Loading..."
				}
			}
			div(classes = "column3") {
				p(classes = "daychange-${i.short}") {
					+"Loading..."
				}
			}
			div(classes = "column3") {
				p(classes = "dayvolume-${i.short}") {
					+"Loading..."
				}
			}
			div(classes = "column3") {
				p(classes = "marketcap-${i.short}") {
					+"Loading..."
				}
			}
			div(classes = "column4") {
				p(classes = "balance-${i.short}") {
					+"Loading..."
				}
			}
			div(classes = "column3") {
				p(classes = "value-${i.short}") {
					+"Loading..."
				}
			}
			div(classes = "column3") {
				p(classes = "portpercent-${i.short}") {
					+"Loading..."
				}
			}
			div(classes = "column3 noborder"){
				p {

				}
			}
		}
	}
}
