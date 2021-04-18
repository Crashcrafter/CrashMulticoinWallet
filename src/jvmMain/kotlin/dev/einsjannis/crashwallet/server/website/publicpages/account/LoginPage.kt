package dev.einsjannis.crashwallet.server.website.publicpages.account

import dev.einsjannis.crashwallet.server.*
import dev.einsjannis.crashwallet.server.logger.accountLogger
import dev.einsjannis.crashwallet.server.logger.log
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
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.mindrot.jbcrypt.BCrypt

suspend fun PipelineContext<Unit, ApplicationCall>.loginget() = run{
	val userdata = getDefaultUserData(user)
	if(userdata.loggedIn){
		accountLogger.log("User ${user?.id} is already logged in")
		call.respondRedirect("/")
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

					when {
						call.isGetSet("pw") -> {
							accountLogger.log("Account ${user!!.id} enters password on login")
							h1 { +"Login" }
							form(action = "?pwvalidate", method = FormMethod.post) {
								input(type = InputType.password, name = "pw"){
									placeholder = "Password"
									required = true
								}
								input(type = InputType.submit){
									value = "Login"
								}
							}
						}
						call.isGetSet("2fa") -> {
							accountLogger.log("Account ${user!!.id} enters 2facode on login")
							h1 { +"Login" }
							form(action = "?confirm", method = FormMethod.post) {
								input(type = InputType.number, name = "2facode"){
									placeholder = "Verification Code"
									required = true
								}
								input(type = InputType.submit){
									value = "Check"
								}
							}
						}
						else -> {
							h1 { +"Login/Register" }
							form(action = "?email", method = FormMethod.post) {
								input(type = InputType.email, name = "email"){
									placeholder = "Email"
									required = true
								}
								input(type = InputType.submit){
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


suspend fun PipelineContext<Unit, ApplicationCall>.loginpost() = run{
	val postparams = call.receiveParameters()
	val id = user?.id ?: 0
	if(call.isGetSet("email")){
		val email2 = postparams["email"]
		if(email2 != null){
			var accExists = false
			transaction {
				accExists = !AccountTable.select{ AccountTable.email eq email2}.empty()
			}
			if(accExists){
				var path = "/login?pw"
				transaction {
					AccountTable.select(where = { AccountTable.email eq email2}).forEach {
						when {
							it[AccountTable.pwhash] == null -> path = "/register?emailverify"
							it[AccountTable.username] == null -> path = "/register?username"
							it[AccountTable.twoFactorCode] == null -> path = "/register?2fa"
						}
						saveLogin(it[AccountTable.id].value, false)
						return@forEach
					}
				}
				call.respondRedirect(path)
			}else{
				var id2 = 0
				transaction {
					id2 = AccountTable.insertAndGetId {
						it[email] = email2
					}.value
				}
				saveLogin(id2, false)
				accountLogger.log("Account $id2 started registration")
				call.respondRedirect("/register?emailverify")
			}
		}else{
			println("email = null")
		}
	}else if(call.isGetSet("pwvalidate")){
		val pw = postparams["pw"]
		var checked = false
		transaction {
			AccountTable.slice(AccountTable.pwhash).select(where = { AccountTable.id eq id}).forEach{
				checked = BCrypt.checkpw(pw, it[AccountTable.pwhash])
				return@forEach
			}
		}
		if(checked){
			call.respondRedirect("/login?2fa")
		}else{
			accountLogger.log("Account $id entered wrong password on login")
			call.respondRedirect("/login?pw&error=wrongpw")
		}
	}else if(call.isGetSet("confirm")){
		val pin = postparams["2facode"] as String
		var secret = ""
		transaction {
			AccountTable.slice(AccountTable.twoFactorCode).select(where = { AccountTable.id eq id}).forEach {
				secret = it[AccountTable.twoFactorCode].toString()
				return@forEach
			}
		}
		if(validate2fa(pin, secret)){
			println("$id logged in")
			saveLogin(id, true)
			call.respondRedirect("/")
		}else{
			accountLogger.log("Account ${user!!.id} entered wrong 2facode on login")
			call.respondRedirect("/login?2fa&error=wrong_code")
		}
	}
}
