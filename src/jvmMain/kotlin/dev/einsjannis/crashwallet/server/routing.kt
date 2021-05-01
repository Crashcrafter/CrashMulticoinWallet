package dev.einsjannis.crashwallet.server

import dev.einsjannis.crashwallet.server.website.admin.*
import dev.einsjannis.crashwallet.server.website.publicpages.*
import dev.einsjannis.crashwallet.server.website.publicpages.account.*
import dev.einsjannis.crashwallet.server.website.publicpages.wallet.coinInfo
import dev.einsjannis.crashwallet.server.website.publicpages.wallet.wallet
import io.ktor.application.*
import io.ktor.http.content.*
import io.ktor.response.*
import io.ktor.routing.*

fun Routing.staticPages() {
	staticAssets()
	staticLogos()
	staticUIData()
	staticJS()
}

fun Routing.staticAssets() =
	static("assets") {
		resources("css")
	}

fun Routing.staticLogos() =
	static("logo") {
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
	get("currencies") { adminCurrencies() }
	post("currencies") {adminCurrenciesPost()}
	get("wallet") { adminWallet() }
}
