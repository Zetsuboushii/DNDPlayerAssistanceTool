package de.zetsu.dndplayerassistancetool.screens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import de.zetsu.dndplayerassistancetool.SpellProvider
import de.zetsu.dndplayerassistancetool.composables.AddToBookButton
import de.zetsu.dndplayerassistancetool.composables.GoToTopButton
import de.zetsu.dndplayerassistancetool.composables.SimpleSearchBar
import de.zetsu.dndplayerassistancetool.composables.SpellCard
import de.zetsu.dndplayerassistancetool.dataclasses.SpellDetail
import de.zetsu.dndplayerassistancetool.dataclasses.SpellListItem

@Composable
fun Search(context: Context) {

    // API call
    val spellListItemList = remember { mutableListOf<SpellListItem>() }
    var spellDetailList = remember { mutableListOf<SpellDetail>() }
    val spellProvider = SpellProvider(context)
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current

    var toBeLoading by remember { mutableStateOf(true) }

    if (toBeLoading) {
        // only make api call when screen is created
        // TODO: use different method to make API-Call only on create, because DisposableEffect is to heavy
        //       if on delete isn't used
        DisposableEffect(lifecycleOwner) {
            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_CREATE -> {
                        spellProvider.loadSpellList { spells ->
                            //Log.d("SpellsLog", spells.toString())
                            spellListItemList.clear()
                            spellListItemList.addAll(spells)
                            for (spell in spellListItemList) {
                                spellProvider.loadSpellDetails(spell.index) { spellDetail ->
                                    //Log.d("SpellDetail: ${spell.name}", spellDetail.toString())
                                    spellDetailList.add(spellDetail)
                                    if (spellProvider.flagAPI) toBeLoading = false
                                }
                            }
                        }
                    }

                    Lifecycle.Event.ON_DESTROY -> {
                        println("on destroy")
                    }

                    else -> {}
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)

            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }
    }

    // spell cards + search bar
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val expands = remember { mutableListOf<SpellDetail>() }
    val selects = remember { mutableListOf<SpellDetail>() }

    spellDetailList.sortBy { it.name }

    LazyColumn(state = listState) {
        item {
            SimpleSearchBar(
                onSearch = { search ->
                    spellDetailList = spellDetailList.filter {
                        it.name.startsWith(search, ignoreCase = true)
                    }.toMutableList()
                    for (i in 0 until spellDetailList.size) {
                        Log.d("SearchResults", spellDetailList[i].name)
                    }
                }
            )
        }
        if (!toBeLoading) {
            // spell cards
            items(spellDetailList) {
                Box(modifier = Modifier.background(Color.White)) {
                    var expanded by remember { mutableStateOf(expands.contains(it)) }
                    var selected by remember { mutableStateOf(selects.contains(it)) }
                    SpellCard(
                        spell = it,
                        expanded = expanded,
                        onClick = {
                            expanded = !expanded
                            if (expanded) expands.add(it) else expands.remove(it)
                        },
                        selected = selected,
                        onLongClick = {
                            selected = !selected
                            if (selected) selects.add(it) else selects.remove(it)
                        }
                    )
                }
            }
        }
    }
    Box(contentAlignment = Alignment.BottomEnd) {
        Row { AddToBookButton(onClick = { /*TODO*/ }) }
        Row { GoToTopButton(coroutineScope = coroutineScope, lazyListState = listState) }
    }
}
