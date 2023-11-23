package de.zetsu.dndplayerassistancetool.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun SpellAttributeBlock(attributeName: String, attributeValue: String) {
    Column(Modifier.padding(10.dp)) {
        Row { Text(text = attributeName.uppercase(), fontWeight = FontWeight.Bold) }
        Row { Text(text = attributeValue) }
    }
}