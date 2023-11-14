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
import de.zetsu.dndplayerassistancetool.dataclasses.Spell
import kotlinx.coroutines.launch

@Composable
fun Search(context: Context) {
    // API call
    val spellList = remember { mutableListOf<Spell>() }
    val spellProvider = SpellProvider(context)
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> {
                    spellProvider.loadSpellList { spells ->
                        Log.d("SpellsLog", spells.toString())
                        println(spells[2].name)
                        spellList.clear()
                        spellList.addAll(spells)
                    }
                    println("on create")
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

    println(spellList.toString())
    // TODO: Check why callback and println is called multiple times only if callback is done


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

        items(spellList) {
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
