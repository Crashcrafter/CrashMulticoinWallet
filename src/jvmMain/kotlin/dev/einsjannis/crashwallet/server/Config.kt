package dev.einsjannis.crashwallet.server

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import dev.einsjannis.crashwallet.server.json.ConfigObj
import dev.einsjannis.crashwallet.server.logger.log
import dev.einsjannis.crashwallet.server.logger.mainLogger
import dev.einsjannis.crashwallet.server.website.setMailConfig
import java.io.File

var DCLink = ""
var TwitterLink = "/"
var etherscanioApiKey = ""
var bscscanApiKey = ""
val disabledAssets = mutableListOf("theta", "tfuel")

fun loadConfigs(){
	val file = File("config.json")
	val configjson = file.readText()
	val mapper = jacksonObjectMapper()
	val obj = mapper.readValue<ConfigObj>(configjson)
	mainLogger.log(obj.startedMessage)
	DCLink = obj.dclink
	TwitterLink = obj.twitterlink
	setDBConfig(obj.dbhost, obj.dbname, obj.dbuser, obj.dbpassword)
	setMailConfig(obj.mailsmtphost, obj.mailsmtpport.toInt(), obj.mailaddress, obj.mailpassword)
	etherscanioApiKey = obj.etherscanapitoken
	bscscanApiKey = obj.bscscanapitoken
}
