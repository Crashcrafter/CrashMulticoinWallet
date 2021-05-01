package dev.einsjannis.crashwallet.server.website.admin

import dev.einsjannis.crashwallet.server.CurrencyTable
import dev.einsjannis.crashwallet.server.wallet.currencies.deleteCurrency
import dev.einsjannis.crashwallet.server.website.*
import dev.einsjannis.crashwallet.server.website.publicpages.defaultFooter
import dev.einsjannis.crashwallet.server.website.publicpages.defaultHeads
import io.ktor.application.*
import io.ktor.html.*
import io.ktor.response.*
import io.ktor.util.pipeline.*
import kotlinx.html.*
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File

suspend fun PipelineContext<Unit, ApplicationCall>.adminCurrencies() = run{
	val userData : DefaultUserData = getDefaultUserData(user)

	adminOnly(userData)

	if(call.isGetSet("addnew")) createNewCurrencyPage()

	val currenciesObjList = mutableListOf<CurrencyTableObject>()
	transaction {
		CurrencyTable.selectAll().forEach {
			currenciesObjList.add(CurrencyTableObject(it[CurrencyTable.id], it[CurrencyTable.name], it[CurrencyTable.short], it[CurrencyTable.img], it[CurrencyTable.explorerLink]))
		}
	}
	call.respondHtml {
		head {
			defaultHeads()
			link(rel = "stylesheet", href = "/assets/admin.css")
			title("Currencies - Crash Wallet")
		}
		body {
			defaultAdminHeader(user!!.id)

			section {
				hr()
				div(classes = "contentcontainer") {
					h1 {
						+"Currency Table ("
						button {
							onClick = "currencyaddnew()"
							+"Add new"
						}
						+")"
					}
					div(classes = "content") {
						div(classes = "header") {
							div(classes = "column2") {
								p {
									+"ID"
								}
							}
							div(classes = "column3") {
								p {
									+"Name"
								}
							}
							div(classes = "column2") {
								p {
									+"Short"
								}
							}
							div(classes = "column4") {
								p {
									+"IMG Path"
								}
							}
							div(classes = "column5") {
								p {
									+"Explorer Link"
								}
							}
							div(classes = "column3 noborder") {
								p {
									+"Actions"
								}
							}
						}
						currenciesObjList.forEach {
							div(classes = "${it.short} currencybox") {
								div(classes = "column2") {
									p {
										+"${it.id}"
									}
								}
								div(classes = "column3") {
									p {
										+it.name
									}
								}
								div(classes = "column2") {
									p {
										+it.short
									}
								}
								div(classes = "column4") {
									p {
										+it.imgPath
									}
								}
								div(classes = "column5") {
									p {
										+it.explorerLink
									}
								}
								div(classes = "column3 noborder") {
									//TODO: Add Edit and Delete Function
									//TODO: On delete: 2fa confirmation
									/*button {
										onClick = "currencydelete(${it.id})"
										+"Delete"
									}*/
									button {
										onClick = "currencyedit(${it.id})"
										+"Edit"
									}
								}
							}
						}
					}
				}
			}

			defaultFooter()
			script(src = "/js/admin.js", type = "text/javascript"){}
		}
	}
}

private data class CurrencyTableObject(val id: Int, val name: String, val short: String, val imgPath: String, val explorerLink: String)

suspend fun PipelineContext<Unit, ApplicationCall>.adminCurrenciesPost(){
	val userData : DefaultUserData = getDefaultUserData(user)
	val postparams = call.parameters
	adminOnly(userData)
	if(call.isGetSet("delete")){
		val deleteid = postparams["delete"].toString().toInt()
		if(deleteCurrency(deleteid)){
			call.respondText("success")
		}else{
			call.respondText("error")
		}
	}else if(call.isGetSet("addnew")){
		if(postparams["assettype"].toString() == "currency"){
			transaction {
				val postid = postparams["id"].toString().toInt()
				val tokenname = postparams["tokenname"].toString()
				if(File("src/jvmMain/resources/img/logo/$tokenname.png").exists()){
					if(CurrencyTable.select(where = {CurrencyTable.id eq postid}).empty()){
						CurrencyTable.insert {
							it[id] = postid
							it[name] = tokenname
							it[short] = postparams["tokenshort"].toString()
							it[img] = "/assets/${tokenname.toLowerCase().replace(" ", "")}.png"
							it[explorerLink] = postparams["explorerlink"].toString()
						}
					}
				}
			}
		}else println(postparams["assettype"].toString())
		call.respondRedirect("/admin/currencies")
	}
}