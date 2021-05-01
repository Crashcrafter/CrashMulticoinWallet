package dev.einsjannis.crashwallet.server

import dev.einsjannis.crashwallet.server.exceptions.UnauthorizedException
import dev.einsjannis.crashwallet.server.logger.*
import dev.einsjannis.crashwallet.server.website.*
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*

fun Application.module() {
	setupServer()
	install(Sessions){
		cookie<UserInfo>("login"){
			cookie.maxAgeInSeconds = 86400
		}
	}
	install(StatusPages) {
		exception<Throwable> { it1 ->
			call.respondText(status = HttpStatusCode.InternalServerError) { "500 - Internal Server Error\nPlease contact the server owner and let him know of this issue!" }
			errorLogger.log(it1)
			it1.printStackTrace()
		}
		exception<UnauthorizedException> {
			call.respondRedirect("/login")
		}
		exception<Exception> { it1 ->
			errorLogger.log(it1)
			it1.printStackTrace()
		}
		status(HttpStatusCode.NotFound){
			call.respond(HttpStatusCode.NotFound)
		}
	}
	routing {
		staticPages()
		dynamicRoutes()
	}
}
