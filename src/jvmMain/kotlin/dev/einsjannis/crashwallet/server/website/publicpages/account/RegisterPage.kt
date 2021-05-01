package dev.einsjannis.crashwallet.server.website.publicpages.account

import dev.einsjannis.crashwallet.server.*
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
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.mindrot.jbcrypt.BCrypt

suspend fun PipelineContext<Unit, ApplicationCall>.registerget() = run{
	val userdata = getDefaultUserData(user)
	if(userdata.loggedIn){
		call.respondRedirect("/account")
	}
	val id = user?.id
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
					if(call.isGetSet("2fa")){
						verificationmail(id!!)
						img {
							src = create2faQRCode(id)
						}
						br
						div(classes = "centeredTextBox") {
							p {
								+"Scan the code above with your Authenticator App (Google Authenticator is recommend). If the QR-Code does not load refresh the website."
							}
						}
						form(action = "?2fa", method = FormMethod.post) {
							input(type = InputType.number, name = "2facode") {
								required = true
								placeholder = "Enter 2FA Code"
							}
							input(type = InputType.number, name = "emailcode") {
								required = true
								placeholder = "Enter Email Code"
							}
							input(type = InputType.submit) {
								value = "Confirm"
							}
						}
					}else {
						form(action = "?register", method = FormMethod.post) {
							input(type = InputType.email, name = "email") {
								required = true
								placeholder = "Email"
							}
							input(type = InputType.text, name = "username") {
								required = true
								placeholder = "Username"
							}
							input(type = InputType.password, name = "pw") {
								required = true
								placeholder = "Password"
							}
							input(type = InputType.password, name = "cpw") {
								required = true
								placeholder = "Confirm Password"
							}
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
								+"I want to receive News about Crash Wallet via email"
							}
							br()
							input(type = InputType.submit) {
								value = "Register"
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
	val id = user?.id ?: -1
	if(call.isGetSet("register")){
		val email = postparams["email"].toString()
		val username = postparams["username"].toString()
		val pw = postparams["pw"].toString()
		val cpw = postparams["cpw"].toString()
		val newsletter = postparams["newsletter"].toBoolean()
		var redirect = "/register?2fa"
		transaction {
			if(newsletter) NewsletterTable.insert { it[NewsletterTable.email] = email }
			if(!AccountTable.select(where = {AccountTable.email eq email}).empty()) redirect = "/register?error=accountemailalreadyexists"
			if(!AccountTable.select(where = {AccountTable.username eq username}).empty()) redirect = "/register?error=accountusernamealreadyexists"
			if(pw != cpw) redirect = "/register?error=pwdontmatch"
			if(redirect == "/register?2fa"){
				val newid = AccountTable.insertAndGetId {
					it[AccountTable.email] = email
					it[AccountTable.username] = username
					it[AccountTable.pwhash] = BCrypt.hashpw(pw, BCrypt.gensalt(7))
				}.value
				saveLogin(newid, false)
			}
		}
		call.respondRedirect(redirect)
	}else if(call.isGetSet("2fa")){
		val twofactorCode = postparams["2facode"].toString()
		val emailCode = postparams["emailcode"].toString()
		if(!validateEmailCode(id, emailCode)) call.respondRedirect("/register?2fa&error=wrongemailcode")
		if(!validate2fa(twofactorCode, id)) call.respondRedirect("/register?2fa&error=wrong2facode")
		transaction {
			AccountTable.update(where = {AccountTable.id eq id}){
				it[AccountTable.role] = "user"
			}
		}
		saveLogin(id, true)
		registerWallets(id, true)
		call.respondRedirect("/wallet")
	}
}
