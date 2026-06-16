package com.example.timezonemembership.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "members")
data class Member(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val email: String,
    val phone: String,
    val tizoBalance: Int = 0,
    val rewardPoints: Int = 0,
    val password: String,
    val role: String = "MEMBER", // "MEMBER" or "ADMIN"
    val isRemembered: Boolean = false
)
