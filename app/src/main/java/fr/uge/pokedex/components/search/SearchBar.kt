package fr.uge.pokedex.components.search

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import fr.uge.pokedex.R
import fr.uge.pokedex.theme.Shapes


@Composable
fun SearchBar(applicationContext: Context, pokemonSearch: (String) -> Unit = {}) {
    var search by remember {
        mutableStateOf("")
    }

    var showListeningDialog by remember {
        mutableStateOf(false)
    }

    var showMicAccessDeniedDialog by remember {
        mutableStateOf(false)
    }

    val textToSpeech = remember(applicationContext) {SpeechRecognizer.createSpeechRecognizer(applicationContext)}
    textToSpeech.setRecognitionListener(object : RecognitionListener{
        override fun onReadyForSpeech(params: Bundle?) {
            showListeningDialog = true
        }

        override fun onBeginningOfSpeech() {
        }

        override fun onRmsChanged(rmsdB: Float) {
        }

        override fun onBufferReceived(buffer: ByteArray?) {
        }

        override fun onEndOfSpeech() {
            showListeningDialog = false
        }

        override fun onError(error: Int) {
            showListeningDialog = false
        }

        override fun onResults(results: Bundle?) {
            val resultsList = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            if (resultsList != null)
                search = resultsList.get(0)
        }

        override fun onPartialResults(partialResults: Bundle?) {
        }

        override fun onEvent(eventType: Int, params: Bundle?) {
        }
    })

    val permissionDialog = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if(isGranted)
            textToSpeech.startListening(Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH))
        else
            showMicAccessDeniedDialog = true
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.pok),
            contentDescription = "Search",
            modifier = Modifier
                .scale(1f)
                .height(30.dp)
                .padding(horizontal = 10.dp),
            tint = Color.Unspecified
        )

        OutlinedTextField(
            modifier = Modifier
                .padding(5.dp)
                .width(280.dp),
            value = search,
            onValueChange = { search = it },
            placeholder = { Text("Search by name") }
        )

        Icon(
            painter = painterResource(id = R.drawable.mic),
            contentDescription = "Voice search",
            modifier = Modifier
                .scale(2f)
                .padding(horizontal = 10.dp)
                .clickable(
                    onClick = {
                        if(ContextCompat.checkSelfPermission(applicationContext, android.Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED){
                            textToSpeech.startListening(Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH))
                        }else{
                            permissionDialog.launch(android.Manifest.permission.RECORD_AUDIO)
                        }
                    }
                ),
            tint = Color.White
        )

        pokemonSearch(search)
    }

    if(showListeningDialog){
        Dialog(onDismissRequest = { showListeningDialog = false }){
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colors.secondary, Shapes.medium)
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ){
                Text(text = "Say a pokemon's name", style = MaterialTheme.typography.h6)
                Spacer(modifier = Modifier.padding(10.dp))
                Icon(
                    painter = painterResource(id = R.drawable.mic),
                    contentDescription = "Voice search",
                    modifier = Modifier
                        .scale(2f)
                        .padding(horizontal = 10.dp),
                    tint = Color.White
                )
            }
        }
    }

    if(showMicAccessDeniedDialog){
        Dialog(onDismissRequest = { showMicAccessDeniedDialog = false }){
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colors.secondary, Shapes.medium)
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ){
                Icon(
                    Icons.Default.Info,
                    contentDescription = "Mic denied information",
                    modifier = Modifier
                        .scale(2f)
                        .padding(horizontal = 10.dp),
                    tint = Color.White
                )
                Spacer(modifier = Modifier.padding(10.dp))
                Text(text = "You disabled the access to the microphone, please go to app settings and enable it.", style = MaterialTheme.typography.h6)
            }
        }
    }


    pokemonSearch(search)
}

