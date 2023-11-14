package de.zetsu.dndplayerassistancetool.dataclasses

import org.json.JSONArray
import org.json.JSONObject

private fun parseSchool(json: JSONObject?): School {
    return School(
        json?.optString("index") ?: "",
        json?.optString("name") ?: "",
        json?.optString("url") ?: ""
    )
}

private fun parseDamageType(json: JSONObject?): DamageType {
    return DamageType(
        json?.optJSONObject("damage_at_character_level")?.keys()?.next() ?: "",
        json?.optJSONObject("damage_type")?.optString("name") ?: "",
        json?.optJSONObject("damage_type")?.optString("url") ?: ""
    )
}


private fun parseClasses(jsonArray: JSONArray?): ArrayList<Class> {
    val classes = arrayListOf<Class>()
    jsonArray?.let {
        for (i in 0 until it.length()) {
            val classObj = it.optJSONObject(i)
            classObj?.let {
                classes.add(
                    Class(
                        it.optString("index"),
                        it.optString("name"),
                        it.optString("url")
                    )
                )
            }
        }
    }
    return classes
}

data class SpellDetail (
    var index: String,
    var url: String,
    var name: String,
    var level: Int,
    //var desc: ArrayList<String>,
    var des: String,
    //var higherLevel: ArrayList<String>,
    var higherLevel: String?,
    var range: String,
    //var aoe: AreaOfEffect,
    var ritual: Boolean,
    var duration: String,
    var concentration: Boolean,
    var castTime: String,
    var attackType: String,
    //var damage: Damage,
    var school: School,
    var classes: ArrayList<Class>

    //TODO create parse Damage with new Damage data class
    //TODO create an new parseAoEType with the new AoE data class
    //TODO parse dec and higher_level not as strings but as String ArrayList

    ) {
    constructor(json: JSONObject):
        this(
            json.optString("index"),
            json.optString("url"),
            json.optString("name"),
            json.optInt("level"),
            json.optJSONArray("desc").toString(),
            json.optJSONArray("higherLevel")?.toString(),
            //json.opt("desc") as ArrayList<String>,
            //json.opt("higherLevel") as ArrayList<String>,
            json.optString("range"),
            //parseAoEType(json.optJSONObject("area_of_effect")),
            json.optBoolean("ritual"),
            json.optString("duration"),
            json.optBoolean("concentration"),
            json.optString("castTime"),
            json.optString("attackType"),
            //json.opt("damageType") as DamageType,
            parseSchool(json.optJSONObject("school")),
            parseClasses(json.optJSONArray("classes"))
        )



}
