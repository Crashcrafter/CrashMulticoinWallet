package de.crash.crashwallet.website.publicpages.account

import de.crash.crashwallet.website.*
import de.crash.crashwallet.website.publicpages.defaultFooter
import de.crash.crashwallet.website.publicpages.defaultHeader
import de.crash.crashwallet.website.publicpages.defaultHeads
import io.ktor.application.*
import io.ktor.html.*
import io.ktor.util.pipeline.*
import kotlinx.html.*

suspend fun PipelineContext<Unit, ApplicationCall>.account() = run{
	val userData : FullUserData = getFullUserData(user)

	userOnly(userData.name, userData.loggedIn)

	if(call.isGetSet("logout")) logout()

	if(call.isGetSet("getAjax")){
		when(call.parameters["getAjax"].toString().toInt()) {
			0 -> accountDetails()
			1 -> accountSettings()
		}
	}

	call.respondHtml {
		head {
			defaultHeads()
			link(rel = "stylesheet", href = "/assets/account.css")
			title("Account - Crash Wallet")
		}
		body {
			defaultHeader(userData.name, userData.role, userData.loggedIn)

			section {
				hr()
				div(classes = "acc-content"){
					div(classes = "sidebar"){
						ul {
							li {
								onClick = "changeAjax(0)"
								p {
									+"Accountdetails"
								}
							}
							li {
								onClick = "changeAjax(1)"
								p {
									+"Accountsettings"
								}
							}
							li(classes = "red_btn") {
								onClick = "logout()"
								p {
									+"Logout"
								}
							}
						}
					}
					div(classes = "main-content") {
						h1 {
							+ "Hello ${userData.name}!"
						}
					}
				}
				/*
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
				*/
			}

			defaultFooter()
			script(src = "/js/account.js") {}
		}
	}
}
