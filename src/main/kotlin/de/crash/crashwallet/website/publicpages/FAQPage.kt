package de.crash.crashwallet.website.publicpages

import de.crash.crashwallet.website.DefaultUserData
import de.crash.crashwallet.website.getDefaultUserData
import de.crash.crashwallet.website.user
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
			}

			defaultFooter()
		}
	}
}
