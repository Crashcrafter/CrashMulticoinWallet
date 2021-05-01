package dev.einsjannis.crashwallet.server.website.publicpages.account

import dev.einsjannis.crashwallet.server.website.*
import dev.einsjannis.crashwallet.server.website.publicpages.defaultFooter
import dev.einsjannis.crashwallet.server.website.publicpages.defaultHeader
import dev.einsjannis.crashwallet.server.website.publicpages.defaultHeads
import io.ktor.application.*
import io.ktor.html.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.util.pipeline.*
import kotlinx.html.*

suspend fun PipelineContext<Unit, ApplicationCall>.loginget() = run{
	val userdata = getDefaultUserData(user)
	if(userdata.loggedIn){
		call.respondRedirect("/account")
	}
	call.respondHtml {
		head {
			defaultHeads()
			link(rel = "stylesheet", href = "/assets/login.css")
			title("Crash Wallet")
		}
		body {
			defaultHeader("", "", false)

			section {
				hr()
				div(classes = "centerbox") {
					h1 {
						+"Login"
					}
					if(call.isGetSet("2fa")){
						form(action = "?2fa", method = FormMethod.post) {
							input(type = InputType.number, name = "2facode"){
								placeholder = "Verification Code"
								required = true
							}
							input(type = InputType.submit){
								value = "Confirm"
							}
						}
					}else {
						form(action = "?login", method = FormMethod.post) {
							input(type = InputType.email, name = "email"){
								placeholder = "Email"
								required = true
							}
							input(type = InputType.password, name = "pw"){
								placeholder = "Password"
								required = true
							}
							input(type = InputType.submit){
								value = "Login"
							}
						}
					}
				}
			}


			defaultFooter()
		}
	}
}


suspend fun PipelineContext<Unit, ApplicationCall>.loginpost() = run{
	val postparams = call.receiveParameters()
	val id = user?.id ?: -1
	if(call.isGetSet("login")){
		val email = postparams["email"].toString()
		val pw = postparams["pw"].toString()
		val newid = validatePw(email, pw)
		if(newid != -1){
			saveLogin(newid, false)
			call.respondRedirect("/login?2fa")
		}else {
			call.respondRedirect("/login?error=pwdontmatch")
		}
	}else if(call.isGetSet("2fa")){
		val code = postparams["2facode"] as String
		if(validate2fa(code, id)){
			saveLogin(id, true)
			call.respondRedirect("/wallet")
		}else {
			call.respondRedirect("/login?error=wrong2facode&2fa")
		}
	}
}
