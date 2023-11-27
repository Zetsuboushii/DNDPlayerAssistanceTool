package de.zetsu.dndplayerassistancetool.screens

import android.content.Context
import android.util.Log
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
import de.zetsu.dndplayerassistancetool.R
import de.zetsu.dndplayerassistancetool.SpellProvider
import de.zetsu.dndplayerassistancetool.dataclasses.SpellDetail
import de.zetsu.dndplayerassistancetool.dataclasses.SpellListItem
import kotlinx.coroutines.launch

@Composable
fun Search(context: Context) {

    // API call
    val spellListItemList = remember { mutableListOf<SpellListItem>() }
    val spellDetailList = remember { mutableListOf<SpellDetail>() }
    val spellProvider = SpellProvider(context)
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    val spellListCacheManager = CacheManager(context)

    var toBeLoading by remember { mutableStateOf(true) }

    if (toBeLoading) {
        // only make api call when screen is created
        // TODO: use different method to make API-Call only on create, because DisposableEffect is to heavy
        //       if on delete isn't used
        // TODO: delete catch if internet is on and on first visit of SpellScreen
        DisposableEffect(lifecycleOwner) {
            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_CREATE -> {
                        //Todo: Dont do this if no internet
                        spellProvider.loadSpellList { spells ->
                            //Log.d("SpellsLog", spells.toString())
                            spellListItemList.clear()
                            spellListItemList.addAll(spells)
                            val cachedSpellDetailList =
                                spellListCacheManager.loadSpellListFromCache()

                            if (cachedSpellDetailList != null) {
                                // Use the cached data
                                Log.d("cache", "used cached data")
                                spellDetailList.addAll(cachedSpellDetailList)
                                // for (spellDetail in cachedSpellDetailList) {
                                //     Log.d("CachedSpellDetail", spellDetail.toString())
                                // }
                                toBeLoading = false
                            } else {
                                // Load the data from the network
                                Log.d("cache", "load data form network")
                                for (spell in spellListItemList) {
                                    spellProvider.loadSpellDetails(spell.index) { spellDetail ->
                                        spellDetailList.add(spellDetail)

                                        // Check if all data is loaded
                                        if (spellProvider.flagAPI) {
                                            toBeLoading = false
                                            // Save the spellDetailList to cache
                                            Log.d("cache", "save spellDetailList to cache")
                                            spellListCacheManager.saveSpellListToCache(
                                                spellDetailList
                                            )
                                        }
                                    }
                                }
                            }
                            /*for (spell in spellListItemList) {
                                spellProvider.loadSpellDetails(spell.index) { spellDetail ->
                                    //Log.d("SpellDetail: ${spell.name}", spellDetail.toString())
                                    spellDetailList.add(spellDetail)
                                    if (spellProvider.flagAPI) toBeLoading = false
                                }
                            } */
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


    if (!toBeLoading) {
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
                                Text(text = it.name, fontWeight = FontWeight.Bold)
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
                                        Row { Text(text = "LEVEL", fontWeight = FontWeight.Bold) }
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
