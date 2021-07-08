package de.crash.crashwallet.website.admin

import de.crash.crashwallet.website.DefaultUserData
import de.crash.crashwallet.website.adminOnly
import de.crash.crashwallet.website.getDefaultUserData
import de.crash.crashwallet.website.publicpages.defaultFooter
import de.crash.crashwallet.website.publicpages.defaultHeads
import de.crash.crashwallet.website.user
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
			link(rel = "stylesheet", href = "/assets/wallet.css")
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

			defaultFooter()
		}
	}
}
