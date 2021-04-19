package dev.einsjannis.crashwallet.server

import dev.einsjannis.crashwallet.server.logger.dbConnectCount
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.TransactionManager
import java.sql.Connection
import java.sql.DriverManager

var DBHost = ""
private var DBName = ""
private var DBUser = ""
private var DBPassword = ""

fun setDBConfig(dbhost: String, dbname: String, dbuser: String, dbpassword:String){
	DBHost = dbhost
	DBName = dbname
	DBUser = dbuser
	DBPassword = dbpassword
}

fun getnewDBConnection() : Connection {
	dbConnectCount()
	return DriverManager.getConnection("jdbc:mysql://$DBHost/$DBName", DBUser, DBPassword)
}

fun initDatabase() {
	val db = Database.connect("jdbc:mysql://$DBHost/$DBName", user = DBUser, password = DBPassword)
	TransactionManager.defaultDatabase = db
}

object AccountTable : IntIdTable("accounts"){
	val email = varchar("email", 320)
	val pwhash = text("pwhash").nullable()
	val username = varchar("username", 20).nullable()
	val twoFactorCode = varchar("2fasecret", 16).nullable()
	val role = varchar("role", 20).default("user")
	val emailcode = varchar("emailcode", 6).nullable()
}

object CurrencyTable : Table("currencies"){
	val id = integer("id")
	val name = varchar("name", 30)
	val short = varchar("short", 10)
	val img = varchar("img", 30)
	val explorerLink = text("explorer_link")
}

object MailingListTable : Table("mailinglist"){
	val email = varchar("email", 320)
}
//Add new currency:
//INSERT INTO `currencies`(`id`, `name`, `short`, `img`, `explorer_link`) VALUES ('4', 'Tron','trx','/assets/tron.png', 'https://tronscan.org/#')

object FaucetTable: Table("faucets"){
	val name = varchar("name", 50)
	val shortdesc = varchar("shortdesc", 300)
	val payoutrate = varchar("payoutrate", 10)
	val currencies = varchar("currency", 10)
	val link = varchar("link", 200)
}

data class DefaultUserData(val name: String, val role : String, val loggedIn : Boolean)
