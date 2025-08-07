package android.app.moodyfoody.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import android.app.moodyfoody.data.repository.FirestoreRepository
import android.app.moodyfoody.data.model.UserData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FirestoreViewModel(
    private val firestoreRepository: FirestoreRepository = FirestoreRepository()
) : ViewModel() {

    private val _firestoreState = MutableStateFlow<FirestoreState>(FirestoreState.Idle)
    val firestoreState: StateFlow<FirestoreState> = _firestoreState.asStateFlow()

    private val _users = MutableStateFlow<List<UserData>>(emptyList())
    val users: StateFlow<List<UserData>> = _users.asStateFlow()

    private val _currentUserData = MutableStateFlow<UserData?>(null)
    val currentUserData: StateFlow<UserData?> = _currentUserData.asStateFlow()

    fun addUser(userData: UserData) {
        viewModelScope.launch {
            _firestoreState.value = FirestoreState.Loading
            firestoreRepository.addUser(userData)
                .onSuccess {
                    _firestoreState.value = FirestoreState.Success("User added successfully")
                    // Refresh users list after adding
                    getAllUsers()
                }
                .onFailure { exception ->
                    _firestoreState.value = FirestoreState.Error(exception.message ?: "Failed to add user")
                }
        }
    }

    fun getUser(userId: String) {
        viewModelScope.launch {
            _firestoreState.value = FirestoreState.Loading
            firestoreRepository.getUser(userId)
                .onSuccess { userData ->
                    _currentUserData.value = userData
                    _firestoreState.value = if (userData != null) {
                        FirestoreState.Success("User retrieved successfully")
                    } else {
                        FirestoreState.Success("User not found")
                    }
                }
                .onFailure { exception ->
                    _firestoreState.value = FirestoreState.Error(exception.message ?: "Failed to get user")
                }
        }
    }

    fun getAllUsers() {
        viewModelScope.launch {
            _firestoreState.value = FirestoreState.Loading
            firestoreRepository.getAllUsers()
                .onSuccess { usersList ->
                    _users.value = usersList
                    _firestoreState.value = FirestoreState.Success("Users retrieved successfully")
                }
                .onFailure { exception ->
                    _firestoreState.value = FirestoreState.Error(exception.message ?: "Failed to get users")
                }
        }
    }

    fun clearFirestoreState() {
        _firestoreState.value = FirestoreState.Idle
    }

    fun clearUserData() {
        _currentUserData.value = null
        _users.value = emptyList()
    }
}

sealed class FirestoreState {
    object Idle : FirestoreState()
    object Loading : FirestoreState()
    data class Success(val message: String) : FirestoreState()
    data class Error(val message: String) : FirestoreState()
}
