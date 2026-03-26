package com.dmytroherez.savingstrack.data.repoimpl

import co.touchlab.kermit.Logger
import com.dmytroherez.savingstrack.domain.repo.AuthRepo
import dev.gitlive.firebase.auth.FirebaseAuth

class AuthRepoImpl(
    private val firebaseAuth: FirebaseAuth
) : AuthRepo {

    override suspend fun register(email: String, pass: String): Result<String> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, pass)
            val token = result.user?.getIdToken(forceRefresh = false)
            Result.success(token!!)
        } catch (e: Exception) {
            Logger.e(e) { "AuthRepoImpl.register()" }
            Result.failure(e)
        }
    }

    override suspend fun login(email: String, pass: String): Result<String> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, pass)
            val token = result.user?.getIdToken(forceRefresh = false)
            Result.success(token!!)
        } catch (e: Exception) {
            Logger.e(e) { "AuthRepoImpl.login()" }
            Result.failure(e)
        }
    }

    override suspend fun logout() {
        firebaseAuth.signOut()
    }

    override fun getCurrentUser(): Result<Unit> {
        return try {
            firebaseAuth.currentUser?.let {
                Result.success(Unit)
            } ?: throw Exception("Unable to get current user")
        } catch (e: Exception) {
            Logger.e(e) { "AuthRepoImpl.getCurrentUser()" }
            Result.failure(e)
        }
    }
}