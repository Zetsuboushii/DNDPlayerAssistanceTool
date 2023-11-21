package de.zetsu.dndplayerassistancetool.screens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
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

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> {
                    //TODO: Only enter for() if SpellList is fully loaded
                    //TODO: Add Shadow load
                    spellProvider.loadSpellList { spells ->
                        Log.d("SpellsLog", spells.toString())
                        spellListItemList.clear()
                        spellListItemList.addAll(spells)
                        for (spell in spellListItemList) {
                            spellProvider.loadSpellDetails(spell.index) { spellDetail ->
                                Log.d("SpellDetail: ${spell.name}", spellDetail.toString())
                                spellDetailList.add(spellDetail)
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

        /*
        for (i in 1 until 10) {
            stickyHeader {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Purple80)
                ) {
                    Text(text = "SL $i")
                }
            }

         */

        // spell cards
        items(spellDetailList) {
            Box(modifier = Modifier.background(Color.White)) {
                ElevatedCard(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
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
                            Text(text = it.name)
                            Text(text = it.school.name)
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
