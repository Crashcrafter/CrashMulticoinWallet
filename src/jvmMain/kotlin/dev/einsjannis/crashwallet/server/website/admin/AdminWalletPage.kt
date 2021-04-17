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

suspend fun PipelineContext<Unit, ApplicationCall>.adminWallet() = run{
	val userData : DefaultUserData = getDefaultUserData(user)

	adminOnly(userData)

	call.respondHtml {
		head {
			defaultHeads()
			link(rel = "stylesheet", href = "/css/home.css")
			title("Admin Wallet - Crash Wallet")
		}
		body {
			defaultAdminHeader(user!!.id)

			section {
				hr()
				//TODO: Wallet Page for website balance
				//TODO: See info about fee
				//TODO: See stats about transactions for each currency
			}

			defaultAdminFooter()
		}
	}
}
