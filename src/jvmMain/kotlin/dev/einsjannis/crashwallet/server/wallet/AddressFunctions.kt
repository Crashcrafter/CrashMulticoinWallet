package dev.einsjannis.crashwallet.server.wallet

import io.leonard.Base58
import org.bouncycastle.jcajce.provider.digest.Keccak
import org.bouncycastle.jcajce.provider.digest.RIPEMD160
import org.bouncycastle.jcajce.provider.digest.SHA256
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.nio.charset.Charset
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.PublicKey
import java.security.interfaces.ECPrivateKey
import java.security.interfaces.ECPublicKey
import java.security.spec.ECGenParameterSpec
import java.security.spec.ECPoint

fun getaddedChecksum(key: ByteArray, checksum: ByteArray) : ByteArray{
	val result = ByteArray(key.size + 4)
	for (i in key.indices) result[i] = key[i]
	for (i in 0..3) result[key.size + i] = checksum[i]
	return result
}

fun getWithVersion(key: ByteArray, version : Byte) : ByteArray{
	val byteArray = ByteArray(key.size + 1)
	byteArray[0] = version
	for (i in key.indices) byteArray[i + 1] = key[i]
	return byteArray
}

fun genECDSAKeyPair(): KeyPair {
	val spec = ECGenParameterSpec("secp256k1")
	val generator = KeyPairGenerator.getInstance("ECDSA", BouncyCastleProvider())
	generator.initialize(spec)
	return generator.generateKeyPair()
}

fun adjustTo64(s: String): String {
	return when (s.length) {
		62 -> "00$s"
		63 -> "0$s"
		64 -> s
		else -> throw IllegalArgumentException("not a valid key: $s")
	}
}

fun PublicKey.getPublicKeyBytes(): ByteArray{
	val point : ECPoint = (this as ECPublicKey).w
	val sx = adjustTo64(point.affineX.toString(16)).toUpperCase()
	val sy = adjustTo64(point.affineY.toString(16)).toUpperCase()
	return "04$sx$sy".toUTF8ByteArray()
}

fun PrivateKey.getPrivateKeyString(): String = adjustTo64((this as ECPrivateKey).s.toString(16))

fun ByteArray.base58(): String = Base58.encode(this)

fun ByteArray.sha256(): ByteArray = SHA256.Digest().digest(this)

fun ByteArray.keccak256() : ByteArray = Keccak.Digest256().digest(this)

fun ByteArray.ripemd160(): ByteArray = RIPEMD160.Digest().digest(this)

fun ByteArray.toHexString() = asUByteArray().joinToString("") { it.toString(16).padStart(2, '0') }

fun String.toUTF8ByteArray(): ByteArray = toByteArray(Charset.forName("UTF-8"))

fun ByteArray.checksum() = this.sha256().sha256()

fun privateKeytoWalletImportFormat(privatekey: String) : String{
	println(privatekey)
	val withversionbyte = "80$privatekey"
	val shahash = withversionbyte.encodeToByteArray().sha256()
	val withchecksum = getaddedChecksum(shahash, shahash.checksum())
	return withchecksum.base58()
}
