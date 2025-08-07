package android.app.moodyfoody.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import android.app.moodyfoody.data.repository.IAuthRepository
import android.app.moodyfoody.data.repository.IFirestoreRepository
import android.app.moodyfoody.data.model.UserData
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserManagementViewModel @Inject constructor(
    private val authRepository: IAuthRepository,
    private val firestoreRepository: IFirestoreRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UserManagementState>(UserManagementState.Idle)
    val uiState: StateFlow<UserManagementState> = _uiState.asStateFlow()

    private val _currentUser = MutableStateFlow<FirebaseUser?>(authRepository.currentUser)
    val currentUser: StateFlow<FirebaseUser?> = _currentUser.asStateFlow()

    private val _userData = MutableStateFlow<UserData?>(null)
    val userData: StateFlow<UserData?> = _userData.asStateFlow()

    init {
        _currentUser.value = authRepository.currentUser
        // If user is already signed in, load their data
        authRepository.currentUser?.let { user ->
            loadUserData(user.uid)
        }
    }

    fun signUpAndCreateProfile(email: String, password: String, name: String) {
        if (email.isBlank() || password.isBlank() || name.isBlank()) {
            _uiState.value = UserManagementState.Error("All fields are required")
            return
        }

        viewModelScope.launch {
            _uiState.value = UserManagementState.Loading

            authRepository.signUp(email, password)
                .onSuccess { firebaseUser ->
                    _currentUser.value = firebaseUser

                    val userData = UserData(
                        id = firebaseUser.uid,
                        name = name,
                        email = email
                    )

                    firestoreRepository.addUser(userData)
                        .onSuccess {
                            _userData.value = userData
                            _uiState.value = UserManagementState.Success("Account created successfully")
                        }
                        .onFailure { exception ->
                            _uiState.value = UserManagementState.Error("Failed to create profile: ${exception.message}")
                        }
                }
                .onFailure { exception ->
                    _uiState.value = UserManagementState.Error("Sign up failed: ${exception.message}")
                }
        }
    }

    fun signInAndLoadProfile(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _uiState.value = UserManagementState.Error("Email and password cannot be empty")
            return
        }

        viewModelScope.launch {
            _uiState.value = UserManagementState.Loading

            authRepository.signIn(email, password)
                .onSuccess { firebaseUser ->
                    _currentUser.value = firebaseUser
                    loadUserData(firebaseUser.uid)
                }
                .onFailure { exception ->
                    _uiState.value = UserManagementState.Error("Sign in failed: ${exception.message}")
                }
        }
    }

    private fun loadUserData(userId: String) {
        viewModelScope.launch {
            firestoreRepository.getUser(userId)
                .onSuccess { userData ->
                    _userData.value = userData
                    _uiState.value = UserManagementState.Success("Profile loaded successfully")
                }
                .onFailure { exception ->
                    _uiState.value = UserManagementState.Error("Failed to load profile: ${exception.message}")
                }
        }
    }

    fun signOut() {
        authRepository.signOut()
        _currentUser.value = null
        _userData.value = null
        _uiState.value = UserManagementState.Success("Signed out successfully")
    }

    fun updateUserProfile(userData: UserData) {
        viewModelScope.launch {
            _uiState.value = UserManagementState.Loading

            firestoreRepository.addUser(userData) // This will update existing user
                .onSuccess {
                    _userData.value = userData
                    _uiState.value = UserManagementState.Success("Profile updated successfully")
                }
                .onFailure { exception ->
                    _uiState.value = UserManagementState.Error("Failed to update profile: ${exception.message}")
                }
        }
    }

    fun clearState() {
        _uiState.value = UserManagementState.Idle
    }

    fun isUserSignedIn(): Boolean = _currentUser.value != null
}

sealed class UserManagementState {
    object Idle : UserManagementState()
    object Loading : UserManagementState()
    data class Success(val message: String) : UserManagementState()
    data class Error(val message: String) : UserManagementState()
}
