package com.example.timezonemembership.data.repository

import com.example.timezonemembership.data.dao.MemberDao
import com.example.timezonemembership.data.dao.TransactionDao
import com.example.timezonemembership.data.model.Constants
import com.example.timezonemembership.data.model.Member
import com.example.timezonemembership.data.model.Transaction
import kotlinx.coroutines.flow.Flow

class TimezoneRepository(
    private val memberDao: MemberDao,
    private val transactionDao: TransactionDao
) {

    // ==================== MEMBER OPERATIONS ====================

    fun getAllMembers(): Flow<List<Member>> = memberDao.getAllMembers()

    fun getAllMembersByRole(role: String): Flow<List<Member>> = memberDao.getAllMembersByRole(role)

    fun getMemberById(id: Int): Flow<Member?> = memberDao.getMemberById(id)

    fun getMemberCount(): Flow<Int> = memberDao.getMemberCount()

    suspend fun insertMember(member: Member): Long = memberDao.insertMember(member)

    suspend fun getMemberByEmail(email: String): Member? = memberDao.getMemberByEmailOnce(email)

    // ==================== AUTH OPERATIONS ====================

    suspend fun login(email: String, password: String, rememberMe: Boolean): Member? {
        val member = memberDao.login(email, password)
        if (member != null) {
            if (rememberMe) {
                memberDao.clearAllRemembered()
                memberDao.setRemembered(member.id, true)
                return member.copy(isRemembered = true)
            } else {
                memberDao.clearAllRemembered()
                return member.copy(isRemembered = false)
            }
        }
        return null
    }

    suspend fun register(member: Member): Long {
        val existing = memberDao.getMemberByEmailOnce(member.email)
        if (existing != null) {
            return -1L // Email already exists
        }
        return memberDao.insertMember(member)
    }

    suspend fun getRememberedMember(): Member? {
        return memberDao.getRememberedMember()
    }

    suspend fun logout(memberId: Int) {
        memberDao.setRemembered(memberId, false)
    }

    // ==================== TRANSACTION QUERIES ====================

    fun getTransactionsByMemberId(memberId: Int): Flow<List<Transaction>> =
        transactionDao.getTransactionsByMemberId(memberId)

    // ==================== TOP-UP ====================

    @androidx.room.Transaction
    suspend fun performTopUp(memberId: Int, tier: Constants.TopUpTier): Boolean {
        val member = memberDao.getMemberByIdOnce(memberId) ?: return false

        val updatedMember = member.copy(
            tizoBalance = member.tizoBalance + tier.tizoAmount,
            rewardPoints = member.rewardPoints + tier.rewardPoints
        )
        memberDao.updateMember(updatedMember)

        val transaction = Transaction(
            memberId = memberId,
            transactionType = "TOPUP",
            description = "Top-Up ${tier.name} (Rp${formatCurrency(tier.priceRp)})",
            tizoMutation = tier.tizoAmount,
            pointMutation = tier.rewardPoints
        )
        transactionDao.insertTransaction(transaction)

        return true
    }

    // ==================== PLAY ====================

    @androidx.room.Transaction
    suspend fun performPlay(memberId: Int, machine: Constants.MachineCategory): Boolean {
        val member = memberDao.getMemberByIdOnce(memberId) ?: return false

        if (member.tizoBalance < machine.tizoCost) return false

        val updatedMember = member.copy(
            tizoBalance = member.tizoBalance - machine.tizoCost,
            rewardPoints = member.rewardPoints + machine.rewardPoints
        )
        memberDao.updateMember(updatedMember)

        val transaction = Transaction(
            memberId = memberId,
            transactionType = "PLAY",
            description = "Bermain ${machine.name}",
            tizoMutation = -machine.tizoCost,
            pointMutation = machine.rewardPoints
        )
        transactionDao.insertTransaction(transaction)

        return true
    }

    // ==================== REDEEM ====================

    @androidx.room.Transaction
    suspend fun performRedeem(memberId: Int, reward: Constants.RedeemReward): Boolean {
        val member = memberDao.getMemberByIdOnce(memberId) ?: return false

        if (member.rewardPoints < reward.pointCost) return false

        val updatedMember = member.copy(
            rewardPoints = member.rewardPoints - reward.pointCost
        )
        memberDao.updateMember(updatedMember)

        val transaction = Transaction(
            memberId = memberId,
            transactionType = "REDEEM",
            description = "Redeem: ${reward.name}",
            tizoMutation = 0,
            pointMutation = -reward.pointCost
        )
        transactionDao.insertTransaction(transaction)

        return true
    }

    // ==================== UTILITY ====================

    private fun formatCurrency(amount: Int): String {
        return String.format("%,d", amount).replace(',', '.')
    }
}
