package de.crash.crashwallet.website.admin

import kotlinx.html.*

fun BODY.defaultAdminHeader(userid: Int) = header {
	div(classes = "logo") {
		a(href = "/") {
			h1 {
				+"Crash Wallet"
			}
		}
	}
	nav {
		img(classes="menu", src = "ui/menu_bar.png", alt = "menu")
		ul {
			li { a(href = "/admin/users") {+"Users"} }
			if(userid == 1){
				li { a(href = "/admin/wallet") { +"Admin Wallet" } }
			}
			li { a(href = "/account") { +"Account" } }
		}
	}
}
