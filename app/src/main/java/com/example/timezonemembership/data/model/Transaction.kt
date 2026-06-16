package com.example.timezonemembership.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "transactions",
    foreignKeys = [
        ForeignKey(
            entity = Member::class,
            parentColumns = ["id"],
            childColumns = ["memberId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["memberId"])]
)
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val memberId: Int,
    val transactionType: String, // "TOPUP", "PLAY", "REDEEM"
    val description: String,
    val tizoMutation: Int,   // positive for credit, negative for debit
    val pointMutation: Int,  // positive for credit, negative for debit
    val timestamp: Long = System.currentTimeMillis()
)
