package com.example.timezonemembership.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.timezonemembership.data.dao.MemberDao
import com.example.timezonemembership.data.dao.TransactionDao
import com.example.timezonemembership.data.model.Member
import com.example.timezonemembership.data.model.Transaction

@Database(
    entities = [Member::class, Transaction::class],
    version = 2,
    exportSchema = false
)
abstract class TimezoneDatabase : RoomDatabase() {

    abstract fun memberDao(): MemberDao
    abstract fun transactionDao(): TransactionDao

    companion object {
        @Volatile
        private var INSTANCE: TimezoneDatabase? = null

        fun getInstance(context: Context): TimezoneDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TimezoneDatabase::class.java,
                    "timezone_membership_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
