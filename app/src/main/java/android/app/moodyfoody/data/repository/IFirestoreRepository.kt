package android.app.moodyfoody.data.repository

import android.app.moodyfoody.data.model.UserData

interface IFirestoreRepository {
    suspend fun addUser(user: UserData): Result<Unit>
    suspend fun getUser(userId: String): Result<UserData?>
    suspend fun getAllUsers(): Result<List<UserData>>
}
