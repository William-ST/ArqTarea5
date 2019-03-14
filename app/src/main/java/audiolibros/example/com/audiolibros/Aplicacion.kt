package audiolibros.example.com.audiolibros

import android.app.Application
import android.graphics.Bitmap
import android.util.LruCache

import com.android.volley.RequestQueue
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.Volley

import audiolibros.example.com.audiolibros.ejemploLibros

/**
 * Created by William_ST on 03/02/19.
 */

class Aplicacion : Application() {

    val listaLibros = ejemploLibros()
    lateinit var adaptador: AdaptadorLibrosFiltro
    lateinit var colaPeticiones : RequestQueue

    override fun onCreate() {
        super.onCreate()
        adaptador = AdaptadorLibrosFiltro(this, listaLibros)
        colaPeticiones = Volley.newRequestQueue(this)
    }

}