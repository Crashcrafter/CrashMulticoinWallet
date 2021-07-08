package de.crash.crashwallet

import de.crash.crashwallet.exceptions.UnauthorizedException
import de.crash.crashwallet.logger.errorLogger
import de.crash.crashwallet.logger.log
import de.crash.crashwallet.website.UserInfo
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

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