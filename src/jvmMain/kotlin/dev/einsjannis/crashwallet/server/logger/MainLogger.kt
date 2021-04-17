package dev.einsjannis.crashwallet.server.logger

import dev.einsjannis.crashwallet.server.createFileIfNotExists
import dev.einsjannis.crashwallet.server.getCurrentTimeStamp
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainLogger

val mainLogger = MainLogger()
private val mainloggingList = mutableListOf<String>()

fun MainLogger.write(msg: String){
	println(msg)
	mainloggingList.add(msg)
}

fun MainLogger.log(loggingMessage: String){
	val timeString = "[${getCurrentTimeStamp()}]: "
	this.write("[Main] $timeString$loggingMessage")
}

fun MainLogger.startlogfile(){
	GlobalScope.launch {
		while (true){
			delay(3600000)
			var finallogString = ""
			mainloggingList.forEach {
				finallogString += "$it\n"
			}
			val path = "logs/main/${getCurrentTimeStamp().replace("/", " ").replace(":", " ")}.log"
			val file = createFileIfNotExists(path)
			file.writeText(finallogString)
		}
	}
}
