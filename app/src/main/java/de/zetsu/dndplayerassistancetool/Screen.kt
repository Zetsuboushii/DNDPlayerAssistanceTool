package de.zetsu.dndplayerassistancetool

sealed class Screen(val route: String, val title: String) {
    object Home : Screen("home", "Home")
    object Search : Screen("search", "Spell Search")
    object SpellBook : Screen("spellbook", "Spell Book")
}
