package de.zetsu.dndplayerassistancetool

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import de.zetsu.dndplayerassistancetool.dataclasses.Spell

class SpellProvider(private val context: Context) {

    val url = "http://www.dnd5eapi.co/api/spells"

    fun loadSpellList(callback: (List<Spell>) -> Unit){
        val jsonRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            {response ->
                val resultsArray = response.getJSONArray("results")
                val spells = (0 until resultsArray.length()).map { index ->
                    Spell(resultsArray.getJSONObject(index))
                }.toList()

                callback.invoke(spells)
            },
            { error ->
                Log.d("APILog", "Error loading spell list: ${error.message}")})

        val queue = Volley.newRequestQueue(context)
        queue.add(jsonRequest)
    }
}
