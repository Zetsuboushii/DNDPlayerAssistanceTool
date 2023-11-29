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
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import de.zetsu.dndplayerassistancetool.CacheManager
import de.zetsu.dndplayerassistancetool.Constants
import de.zetsu.dndplayerassistancetool.R
import de.zetsu.dndplayerassistancetool.SpellProvider
import de.zetsu.dndplayerassistancetool.composables.AddToBookButton
import de.zetsu.dndplayerassistancetool.composables.GoToTopButton
import de.zetsu.dndplayerassistancetool.composables.SimpleSearchBar
import de.zetsu.dndplayerassistancetool.composables.SpellCard
import de.zetsu.dndplayerassistancetool.dataclasses.Spell
import de.zetsu.dndplayerassistancetool.dataclasses.SpellDetail
import de.zetsu.dndplayerassistancetool.dataclasses.SpellListItem
import kotlinx.coroutines.launch

@Composable
fun Search(context: Context) {

    // API call
    val spellList = remember { mutableListOf<Spell>() }
    var spellDetailList = remember { mutableListOf<SpellDetail>() }
    val spellProvider = SpellProvider(context)
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    val spellListCacheManager = CacheManager(context)

    var loaded by remember { mutableStateOf(false) }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> {
                    if (!loaded) {
                        if (!wasScreenVisitedBefore(context)) {
                            //get data from network
                            setScreenAsVisited(context, true)
                            spellProvider.loadSpellList(callback = { spells ->
                                spellList.clear()
                                spellList.addAll(spells)
                                // Load the data from the network
                                Log.d("cache", "load data from network")
                                spellProvider.loadAllSpellDetails {
                                    spellDetailList.clear()
                                    spellDetailList.addAll(it)
                                    loaded = true
                                }
                            }
                            ) { _ ->
                                // load data from cache if no network
                                val spellDetails =
                                    spellListCacheManager.loadSpellListFromCache()
                                // case: no internet without cache
                                if (spellDetails == null) {
                                    Toast.makeText(
                                        context,
                                        "No cache available try loading again with an internet connection",
                                        Toast.LENGTH_LONG
                                    ).show()
                                } else {
                                    //case: no internet with cache
                                    spellDetailList = spellDetails.toMutableList()
                                    loaded = true
                                }
                            }
                        } else {
                            //load data from cache
                            spellListCacheManager.loadSpellListFromCache()?.toMutableStateList()
                                ?.let {
                                    spellDetailList.clear()
                                    spellDetailList.addAll(it)
                                    Log.d("Cache", "loaded Spells from cache")
                                    loaded = true
                                } ?: {
                                Log.d("Cache", "Error: loading cache")
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

//--------------------------Mauer---------------------

    if (loaded) {
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
            if (loaded) {
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