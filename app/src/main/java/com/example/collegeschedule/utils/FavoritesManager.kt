package com.example.collegeschedule.utils

import android.content.Context

object FavoritesManager {

    private const val PREFS_NAME = "favorites_prefs"
    private const val KEY_GROUPS = "favorite_groups"

    fun getFavorites(context: Context): Set<String> {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getStringSet(KEY_GROUPS, emptySet()) ?: emptySet()
    }

    fun addFavorite(context: Context, group: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val current = getFavorites(context).toMutableSet()
        current.add(group)
        prefs.edit().putStringSet(KEY_GROUPS, current).apply()
    }

    fun removeFavorite(context: Context, group: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val current = getFavorites(context).toMutableSet()
        current.remove(group)
        prefs.edit().putStringSet(KEY_GROUPS, current).apply()
    }

    fun isFavorite(context: Context, group: String): Boolean {
        return getFavorites(context).contains(group)
    }
}
