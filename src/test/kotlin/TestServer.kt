import de.crash.crashwallet.wallet.getBalance
import dev.crash.address.AddressType

fun main(){
    println(getBalance("0xde0b295669a9fd93d5f28d9ec85e40f4cb697bae", AddressType.ETH).balance)
}