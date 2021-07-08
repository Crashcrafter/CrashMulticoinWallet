package de.crash.crashwallet.website.publicpages.account

import de.crash.crashwallet.website.*
import de.crash.crashwallet.website.publicpages.defaultFooter
import de.crash.crashwallet.website.publicpages.defaultHeader
import de.crash.crashwallet.website.publicpages.defaultHeads
import io.ktor.application.*
import io.ktor.html.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.util.pipeline.*
import kotlinx.html.*

suspend fun PipelineContext<Unit, ApplicationCall>.changepwget() = run{
	val userdata = getDefaultUserData(user)
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
				//TODO: Change PW
			}

			defaultFooter()
		}
	}
}

suspend fun PipelineContext<Unit, ApplicationCall>.changepwpost() = run{
	val postparams = call.receiveParameters()
	val id = user!!.id
	if(call.isGetSet("emailverify")){
		if(validateEmailCode(id, postparams["code"].toString())){
			call.respondRedirect("/changepw?newpw")
		}else{
			call.respondRedirect("/changepw?error=wrongcode")
		}
	}else if(call.isGetSet("validatepw")){
		val pw = postparams["pw"].toString()
		val cpw = postparams["cpw"].toString()
		if(pw == cpw){
			changePw(id, pw)
			call.respondRedirect("/account?changepwsuccess")
		}else{
			call.respondRedirect("/changepw?newpw&error=pwdontmatch")
		}
	}
}
