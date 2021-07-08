package de.crash.crashwallet.website.publicpages.account

import io.ktor.application.*
import io.ktor.html.*
import io.ktor.util.pipeline.*
import kotlinx.html.body
import kotlinx.html.h1

suspend fun PipelineContext<Unit, ApplicationCall>.accountSettings(){
    call.respondHtml {
        body {
            h1 {
                +"Accountsettings"
            }
        }
    }
}