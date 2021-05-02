package dev.einsjannis.crashwallet.server

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jacksonMapperBuilder
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import dev.einsjannis.crashwallet.server.json.files.ConfigObj
import dev.einsjannis.crashwallet.server.logger.*
import dev.einsjannis.crashwallet.server.wallet.AddressType
import dev.einsjannis.crashwallet.server.wallet.currencies.updatePrices
import dev.einsjannis.crashwallet.server.wallet.initAddressType
import dev.einsjannis.crashwallet.server.wallet.initFaucets
import dev.einsjannis.crashwallet.server.website.setMailConfig
import java.io.File

var DCLink = ""
var TwitterLink = "/"
var etherscanioApiKey = ""
var bscscanApiKey = ""
val donationAddresses = HashMap<AddressType, String>()

fun setupServer(){
	loadConfigs()
	initStatistics()
	initDatabase()
	updatePrices()
	initPaths()
	initAddressType()
	initFaucets()
	setupDonationAddresses()
	mainLogger.startlogfile()
	mainLogger.log("Loaded DB and prices!")
}

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

fun setupDonationAddresses(){
	val donationFile = File("donationaddresses.json")
	if(donationFile.exists()){
		val jsonString = donationFile.readText()
		val obj = jacksonObjectMapper().readTree(jsonString)
		obj.fields().forEach {
			val type = AddressType.valueOf(it.key.toUpperCase())
			donationAddresses[type] = obj.get(it.key).asText()
		}
	}else {
		mainLogger.log("No donation addresses set, you can fix this issue by creating a donationaddresses.json like in the github repo.")
	}
}