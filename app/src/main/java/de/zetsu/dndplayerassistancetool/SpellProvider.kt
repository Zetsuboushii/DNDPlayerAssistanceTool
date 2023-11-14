package de.zetsu.dndplayerassistancetool

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import de.zetsu.dndplayerassistancetool.dataclasses.SpellList

class SpellProvider(private val context: Context) {

    val url = "http://www.dnd5eapi.co/api/spells"

    fun loadSpellList(callback: (List<SpellList>) -> Unit){
        val jsonRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            {response ->
                val resultsArray = response.getJSONArray("results")
                val spellLists = (0 until resultsArray.length()).map { index ->
                    SpellList(resultsArray.getJSONObject(index))
                }.toList()

                callback.invoke(spellLists)
            },
            { error ->
                Log.d("APILog", "Error loading spell list: ${error.message}")})

        val queue = Volley.newRequestQueue(context)
        queue.add(jsonRequest)
    }
}
