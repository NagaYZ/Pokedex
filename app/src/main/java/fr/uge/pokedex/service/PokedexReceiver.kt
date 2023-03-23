package fr.uge.pokedex.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast


class PokedexReceiver : BroadcastReceiver() {

    companion object{
        fun newIntent(context: Context, action: String, message: String) {
            val intent = Intent(action)
            intent.putExtra("message", message)
            context.sendBroadcast(intent)
        }
    }
    override fun onReceive(context: Context?, intent: Intent?) {
            val message: String? = intent?.getStringExtra("message")
            if (message != null) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
    }
}
