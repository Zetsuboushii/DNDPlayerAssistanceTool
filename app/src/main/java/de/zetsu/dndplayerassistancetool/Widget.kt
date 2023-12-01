package de.zetsu.dndplayerassistancetool

import android.content.Context
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle

class Widget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            var nextSession by remember { mutableStateOf("Loading...") }

            LaunchedEffect(key1 = true) {
                fetchData { result ->
                    nextSession = result
                }
            }

            Box(
                modifier = GlanceModifier
                    .width(300.dp)
                    .background(Color.White)
            ) {
                Column(
                    modifier = GlanceModifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(
                        text = "Next D&D Session on",
                        style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                    )
                }
                Column(
                    modifier = GlanceModifier
                        .fillMaxWidth()
                        .padding(top = 60.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Row {
                        Image(
                            provider = ImageProvider(R.drawable.ic_event_fill),
                            contentDescription = null,
                            modifier = GlanceModifier.size(30.dp)
                        )
                        Text(
                            text = nextSession,
                            style = TextStyle(fontSize = 25.sp)
                        )
                    }
                }
            }
        }
    }
}
