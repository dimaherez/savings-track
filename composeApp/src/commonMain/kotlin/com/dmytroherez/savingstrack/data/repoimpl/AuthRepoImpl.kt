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
        } catch (e: Exception) {
            println("Registration Error: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun login(email: String, pass: String): Result<String> {
        return try {
            val result = Firebase.auth.signInWithEmailAndPassword(email, pass)
            val token = result.user?.getIdToken(forceRefresh = false)
            Result.success(token!!)
        } catch (e: Exception) {
            println("Login Error: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun logout() {
        Firebase.auth.signOut()
    }

    override fun getCurrentUser(): Result<Unit> {
        return try {
            Firebase.auth.currentUser?.let {
                Result.success(Unit)
            } ?: throw Exception("Unable to get current user")
        } catch (e: Exception) {
            println("Current User Error: ${e.message}")
            Result.failure(e)
        }
    }
}