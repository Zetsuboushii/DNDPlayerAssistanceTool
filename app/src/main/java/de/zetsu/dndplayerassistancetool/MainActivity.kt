package de.zetsu.dndplayerassistancetool

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import de.zetsu.dndplayerassistancetool.Screen.SpellBook
import de.zetsu.dndplayerassistancetool.ui.screens.Home
import de.zetsu.dndplayerassistancetool.ui.screens.Search
import de.zetsu.dndplayerassistancetool.ui.screens.SpellBook
import de.zetsu.dndplayerassistancetool.ui.screens.setScreenAsVisited
import de.zetsu.dndplayerassistancetool.ui.theme.DndplayerassistancetoolTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // write the backup spell list into file system
        val cacheManager = CacheManager(this)
        cacheManager.checkForWritingBackUpIntoCache()

        super.onCreate(savedInstanceState)
        setScreenAsVisited(applicationContext, false)
        setContent {
            DndplayerassistancetoolTheme {

                val items = listOf(
                    Screen.Home,
                    Screen.Search,
                    SpellBook
                )

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    Scaffold(
                        bottomBar = {
                            NavigationBar {
                                val navBackStackEntry by navController
                                    .currentBackStackEntryAsState()
                                val currentDestination = navBackStackEntry?.destination
                                items.forEach { screen ->
                                    NavigationBarItem(
                                        selected =
                                        currentDestination?.hierarchy?.any {
                                            it.route == screen.route
                                        } == true,
                                        onClick = {
                                            navController.navigate(screen.route) {
                                                popUpTo(
                                                    navController.graph.findStartDestination().id
                                                ) {
                                                    saveState = true
                                                }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        },
                                        icon = {
                                            Icon(
                                                painter = painterResource(
                                                    when (screen.route) {
                                                        "home" -> R.drawable.ic_home_fill
                                                        "search" -> R.drawable.ic_search2
                                                        "spellbook" -> R.drawable.ic_book_fill
                                                        else -> R.drawable.ic_taunt_fill
                                                    }
                                                ),
                                                contentDescription = null
                                            )
                                        },
                                        label = { Text(screen.title) }
                                    )
                                }
                            }
                        }
                    ) { innerPadding ->
                        NavHost(
                            navController,
                            startDestination = Screen.Home.route,
                            Modifier
                                .padding(innerPadding)
                        ) {
                            composable(Screen.Home.route) { Home() }
                            composable(Screen.Search.route) { Search(this@MainActivity) }
                            composable(SpellBook.route) { SpellBook(this@MainActivity) }
                        }
                    }
                }
            }
        }
    }
}
