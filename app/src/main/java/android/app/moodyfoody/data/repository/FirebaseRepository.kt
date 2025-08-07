package android.app.moodyfoody.data.repository

import android.app.moodyfoody.data.model.UserData
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestoreRepository @Inject constructor(
    private val db: FirebaseFirestore
) : IFirestoreRepository {

    override suspend fun addUser(user: UserData): Result<Unit> {
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

    override suspend fun getUser(userId: String): Result<UserData?> {
        return try {
            val document = db.collection("users")
                .document(userId)
                .get()
                .await()

            val user = document.toObject(UserData::class.java)
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getAllUsers(): Result<List<UserData>> {
        return try {
            val snapshot = db.collection("users")
                .get()
                .await()

            val users = snapshot.toObjects(UserData::class.java)
            Result.success(users)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}