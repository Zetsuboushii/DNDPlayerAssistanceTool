package de.zetsu.dndplayerassistancetool

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.android.volley.NoConnectionError
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import de.zetsu.dndplayerassistancetool.dataclasses.Spell
import de.zetsu.dndplayerassistancetool.dataclasses.SpellDetail
import java.io.File
import java.io.FileNotFoundException

class SpellProvider(private val context: Context) {

    companion object {
        private fun filterByIndex(
            indices: List<String>,
            spellDetails: List<SpellDetail>
        ): List<SpellDetail> {
            return spellDetails.filter { indices.contains(it.index) }
        }
    }

    val queue = Volley.newRequestQueue(context)
    val url = "http://www.dnd5eapi.co/api/spells"
    var spellListLength = 0
    val cacheManager = CacheManager(context)
    var spellList = mutableListOf<Spell>()
    var spellDetailList = mutableListOf<SpellDetail>()
    fun loadSpellList(callback: (List<Spell>) -> Unit, errorCallback: (Exception) -> Unit) {
        val jsonRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                val resultsArray = response.getJSONArray("results")
                spellListLength = resultsArray.length()
                val spells = (0 until spellListLength).map { index ->
                    Spell(resultsArray.getJSONObject(index))
                }.toMutableList()
                spellList = spells
                callback.invoke(spells)
            },
            { error ->
                Log.d("APILog", "Error loading spell list: ${error.message}")
                handleError(error)
                errorCallback.invoke(error)
            })

        queue.add(jsonRequest)
    }

    // create API call to get detailed spell information's based on its index
    fun loadSpellDetails(index: String, callback: (SpellDetail) -> Unit) {
        val jsonRequest = JsonObjectRequest(
            Request.Method.GET, url.plus("/").plus(index), null,
            { response ->
                try {
                    val spellDetail = SpellDetail(response)
                    callback.invoke(spellDetail)
                } catch (exception: Exception) {
                    handleError(exception)
                }

            },
            { error ->
                Log.d("APILog", "Error loading spell details: ${error}")
                handleError(error)
            })

        queue.add(jsonRequest)

    }

    fun loadAllSpellDetails(callback: (List<SpellDetail>) -> Unit) {
        Log.d("APILoadFlag", "started api calls")
        var spellDetailList = mutableListOf<SpellDetail>()
        for (spell in spellList) {
            loadSpellDetails(spell.index) { spellDetail ->
                spellDetailList.add(spellDetail)
                if (spell == spellList.lastOrNull()) {
                    this.spellDetailList = spellDetailList
                    callback.invoke(spellDetailList)
                    Log.d("APILoadFlag", "API Call is completed")

                    Log.d("cache", "save spellDetailList to cache")
                    cacheManager.saveSpellListToCache(
                        spellDetailList
                    )
                }
            }
        }
    }

    fun loadAllSpellDetailData(
        successCallback: (List<SpellDetail>) -> Unit,
        errorCallback: (Exception) -> Unit
    ) {
        cacheManager.loadSpellDetailListFromCache()?.let(successCallback)

        loadSpellList(callback = { _ ->
            loadAllSpellDetails(callback = successCallback)
        }, errorCallback = errorCallback)
    }

    private fun getSelectedIndicesFile() =
        File(context.filesDir, Constants.SELECTED_SPEELS_FILE_NAME)

    fun saveSelectedIndices(spellDetails: List<SpellDetail>) {
        val gson = Gson()
        val json = gson.toJson(spellDetails.map { it.index })
        getSelectedIndicesFile().writeText(json)
    }

    fun loadSelectedIndices(): List<String> {
        val gson = Gson()
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(getSelectedIndicesFile().readText(), type)
    }

    fun loadSelectedSpells(callback: (List<SpellDetail>) -> Unit) {
        // if no selected list on disk
        val indices = try {
            loadSelectedIndices()
        } catch (e: Exception) {
            null
        }
        // if no SpellDetailsList in cache make api call, else load from cache
        try {
            cacheManager.loadSpellDetailListFromCache()?.let {
                callback.invoke(filterByIndex(indices ?: listOf(), it))
            }
        } catch (exception: Exception) {
            loadAllSpellDetailData(successCallback = {
                callback.invoke(filterByIndex(indices ?: listOf(), it))
            }) {
                handleError(it)
            }
        }
    }

    fun handleError(exception: Exception) {
        val errorMessage: String = when (exception) {
            is NoConnectionError -> "No internet connection, load from cache"
            is FileNotFoundException -> "No cache available try loading again with an internet connection"
            else -> "Error occurred: ${exception.message}"
        }
        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
    }

}
