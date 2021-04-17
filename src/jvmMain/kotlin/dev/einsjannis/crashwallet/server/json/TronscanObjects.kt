package dev.einsjannis.crashwallet.server.json

data class TronscanBalanceObject (
	val trc20token_balances: List<Any?>,
	val tokenBalances: List<TronscanBalance>,
	val delegateFrozenForEnergy: Long,
	val balances: List<TronscanBalance>,
	val trc721token_balances: List<Any?>,
	val balance: Long,
	val voteTotal: Long,
	val totalFrozen: Long,
	val tokens: List<TronscanBalance>,
	val delegated: TronscanDelegated,
	val totalTransactionCount: Long,
	val representative: TronscanRepresentative,
	val frozenForBandWidth: Long,
	val allowExchange: List<Any?>,
	val address: String,
	val frozen_supply: List<Any?>,
	val bandwidth: TronscanBandwidth,
	val date_created: Long,
	val accountType: Long,
	val exchanges: List<Any?>,
	val frozen: TronscanFrozen,
	val accountResource: TronscanAccountResource,
	val delegateFrozenForBandWidth: Long,
	val name: String,
	val frozenForEnergy: Long,
	val activePermissions: List<TronscanActivePermission>
)

data class TronscanAccountResource (
	val frozen_balance_for_energy: Any
)

data class TronscanActivePermission (
	val operations: String,
	val keys: List<TronscanKey>,
	val threshold: Long,
	val id: Long,
	val type: String,
	val permission_name: String
)

data class TronscanKey (
	val address: String,
	val weight: Long
)

data class TronscanBalance (
	val amount: Any,
	val tokenPriceInTrx: Double,
	val tokenId: String,
	val balance: String,
	val tokenName: String,
	val tokenDecimal: Long,
	val tokenAbbr: String,
	val tokenCanShow: Long,
	val tokenType: String,
	val vip: Boolean,
	val tokenLogo: String,
	val owner_address: String? = null
)

data class TronscanBandwidth (
	val energyRemaining: Long,
	val totalEnergyLimit: Long,
	val totalEnergyWeight: Long,
	val netUsed: Long,
	val storageLimit: Long,
	val storagePercentage: Double,
	val assets: Map<String, TronscanAsset>,
	val netPercentage: Double,
	val storageUsed: Long,
	val storageRemaining: Long,
	val freeNetLimit: Long,
	val energyUsed: Long,
	val freeNetRemaining: Long,
	val netLimit: Long,
	val netRemaining: Long,
	val energyLimit: Long,
	val freeNetUsed: Long,
	val totalNetWeight: Long,
	val freeNetPercentage: Double,
	val energyPercentage: Double,
	val totalNetLimit: Long
)

data class TronscanAsset (
	val netPercentage: Double,
	val netLimit: Long,
	val netRemaining: Long,
	val netUsed: Long
)

data class TronscanDelegated (
	val sentDelegatedBandwidth: List<Any?>,
	val sentDelegatedResource: List<Any?>,
	val receivedDelegatedResource: List<Any?>,
	val receivedDelegatedBandwidth: List<Any?>
)

data class TronscanFrozen (
	val total: Long,
	val balances: List<Any?>
)

data class TronscanRepresentative (
	val lastWithDrawTime: Long,
	val allowance: Long,
	val enabled: Boolean,
	val url: String
)
