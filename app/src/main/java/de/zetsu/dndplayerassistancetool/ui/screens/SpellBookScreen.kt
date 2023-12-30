package de.zetsu.dndplayerassistancetool.ui.screens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import de.zetsu.dndplayerassistancetool.SpellProvider
import de.zetsu.dndplayerassistancetool.composables.GoToTopButton
import de.zetsu.dndplayerassistancetool.composables.HeaderSection
import de.zetsu.dndplayerassistancetool.composables.NoSpellsSaved
import de.zetsu.dndplayerassistancetool.composables.SpellCard
import de.zetsu.dndplayerassistancetool.dataclasses.SpellDetail


@Composable
fun SpellBook(context: Context) {
    var loaded by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    //val navController = rememberNavController()

    val spellProvider = SpellProvider(context)
    var spellDetailList = remember { mutableStateListOf<SpellDetail>() }
    val expands = remember { mutableListOf<SpellDetail>() }
    val selects = remember { mutableListOf<SpellDetail>() }
    if (!loaded) {
        loaded = true
        spellProvider.loadSelectedSpells {
            spellDetailList.clear()
            spellDetailList.addAll(it)
        }
    }
    var moin = mutableListOf<String>()

    Log.d("SpellBook", spellDetailList.toString())


    // not loading picture if spells are saved into the spellbook
    if (spellDetailList.isEmpty()) {
        Column {
            HeaderSection()
            NoSpellsSaved()
        }
    }


    LazyColumn(state = listState) {
        item {
            HeaderSection()

        }

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
                    spellDetailList.remove(spell)
                    //TODO: all spells get deleted
                    // spellProvider.saveSelectedIndices(selects)
                    Log.d("SpellBook", selects.toString())
                })

            }
        }
    }
    Box(contentAlignment = Alignment.BottomEnd) {
        //Row { AddToBookButton(onClick = {navController.navigate(Screen.Search.route) }) }
        Row { GoToTopButton(coroutineScope = coroutineScope, lazyListState = listState) }
    }
}
