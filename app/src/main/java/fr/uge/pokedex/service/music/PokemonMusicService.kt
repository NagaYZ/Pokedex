package fr.uge.pokedex.service.music

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import fr.uge.pokedex.R

class PokemonMusicService : Service(), MediaPlayer.OnPreparedListener,
    MediaPlayer.OnCompletionListener {
    private lateinit var mediaPlayer: MediaPlayer
    private val binder: IBinder = LocalBinder()

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer.create(this, R.raw.music_pokemon_lobby)
        mediaPlayer.setOnPreparedListener(this)
        mediaPlayer.setOnCompletionListener(this)
    }

    override fun onPrepared(mp: MediaPlayer?) {
        mediaPlayer.start()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.stop()
        mediaPlayer.release()
    }

    fun getMediaPlayer(): MediaPlayer {
        return mediaPlayer
    }

    inner class LocalBinder : Binder() {
        fun getService(): PokemonMusicService = this@PokemonMusicService
    }

    override fun onCompletion(p0: MediaPlayer?) {
        mediaPlayer.start()
    }

}