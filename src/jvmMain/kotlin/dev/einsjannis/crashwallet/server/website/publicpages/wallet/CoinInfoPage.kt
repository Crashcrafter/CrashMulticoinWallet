package dev.einsjannis.crashwallet.server.website.publicpages.wallet

import dev.einsjannis.crashwallet.server.wallet.*
import dev.einsjannis.crashwallet.server.wallet.currencies.currencylist
import dev.einsjannis.crashwallet.server.website.genQRCode
import dev.einsjannis.crashwallet.server.website.user
import io.ktor.application.*
import io.ktor.html.*
import io.ktor.util.pipeline.*
import kotlinx.html.*

suspend fun PipelineContext<Unit, ApplicationCall>.coinInfo() = run {
	val short = call.request.queryParameters["id"]!!
	val coinobj = currencylist[short]!!
	val userobj = getBalance(user!!.id, AddressType.valueOf(short.toAddressTypeString()))
	call.respondHtml {
		//TODO: Send Crypto
		//TODO: Display Transactions
		//TODO: Link to exchanges
		body {
			div(classes = "back_button") {
				button {
					onClick = "backtoListing()"
					p {
						+"<- Back"
					}
				}
			}
			div(classes = "coininfocontent") {
				div(classes = "coin_logo") {
					img {
						src = coinobj.img
						alt = coinobj.name
					}
				}
				div(classes = "address_info_box") {
					div(classes = "address_info") {
						h2{
							+"${userobj.balance} ${short.toUpperCase()}"
						}
						h3{
							+"~${(userobj.balance*coinobj.currentprice).round(3)} USD"
						}
						p {
							+"Your ${short.toUpperCase()} Address: "
							button(classes = "explorer_link") {
								onClick = "window.open('${coinobj.explorerLink}/address/${userobj.address}', '_blank')"
								+"Explorer"
							}
						}
						div(classes = "address_background") {
							p(classes = "address"){
								+userobj.address
								button {
									onClick = "copyAddress()"
									+"Copy"
								}
							}
						}
					}
				}
				div(classes = "address_qrcode") {
					img {
						src = "data:image/png;base64,${genQRCode(userobj.address, 200, 200)}"
						alt = "QRCode"
					}
				}
			}
			div(classes = "marketinfo") {
				div(classes = "marketinfobox") {
					p {
						+"Current Price"
					}
					p {
						+"${coinobj.currentprice} USD"
					}
				}
				div(classes = "marketinfobox") {
					p {
						+"24h Change"
					}
					var prefix = ""
					var classname = "down"
					if(coinobj.daychange > 0){
						prefix = "+"
						classname = "up"
					}
					p(classes = classname) {
						+"$prefix${coinobj.daychange} %"
					}
				}
				div(classes = "marketinfobox") {
					p {
						+"24h Volume"
					}
					p {
						+"${coinobj.dayvolume.toReadableString()} USD"
					}
				}
				div(classes = "marketinfobox") {
					p {
						+"Market Cap"
					}
					p {
						+"${coinobj.marketcap.toReadableString()} USD"
					}
				}
			}
			div(classes = "transactions") {
				h1 {
					+"Transactions"
				}
				div(classes = "transactionelementcontainer") {
					
				}
			}
			input(classes = "hidden_address") {
				type = InputType.hidden
				value = userobj.address
			}
		}
	}
}

