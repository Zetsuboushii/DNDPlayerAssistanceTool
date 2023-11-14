package de.zetsu.dndplayerassistancetool.dataclasses

import org.json.JSONObject

data class SpellDetail(
    var index: String,
    var url: String,
    var name: String,
    var level: Int,
    var desc: String,
    var higherLevel: String,
    var range: String,
    var aoe: AreaOfEffect,
    var ritual: Boolean,
    var duration: String,
    var concentration: Boolean,
    var castTime: String,
    var attackType: String,
    var damageType: DamageType,
    var school: School,
    var classes: ArrayList<Class>
) {
    constructor(json: JSONObject) :
            this(
                json.optString("index"),
                json.optString("url"),
                json.optString("name"),
                json.optInt("level"),
                json.optString("desc"),
                json.optString("higherLevel"),
                json.optString("range"),
                json.opt("aoe") as AreaOfEffect,
                json.optBoolean("ritual"),
                json.optString("duration"),
                json.optBoolean("concentration"),
                json.optString("castTime"),
                json.optString("attackType"),
                json.opt("damageType") as DamageType,
                json.opt("school") as School,
                json.opt("classes") as ArrayList<Class>
            )
}
