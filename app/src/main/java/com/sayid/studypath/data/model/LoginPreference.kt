package com.sayid.studypath.data.model

import android.content.Context
import android.content.SharedPreferences
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class LoginPreference(
    context: Context,
) {
    private val spec =
        KeyGenParameterSpec
            .Builder(
                MasterKey.DEFAULT_MASTER_KEY_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT,
            ).setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setKeySize(MasterKey.DEFAULT_AES_GCM_MASTER_KEY_SIZE)
            .build()

    private val masterKey =
        MasterKey
            .Builder(context)
            .setKeyGenParameterSpec(spec)
            .build()

    private val pref: SharedPreferences =
        EncryptedSharedPreferences.create(
            context,
            PREF_KEY,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
        )

    companion object {
        private const val PREF_KEY = "session_prefs"
        private const val TOKEN_KEY = "access_token"
    }

    fun saveAccessToken(token: String) {
        pref.edit().putString(TOKEN_KEY, token).apply()
    }

    fun getAccessToken(): String? = pref.getString(TOKEN_KEY, null)

    fun clearAccessToken() {
        pref.edit().remove(TOKEN_KEY).apply()
    }
}
