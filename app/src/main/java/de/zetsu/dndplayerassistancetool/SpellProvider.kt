package de.zetsu.dndplayerassistancetool

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.android.volley.NoConnectionError
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import de.zetsu.dndplayerassistancetool.dataclasses.Spell
import de.zetsu.dndplayerassistancetool.dataclasses.SpellDetail

class SpellProvider(private val context: Context) {

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
                Log.d("APILog", "Error loading spell details: $error")
                handleError(error)
            })

        queue.add(jsonRequest)

    }

    fun loadAllSpellDetails(callback: (List<SpellDetail>) -> Unit) {
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

    fun loadSelectedSpellDetailsFromCache() {
        //TODO:Implement loadSelectedSpellDetailsFromCache

        // load selected-cache
        // filter indexes from full-cache by indexes from selected-cache
        // parse the selected SpellDetails
        // add the parsed SpellDetails to a List<SpellDetails>

        // or save the detailed-selectedSpells in cache and simply load it
    }

    private fun handleError(exception: Exception) {
        val errorMessage: String = when (exception) {
            is NoConnectionError -> "No internet connection, load from cache"
            else -> "Error occurred: ${exception.message}"
        }
        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
    }


}
