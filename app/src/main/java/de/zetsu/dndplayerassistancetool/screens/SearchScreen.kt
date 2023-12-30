package de.zetsu.dndplayerassistancetool.screens

import android.content.Context
import android.util.Log
import android.widget.Toast
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
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import de.zetsu.dndplayerassistancetool.CacheManager
import de.zetsu.dndplayerassistancetool.Constants
import de.zetsu.dndplayerassistancetool.SpellProvider
import de.zetsu.dndplayerassistancetool.composables.GoToTopButton
import de.zetsu.dndplayerassistancetool.composables.SimpleSearchBar
import de.zetsu.dndplayerassistancetool.composables.SpellCard
import de.zetsu.dndplayerassistancetool.dataclasses.Spell
import de.zetsu.dndplayerassistancetool.dataclasses.SpellDetail


@Composable
fun Search(context: Context) {

    // API call
    val spellList = remember { mutableListOf<Spell>() }
    var spellDetailList = remember { mutableListOf<SpellDetail>() }
    val spellProvider = SpellProvider(context)
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    val spellListCacheManager = CacheManager(context)

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val expands = remember { mutableListOf<SpellDetail>() }
    var selects = remember { mutableListOf<SpellDetail>() }

    var loaded by remember { mutableStateOf(false) }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> {
                    try {
                        if (!loaded) {
                            if (!wasScreenVisitedBefore(context)) {
                                //get data from network
                                setScreenAsVisited(context, true)

                                spellProvider.loadAllSpellDetailData(successCallback = {
                                    spellDetailList.clear()
                                    spellDetailList.addAll(it)
                                    spellDetailList.sortBy { it.name }
                                    loaded = true
                                    //TODO: make new function for loadSelected Spells,
                                    // which can only load from cache and doesn't throw toast for
                                    // not selected spells
                                    spellProvider.loadSelectedSpells {
                                        selects.clear()
                                        selects.addAll(it)
                                        //loaded = true
                                    }
                                    //TODO: Fix error where Screen is written before Selects is fully ready

                                }) {
                                    // load data from cache if no network
                                    val spellDetails =
                                        spellListCacheManager.loadSpellDetailListFromCache()
                                    // case: no internet without cache
                                    if (spellDetails == null) {
                                        Toast.makeText(
                                            context,
                                            "No cache available try loading again with an internet connection",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    } else {
                                        //case: no internet with cache
                                        spellDetailList.clear()
                                        spellDetailList.addAll(spellDetails.toMutableList())
                                        spellDetailList.sortBy { it.name }
                                        loaded = true
                                        spellProvider.loadSelectedSpells {
                                            selects.clear()
                                            selects.addAll(it)
                                            //loaded = true
                                        }

                                    }
                                }
                            } else {
                                //load data from cache
                                spellListCacheManager.loadSpellDetailListFromCache()
                                    ?.toMutableStateList()
                                    ?.let {
                                        spellDetailList.clear()
                                        spellDetailList.addAll(it)
                                        Log.d("Cache", "loaded Spells from cache")
                                        spellDetailList.sortBy { it.name }
                                        loaded = true
                                        spellProvider.loadSelectedSpells {
                                            selects.clear()
                                            selects.addAll(it)
                                            //loaded = true
                                        }
                                    } ?: {
                                    Log.d("Cache", "Error: loading cache")
                                }
                            }
                        }
                    } catch (exception: Exception) {
                        loaded = true
                        spellProvider.handleError(exception)
                    }
                }

                Lifecycle.Event.ON_PAUSE -> {
                    spellProvider.saveSelectedIndices(selects)
                    Log.d("Lifecycle", "On_Pause")
                }

                Lifecycle.Event.ON_DESTROY -> {
                    Log.d("Lifecycle", "On_Destroy")
                }

                else -> {}
            }

        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

//--------------------------Mauer---------------------

    // spell cards + search bar

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
        if (loaded) {
            // spell cards
            items(spellDetailList) {
                Box() {
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
        Row { GoToTopButton(coroutineScope = coroutineScope, lazyListState = listState) }
    }
}

//--------------------Mauer---------------------------

fun wasScreenVisitedBefore(context: Context): Boolean {
    val sharedPreferences =
        context.getSharedPreferences(Constants.REFERENCE_NAME, Context.MODE_PRIVATE)
    return sharedPreferences.getBoolean(Constants.SCREEN_VISITED_KEY, false)
}

fun setScreenAsVisited(context: Context, status: Boolean) {
    val sharedPreferences =
        context.getSharedPreferences(Constants.REFERENCE_NAME, Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putBoolean(Constants.SCREEN_VISITED_KEY, status)
    editor.apply()
}