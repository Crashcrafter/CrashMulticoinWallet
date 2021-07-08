package de.crash.crashwallet

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import de.crash.crashwallet.json.files.ConfigObj
import de.crash.crashwallet.logger.initStatistics
import de.crash.crashwallet.logger.log
import de.crash.crashwallet.logger.mainLogger
import de.crash.crashwallet.logger.startlogfile
import de.crash.crashwallet.wallet.currencies.updatePrices
import de.crash.crashwallet.wallet.initFaucets
import de.crash.crashwallet.website.setMailConfig
import dev.crash.address.AddressType
import java.io.File
import java.util.*
import kotlin.collections.HashMap

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
			val type = AddressType.valueOf(it.key.uppercase(Locale.getDefault()))
			donationAddresses[type] = obj.get(it.key).asText()
		}
	}else {
		mainLogger.log("No donation addresses set, you can fix this issue by creating a donationaddresses.json like in the github repo.")
	}
}