package de.zetsu.dndplayerassistancetool

sealed class Screen(val route: String, val title: String) {
    object Home : Screen("home", "Home")
    object Search : Screen("search", "SpellDetail Search")
    object SpellBook : Screen("spellbook", "SpellDetail Book")
}
