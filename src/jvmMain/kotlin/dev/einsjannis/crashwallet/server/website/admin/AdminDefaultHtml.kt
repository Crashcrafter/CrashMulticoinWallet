package dev.einsjannis.crashwallet.server.website.admin

import kotlinx.html.*

fun BODY.defaultAdminFooter() = run {
	footer {
		p{
			+"©Crash Wallet 2021. All Rights Reserved. You are on the Admin Page!"
		}
	}
	script(src="https://code.jquery.com/jquery-1.12.4.min.js", type="text/javascript"){}
	script(src = "/js/mobilemenu.js", type = "text/javascript"){}
}

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
			li { a(href = "/admin/db") { +"DB" } }
			li { a(href = "/admin/users") {+"Users"} }
			if(userid == 1){
				li { a(href = "/admin/wallet") { +"Admin Wallet" } }
			}
			li { a(href = "/account") { +"Account" } }
		}
	}
}
