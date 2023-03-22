package fr.uge.pokedex.service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import fr.uge.pokedex.R

class PokemonMusicService : Service(), MediaPlayer.OnPreparedListener {
    private lateinit var mediaPlayer: MediaPlayer

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer.create(this, R.raw.music_pokemon_lobby)
        mediaPlayer.setOnPreparedListener(this)
        mediaPlayer.prepareAsync()
    }

    override fun onPrepared(mp: MediaPlayer?) {
        mediaPlayer.start()
    }
}