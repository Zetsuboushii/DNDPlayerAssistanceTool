package de.zetsu.dndplayerassistancetool

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import de.zetsu.dndplayerassistancetool.ui.theme.DndplayerassistancetoolTheme

class SearchActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DndplayerassistancetoolTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    var selectedItem by remember { mutableStateOf(0) }
                    val items = listOf("Home", "All Spells", "My Spell Book")

                    val homeIntent = Intent(this, MainActivity::class.java)
                    val searchIntent = Intent(this, SearchActivity::class.java)
                    val spellBookIntent = Intent(this, SpellBookActivity::class.java)

                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                    ) {
                        Text(text = "Search")
                        Spacer(
                            modifier = Modifier
                                .weight(1f)
                        )
                        Row {
                            NavigationBar {
                                items.forEachIndexed { index, item ->
                                    NavigationBarItem(
                                        selected = selectedItem == index,
                                        onClick = {
                                            selectedItem = index
                                            when (index) {
                                                0 -> startActivity(homeIntent)
                                                1 -> startActivity(searchIntent)
                                                2 -> startActivity(spellBookIntent)
                                            }
                                        },
                                        label = { Text(item) },
                                        icon = {
                                            Icon(
                                                when (index) {
                                                    0 -> Icons.Filled.Home
                                                    1 -> Icons.Filled.List
                                                    2 -> Icons.Filled.Create
                                                    else -> {
                                                        Icons.Filled.Build
                                                    }
                                                },
                                                contentDescription = item
                                            )
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
