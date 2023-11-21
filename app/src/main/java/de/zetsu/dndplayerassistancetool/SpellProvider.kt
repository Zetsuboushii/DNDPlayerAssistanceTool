package de.zetsu.dndplayerassistancetool

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import de.zetsu.dndplayerassistancetool.dataclasses.SpellListItem
import de.zetsu.dndplayerassistancetool.dataclasses.SpellDetail

class SpellProvider(private val context: Context) {

    val queue = Volley.newRequestQueue(context)
    val url = "http://www.dnd5eapi.co/api/spells"
    var countRequest = 0
    var flagAPI = false
    var spellListLength = 0
    fun loadSpellList(callback: (List<SpellListItem>) -> Unit) {
        val jsonRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                countRequest++
                val resultsArray = response.getJSONArray("results")
                spellListLength = resultsArray.length()
                val spellListItems = (0 until spellListLength).map { index ->
                    SpellListItem(resultsArray.getJSONObject(index))
                }.toList()

                callback.invoke(spellListItems)
            },
            { error ->
                Log.d("APILog", "Error loading spell list: ${error.message}")
            })

        queue.add(jsonRequest)
    }

    fun loadSpellDetails(index: String, callback: (SpellDetail) -> Unit) {
        val jsonRequest = JsonObjectRequest(
            Request.Method.GET, url.plus("/").plus(index), null,
            { response ->
                //println(response)
                countRequest++
                // for all elements in Spellist + SpellList itself
                if(countRequest == spellListLength+1){
                    flagAPI = true
                    Log.d("APILoadFlag", "API Call is completed")
                }
                callback.invoke(SpellDetail(response))
            },
            { error ->
                Log.d("APILog", "Error loading spell list: ${error.message}")
            })

        queue.add(jsonRequest)

    }
}
