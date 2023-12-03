package de.zetsu.dndplayerassistancetool.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import de.zetsu.dndplayerassistancetool.R
import de.zetsu.dndplayerassistancetool.dataclasses.SpellDetail
import de.zetsu.dndplayerassistancetool.ui.theme.Purple80

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SpellCard(
    spell: SpellDetail,
    expanded: Boolean,
    onClick: () -> Unit,
    selected: Boolean,
    onLongClick: () -> Unit
) {
    val haptics = LocalHapticFeedback.current
    ElevatedCard(
        colors = CardDefaults.cardColors(
            containerColor = if (!selected) {
                MaterialTheme.colorScheme.surfaceVariant
            } else {
                Purple80
            }
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .combinedClickable(
                onClick = { onClick.invoke() },
                onLongClick = {
                    onLongClick.invoke()
                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                }
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

            //Spell Attributes
            Row(Modifier.fillMaxWidth()) {
                Column(Modifier.weight(1f)) {
                    SpellAttributeBlock(
                        "LEVEL",
                        if (spell.level == 0) "Cantrip" else spell.level.toString()
                    )
                }
                Column(Modifier.weight(1f)) {
                    SpellAttributeBlock("CAST TIME", spell.castTime)
                }
            }
            Row(Modifier.fillMaxWidth()) {
                Column(Modifier.weight(1f)) {
                    SpellAttributeBlock("RANGE/AREA", spell.range)
                }
                Column(Modifier.weight(1f)) {
                    SpellAttributeBlock("DURATION", spell.duration)
                }
            }
            Row(Modifier.fillMaxWidth()) {
                Column(Modifier.weight(1f)) {
                    spell.damage.damageType?.name?.let {
                        SpellAttributeBlock("DAMAGE/EFFECT", it)
                    }
                }
                Column(Modifier.weight(1f)) {
                    spell.attackType?.let { SpellAttributeBlock("ATTACK/SAVE", it) }
                }
            }

            Divider(Modifier.padding(10.dp), thickness = 2.dp)

            //Description
            val scrollState = rememberScrollState()
            Row(
                modifier = Modifier
                    .padding(10.dp)
                    .height(300.dp)
                    .verticalScroll(scrollState)
            ) {
                Text(text = description)
            }

            //Classes
            Row(
                Modifier
                    .padding(horizontal = 10.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                for (i in 0 until spell.classes.size) {
                    Text(text = spell.classes[i].name)
                    if (i < spell.classes.size - 1) Text(text = " ⋅ ")
                }
            }
        }
    }
}
