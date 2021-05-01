package dev.einsjannis.crashwallet.server.website.admin

import dev.einsjannis.crashwallet.server.website.DefaultUserData
import dev.einsjannis.crashwallet.server.website.adminOnly
import dev.einsjannis.crashwallet.server.website.getDefaultUserData
import dev.einsjannis.crashwallet.server.website.publicpages.defaultFooter
import dev.einsjannis.crashwallet.server.website.publicpages.defaultHeads
import dev.einsjannis.crashwallet.server.website.user
import io.ktor.application.*
import io.ktor.html.*
import io.ktor.util.pipeline.*
import kotlinx.html.*

suspend fun PipelineContext<Unit, ApplicationCall>.adminUsers() = run{
	val userData : DefaultUserData = getDefaultUserData(user)

	adminOnly(userData)

	call.respondHtml {
		head {
			defaultHeads()
			link(rel = "stylesheet", href = "/assets/admin.css")
			title("Users - Crash Wallet")
		}
		body {
			defaultAdminHeader(user!!.id)

			section {
				hr()
				//TODO: Full functional User Table with
				//TODO: Search function on email, id, name
				//TODO: Delete accounts
			}

			defaultFooter()
		}
	}
}


