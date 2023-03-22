package fr.uge.pokedex.bluetooth

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast


class PokedexReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
            val message: String? = intent?.getStringExtra("message")
            if (message != null) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
    }
}
