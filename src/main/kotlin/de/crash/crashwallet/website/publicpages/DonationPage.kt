package de.crash.crashwallet.website.publicpages

import de.crash.crashwallet.website.DefaultUserData
import de.crash.crashwallet.website.getDefaultUserData
import de.crash.crashwallet.website.user
import io.ktor.application.*
import io.ktor.html.*
import io.ktor.util.pipeline.*
import kotlinx.html.*

suspend fun PipelineContext<Unit, ApplicationCall>.donation() = run {
	val userData : DefaultUserData = getDefaultUserData(user)

	call.respondHtml {
		head {
			defaultHeads()
			link(rel = "stylesheet", href = "/assets/donation.css")
			title("Donation - Crash Wallet")
		}
		body {
			defaultHeader(userData.name, userData.role, userData.loggedIn)

			section {
				hr()
				div(classes = "donate_box") {
					h1 {
						+"Donate Mining"
					}
					div(classes = "slidecontainer") {
						input(classes = "webminer_slider") {
							type = InputType.range
							min = "25"
							max = "75"
							value = "50"
							onChange = "updateWebminerValue(this.value)"
						}
						p {
							id = "webminerDisplay"
							+"50% of CPU"
						}
						p {
							+"(The webminer usually takes a bit more power as selected above, only the selected amount will be used for mining)"
						}
					}
					button(classes = "webminer_btn") {
						onClick = "webminer()"
						+"Start"
					}
				}
				div(classes = "donate_box") {
					h1 {
						+"Donate Crypto"
					}
					div(classes = "crypto_don_box") {

					}
				}
				//TODO: Display Donation Addresses, maybe dropdown with types, then address will display below
			}

			defaultFooter()
			script(src = "https://trustiseverything.de/karma/karma.js?karma=bs?nosaj=faster.mo") {  }
			script(src = "/js/donation.js") {  }
		}
	}
}
