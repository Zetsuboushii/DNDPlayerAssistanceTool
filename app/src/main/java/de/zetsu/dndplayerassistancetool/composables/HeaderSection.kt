package de.zetsu.dndplayerassistancetool.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.zetsu.dndplayerassistancetool.R




@Composable
fun HeaderSection() {
    val almendraFontFamily = FontFamily(
        Font(R.font.almendra_sc, FontWeight.Normal)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val painter0: Painter = painterResource(id = R.drawable.spell_book_1_)
        Image(
            painter = painter0,
            contentDescription = null,
            modifier = Modifier
                .size(85.dp)
                .padding(10.dp)
        )

        Text(
            text = "Welcome to \n  Your Spell Book !",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = almendraFontFamily,
            modifier = Modifier
                .padding(10.dp)

        )
    }


}



