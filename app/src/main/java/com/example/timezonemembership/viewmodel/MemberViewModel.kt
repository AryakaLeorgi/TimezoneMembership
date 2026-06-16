package com.example.timezonemembership.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.timezonemembership.data.database.TimezoneDatabase
import com.example.timezonemembership.data.model.Constants
import com.example.timezonemembership.data.model.Member
import com.example.timezonemembership.data.repository.TimezoneRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MemberViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TimezoneRepository

    // Current logged-in user
    private val _currentUser = MutableStateFlow<Member?>(null)
    val currentUser: StateFlow<Member?> = _currentUser.asStateFlow()

    // Flag indicating whether checking remembered user is in progress
    private val _isCheckingAuth = MutableStateFlow(true)
    val isCheckingAuth: StateFlow<Boolean> = _isCheckingAuth.asStateFlow()

    // All MEMBER users list (for Admin dashboard)
    val allMembers: StateFlow<List<Member>>

    // Member count (for Admin dashboard stats)
    val memberCount: StateFlow<Int>

    // Currently selected member for details view (admin viewing a member, or member viewing self)
    private val _selectedMember = MutableStateFlow<Member?>(null)
    val selectedMember: StateFlow<Member?> = _selectedMember.asStateFlow()

    // UI feedback messages
    private val _uiMessage = MutableStateFlow<String?>(null)
    val uiMessage: StateFlow<String?> = _uiMessage.asStateFlow()

    // Auth error messages
    private val _authError = MutableStateFlow<String?>(null)
    val authError: StateFlow<String?> = _authError.asStateFlow()

    init {
        val database = TimezoneDatabase.getInstance(application)
        repository = TimezoneRepository(
            database.memberDao(),
            database.transactionDao()
        )

        allMembers = repository.getAllMembersByRole("MEMBER")
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        memberCount = repository.getMemberCount()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

        checkRememberedUser()
    }

    // ==================== AUTH OPERATIONS ====================

    fun checkRememberedUser() {
        viewModelScope.launch {
            _isCheckingAuth.value = true
            val remembered = repository.getRememberedMember()
            if (remembered != null) {
                _currentUser.value = remembered
                _selectedMember.value = remembered
            }
            _isCheckingAuth.value = false
        }
    }

    fun login(email: String, password: String, rememberMe: Boolean) {
        viewModelScope.launch {
            _authError.value = null
            val member = repository.login(email, password, rememberMe)
            if (member != null) {
                _currentUser.value = member
                _selectedMember.value = member
                _uiMessage.value = "Login berhasil! Selamat datang, ${member.name}."
            } else {
                _authError.value = "Email atau password salah!"
            }
        }
    }

    fun register(name: String, email: String, phone: String, password: String, role: String) {
        viewModelScope.launch {
            _authError.value = null
            // Check email duplication
            val existing = repository.getMemberByEmail(email)
            if (existing != null) {
                _authError.value = "Email sudah terdaftar!"
                return@launch
            }

            val newMember = Member(
                name = name,
                email = email,
                phone = phone,
                password = password,
                role = role
            )
            val id = repository.register(newMember)
            if (id > 0) {
                _uiMessage.value = "Registrasi berhasil! Silakan login."
            } else {
                _authError.value = "Registrasi gagal!"
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            val current = _currentUser.value
            if (current != null) {
                repository.logout(current.id)
            }
            _currentUser.value = null
            _selectedMember.value = null
            _authError.value = null
        }
    }

    fun clearAuthError() {
        _authError.value = null
    }

    // ==================== MEMBER OPERATIONS ====================

    // This remains for Admin to add member from dashboard if they want
    fun addMemberByAdmin(name: String, email: String, phone: String, password: String, role: String) {
        viewModelScope.launch {
            val member = Member(
                name = name,
                email = email,
                phone = phone,
                password = password,
                role = role
            )
            val result = repository.register(member)
            if (result > 0) {
                _uiMessage.value = "Member berhasil ditambahkan oleh Admin!"
            } else {
                _uiMessage.value = "Gagal menambahkan member (Email mungkin duplikat)."
            }
        }
    }

    fun loadMember(memberId: Int) {
        viewModelScope.launch {
            repository.getMemberById(memberId).collect { member ->
                _selectedMember.value = member
            }
        }
    }

    // ==================== TOP-UP ====================

    fun performTopUp(memberId: Int, tier: Constants.TopUpTier) {
        viewModelScope.launch {
            val success = repository.performTopUp(memberId, tier)
            if (success) {
                _uiMessage.value = "Top-Up ${tier.name} berhasil! +${tier.tizoAmount} Tizo, +${tier.rewardPoints} Poin"
                // Refresh currentUser if the updated member is the logged-in user
                if (_currentUser.value?.id == memberId) {
                    val updated = repository.getMemberByEmail(_currentUser.value!!.email)
                    _currentUser.value = updated
                }
            } else {
                _uiMessage.value = "Top-Up gagal. Silakan coba lagi."
            }
        }
    }

    // ==================== PLAY ====================

    fun performPlay(memberId: Int, machine: Constants.MachineCategory) {
        viewModelScope.launch {
            val success = repository.performPlay(memberId, machine)
            if (success) {
                _uiMessage.value = "Bermain ${machine.name} berhasil! -${machine.tizoCost} Tizo, +${machine.rewardPoints} Poin"
                // Refresh currentUser if the updated member is the logged-in user
                if (_currentUser.value?.id == memberId) {
                    val updated = repository.getMemberByEmail(_currentUser.value!!.email)
                    _currentUser.value = updated
                }
            } else {
                _uiMessage.value = "Saldo Tizo tidak mencukupi!"
            }
        }
    }

    // ==================== REDEEM ====================

    fun performRedeem(memberId: Int, reward: Constants.RedeemReward) {
        viewModelScope.launch {
            val success = repository.performRedeem(memberId, reward)
            if (success) {
                _uiMessage.value = "Redeem ${reward.name} berhasil! -${reward.pointCost} Poin"
                // Refresh currentUser if the updated member is the logged-in user
                if (_currentUser.value?.id == memberId) {
                    val updated = repository.getMemberByEmail(_currentUser.value!!.email)
                    _currentUser.value = updated
                }
            } else {
                _uiMessage.value = "Poin tidak mencukupi!"
            }
        }
    }

    // ==================== UI MESSAGE ====================

    fun clearMessage() {
        _uiMessage.value = null
    }
}
