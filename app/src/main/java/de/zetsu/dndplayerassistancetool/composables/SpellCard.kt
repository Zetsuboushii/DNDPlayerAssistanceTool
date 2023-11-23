package de.zetsu.dndplayerassistancetool.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import de.zetsu.dndplayerassistancetool.R
import de.zetsu.dndplayerassistancetool.dataclasses.SpellDetail

@Composable
fun SpellCard(spell: SpellDetail, expanded: Boolean, onClick: () -> Unit) {
    ElevatedCard(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable(
                onClick = { onClick.invoke() }
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(10.dp)
        ) {
            Icon(
                painter = painterResource(
                    when (spell.school.index) {
                        "abjuration" -> R.drawable.ic_school_abjuration
                        "conjuration" -> R.drawable.ic_school_conjuration
                        "divination" -> R.drawable.ic_school_divination
                        "enchantment" -> R.drawable.ic_school_enchantment
                        "evocation" -> R.drawable.ic_school_evocation
                        "illusion" -> R.drawable.ic_school_illusion
                        "necromancy" -> R.drawable.ic_school_necromancy
                        "transmutation" -> R.drawable.ic_school_transmutation
                        else -> R.drawable.ic_taunt_fill
                    }
                ),
                contentDescription = null,
                modifier = Modifier.size(35.dp)
            )
            Text(
                text = spell.level.toString(),
                modifier = Modifier.padding(5.dp)
            )
            Spacer(modifier = Modifier.size(15.dp))
            Column(horizontalAlignment = Alignment.Start) {
                Text(text = spell.name, fontWeight = FontWeight.Bold)
                Text(text = spell.school.name)
            }
        }
        if (expanded) {
            var description = ""
            for (i in 0 until spell.desc.size) {
                description += spell.desc[i]
                if (i < spell.desc.size - 1) {
                    description += "\n"
                }
            }
            Row {
                SpellAttributeBlock("LEVEL", spell.level.toString())
                SpellAttributeBlock("CAST TIME", spell.castTime)
                SpellAttributeBlock("RANGE/AREA", spell.range)
            }
            Row {
                SpellAttributeBlock("DURATION", spell.duration)
                SpellAttributeBlock("SCHOOL", spell.school.name)
            }
            Row {
                spell.attackType?.let { SpellAttributeBlock("ATTACK/SAVE", it) }
                spell.damage.damageType?.name?.let {
                    SpellAttributeBlock("DAMAGE/EFFECT", it)
                }
            }
            
            LazyHorizontalGrid(rows = GridCells.Fixed(3)) {
                
            }

            Divider(Modifier.padding(10.dp), thickness = 2.dp)
            Row(modifier = Modifier.padding(10.dp)) {
                Text(text = description)
            }
        }
    }
}