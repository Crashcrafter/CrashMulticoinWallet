package dev.einsjannis.crashwallet.server.website.publicpages

import dev.einsjannis.crashwallet.server.website.*
import io.ktor.application.*
import io.ktor.html.*
import io.ktor.util.pipeline.*
import kotlinx.html.*

suspend fun PipelineContext<Unit, ApplicationCall>.news() = run {
	val userData : DefaultUserData = getDefaultUserData(user)

	call.respondHtml {
		head {
			defaultHeads()
			link(rel = "stylesheet", href = "/assets/home.css")
			title("News - Crash Wallet")
		}
		body {
			defaultHeader(userData.name, userData.role, userData.loggedIn)

			section {
				hr()
				//TODO: Newsletter subscription
				//TODO: News Feed
				//TODO: Article Display
			}

			defaultFooter()
		}
	}
}
