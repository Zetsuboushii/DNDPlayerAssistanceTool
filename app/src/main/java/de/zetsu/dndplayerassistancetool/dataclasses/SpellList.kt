package de.zetsu.dndplayerassistancetool.dataclasses

import org.json.JSONObject

data class SpellList(

    var index: String,
    var name: String,
    var url: String){

    constructor(json: JSONObject):
            this(
                json.optString("index"),
                json.optString("name"),
                json.optString("url")
            )
}



