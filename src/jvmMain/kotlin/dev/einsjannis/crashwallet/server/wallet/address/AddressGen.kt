package dev.einsjannis.crashwallet.server.wallet.address

import dev.einsjannis.crashwallet.server.DBHost
import dev.einsjannis.crashwallet.server.exceptions.UnknownAddressTypeException
import org.bouncycastle.crypto.digests.Blake2bDigest
import java.net.URL
import java.util.*

data class Address(val privateKey: String, val address: String, val type: AddressType)

enum class AddressType{ BTC, ETH, BNB, TRX, LTC, BCH, ZEC, DASH, DOGE, DGB, NANO, RDD, THETA, TFUEL}

fun genAddress(type: AddressType): Address {
	try {
		return when (type) {
			AddressType.BTC -> genBTCAddress()
			AddressType.BCH -> genBCHAddress()
			AddressType.ETH -> genEthAddress()
			AddressType.BNB -> genSmartChainAddress()
			AddressType.TRX -> genTronAddress()
			AddressType.LTC -> genLitecoinAddress()
			AddressType.ZEC -> genZcashAddress()
			AddressType.DASH -> genDashAddress()
			AddressType.DOGE -> genDogeAddress()
			AddressType.DGB -> genDigibyteAddress()
			AddressType.NANO -> genNanoAddress()
			AddressType.RDD -> genReddAddress()
			AddressType.THETA -> genThetaAddress()
			else -> throw UnknownAddressTypeException()
		}
	}catch (ex: IllegalArgumentException) {
		return genAddress(type)
	}
}

private fun genBCHAddress() : Address = genBTCAddress(AddressType.BCH)

private fun genDogeAddress() : Address = genBTCAddress( AddressType.DOGE,0x1E)

private fun genDigibyteAddress() : Address = genBTCAddress(AddressType.DGB, 0x1E)

private fun genReddAddress() : Address = genBTCAddress(AddressType.RDD,0x3d)

private fun genLitecoinAddress() : Address = genBTCAddress(AddressType.LTC,0x30)

private fun genDashAddress() : Address = genBTCAddress(AddressType.DASH,0x4c)

private fun genZcashAddress() : Address {
	val btcaddr = genBTCAddress()
	val zcashaddress = URL("http://$DBHost:3000/genzcashaddress?address=${btcaddr.address}").readText()
	return Address(btcaddr.privateKey, zcashaddress, AddressType.ZEC)
}

private fun genNanoAddress() : Address {
	val response = URL("http://$DBHost:3000/gennanoaddress").readText().split(',')
	val blake2b = Blake2bDigest(32)
	val indexbytes = "00000000".toUTF8ByteArray()
	blake2b.update(indexbytes, 0, indexbytes.size)
	val randombytes = ByteArray(64)
	Random().nextBytes(randombytes)
	blake2b.update(randombytes, 0, randombytes.size)
	val privateKey = ByteArray(32)
	blake2b.doFinal(privateKey,0)
	return Address(response[1], response[0], AddressType.NANO)
}

private fun genThetaAddress() : Address {
	val ethaddress = genEthAddress()
	return Address(ethaddress.privateKey, ethaddress.address, AddressType.THETA)
}

private fun genEthAddress() : Address {
	val keypair = genECDSAKeyPair()
	val privateKey = keypair.private.getPrivateKeyString()
	val publicKey = keypair.public.getPublicKeyBytes()
	val keccak = publicKey.keccak256()
	val last20bytes = keccak.copyOfRange(keccak.size - 20, keccak.size)
	val address = "0x${last20bytes.toHexString()}"
	return Address(privateKey, address, AddressType.ETH)
}

private fun genSmartChainAddress() : Address {
	val keypair = genECDSAKeyPair()
	val privateKey = keypair.private.getPrivateKeyString()
	val publicKey = keypair.public.getPublicKeyBytes()
	val address = publicKey.sha256().ripemd160()
	return Address(privateKey, "0x${address.toHexString()}", AddressType.BNB)
}

private fun genTronAddress() : Address {
	val keypair = genECDSAKeyPair()
	val privateKey = keypair.private.getPrivateKeyString()
	val publicKey = keypair.public.getPublicKeyBytes()
	val keccak = publicKey.keccak256()
	val last20bytes = keccak.copyOfRange(keccak.size - 20, keccak.size)
	val withversion = getWithVersion(last20bytes, 0x41)
	val address = (getaddedChecksum(withversion, withversion.checksum())).base58()
	return Address(privateKey, address, AddressType.TRX)
}

private fun genBTCAddress(type: AddressType = AddressType.BTC, version: Byte = 0x00) : Address {
	val keypair = genECDSAKeyPair()
	val privateKey = keypair.private.getPrivateKeyString()
	val publicKey = keypair.public.getPublicKeyBytes()
	val shapublickey = publicKey.sha256()
	val publickeywithversion = getWithVersion(shapublickey.ripemd160(), version)
	val address = (getaddedChecksum(publickeywithversion, publickeywithversion.checksum())).base58()
	return Address(privateKey, address, type)
}
