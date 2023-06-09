package fr.uge.pokedex.components.list

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.uge.pokedex.data.pokedex.pokemon.Type

@Preview
@Composable
fun TypeDisplay(type: Type = Type.NORMAL) {
    if (type == Type.NONE) {
        return
    }
    Text(
        text = type.name,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.button.copy(
            shadow = Shadow(
                color = Color.LightGray,
                offset = Offset(x = 0f, y = 3f),
                blurRadius = 0.1f
            )
        ),
        modifier = Modifier
            .width(80.dp)
            .border(
                width = 2.dp,
                color = Color.White,
                shape = RoundedCornerShape(20.dp),
            )
            .padding(2.dp)
            .border(
                width = 1.dp,
                color = Color.LightGray,
                shape = RoundedCornerShape(20.dp),
            )
            .background(
                color = typeToColor(type),
                shape = RoundedCornerShape(20.dp),
            )
            .padding(all = 4.dp),
        color = Color.White,
        fontSize = 10.sp,
        fontWeight = FontWeight.Bold
    )
}


private fun typeToColor(type: Type): Color {
    return when (type) {
        Type.NORMAL -> Color(0xFFBBBCAD)
        Type.FIGHTING -> Color(0xFFA1533E)
        Type.FLYING -> Color(0xFF79A1FF)
        Type.POISON -> Color(0xFFAB5EA4)
        Type.GROUND -> Color(0xFFEBC658)
        Type.ROCK -> Color(0xFFCEBC73)
        Type.BUG -> Color(0xFFC3D21F)
        Type.GHOST -> Color(0xFF7874D3)
        Type.STEEL -> Color(0xFFC4C2DA)
        Type.FIRE -> Color(0xFFFB5644)
        Type.WATER -> Color(0xFF55AEFE)
        Type.GRASS -> Color(0xFF8CD850)
        Type.ELECTRIC -> Color(0xFFF5D93B)
        Type.PSYCHIC -> Color(0xFFFB5FB2)
        Type.ICE -> Color(0xFF95F1FF)
        Type.DRAGON -> Color(0xFF8975FB)
        Type.DARK -> Color(0xFF886554)
        Type.FAIRY -> Color(0xFFF8ADFF)
        Type.UNKNOWN -> Color(0xFF1C9C88)
        Type.SHADOW -> Color(0xFF242423)
        Type.NONE -> Color(0xFFFFFFFF)
    }
}