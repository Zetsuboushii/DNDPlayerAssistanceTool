package de.zetsu.dndplayerassistancetool.dataclasses

data class AreaOfEffect(
    var size: Int? = 0,
    var type: AreaOfEffectType? = AreaOfEffectType.CUBE
)

