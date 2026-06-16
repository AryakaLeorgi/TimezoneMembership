package com.example.timezonemembership.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.timezonemembership.data.model.Member
import kotlinx.coroutines.flow.Flow

@Dao
interface MemberDao {

    @Query("SELECT * FROM members ORDER BY id DESC")
    fun getAllMembers(): Flow<List<Member>>

    @Query("SELECT * FROM members WHERE role = :role ORDER BY id DESC")
    fun getAllMembersByRole(role: String): Flow<List<Member>>

    @Query("SELECT * FROM members WHERE id = :id")
    fun getMemberById(id: Int): Flow<Member?>

    @Query("SELECT * FROM members WHERE id = :id")
    suspend fun getMemberByIdOnce(id: Int): Member?

    @Query("SELECT * FROM members WHERE email = :email LIMIT 1")
    suspend fun getMemberByEmailOnce(email: String): Member?

    @Query("SELECT * FROM members WHERE email = :email AND password = :password LIMIT 1")
    suspend fun login(email: String, password: String): Member?

    @Query("SELECT * FROM members WHERE isRemembered = 1 LIMIT 1")
    suspend fun getRememberedMember(): Member?

    @Query("UPDATE members SET isRemembered = 0")
    suspend fun clearAllRemembered()

    @Query("UPDATE members SET isRemembered = :isRemembered WHERE id = :id")
    suspend fun setRemembered(id: Int, isRemembered: Boolean)

    @Query("SELECT COUNT(*) FROM members WHERE role = 'MEMBER'")
    fun getMemberCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMember(member: Member): Long

    @Update
    suspend fun updateMember(member: Member)
}
