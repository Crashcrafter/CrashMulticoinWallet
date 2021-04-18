package dev.einsjannis.crashwallet.server.wallet

import kotlin.collections.HashMap

enum class AddressType{ BTC, ETH, BNB, TRX, LTC, BCH, ZEC, DASH, DOGE, DGB, NANO, RDD, THETA, TFUEL}

val noOwnAddress = HashMap<AddressType, AddressType>()

val disabledCurrencies = listOf(AddressType.TFUEL, AddressType.THETA)

fun initAddressType(){
    noOwnAddress[AddressType.TFUEL] = AddressType.THETA
}

fun String.toAddressTypeString() : String {
    var type = AddressType.valueOf(this.toUpperCase())
    if(noOwnAddress.containsKey(type)){
        type = noOwnAddress[type]!!
    }
    return type.name
}