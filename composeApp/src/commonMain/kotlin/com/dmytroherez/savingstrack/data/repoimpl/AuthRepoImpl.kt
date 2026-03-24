package com.dmytroherez.savingstrack.data.repoimpl

import com.dmytroherez.savingstrack.domain.repo.AuthRepo
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseAuthException
import dev.gitlive.firebase.auth.auth

class AuthRepoImpl : AuthRepo {

    override suspend fun register(email: String, pass: String): Result<String> {
        return try {
            val result = Firebase.auth.createUserWithEmailAndPassword(email, pass)
            val token = result.user?.getIdToken(forceRefresh = false)
            Result.success(token!!)
        } catch (e: FirebaseAuthException) {
            println("Registration Error: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun login(email: String, pass: String): Result<String> {
        return try {
            val result = Firebase.auth.signInWithEmailAndPassword(email, pass)
            val token = result.user?.getIdToken(forceRefresh = false)
            Result.success(token!!)
        } catch (e: FirebaseAuthException) {
            println("Login Error: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun logout() {
        Firebase.auth.signOut()
    }

    override fun isUserLoggedIn(): Result<Unit> {
        return Firebase.auth.currentUser?.let {
            Result.success(Unit)
        } ?: Result.failure(Exception())
    }
}