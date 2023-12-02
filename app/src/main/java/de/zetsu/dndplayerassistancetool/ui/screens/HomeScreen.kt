package de.zetsu.dndplayerassistancetool.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.zetsu.dndplayerassistancetool.composables.*

@SuppressLint("SimpleDateFormat")
@Composable
fun Home() {
    Column {
        TitleBlock()

        Spacer(modifier = Modifier.size(100.dp))

        HomeSearchBar()

        Spacer(modifier = Modifier.size(20.dp))

        NextSessionCard()
    }
}
