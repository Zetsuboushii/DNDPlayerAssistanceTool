package de.zetsu.dndplayerassistancetool.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
fun Search() {
    val items = listOf<String>("Divine Headache", "Smite", "Explosive Diarrhea")

    LazyColumn {
        for (i in 1 until 10) {
            stickyHeader {
                Text(text = "SL $i")
            }

            items(items) {
                Text(text = it)
            }
        }
    }

}