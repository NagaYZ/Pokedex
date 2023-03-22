package fr.uge.pokedex.bluetooth

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview


class PokedexReceiver : BroadcastReceiver() {

    fun PokedexReceiver() {}

    override fun onReceive(context: Context?, intent: Intent?) {

            val message: String? = intent?.getStringExtra("message")
            if (message != null) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                Log.d("TAG", message)
            }
    }
}
