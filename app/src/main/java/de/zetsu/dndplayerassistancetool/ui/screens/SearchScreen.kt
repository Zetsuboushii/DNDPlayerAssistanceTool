package de.zetsu.dndplayerassistancetool.ui.screens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import de.zetsu.dndplayerassistancetool.dataclasses.SpellDetail

@Composable
fun Search(context: Context) {

    val spellDetailList = remember { mutableStateListOf<SpellDetail>() }
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
                        // test if screen is already on display and loaded
                        // if yes don't load cache again
                        if (!loaded) {

                            // load selected spells to show them as marked

                            spellProvider.loadSelectedSpells {
                                selects.clear()
                                selects.addAll(it)
                            }

                            if (!wasScreenVisitedBefore(context)) {
                                // load cache and then update it via api call
                                setScreenAsVisited(context, true)
                                val spellDetails =
                                    spellListCacheManager.loadSpellDetailListFromCache()
                                spellDetails?.let {
                                    spellDetailList.clear()
                                    spellDetailList.addAll(spellDetails.toMutableList())
                                    spellDetailList.sortBy { it.name }
                                    loaded = true
                                }

                                spellProvider.updateCacheViaAPI()

                            } else {
                                //load data from cache
                                spellListCacheManager.loadSpellDetailListFromCache()
                                    ?.let {
                                        spellDetailList.clear()
                                        spellDetailList.addAll(it)
                                        Log.d("Cache", "loaded Spells from cache")
                                        spellDetailList.sortBy { it.name }
                                        loaded = true

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
    val searchTerm = remember { mutableStateOf<String>("") }
    LazyColumn(state = listState) {
        if (loaded) {
            item {
                SimpleSearchBar(
                    onSearch = { search -> searchTerm.value = search }
                )
            }
            // spell cards
            items(spellDetailList.filter {
                it.name.startsWith(searchTerm.value, ignoreCase = true)
            }.toList()) {
                Box {
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
        context.getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE)
    return sharedPreferences.getBoolean(Constants.SCREEN_VISITED_KEY, false)
}

fun setScreenAsVisited(context: Context, status: Boolean) {
    val sharedPreferences =
        context.getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putBoolean(Constants.SCREEN_VISITED_KEY, status)
    editor.apply()
}