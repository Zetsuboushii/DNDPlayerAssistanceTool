package de.zetsu.dndplayerassistancetool

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.android.volley.NoConnectionError
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

    // create API call to get List of all spell-names and indexes to create detailed spell list
    // this list doesn't has more information than name and index
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
                handleError(error)
            })

        queue.add(jsonRequest)
    }

    // create API call to get detailed spell information's based on its index
    fun loadSpellDetails(index: String, callback: (SpellDetail) -> Unit) {
        val jsonRequest = JsonObjectRequest(
            Request.Method.GET, url.plus("/").plus(index), null,
            { response ->
                //println(response)
                countRequest++
                // for all elements in Spellist + SpellList itself
                if (countRequest == spellListLength + 1) {
                    flagAPI = true
                    Log.d("APILoadFlag", "API Call is completed")
                }
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

    // shows errors to the user
    private fun handleError(exception: Exception) {
        val errorMessage: String = when (exception) {
            is NoConnectionError -> "No Internet Connection"
            else -> "Error occurred: ${exception.message}"
        }
        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
    }

}
