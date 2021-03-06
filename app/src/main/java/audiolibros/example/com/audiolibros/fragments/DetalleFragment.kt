package audiolibros.example.com.audiolibros.fragments

import android.app.Fragment
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController

import java.io.IOException

import audiolibros.example.com.audiolibros.Aplicacion
import audiolibros.example.com.audiolibros.MainActivity
import audiolibros.example.com.audiolibros.R
import audiolibros.example.com.audiolibros.util.GlideApp
import kotlinx.android.synthetic.main.fragment_detalle.view.*
import com.bumptech.glide.request.RequestOptions



/**
 * Created by William_ST on 05/02/19.
 */

class DetalleFragment : Fragment(), View.OnTouchListener, MediaPlayer.OnPreparedListener, MediaController.MediaPlayerControl {
    lateinit var mediaPlayer: MediaPlayer
    lateinit var mediaController: MediaController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mediaPlayer = MediaPlayer()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val vista = inflater.inflate(R.layout.fragment_detalle, container, false)
        val position = arguments?.let {
            arguments.getInt(ARG_ID_LIBRO,0)
        } ?: 0
        ponInfoLibro(position, vista)
        return vista
    }

    override fun onResume() {
        val detalleFragment = fragmentManager.findFragmentById(R.id.detalle_fragment) as? DetalleFragment
        if (detalleFragment == null) {
            (activity as MainActivity).mostrarElementos(false)
        }
        super.onResume()
    }

    private fun ponInfoLibro(id: Int, vista: View) {

        val (titulo, autor, urlImagen, urlAudio) = (activity.application as Aplicacion).listaLibros[id]
        vista.titulo.text = titulo
        vista.autor.text = autor

        val requestOptions = RequestOptions()
        //requestOptions.placeholder(R.drawable.ic_placeholder)
        requestOptions.error(R.drawable.books)

        GlideApp.with(this).setDefaultRequestOptions(requestOptions).load(urlImagen).fitCenter()
                .into(vista.portada)

        vista.setOnTouchListener(this)

        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
            mediaPlayer.release()
        }

        mediaPlayer.setOnPreparedListener(this)
        mediaController = MediaController(activity)
        val audio = Uri.parse(urlAudio)
        try {
            mediaPlayer.reset()
            mediaPlayer.setDataSource(activity, audio)
            mediaPlayer.prepareAsync()
        } catch (e: IOException) {
            Log.e("Audiolibros", "ERROR: No se puede reproducir $audio", e)
        }
    }

    fun ponInfoLibro(id: Int) {
        ponInfoLibro(id, view)
    }

    override fun onPrepared(mediaPlayer: MediaPlayer) {
        Log.d("Audiolibros", "Entramos en onPrepared de MediaPlayer")
        mediaPlayer.start()
        mediaController.setMediaPlayer(this)
        mediaController.setAnchorView(view.findViewById(R.id.fragment_detalle))
        mediaController.isEnabled = true
        mediaController.show()
    }

    override fun onTouch(vista: View, evento: MotionEvent): Boolean {
        mediaController.show()
        return false
    }

    override fun onStop() {
        mediaController.hide()
        try {
            mediaPlayer.stop()
            mediaPlayer.release()
        } catch (e: Exception) {
            Log.d("Audiolibros", "Error en mediaPlayer.stop()")
        }

        super.onStop()
    }

    override fun canPause(): Boolean {
        return true
    }

    override fun canSeekBackward(): Boolean {
        return true
    }

    override fun canSeekForward(): Boolean {
        return true
    }

    override fun getBufferPercentage(): Int {
        return 0
    }

    override fun getCurrentPosition(): Int {
        try {
            return mediaPlayer.currentPosition
        } catch (e: Exception) {
            return 0
        }

    }

    override fun getDuration(): Int {
        return mediaPlayer.duration
    }

    override fun isPlaying(): Boolean {
        return mediaPlayer.isPlaying
    }

    override fun pause() {
        mediaPlayer.pause()
    }

    override fun seekTo(pos: Int) {
        mediaPlayer.seekTo(pos)
    }

    override fun start() {
        mediaPlayer.start()
    }

    override fun getAudioSessionId(): Int {
        return 0
    }

    companion object {

        var ARG_ID_LIBRO = "id_libro"
    }
}
