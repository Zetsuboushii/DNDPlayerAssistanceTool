package de.zetsu.dndplayerassistancetool

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import de.zetsu.dndplayerassistancetool.dataclasses.SpellDetail
import java.io.File

class CacheManager(private val context: Context) {

    private fun getFullCacheFile() = File(context.filesDir, Constants.FULL_CACHE_FILE_NAME)

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

    // write the pre installed JsonFile for SpellDetails into file system, to make
    // first load faster and user friendly
    fun writeBackUpIntoCacheFile() {
        val stream = context.assets.open(Constants.BACKUP_ASSET_FILE)
        val jsonContent = stream.bufferedReader().use { it.readText() }
        getFullCacheFile().writeText(jsonContent)
    }


    fun checkForWritingBackUpIntoCache() {
        val sharedPreferences =
            context.getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE)
        if (!sharedPreferences.getBoolean(Constants.DID_WRITE_BACKUP_INTO_CACHE, false)) {
            Log.d(this::class.simpleName, "writing backup into files")
            writeBackUpIntoCacheFile()
            val editor = sharedPreferences.edit()
            editor.putBoolean(Constants.DID_WRITE_BACKUP_INTO_CACHE, true)
            editor.apply()
        }
    }
}