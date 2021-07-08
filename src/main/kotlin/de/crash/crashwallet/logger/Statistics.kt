package de.crash.crashwallet.logger

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private var dbconnectcount = 0

fun dbConnectCount() { dbconnectcount++}

fun initStatistics(){
	GlobalScope.launch {
		while (true){
			delay(600000)
			mainLogger.log("Created $dbconnectcount Connections via java mysql last 10 minutes")
			dbconnectcount = 0
		}
	}
}
