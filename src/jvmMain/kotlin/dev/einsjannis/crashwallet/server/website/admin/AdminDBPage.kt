package dev.einsjannis.crashwallet.server.website.admin

import dev.einsjannis.crashwallet.server.DefaultUserData
import dev.einsjannis.crashwallet.server.website.adminOnly
import dev.einsjannis.crashwallet.server.website.getDefaultUserData
import dev.einsjannis.crashwallet.server.website.publicpages.defaultHeads
import dev.einsjannis.crashwallet.server.website.user
import io.ktor.application.*
import io.ktor.html.*
import io.ktor.util.pipeline.*
import kotlinx.html.*

suspend fun PipelineContext<Unit, ApplicationCall>.adminDB() = run{
	val userData : DefaultUserData = getDefaultUserData(user)

	adminOnly(userData)

	call.respondHtml {
		head {
			defaultHeads()
			link(rel = "stylesheet", href = "/css/home.css")
			title("DB - Crash Wallet")
		}
		body {
			defaultAdminHeader(user!!.id)

			section {
				hr()
				//TODO: Change currencies table -> Add new currencies/remove/modify currencies
				//TODO: Add FAQ Elements
				//TODO: Link to users page
				br()
				br()
				br()
				br()
				a(href = "/admin/users") {
					+"User"
				}
			}

			defaultAdminFooter()
		}
	}
}
