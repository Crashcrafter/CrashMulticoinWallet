package dev.einsjannis.crashwallet.server

import dev.einsjannis.crashwallet.server.website.admin.adminDB
import dev.einsjannis.crashwallet.server.website.admin.adminHome
import dev.einsjannis.crashwallet.server.website.admin.adminUsers
import dev.einsjannis.crashwallet.server.website.admin.adminWallet
import dev.einsjannis.crashwallet.server.website.publicpages.*
import io.ktor.application.*
import io.ktor.http.content.*
import io.ktor.response.*
import io.ktor.routing.*

fun Routing.staticPages() {
	staticAssets()
	staticUIData()
	staticJS()
}

fun Routing.staticAssets() =
	static("assets") {
		resources("css")
		resources("img/logo")
	}

fun Routing.staticUIData() =
	static("ui") {
		resources("img/ui")
	}

fun Routing.staticJS() =
	static("js") {
		resources("js")
	}

fun Routing.dynamicRoutes() = route("/") {
	get { home() }
	walletRoute()
	login()
	register()
	get("account") { account() }
	changePasswordRoute()
	get("tos") { tos() }
	get("privacy-policy") { dataprivacy() }
	get("cookie-policy") { cookiepolicy() }
	get("contact") { contact() }
	get("news") { news() }
	get("donation") { donation() }
	get("faq") { faq() }
	get("discord") { call.respondRedirect(DCLink) }
	get("twitter") { call.respondRedirect(TwitterLink) }
	adminRoute()
}

fun Route.walletRoute() = route("wallet") {
	get { wallet() }
	get("info") { coinInfo() }
}

fun Route.login() = route("login") {
	get { loginget() }
	post { loginpost() }
}

fun Route.register() = route("register") {
	get { registerget() }
	post { registerpost() }
}

fun Route.changePasswordRoute() = route("changepw") {
	get { changepwget() }
	post { changepwpost() }
}

fun Route.adminRoute() = route("admin") {
	get { adminHome() }
	get("users") { adminUsers() }
	get("db") { adminDB() }
	get("wallet") { adminWallet() }
}
