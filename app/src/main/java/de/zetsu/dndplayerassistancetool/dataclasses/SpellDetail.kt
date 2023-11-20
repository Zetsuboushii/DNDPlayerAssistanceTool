package de.zetsu.dndplayerassistancetool.dataclasses

import de.zetsu.dndplayerassistancetool.SpellDetailParser
import org.json.JSONObject

data class SpellDetail(
    var index: String,
    var url: String,
    var name: String,
    var level: Int,
    var desc: ArrayList<String>,
    var higherLevel: ArrayList<String>,
    var range: String,
    var aoe: AreaOfEffect?,
    var ritual: Boolean,
    var duration: String,
    var concentration: Boolean,
    var castTime: String,
    var attackType: String,
    var damage: Damage,
    var school: School,
    var classes: ArrayList<Class>

    //TODO: Add remaining nullpointer checks
) {
    constructor(json: JSONObject) :
            this(
                json.optString("index"),
                json.optString("url"),
                json.optString("name"),
                json.optInt("level"),
                SpellDetailParser().toStringArrayList(json.optJSONArray("desc")),
                SpellDetailParser().toStringArrayList(json.optJSONArray("higher_level")),
                json.optString("range"),
                SpellDetailParser().parseAoE(json.optJSONObject("area_of_effect")),
                json.optBoolean("ritual"),
                json.optString("duration"),
                json.optBoolean("concentration"),
                json.optString("castTime"),
                json.optString("attackType"),
                SpellDetailParser().parseDamage(json.optJSONObject("damage")),
                SpellDetailParser().parseSchool(json.optJSONObject("school")),
                SpellDetailParser().parseClasses(json.optJSONArray("classes")),
            )


}
