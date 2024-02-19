package io.curity.haapidemo.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import se.curity.identityserver.haapi.android.driver.Storage

/*
 * A utility store for temporary DPoP related data when getting OAuth tokens from the token endpoint
 */
class SharedPreferenceStorage(
    private val context: Context,
    private val name: String = "tokenBoundKeyStorage"
) : Storage {

    override fun get(key: String): String? {
        return getSharedPreferences().getString(key, null)
    }

    override fun set(
        value: String,
        key: String
    ) {
        getSharedPreferences().edit(commit = true) {
            putString(key, value)
        }
    }

    override fun delete(key: String) {
        getSharedPreferences().edit(commit = true) {
            remove(key)
        }
    }

    override fun getAll(): Map<String, String> {
        @Suppress("UNCHECKED_CAST")
        return getSharedPreferences().all.filterValues { it is String } as Map<String, String>
    }

    private fun getSharedPreferences(): SharedPreferences {
        return context.getSharedPreferences(
            name,
            Context.MODE_PRIVATE
        )
    }
}
