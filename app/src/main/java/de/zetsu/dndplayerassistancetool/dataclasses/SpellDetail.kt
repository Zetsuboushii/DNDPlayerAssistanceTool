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
    var attackType: String?,
    var damage: Damage,
    var school: School,
    var classes: ArrayList<Class>

) {

    companion object{
        val exception = Exception("Error Parsing SpellDetails")
    }

    constructor(json: JSONObject) :
            this(
                json.optString("index",null)?: throw exception,
                json.optString("url",null)?: throw exception,
                json.optString("name",null)?: throw exception,
                // TODO: make pretty
                if(json.optInt("level",-1) != -1) json.optInt("level",-1)
                else throw exception,
                SpellDetailParser().toStringArrayList(json.optJSONArray("desc")),
                SpellDetailParser().toStringArrayList(json.optJSONArray("higher_level")),
                json.optString("range",null)?: throw exception,
                SpellDetailParser().parseAoE(json.optJSONObject("area_of_effect")),
                json.optBoolean("ritual"),
                json.optString("duration",null)?: throw exception,
                json.optBoolean("concentration"),
                json.optString("casting_time",null)?: throw exception,
                // AttackType is optional because of that we accept null
                json.optString("attack_type",null),
                SpellDetailParser().parseDamage(json.optJSONObject("damage")),
                SpellDetailParser().parseSchool(json.optJSONObject("school")),
                SpellDetailParser().parseClasses(json.optJSONArray("classes")),
            )


}
