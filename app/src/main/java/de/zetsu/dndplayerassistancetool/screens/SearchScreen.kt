package de.zetsu.dndplayerassistancetool.screens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
                        //for (spell in spellListItemList) {      //don't know if this really works but it is slow
                        //    spellProvider.loadSpellDetails(spell.index) { spellDetail ->
                        //        Log.d("SpellDetail: ${spell.name}", spellDetail.toString())
                        //        spellDetailList.add(spellDetail)
                        //    }
                        //}
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

    LazyColumn(
        state = listState
    ) {
        item {
            // Search Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                var text by remember {
                    mutableStateOf(TextFieldValue(""))
                }
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_search),
                            contentDescription = null
                        )
                    },
                    modifier = Modifier
                        .padding(8.dp)
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

        items(spellListItemList) {
            Box(
                modifier = Modifier
                    .background(Color.White)
            ) {
                ElevatedCard(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text(
                        text = it.name, modifier = Modifier.padding(5.dp)
                    )
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
