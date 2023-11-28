package de.zetsu.dndplayerassistancetool

import android.content.Context
import de.zetsu.dndplayerassistancetool.dataclasses.SpellDetail
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

class CacheManager(private val context: Context) {

    private fun getFullCacheFile() = File(context.cacheDir, Constants.FULL_CACHE_FILE_NAME)

    fun saveSpellListToCache(spellDetailList: List<SpellDetail>) {
        val gson = Gson()
        val json = gson.toJson(spellDetailList)
        getFullCacheFile().writeText(json)
    }


    fun loadSpellListFromCache(): List<SpellDetail>? {

        val json = getFullCacheFile().readText()
        val gson = Gson()
        val type = object : TypeToken<List<SpellDetail>>() {}.type
        return gson.fromJson(json, type)

        /*
        return if (json != null) {
            val type = object : TypeToken<List<SpellDetail>>() {}.type
            gson.fromJson(json, type)
        } else {
            null
        }
         */
    }
}