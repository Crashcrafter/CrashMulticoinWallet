package dev.einsjannis.crashwallet.server.website.publicpages

import dev.einsjannis.crashwallet.server.*
import dev.einsjannis.crashwallet.server.website.*
import io.ktor.application.*
import io.ktor.html.*
import io.ktor.util.pipeline.*
import kotlinx.html.*

suspend fun PipelineContext<Unit, ApplicationCall>.account() = run{
	val userData : DefaultUserData = getDefaultUserData(user)

	userOnly(userData.name, userData.loggedIn)

	if(call.request.queryParameters["logout"] != null) logout()

	call.respondHtml {
		head {
			defaultHeads()
			link(rel = "stylesheet", href = "/css/home.css")
			title("Account - Crash Wallet")
		}
		body {
			defaultHeader(userData.name, userData.role, userData.loggedIn)

			section {
				hr()
				h1 {
					+ "Hello ${userData.name}!"
				}
				h4 {
					+"Your Role is ${userData.role}"
				}
				//TODO: Display Account Data
				a{
					href="/account?logout"
					+"Logout"
				}
				//TODO: Change Username
				a{
					href="/"
					+"Change your Username"
				}
				//TODO: Change Password
				a{
					href="/"
					+"Change your Password"
				}
				//TODO: Change Email
				a{
					href="/"
					+"Change your Email"
				}
				//TODO: Delete Account
			}

			defaultFooter()
		}
	}
}
