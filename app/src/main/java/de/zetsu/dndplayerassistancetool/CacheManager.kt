package de.zetsu.dndplayerassistancetool

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import de.zetsu.dndplayerassistancetool.dataclasses.SpellDetail
import java.io.File

class CacheManager(private val context: Context) {

    private fun getFullCacheFile() = File(context.cacheDir, Constants.FULL_CACHE_FILE_NAME)

    fun saveSpellListToCache(spellDetailList: List<SpellDetail>) {
        val gson = Gson()
        val json = gson.toJson(spellDetailList)
        getFullCacheFile().writeText(json)
    }

    fun loadSpellDetailListFromCache(): List<SpellDetail>? {

        val json = getFullCacheFile().readText()
        val gson = Gson()
        val type = object : TypeToken<List<SpellDetail>>() {}.type
        return gson.fromJson(json, type)
    }
}