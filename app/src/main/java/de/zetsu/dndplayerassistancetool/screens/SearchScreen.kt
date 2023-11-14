package de.zetsu.dndplayerassistancetool.screens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import de.zetsu.dndplayerassistancetool.SpellProvider
import de.zetsu.dndplayerassistancetool.dataclasses.Spell

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
fun Search(context: Context) {

    var spellList = remember {mutableListOf<Spell>()}

    val spellProvider = SpellProvider(context)
    val items = listOf<String>("Divine Headache", "Smite", "Explosive Diarrhea")

    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner){
        val observer = LifecycleEventObserver { _, event ->
            when(event) {
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
    // TODO: Check why callback and println is called multible times only if callback is done

    LazyColumn {
        for (spell in spellList) {
            stickyHeader {
                Text(text = spell.name)
            }

            items(items) {

            }
        }

}