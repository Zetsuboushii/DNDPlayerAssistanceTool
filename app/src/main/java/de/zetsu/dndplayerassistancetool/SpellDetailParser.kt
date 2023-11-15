package de.zetsu.dndplayerassistancetool

import de.zetsu.dndplayerassistancetool.dataclasses.AreaOfEffect
import de.zetsu.dndplayerassistancetool.dataclasses.AreaOfEffectType
import de.zetsu.dndplayerassistancetool.dataclasses.Class
import de.zetsu.dndplayerassistancetool.dataclasses.Damage
import de.zetsu.dndplayerassistancetool.dataclasses.DamageType
import de.zetsu.dndplayerassistancetool.dataclasses.School
import org.json.JSONArray
import org.json.JSONObject

class SpellDetailParser {


    fun parseSchool(json: JSONObject?): School {
        return School(
            json?.optString("index") ?: "",
            json?.optString("name") ?: "",
            json?.optString("url") ?: ""
        )
    }

    fun parseClasses(jsonArray: JSONArray?): ArrayList<Class> {
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

    fun parseAoE(json: JSONObject): AreaOfEffect {
        val size = json.optInt("size", 0)
        val typeString = json.optString("type", "").lowercase()

        val type = when (typeString) {
            "sphere" -> AreaOfEffectType.SPHERE
            "cone" -> AreaOfEffectType.CONE
            "cylinder" -> AreaOfEffectType.CYLINDER
            "line" -> AreaOfEffectType.LINE
            "cube" -> AreaOfEffectType.CUBE
            else -> throw IllegalArgumentException("Invalid AreaOfEffectType: $typeString")
        }

        return AreaOfEffect(size, type)
    }

    fun parseDamage(json: JSONObject): Damage {
        val damageTypeJson = json.optJSONObject("damage_type")
        val damageType = if (damageTypeJson != null) parseDamageType(damageTypeJson) else DamageType("", "", "")

        val damageAtLevelJson = json.optJSONObject("damage_at_slot_level")
        val damageAtLevel = if (damageAtLevelJson != null) parseDamageAtLevel(damageAtLevelJson) else emptyMap<String, Any>()

        return Damage(damageType, damageAtLevel)

    }

    private fun parseDamageType(json: JSONObject): DamageType {
        return DamageType(
            json.optString("index", ""),
            json.optString("name", ""),
            json.optString("url", "")
        )
    }

    private fun parseDamageAtLevel(json: JSONObject): Map<String, Any> {
        val damageAtLevelMap = mutableMapOf<String, Any>()

        json.keys().forEach { key ->
            val value = json.opt(key)
            if (value != null) {
                damageAtLevelMap[key] = value
            }
        }

        return damageAtLevelMap
    }

    fun toStringArrayList(jsonArray: JSONArray?): ArrayList<String> {
        val stringList = ArrayList<String>()

        if (jsonArray != null) {
            for (i in 0 until jsonArray.length()) {
                stringList.add(jsonArray.optString(i))
            }
        }

        return stringList
    }
}