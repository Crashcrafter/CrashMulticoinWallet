package dev.einsjannis.crashwallet.server

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import dev.einsjannis.crashwallet.server.json.AddressSaveObject
import dev.einsjannis.crashwallet.server.json.TransactionSaveObject
import dev.einsjannis.crashwallet.server.logger.log
import dev.einsjannis.crashwallet.server.logger.mainLogger
import dev.einsjannis.crashwallet.server.wallet.address.Address
import dev.einsjannis.crashwallet.server.wallet.currencies
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File
import java.nio.charset.Charset
import java.text.SimpleDateFormat
import java.util.*

fun initPaths(){
	createDirIfNotExists("logs/")
	createDirIfNotExists("logs/main/")
	createFileIfNotExists("logs/main/current.log")
	createDirIfNotExists("logs/account/")
	createFileIfNotExists("logs/account/current.log")
	createDirIfNotExists("logs/errors/")
	createDirIfNotExists("data/")
	createDirIfNotExists("data/addresses/")
	transaction {
		CurrencyTable.slice(CurrencyTable.short).selectAll().forEach{
			createDirIfNotExists("data/addresses/${it[CurrencyTable.short]}/")
		}
	}
	deleteUnneccessaryData()
}

fun saveAddress(address: Address, userid: Int){
	createDirIfNotExists("data/addresses/${address.type.name.toLowerCase()}/$userid/")
	val file = createFileIfNotExists("data/addresses/${address.type.name.toLowerCase()}/$userid/${address.address}.json")
	val obj = AddressSaveObject(address.address, address.privateKey, getCurrentTimeStamp(), ArrayList<TransactionSaveObject>())
	val mapper = jacksonObjectMapper()
	file.writeText(mapper.writeValueAsString(obj), Charset.defaultCharset())
}

fun createDirIfNotExists(pathname: String){
	val folder = File(pathname)
	if(folder.notexists()){
		folder.mkdir()
	}
}

fun createFileIfNotExists(pathname: String) : File{
	val file = File(pathname)
	if(file.notexists()){
		file.createNewFile()
	}
	return file
}

fun File.notexists() = !this.exists()

fun getCurrentTimeStamp(): String {
	val date = Calendar.getInstance().time
	val dateformat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
	return dateformat.format(date).toString()
}

fun deleteUnneccessaryData(){
	var count = 0
	val ids = mutableListOf<String>()
	transaction {
		AccountTable.slice(AccountTable.id).selectAll().forEach {
			ids += it[AccountTable.id].value.toString()
		}
	}
	val con = getnewDBConnection()
	currencies.forEach {
		File("data/addresses/${it.short}/").listFiles()?.forEach { file ->
			if(!ids.contains(file.name)){
				file.delete()
				count++
			}else{
				val queryString = "SELECT ${it.short} FROM wallets WHERE id=${file.name}"
				val statement = con.prepareStatement(queryString)
				statement.execute()
				val resultset = statement.resultSet
				if(resultset.next()){
					val mainAddress = resultset.getString(it.short)
				}
			}
		}
	}
	con.close()
	mainLogger.log("Deleted $count files at cleanup!")
}
