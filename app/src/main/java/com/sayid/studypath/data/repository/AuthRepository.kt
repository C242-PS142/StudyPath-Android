package com.sayid.studypath.data.repository

import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
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

    suspend fun getIdToken(): String? =
        firebaseAuth.currentUser
            ?.getIdToken(true)
            ?.await()
            ?.token

    fun getCurrentUser(): FirebaseUser? = firebaseAuth.currentUser

    fun signOut()  {
        firebaseAuth.signOut()
        googleSignInClient.signOut()
    }
}
