package de.crash.crashwallet.website.publicpages

import kotlinx.html.*

fun BODY.defaultFooter() = run {
	footer {
		div(classes = "footercontainer"){
			div(classes = "footerbox") {
				h3 {
					+"Crash Wallet"
				}
				p{
					a{
						href = "/news"
						+"News"
					}
				}
				p{
					a{
						href = "/donation"
						+"Donation"
					}
				}
			}
			div(classes = "footerbox") {
				h3 {
					+"Social Media"
				}
				p{
					a{
						href = "/twitter"
						+"Twitter"
					}
				}
				p{
					a{
						href = "/discord"
						+"Discord"
					}
				}
			}
			div(classes = "footerbox") {
				h3 {
					+"Support"
				}
				p{
					a{
						href = "/faq"
						+"FAQ"
					}
				}
				p{
					a{
						href = "/contact"
						+"Contact"
					}
				}
			}
			div(classes = "footerbox") {
				h3 {
					+"Legal"
				}
				p{
					a{
						href = "/tos"
						+"Terms of Service"
					}
				}
				p{
					a{
						href = "/privacy-policy"
						+"Privacy Policy"
					}
				}
				p{
					a{
						href = "/cookie-policy"
						+"Cookie Policy"
					}
				}
			}
		}
		div(classes = "copyrightbox") {
			p{
				+"©Crash Wallet 2021. All Rights Reserved."
			}
		}
	}
	script(src = "/js/mobilemenu.js", type = "text/javascript"){}
}

fun BODY.defaultHeader(name: String, role: String, loggedin: Boolean) = header {
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
			li { a(href = "/") { +"Home" } }
			li { a(href = "/wallet"){ +"Wallet" } }
			if(role == "admin" && loggedin){
				li { a(href = "/admin"){ +"Admin" } }
			}
			if(name == "" || !loggedin){
				li { a(href = "/login"){ +"Login" } }
				li { a(href = "/register"){ +"Register" } }
			}else{
				li { a(href = "/account"){ +name } }
			}
		}
	}
}

fun HEAD.defaultHeads() = run {
	meta(charset = "utf-8")
	meta(name = "viewport", content="width=device-width, initial-scale=1")
	link(rel = "preconnect", href = "https://fonts.gstatic.com")
	link(href="https://fonts.googleapis.com/css2?family=Roboto:wght@500&display=swap", rel="stylesheet")
	link(href="https://fonts.googleapis.com/css2?family=Roboto+Slab&display=swap", rel="stylesheet")
	link(rel = "stylesheet", href = "/assets/style.css")
}

fun P.dbr() = run {
	br()
	br()
}
