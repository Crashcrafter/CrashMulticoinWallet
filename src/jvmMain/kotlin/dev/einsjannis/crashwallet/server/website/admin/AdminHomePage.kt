package dev.einsjannis.crashwallet.server.website.admin

import dev.einsjannis.crashwallet.server.*
import dev.einsjannis.crashwallet.server.website.*
import dev.einsjannis.crashwallet.server.website.publicpages.defaultFooter
import dev.einsjannis.crashwallet.server.website.publicpages.defaultHeads
import io.ktor.application.*
import io.ktor.html.*
import io.ktor.util.pipeline.*
import kotlinx.html.*

suspend fun PipelineContext<Unit, ApplicationCall>.adminHome() = run{
	val userData : DefaultUserData = getDefaultUserData(user)

	adminOnly(userData)

	call.respondHtml {
		head {
			defaultHeads()
			link(rel = "stylesheet", href = "/assets/admin.css")
			title("Admin - Crash Wallet")
		}
		body {
			defaultAdminHeader(user!!.id)

			section {
				hr()
			}

			defaultFooter()
		}
	}
}
