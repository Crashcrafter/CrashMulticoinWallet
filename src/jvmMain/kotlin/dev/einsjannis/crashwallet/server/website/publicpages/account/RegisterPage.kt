package dev.einsjannis.crashwallet.server.website.publicpages.account

import dev.einsjannis.crashwallet.server.*
import dev.einsjannis.crashwallet.server.logger.accountLogger
import dev.einsjannis.crashwallet.server.logger.log
import dev.einsjannis.crashwallet.server.wallet.registerWallets
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
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.net.URL

suspend fun PipelineContext<Unit, ApplicationCall>.registerget() = run{
	val userdata = getDefaultUserData(user)
	if(userdata.loggedIn){
		call.respondRedirect("/")
	}
	val id = user?.id
	if(id == null || call.request.queryParameters.entries().isEmpty()){
		call.respondRedirect("/login")
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
					hr()
					h1 { +"Register" }
					when {
						call.isGetSet("2fa") -> {
							var name = ""
							transaction {
								AccountTable.slice(AccountTable.username).select(where = { AccountTable.id eq id}).forEach{
									name = it[AccountTable.username].toString()
									return@forEach
								}
							}
							val secretCode = randomCode(16)
							val qrcodehtml = URL("https://www.authenticatorapi.com/pair.aspx?AppName=CrashWallet&AppInfo=$name&SecretCode=$secretCode").readText()
							val imgsrcurl = extractURLs(qrcodehtml)[1]
							img {
								src = imgsrcurl
							}
							br
							div(classes = "centeredTextBox") {
								p {
									+"Scan the code above with your Authenticator App (Google Authenticator is recommend). If the QR-Code does not load refresh the website."
								}
							}
							form(action = "?confirm2facode", method = FormMethod.post) {
								input(type = InputType.number, name = "2facode") {
									placeholder = "Enter 2FA Code"
									required = true
								}
								input(type = InputType.hidden, name = "secret") {
									value = secretCode
								}
								input(type = InputType.submit) {
									value = "Next"
								}
							}
						}
						call.isGetSet("pw") -> {
							form(action = "?pwvalidate", method = FormMethod.post) {
								input(type = InputType.password, name = "pw") {
									placeholder = "Enter Password"
									required = true
								}
								input(type = InputType.password, name = "cpw") {
									placeholder = "Confirm Password"
									required = true
								}
								input(type = InputType.submit) {
									value = "Next"
								}
							}
						}
						call.isGetSet("username") -> {
							form(action = "?usernamecheck", method = FormMethod.post) {
								input(type = InputType.text, name = "username") {
									placeholder = "Enter Username"
									required = true
								}
								input(type = InputType.submit) {
									value = "Next"
								}
							}
						}
						call.isGetSet("emailverify") -> {
							verificationmail(id!!)
							form(action = "?emailverify", method = FormMethod.post) {
								input(type = InputType.text, name = "code") {
									placeholder = "Enter Verification Code"
									required = true
								}
								br()
								label {
									input(type = InputType.checkBox, name = "tos") {
										required = true
									}
									+"I agree to the "
									a(href = "/tos", target = "_blank") {+"ToS"}
									+" and the "
									a(href = "/privacy-policy", target = "_blank") {+"Privacy Policy"}
								}
								br()
								br()
								label {
									input(type = InputType.checkBox, name = "newsletter") {
										required = false
									}
									+"I want to receive News about this website via email"
								}
								br()
								input(type = InputType.submit) {
									value = "Next"
								}
							}
						}
					}
				}
			}

			defaultFooter()
		}
	}
}

suspend fun PipelineContext<Unit, ApplicationCall>.registerpost() = run{
	val postparams = call.receiveParameters()
	val id = user?.id ?: 0
	if(call.isGetSet("confirm2facode")){
		val secret = postparams["secret"].toString()
		val pin = postparams["2facode"]!!
		if(validate2fa(pin, secret)){
			transaction {
				AccountTable.update(where = { AccountTable.id eq id}){
					it[twoFactorCode] = secret
				}
			}
			accountLogger.log("Account $id confirmed 2facode on registration")
			accountLogger.log("Account $id registered successfully")
			saveLogin(id, true)
			registerWallets(id, true)
			call.respondRedirect("/wallet")
		}else{
			accountLogger.log("Account $id entered wrong 2facode on registration")
			call.respondRedirect("/register?confirm2fa&error=wrongcode")
		}
	}else if(call.isGetSet("pwvalidate")){
		if(postparams["pw"].toString() == postparams["cpw"].toString()){
			changePw(id, postparams["pw"].toString())
			accountLogger.log("Account ${user!!.id} entered password on registration")
			call.respondRedirect("/register?username")
		}else{
			accountLogger.log("Account ${user!!.id} entered not matching password on registration")
			call.respondRedirect("/register?pw&error=pwmatch")
		}
	}else if(call.isGetSet("usernamecheck")){
		var existsUsername = true
		val username = postparams["username"]
		transaction {
			existsUsername = !AccountTable.slice(AccountTable.username).select(where = { AccountTable.username eq username}).empty()
		}
		if(existsUsername){
			accountLogger.log("Account ${user!!.id} entered existing username on registration")
			call.respondRedirect("/register?username&error=userexists")
		}else{
			transaction {
				AccountTable.update(where = { AccountTable.id eq id}){
					it[AccountTable.username] = username
				}
			}
			accountLogger.log("Account ${user!!.id} entered username on registration")
			call.respondRedirect("/register?2fa")
		}
	}else if(call.isGetSet("emailverify")){
		if(verifyEmailCode(id, postparams["code"].toString())){
			accountLogger.log("Account ${user!!.id} verificated email on registration")
			call.respondRedirect("/register?pw")
		}else{
			accountLogger.log("Account ${user!!.id} entered wrong email code on registration")
			call.respondRedirect("/register?emailverify&error=wrongemailcode")
		}
	}
}
