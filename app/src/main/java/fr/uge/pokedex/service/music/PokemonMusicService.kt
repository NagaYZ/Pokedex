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
    private var state: Boolean = true

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        state = true
        mediaPlayer = MediaPlayer.create(this, R.raw.music_pokemon_lobby)
        mediaPlayer.setOnPreparedListener(this)
        mediaPlayer.setOnCompletionListener(this)
    }

    override fun onPrepared(mp: MediaPlayer?) {
        mediaPlayer.start()
        state = true
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        state = true
        return START_STICKY
    }

    override fun onDestroy() {
        state = false
        super.onDestroy()
        mediaPlayer.stop()
        mediaPlayer.release()

    }

    fun getState(): Boolean {
        return state;
    }

    fun setState() {
        state = !state;
    }

    fun getMediaPlayer(): MediaPlayer {
        return mediaPlayer
    }

    inner class LocalBinder : Binder() {
        fun getService(): PokemonMusicService = this@PokemonMusicService
    }

    override fun onCompletion(p0: MediaPlayer?) {
        mediaPlayer.start()
        state = true
    }

}