package dev.einsjannis.crashwallet.server.logger

import dev.einsjannis.crashwallet.server.createFileIfNotExists
import dev.einsjannis.crashwallet.server.getCurrentTimeStamp
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AccountLogger

val accountLogger = AccountLogger()
private val accountloggingList = mutableListOf<String>()

fun AccountLogger.write(msg: String){
	println(msg)
	accountloggingList.add(msg)
}

fun AccountLogger.log(loggingMessage: String){
	val timeString = "[${getCurrentTimeStamp()}]: "
	accountLogger.write("[Account] $timeString$loggingMessage")
}

fun AccountLogger.startlogfile(){
	GlobalScope.launch {
		while (true){
			delay(3600000)
			var finallogString = ""
			accountloggingList.forEach {
				finallogString += "$it\n"
			}
			val path = "logs/account/${getCurrentTimeStamp().replace("/", " ").replace(":", " ")}.log"
			val file = createFileIfNotExists(path)
			file.writeText(finallogString)
		}
	}
}
