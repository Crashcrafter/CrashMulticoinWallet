package dev.einsjannis.crashwallet.server.website

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.sessions.*
import io.ktor.util.pipeline.*

data class UserInfo(val id: Int, val loggedin: Boolean)

fun PipelineContext<Unit, ApplicationCall>.saveLogin(id: Int, loggedin : Boolean) {
	this.call.sessions.set("login", UserInfo(id, loggedin))
}

suspend fun PipelineContext<Unit, ApplicationCall>.logout() {
	this.call.sessions.clear("login")
	this.call.respondRedirect("/")
}

val PipelineContext<Unit, ApplicationCall>.user: UserInfo? get() = this.call.sessions.get("login") as? UserInfo
