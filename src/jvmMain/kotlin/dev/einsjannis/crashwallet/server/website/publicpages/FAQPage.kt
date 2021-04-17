package dev.einsjannis.crashwallet.server.website.publicpages

import dev.einsjannis.crashwallet.server.DefaultUserData
import dev.einsjannis.crashwallet.server.website.*
import io.ktor.application.*
import io.ktor.html.*
import io.ktor.util.pipeline.*
import kotlinx.html.*

suspend fun PipelineContext<Unit, ApplicationCall>.faq() = run {
	val userData : DefaultUserData = getDefaultUserData(user)

	call.respondHtml {
		head {
			defaultHeads()
			link(rel = "stylesheet", href = "/assets/home.css")
			title("FAQ - Crash Wallet")
		}
		body {
			defaultHeader(userData.name, userData.role, userData.loggedIn)

			section {
				hr()
				//TODO: Display FAQ
				//TODO: Add input for suggestion

			}

			defaultFooter()
		}
	}
}
