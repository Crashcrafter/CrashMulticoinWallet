package de.crash.crashwallet

import java.net.URL
import java.nio.charset.Charset
import javax.net.ssl.HttpsURLConnection

fun URL.get(): String{
    val con: HttpsURLConnection = this.openConnection() as HttpsURLConnection
    con.requestMethod = "GET"
    con.setRequestProperty("Content-Type", "application/json")
    con.setRequestProperty(
        "User-Agent",
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36"
    )
    return con.inputStream.readBytes().toString(Charset.defaultCharset())
}