package android.app.moodyfoody.data.repository

import android.annotation.SuppressLint
import android.app.moodyfoody.data.model.UserData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import kotlinx.coroutines.tasks.await

class FirestoreRepository {
    private val db = FirebaseFirestore.getInstance()

    suspend fun addUser(user: UserData): Result<Unit> {
        return try {
            db.collection("users")
                .document(user.id)
                .set(user)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @SuppressLint("RestrictedApi")
    suspend fun getUser(userId: String): Result<User?> {
        return try {
            val document = db.collection("users")
                .document(userId)
                .get()
                .await()

            val user = document.toObject(User::class.java)
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @SuppressLint("RestrictedApi")
    suspend fun getAllUsers(): Result<List<User>> {
        return try {
            val snapshot = db.collection("users")
                .get()
                .await()

            val users = snapshot.toObjects(User::class.java)
            Result.success(users)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}