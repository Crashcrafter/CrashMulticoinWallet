package dev.einsjannis.crashwallet.server.logger

import dev.einsjannis.crashwallet.server.createFileIfNotExists
import dev.einsjannis.crashwallet.server.getCurrentTimeStamp
import java.io.File

class ErrorLogger

val errorLogger = ErrorLogger()

fun ErrorLogger.write(msg: String){
	val path = "logs/errors/${getCurrentTimeStamp().replace("/", " ").replace(":", " ")}.log"
	val file = createFileIfNotExists(noDuplicateFile(path))
	file.writeText(msg)
}

fun ErrorLogger.log(loggingMessage: String){
	val timeString = "[${getCurrentTimeStamp()}]: "
	this.write("[ERROR] $timeString$loggingMessage")
	mainLogger.log("Error catched, see error log for more details")
}

fun ErrorLogger.log(throwable: Throwable){
	var finalerrorString = ""
	throwable.stackTrace.forEach {
		finalerrorString += it.toString() + "\n"
	}
	this.write(finalerrorString)
	mainLogger.log("Error catched, see error log for more details")
}

private fun noDuplicateFile(path: String) : String{
	val finalpath = path
	if(File(finalpath).exists()){
		return noDuplicateFile(finalpath + "1")
	}
	return finalpath
}
