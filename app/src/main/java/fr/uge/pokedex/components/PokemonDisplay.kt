package fr.uge.pokedex.components

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.uge.pokedex.data.Pokemon
import fr.uge.pokedex.data.Type

@Composable
fun PokemonBoxDisplay(
    pokemon: Pokemon,
    context: Context,
    onClick: () ->  Unit = {},
    onClickFavorite: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .width(180.dp)
            .background(MaterialTheme.colors.background)
            .padding(8.dp),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        PokemonSprite(spriteResource = pokemon.getSprite(context))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier.padding(vertical = 4.dp)
        ) {
            PokemonBoxTitle(name = pokemon.name)
            Spacer(modifier = Modifier.width(3.dp))
            Text(
                text = "#${pokemon.id.toString().padStart(3, '0')}",
                color = Color.LightGray,
                fontStyle = FontStyle.Italic,
                textAlign = TextAlign.Center
            )
        }
        PokemonTypeDisplay(type = pokemon.type)
    }
    FavoriteButton(onClick = onClickFavorite)
}

@Composable
private fun PokemonSprite(spriteResource: Int) {
    Image(
        painter = painterResource(id = spriteResource),
        contentDescription = "Pokemon sprite",
        modifier = Modifier
            .background(Color.White)
            .aspectRatio(1f)
            .fillMaxWidth()
            .border(width = 1.dp, color = Color.LightGray)
    )
}

@Composable
fun PokemonListDisplay(
    pokemon: Pokemon,
    context: Context,
    onClick: () ->  Unit = {},
    onClickFavorite: () -> Unit = {}
) {
    Row(
        Modifier
            .clickable(onClick = onClick)
            .fillMaxWidth()
            .background(MaterialTheme.colors.background)
            .height(70.dp)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PokemonIcon(pokemon.getIcon(context))
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                PokemonBoxTitle(name = pokemon.name)
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = "#${pokemon.id.toString().padStart(3, '0')}",
                    color = Color.LightGray,
                    fontStyle = FontStyle.Italic,
                    textAlign = TextAlign.Center
                )
            }
            PokemonTypeDisplay(type = pokemon.type)
        }
        Spacer(modifier = Modifier.weight(1.0f))
        FavoriteButton(onClick = onClickFavorite)
    }
}

@Preview
@Composable
private fun FavoriteButton(filled : Boolean = false, onClick: () -> Unit = {}) {
    IconButton(
        onClick = onClick,
    ) {
        Icon(
            if (filled) Icons.Filled.Favorite else Icons.Default.FavoriteBorder,
            contentDescription = "Favorite",
            modifier = Modifier
                .size(ButtonDefaults.IconSize)
                .scale(1.25f),
            tint = Color.LightGray
        )
    }
}

@Composable
private fun PokemonIcon(iconResource: Int) {
    Image(
        painter = painterResource(id = iconResource),
        contentDescription = "Pokemon icon",
        modifier = Modifier
            .background(Color.White)
            .aspectRatio(1f)
            .fillMaxHeight()
            .border(width = 1.dp, color = Color.LightGray)
    )
}

@Composable
private fun PokemonBoxTitle(name: String) {
    Text(
        text = name,
        fontSize = 18.sp,
        modifier = Modifier.padding(horizontal = 3.dp),
        style = MaterialTheme.typography.h3.copy(
            shadow = Shadow(
                color = Color.LightGray,
                offset = Offset(x = 0f, y = 3f),
                blurRadius = 0.1f
            )
        )
    )
}

@Preview
@Composable
private fun PokemonTypeDisplay(type: Pair<Type, Type> = Pair(Type.ELECTRIC, Type.DRAGON)) {
    Row() {
        TypeBox(type.first)
        Spacer(modifier = Modifier.width(3.dp))
        TypeBox(type.second)
    }
}

@Preview
@Composable
private fun TypeBox(type: Type = Type.NORMAL) {
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