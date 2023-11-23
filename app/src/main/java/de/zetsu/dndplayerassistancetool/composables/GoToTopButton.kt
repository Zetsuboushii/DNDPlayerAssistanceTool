package de.zetsu.dndplayerassistancetool.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import de.zetsu.dndplayerassistancetool.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun GoToTopButton(coroutineScope: CoroutineScope, lazyListState: LazyListState) {
    Box(modifier = Modifier.fillMaxSize()) {
        FloatingActionButton(
            onClick = {
                coroutineScope.launch {
                    lazyListState.animateScrollToItem(index = 0)
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