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

    fun loadSpellList(callback: (List<SpellListItem>) -> Unit) {
        val jsonRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                val resultsArray = response.getJSONArray("results")
                val spellListItems = (0 until resultsArray.length()).map { index ->
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
                println(response)
                callback.invoke(SpellDetail(response))
            },
            { error ->
                Log.d("APILog", "Error loading spell list: ${error.message}")
            })

        queue.add(jsonRequest)
    }
}
