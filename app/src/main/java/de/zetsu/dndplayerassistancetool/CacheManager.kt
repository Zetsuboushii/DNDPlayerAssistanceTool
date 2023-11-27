package de.zetsu.dndplayerassistancetool

import android.content.Context
import de.zetsu.dndplayerassistancetool.dataclasses.SpellDetail
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CacheManager(private val context: Context) {

    private val SPELL_LIST_CACHE_KEY = "fullSpellListCacheKey"

    fun saveSpellListToCache(spellDetailList: List<SpellDetail>) {
        val gson = Gson()
        val json = gson.toJson(spellDetailList)
        val sharedPreferences =
            context.getSharedPreferences("listDetailPreference", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(SPELL_LIST_CACHE_KEY, json)
        editor.apply()
    }

    fun loadSpellListFromCache(): List<SpellDetail>? {
        val sharedPreferences =
            context.getSharedPreferences("listDetailPreference", Context.MODE_PRIVATE)
        val json = sharedPreferences.getString(SPELL_LIST_CACHE_KEY, null)
        val gson = Gson()

        return if (json != null) {
            val type = object : TypeToken<List<SpellDetail>>() {}.type
            gson.fromJson(json, type)
        } else {
            null
        }
    }
}