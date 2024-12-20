package com.sayid.studypath.data.repository

import android.content.Intent
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.sayid.studypath.utils.PredictionResultSingleton
import com.sayid.studypath.utils.UserLoginDataSingleton
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await

class AuthRepository(
    private val firebaseAuth: FirebaseAuth,
    private val googleSignInClient: GoogleSignInClient,
) {
    fun getGoogleSignInIntent(): Intent = googleSignInClient.signInIntent

    suspend fun firebaseAuthWithGoogle(idToken: String): Result<FirebaseUser> =
        try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val authResult = firebaseAuth.signInWithCredential(credential).await()

            val user = authResult.user
            if (user != null) {
                Result.success(user)
            } else {
                Result.failure(Exception("User is null after authentication"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }

    suspend fun getIdToken(): String? {
        var attempt = 0
        while (attempt < 3) {
            try {
                val token =
                    firebaseAuth.currentUser
                        ?.getIdToken(true)
                        ?.await()
                        ?.token
                return token
            } catch (e: Exception) {
                attempt++
                if (attempt == 3) {
                    Log.e("AuthRepository", "Failed to get ID token: ${e.message}")
                    return null
                }
                delay(1000)
            }
        }
        return null
    }

    fun getCurrentUser(): FirebaseUser? = firebaseAuth.currentUser

    fun signOut() {
        PredictionResultSingleton.clearPrediction()
        UserLoginDataSingleton.clearLoginData()
        firebaseAuth.signOut()
        googleSignInClient.signOut()
    }
}
