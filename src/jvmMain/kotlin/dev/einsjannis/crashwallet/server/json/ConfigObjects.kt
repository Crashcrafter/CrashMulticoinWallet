package dev.einsjannis.crashwallet.server.json

data class ConfigObj(
	val startedMessage: String,
	val etherscanapitoken: String,
	val bscscanapitoken: String,
	val mailsmtphost: String,
	val mailsmtpport: String,
	val mailaddress: String,
	val mailpassword: String,
	val dbhost: String,
	val dbname: String,
	val dbuser: String,
	val dbpassword: String,
	val dclink: String,
	val twitterlink: String
)
