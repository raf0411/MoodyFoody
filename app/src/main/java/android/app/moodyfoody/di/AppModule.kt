package android.app.moodyfoody.di

import android.app.moodyfoody.data.repository.AuthRepository
import android.app.moodyfoody.data.repository.FirestoreRepository
import android.app.moodyfoody.data.repository.IAuthRepository
import android.app.moodyfoody.data.repository.IFirestoreRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(authRepository: AuthRepository): IAuthRepository

    @Binds
    @Singleton
    abstract fun bindFirestoreRepository(firestoreRepository: FirestoreRepository): IFirestoreRepository
}
