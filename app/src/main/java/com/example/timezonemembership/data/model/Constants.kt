package com.example.timezonemembership.data.model

/**
 * Single Source of Truth for all business rules.
 * All monetary calculations reference these constants.
 */
object Constants {

    // ==================== TOP-UP TIERS ====================

    data class TopUpTier(
        val name: String,
        val priceRp: Int,
        val tizoAmount: Int,
        val rewardPoints: Int
    )

    val TOP_UP_TIERS = listOf(
        TopUpTier("Tier 1", 100_000, 100, 10),
        TopUpTier("Tier 2", 200_000, 200, 25),
        TopUpTier("Tier 3", 300_000, 300, 40),
        TopUpTier("Tier 4", 500_000, 500, 75),
        TopUpTier("Tier 5", 1_000_000, 1000, 200),
        TopUpTier("Tier 6", 2_000_000, 2000, 500),
    )

    // ==================== MACHINE CATEGORIES ====================

    data class MachineCategory(
        val name: String,
        val description: String,
        val tizoCost: Int,
        val rewardPoints: Int
    )

    val MACHINE_CATEGORIES = listOf(
        MachineCategory("Mesin Ringan", "Capit boneka kecil, dsb", 5, 2),
        MachineCategory("Mesin Sedang", "Arcade standard, balapan", 10, 3),
        MachineCategory("Mesin Berat", "VR, mesin tiket besar", 15, 4),
    )

    // ==================== REDEEM REWARDS ====================

    data class RedeemReward(
        val name: String,
        val pointCost: Int,
        val category: RedeemCategory,
        val description: String = ""
    )

    enum class RedeemCategory(val displayName: String) {
        FREE_PLAY("Gratis Bermain"),
        MERCHANDISE("Hadiah Fisik"),
        DISCOUNT("Diskon Top-Up")
    }

    val REDEEM_FREE_PLAY = listOf(
        RedeemReward("1x Voucher Mesin Sedang", 30, RedeemCategory.FREE_PLAY, "Senilai 10 Tizo"),
        RedeemReward("1x Voucher Mesin Berat", 45, RedeemCategory.FREE_PLAY, "Senilai 15 Tizo"),
        RedeemReward("1x Voucher Mesin Premium/VR", 80, RedeemCategory.FREE_PLAY, "Senilai 30 Tizo"),
    )

    val REDEEM_MERCHANDISE = listOf(
        RedeemReward("Gantungan Kunci / Snack Ringan", 500, RedeemCategory.MERCHANDISE),
        RedeemReward("Boneka Timezone / Tumbler Eksklusif", 1500, RedeemCategory.MERCHANDISE),
        RedeemReward("Kaos Timezone / Tas Selempang", 3000, RedeemCategory.MERCHANDISE),
        RedeemReward("TWS / Smartwatch Entry-Level", 10000, RedeemCategory.MERCHANDISE),
    )

    val REDEEM_DISCOUNT = listOf(
        RedeemReward("Voucher Diskon 10%", 150, RedeemCategory.DISCOUNT, "Max Rp300.000"),
        RedeemReward("Voucher Diskon 25%", 300, RedeemCategory.DISCOUNT, "Max Rp500.000"),
    )

    val ALL_REWARDS = REDEEM_FREE_PLAY + REDEEM_MERCHANDISE + REDEEM_DISCOUNT
}
