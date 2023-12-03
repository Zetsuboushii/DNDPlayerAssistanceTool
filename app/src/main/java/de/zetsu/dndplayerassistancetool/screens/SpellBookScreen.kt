package de.zetsu.dndplayerassistancetool.screens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import de.zetsu.dndplayerassistancetool.SpellProvider
import de.zetsu.dndplayerassistancetool.composables.GoToTopButton
import de.zetsu.dndplayerassistancetool.composables.SpellCard
import de.zetsu.dndplayerassistancetool.dataclasses.SpellDetail

@Composable
fun SpellBook(context: Context) {
    var loaded by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val spellProvider = SpellProvider(context)
    var spellDetailList = remember { mutableListOf<SpellDetail>() }
    val expands = remember {mutableListOf<SpellDetail>() }
    val selects = remember {mutableListOf<SpellDetail>() }
    if (!loaded) {
        loaded = true
        spellProvider.loadSelectedSpells {
            spellDetailList.clear()
            spellDetailList.addAll(it)
        }
    }
    var moin = mutableListOf<String>()
    for (i in 0 until spellDetailList.size) {
        moin.add(spellDetailList[i].name)
        Column {
            Text(text = moin.toString())
        }
    }
    Log.d("SpellBook", spellDetailList.toString())
    //Text(text = "SpellDetail Book")
    LazyColumn(state = listState) {
        // Display selected spells
        items(spellDetailList) { spell ->
            Box(modifier = Modifier.background(Color.White)) {
                var expanded by remember {
                    mutableStateOf(expands.contains(spell))
                }
                var selected by remember {
                    mutableStateOf(expands.contains(spell))
                }
                SpellCard(spell, expanded = expanded, onClick = {
                    expanded = !expanded
                    if (expanded) expands.add(spell) else expands.remove(spell)

                }, selected = selected, onLongClick = {
                    selected = !selected
                    if (selected) selects.add(spell) else selects.remove(spell)
                })

            }
        }
    }
    Box(contentAlignment = Alignment.BottomEnd) {
        // Row { AddToBookButton(onClick = {  }) }
        Row { GoToTopButton(coroutineScope = coroutineScope, lazyListState = listState) }
    }

}