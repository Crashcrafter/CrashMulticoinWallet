package dev.einsjannis.crashwallet.server.website.publicpages

import dev.einsjannis.crashwallet.server.website.*
import io.ktor.application.*
import io.ktor.html.*
import io.ktor.util.pipeline.*
import kotlinx.html.*

suspend fun PipelineContext<Unit, ApplicationCall>.contact() = run {
	val userData : DefaultUserData = getDefaultUserData(user)

	call.respondHtml {
		head {
			defaultHeads()
			link(rel = "stylesheet", href = "/assets/contact.css")
			title("Contact - Crash Wallet")
		}
		body {
			defaultHeader(userData.name, userData.role, userData.loggedIn)
			section {
				hr()
				h1(classes = "contacth1") {
					+"Contact"
				}
				div(classes = "contactcontainer") {
					div(classes = "outerbox") {
						div(classes = "contactbox") {
							a {
								href = "mailto:$EmailAddress"
								div(classes = "innerbox") {
									h1 {
										+"Email"
									}
									p {
										+"Our Email: $EmailAddress"
										dbr()
										+"Our answer might take more than 24h, if you need fast support, consider joining our Discord instead of writing an email."
										dbr()
										+"Just write the relevant information in your email to us, so we can reply you faster."
									}
									h3 {
										+"Support via Email might have huge delays (up to 3 days)!"
									}
								}
							}
						}
					}
					div(classes = "outerbox") {
						div(classes = "contactbox") {
							a {
								href = "/discord"
								target = "_blank"
								div(classes = "innerbox") {
									h1 {
										+"Discord"
									}
									p {
										+"You get the fastest response here on our Discord. "
										dbr()
										+"Open a ticket in the channel #support, if you need support."
										dbr()
										+"If you have a general question, feel free to ask in the #general chat."
										dbr()
									}
									h3 {
										+"Please be patient, we cannot answer instantly!"
									}
								}
							}
						}
					}
					div(classes = "outerfaqbox") {
						div(classes = "faqbox") {
							a {
								href = "/faq"
								div(classes = "innerfaqbox") {
									h1 {
										+"FAQ"
									}
									h3 {
										+"Check out our FAQ before you send us a message! If your question is not answered in the FAQ, feel free to contact us."
									}
								}
							}
						}
					}
				}
			}
			defaultFooter()
		}
	}
}
