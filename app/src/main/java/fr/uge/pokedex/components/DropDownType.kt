package fr.uge.pokedex.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import fr.uge.pokedex.data.Type

@Composable
fun DropDownType(name: String, typeNum: (String) -> Unit) {

    var types by remember {
        mutableStateOf(Type.values().toMutableList())
    }

    var type by remember {
        mutableStateOf(Type.NONE)
    }

    var state by remember {
        mutableStateOf(false)
    }


    OutlinedButton(
        onClick = {
            state = true
        },
        border = BorderStroke(1.dp, Color.Gray),
        colors = ButtonDefaults.buttonColors(Color.Transparent)

    ) {
        Row(horizontalArrangement = Arrangement.Center) {

            Text(text = if (type != Type.NONE) type.toString() else name, color = Color.Gray)
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Open Filter",
                tint = Color.Gray
            )
        }
        DropdownMenu(
            expanded = state,
            onDismissRequest = {
                state = false
            }
        ) {
            types.forEach { nameType ->
                DropdownMenuItem(
                    onClick = {
                        type = nameType
                        typeNum(type.toString())
                        state = false
                    },
                ) {
                    Text(text = nameType.toString())
                }
            }
        }
    }
}

