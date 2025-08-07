package android.app.moodyfoody.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import android.app.moodyfoody.data.repository.IAuthRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: IAuthRepository
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _currentUser = MutableStateFlow(authRepository.currentUser)
    val currentUser: StateFlow<FirebaseUser?> = _currentUser.asStateFlow()

    init {
        _currentUser.value = authRepository.currentUser
    }

    fun signUp(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _authState.value = AuthState.Error("Email and password cannot be empty")
            return
        }

        viewModelScope.launch {
            _authState.value = AuthState.Loading
            authRepository.signUp(email, password)
                .onSuccess { user ->
                    _currentUser.value = user
                    _authState.value = AuthState.Success("Account created successfully")
                }
                .onFailure { exception ->
                    _authState.value = AuthState.Error(exception.message ?: "Sign up failed")
                }
        }
    }

    fun signIn(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _authState.value = AuthState.Error("Email and password cannot be empty")
            return
        }

        viewModelScope.launch {
            _authState.value = AuthState.Loading
            authRepository.signIn(email, password)
                .onSuccess { user ->
                    _currentUser.value = user
                    _authState.value = AuthState.Success("Signed in successfully")
                }
                .onFailure { exception ->
                    _authState.value = AuthState.Error(exception.message ?: "Sign in failed")
                }
        }
    }

    fun signOut() {
        authRepository.signOut()
        _currentUser.value = null
        _authState.value = AuthState.Success("Signed out successfully")
    }

    fun clearAuthState() {
        _authState.value = AuthState.Idle
    }

    fun isUserSignedIn(): Boolean {
        return _currentUser.value != null
    }
}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val message: String) : AuthState()
    data class Error(val message: String) : AuthState()
}
