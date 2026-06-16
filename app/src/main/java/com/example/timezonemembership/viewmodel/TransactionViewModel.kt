package com.example.timezonemembership.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.timezonemembership.data.database.TimezoneDatabase
import com.example.timezonemembership.data.model.Transaction
import com.example.timezonemembership.data.repository.TimezoneRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TransactionViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TimezoneRepository

    private val _transactions = MutableStateFlow<List<Transaction>>(emptyList())
    val transactions: StateFlow<List<Transaction>> = _transactions.asStateFlow()

    init {
        val database = TimezoneDatabase.getInstance(application)
        repository = TimezoneRepository(
            database.memberDao(),
            database.transactionDao()
        )
    }

    fun loadTransactions(memberId: Int) {
        viewModelScope.launch {
            repository.getTransactionsByMemberId(memberId).collect { txList ->
                _transactions.value = txList
            }
        }
    }
}
