package js

import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.HTMLElement

fun main() {
	window.onload = {
		(document.querySelector(".menu") as HTMLElement).onclick = {
			(document.querySelector("header nav ul") as HTMLElement).classList.toggle("open"); Unit
		}; Unit
	}
}

