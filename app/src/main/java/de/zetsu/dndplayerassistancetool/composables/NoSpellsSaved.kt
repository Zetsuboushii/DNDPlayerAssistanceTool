package de.zetsu.dndplayerassistancetool.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NoSpellsSaved() {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        item {
            Card(

                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(25.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Warning,
                        contentDescription = null,
                        modifier = Modifier
                            .size(30.dp)
                            .padding(4.dp)
                            .offset(0.dp, 1.dp),
                        //tint = if (MaterialTheme.colorScheme.isDark) Color.White else Color.DarkGray
                    )

                    Spacer(modifier = Modifier.width(20.dp))
                    Text(
                        text = "You have not saved \nany spells yet...",
                        color = Color(0xFFDE281D),
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Normal,
                        fontSize = 20.sp,
                    )
                }
            }
        }

        item {
            var expanded by remember { mutableStateOf(false) }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(25.dp)
                    .clickable { expanded = !expanded },
                shape = MaterialTheme.shapes.medium
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Settings,
                        contentDescription = null,
                        modifier = Modifier
                            .size(30.dp)
                            .padding(4.dp)
                            .offset(0.dp, 1.dp),
                        //tint = if (MaterialTheme.colorScheme.isDark) Color.White else Color.DarkGray
                    )

                    Spacer(modifier = Modifier.width(20.dp))
                    Text(
                        text = "Don't know how to add a Spell? --> Tap Me",
                        color = Color(0xFFDE281D),
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Normal,
                        fontSize = 20.sp,
                    )
                }
                if (expanded) {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    fontSize = 18.sp,
                                    fontFamily = FontFamily.Serif,
                                    fontWeight = FontWeight.W400,
                                    //color = if (MaterialTheme.colorScheme.isDark) Color.White else Color.DarkGray
                                )
                            )

                            {
                                Divider(Modifier.padding(10.dp), thickness = 2.dp)
                                appendLine("1. For starters:\n ")
                                appendLine("I contain a description when I expand, like all our spells.\n")
                                appendLine("2. To save a spell:\n")
                                appendLine("Go to the Spell Search Button on the bottom.")
                                appendLine("With a long tap on a spell (hold 2 sec.), you save it.\n")
                                appendLine("3. To delete a spell:\n")
                                appendLine("With a long tap in the Spell Book, you delete the spell.")
                            }
                        },
                        modifier = Modifier.padding(14.dp)
                    )
                }
            }
        }
    }
}
