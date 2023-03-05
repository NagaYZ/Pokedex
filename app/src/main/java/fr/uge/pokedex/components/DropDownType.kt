package fr.uge.pokedex.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import fr.uge.pokedex.data.Pokemon
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


    IconButton(onClick = {
        state = true
    }){
        Row() {

            Text(text = if (type != Type.NONE) type.toString() else name)
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Open Filter"
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

