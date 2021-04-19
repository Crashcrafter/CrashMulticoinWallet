package dev.einsjannis.crashwallet.server.website.admin

import io.ktor.application.*
import io.ktor.html.*
import io.ktor.util.pipeline.*
import kotlinx.html.*

suspend fun PipelineContext<Unit, ApplicationCall>.currencyAjax() {
    call.respondHtml {
        body {
            div(classes = "inputbox") {
                p {
                    +"ID: "
                }
                input(type = InputType.number, classes = "short") {
                    placeholder = "Enter ID"
                    name = "id"
                    required = true
                }
            }
            div(classes = "inputbox") {
                p {
                    +"Token Name: "
                }
                input(type = InputType.text) {
                    placeholder = "Enter Token Name"
                    name = "tokenname"
                    required = true
                }
            }
            div(classes = "inputbox") {
                p {
                    +"Token Short: "
                }
                input(type = InputType.text, classes = "short") {
                    placeholder = "Enter Short"
                    name = "tokenshort"
                    required = true
                }
            }
            div(classes = "inputbox") {
                p {
                    +"Explorer Link: "
                }
                input(type = InputType.text, classes = "long") {
                    placeholder = "Enter Explorer Link"
                    name = "explorerlink"
                    required = true
                }
            }
            div(classes = "inputbox") {
                input(type = InputType.submit, classes = "short") {
                    value = "Save"
                }
            }
        }
    }
}