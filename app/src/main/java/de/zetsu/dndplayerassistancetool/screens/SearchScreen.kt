package de.zetsu.dndplayerassistancetool.screens

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import de.zetsu.dndplayerassistancetool.CacheManager
import de.zetsu.dndplayerassistancetool.Constants
import de.zetsu.dndplayerassistancetool.R
import de.zetsu.dndplayerassistancetool.SpellProvider
import de.zetsu.dndplayerassistancetool.dataclasses.Spell
import de.zetsu.dndplayerassistancetool.dataclasses.SpellDetail
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

    if (loaded) {
        // spell cards + search bar
        val listState = rememberLazyListState()
        val coroutineScope = rememberCoroutineScope()

        LazyColumn(state = listState) {
            item {
                // Search Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    var text by remember { mutableStateOf(TextFieldValue("")) }
                    OutlinedTextField(
                        value = text,
                        onValueChange = { text = it },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.ic_search),
                                contentDescription = null
                            )
                        },
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }

            // spell cards
            items(spellDetailList) {
                Box(modifier = Modifier.background(Color.White)) {
                    var expanded by remember { mutableStateOf(false) }
                    ElevatedCard(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                            .clickable(
                                onClick = { expanded = !expanded }
                            )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(10.dp)
                        ) {
                            Icon(
                                painter = painterResource(
                                    when (it.school.index) {
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
                                text = it.level.toString(),
                                modifier = Modifier.padding(5.dp)
                            )
                            Spacer(modifier = Modifier.size(15.dp))
                            Column(horizontalAlignment = Alignment.Start) {
                                Text(
                                    text = it.name,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(text = it.school.name)
                            }
                        }
                        if (expanded) {
                            var description = ""
                            for (i in 0 until it.desc.size) {
                                description += it.desc[i]
                                if (i < it.desc.size - 1) {
                                    description += "\n"
                                }
                            }
                            Row(modifier = Modifier.padding(10.dp)) {
                                Row(modifier = Modifier.fillMaxWidth()) {
                                    Column {
                                        Row {
                                            Text(
                                                text = "LEVEL",
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                        Row { Text(text = it.level.toString()) }
                                    }
                                }
                                Row { Text(text = description) }
                            }
                        }
                    }
                }
            }
        }

        // back to top floating action button
        Box(modifier = Modifier.fillMaxSize()) {
            FloatingActionButton(
                onClick = {
                    coroutineScope.launch {
                        listState.animateScrollToItem(index = 0)
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrowup),
                    contentDescription = null
                )
            }
        }
    }
}


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