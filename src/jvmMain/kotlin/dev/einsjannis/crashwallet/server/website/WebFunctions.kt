package dev.einsjannis.crashwallet.server.website

import dev.einsjannis.crashwallet.server.AccountTable
import dev.einsjannis.crashwallet.server.DefaultUserData
import dev.einsjannis.crashwallet.server.MailingListTable
import dev.einsjannis.crashwallet.server.exceptions.UnauthorizedException
import dev.einsjannis.crashwallet.server.logger.accountLogger
import dev.einsjannis.crashwallet.server.logger.log
import io.ktor.application.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.glxn.qrgen.javase.QRCode
import org.apache.commons.mail.DefaultAuthenticator
import org.apache.commons.mail.SimpleEmail
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.mindrot.jbcrypt.BCrypt
import java.net.URL
import java.util.*
import kotlin.random.Random

private var EmailHost = ""
private var EmailPort = 0
var EmailAddress = ""
private var EmailPassword = ""

fun setMailConfig(mailhost: String, mailport: Int, mailaddress: String, mailpassword: String){
	EmailHost = mailhost
	EmailPort = mailport
	EmailAddress = mailaddress
	EmailPassword = mailpassword
}

fun userOnly(name : String, loggedin : Boolean){
	if(name == "" || !loggedin){
		throw UnauthorizedException()
	}
}

fun adminOnly(userdata: DefaultUserData){
	if(userdata.role != "admin" || userdata.name == "" || !userdata.loggedIn){
		throw UnauthorizedException()
	}
}

fun randomCode(length: Int): String {
	val charPool = "1234567890qwertzuiopasdfghjklyxcvbnmQWERTZUIOPASDFGHJKLYXCVBNM"
	return (1..length)
		.map { Random.nextInt(0, charPool.length) }
		.map(charPool::get)
		.joinToString("")
}

fun randomIntCode(length: Int): String {
	val charPool = "1234567890"
	return (1..length)
		.map { Random.nextInt(0, charPool.length) }
		.map(charPool::get)
		.joinToString("")
}

fun extractURLs(input : String) : MutableList<String>{
	val result = mutableListOf<String>()
	input.split('\'').forEach {
		if(it.startsWith("http")){
			result.add(it)
		}
	}
	return result
}

fun validate2fa(pin : String, secret : String) : Boolean{
	val validate = URL("https://www.authenticatorApi.com/Validate.aspx?Pin=$pin&SecretCode=$secret").readText()
	if(validate == "True"){
		return true
	}
	return false
}

fun verificationmail(id: Int){
	GlobalScope.launch {
		var emailaddress = ""
		val code = randomIntCode(6)
		transaction {
			AccountTable.slice(AccountTable.email).select(where = { AccountTable.id eq id}).forEach {
				emailaddress = it[AccountTable.email]
				return@forEach
			}
			AccountTable.update(where = { AccountTable.id eq id}){
				it[emailcode] = code
			}
		}
		val email =  SimpleEmail()
		email.hostName = EmailHost
		email.setSmtpPort(EmailPort)
		email.setAuthenticator(DefaultAuthenticator(EmailAddress, EmailPassword))
		email.isSSLOnConnect = true
		email.setFrom(EmailAddress)
		email.subject = "Verification"
		email.setMsg("Verification Code:\n\n$code\n\nDon't reply to this mail!")
		email.addTo(emailaddress)
		email.send()
		accountLogger.log("Sent verification mail to ID $id")
	}
}

fun getDefaultUserData(userInfo: UserInfo?) : DefaultUserData {
	if(userInfo == null) return DefaultUserData("", "", false)
	var name = ""
	var role = ""
	val userId = userInfo.id
	var exists = false
	transaction {
		exists = !AccountTable.select { AccountTable.id eq userId }.empty()
		if(exists){
			AccountTable.select { AccountTable.id eq userId }.firstOrNull()?.let {
				name = it[AccountTable.username].toString()
				role = it[AccountTable.role].toString()
			}
		}
	}
	if(!exists){
		return DefaultUserData("", "", false)
	}
	val loggedin = userInfo.loggedin
	return DefaultUserData(name, role, loggedin)
}

fun verifyEmailCode(userid: Int, code: String): Boolean{
	var verify = false
	transaction {
		var email = ""
		AccountTable.slice(AccountTable.emailcode, AccountTable.email).select(where = { AccountTable.id eq userid}).forEach{
			if(code == it[AccountTable.emailcode].toString()){
				verify = true
				email = it[AccountTable.email]
			}
		}
		if(verify){
			MailingListTable.insert {
				it[MailingListTable.email] = email
			}
		}
	}
	return verify
}

fun changePw(userid: Int, pw: String){
	val hashed = BCrypt.hashpw(pw, BCrypt.gensalt(7))
	transaction {
		AccountTable.update(where = { AccountTable.id eq userid}){
			it[pwhash] = hashed
		}
	}
}

fun ApplicationCall.isGetSet(param: String): Boolean = this.request.queryParameters[param] != null

fun genQRCode(text: String, width: Int, height: Int) : String{
	val bytes = QRCode.from(text).withSize(width, height).stream().toByteArray()
	return Base64.getEncoder().encodeToString(bytes)
}
