package de.zetsu.dndplayerassistancetool.screens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import de.zetsu.dndplayerassistancetool.SpellProvider
import de.zetsu.dndplayerassistancetool.dataclasses.SpellDetail

@Composable
fun SpellBook(context: Context) {
    var loaded by remember { mutableStateOf(false) }

    val spellProvider = SpellProvider(context)
    var spellDetailList = remember { mutableListOf<SpellDetail>() }
    if (!loaded) {
        loaded = true
        spellProvider.loadSelectedSpells {
            spellDetailList.clear()
            spellDetailList.addAll(it)
        }
    }
    var moin = mutableListOf<String>()
    for (i in 0 until spellDetailList.size) {
        moin.add(spellDetailList[i].name)
        Column {
            Text(text = moin.toString())
        }
    }
    Log.d("SpellBook", spellDetailList.toString())
    //Text(text = "SpellDetail Book")
}