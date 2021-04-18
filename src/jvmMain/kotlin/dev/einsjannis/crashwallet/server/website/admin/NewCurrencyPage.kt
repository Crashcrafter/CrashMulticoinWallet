package dev.einsjannis.crashwallet.server.website.admin

import dev.einsjannis.crashwallet.server.website.publicpages.defaultFooter
import dev.einsjannis.crashwallet.server.website.publicpages.defaultHeads
import dev.einsjannis.crashwallet.server.website.user
import io.ktor.application.*
import io.ktor.html.*
import io.ktor.util.pipeline.*
import kotlinx.html.*

suspend fun PipelineContext<Unit, ApplicationCall>.createNewCurrencyPage(){
    val type = call.parameters["addnew"].toString()
    if(type != ""){
        when(type) {
            "currency" -> {
                call.respondHtml {
                    body {
                        input(type = InputType.text) {

                        }
                    }
                }
            }
            else -> call.respondHtml { body { p { +"Not supported type $type" } } }
        }
    }
    call.respondHtml {
        head {
            defaultHeads()
            link(rel = "stylesheet", href = "/assets/admin.css")
            title("Add new currency - Crash Wallet")
        }
        body {
            defaultAdminHeader(user!!.id)

            section {
                hr()
                div(classes = "contentcontainer") {
                    h1 {
                        +"Add new Currency"
                    }
                    div(classes = "content") {
                        form {
                            method = FormMethod.post
                            action = "/admin/currencies?addNew"
                            div(classes = "newajaxcontent") {
                                select {
                                    id = "assettype"
                                    onChange = "loadnewcurrencyform()"
                                    required = true
                                    name = "assettype"
                                    option {
                                        value = "unselected"
                                        selected = true
                                        +"Select Type"
                                    }
                                    option {
                                        value = "currency"
                                        +"Currency"
                                    }
                                    option {
                                        value = "erctoken"
                                        +"ERC-20 Token"
                                    }
                                    option {
                                        value = "beptoken"
                                        +"BEP-20 Token"
                                    }
                                    option {
                                        value = "trc10token"
                                        +"TRC-10 Token"
                                    }
                                    option {
                                        value = "trc20token"
                                        +"TRC-20 Token"
                                    }
                                }
                            }
                            div(classes = "newcurrencyAjax")
                        }
                    }
                }
            }

            defaultFooter()
            script(src = "/js/admin.js", type = "text/javascript"){}
        }
    }
}