package audiolibros.example.com.audiolibros

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import java.util.ArrayList

/**
 * Created by William_ST on 08/02/19.
 */

class AdaptadorLibrosFiltro(contexto: Context, override var listaLibros : ArrayList<Libro>) : AdaptadorLibros(contexto, listaLibros) {
    // TODO : LIST
    private var listaSinFiltro : List<Libro> = ArrayList()
    private var indiceFiltro: MutableList<Int>? = null
    private var busqueda = ""
    private var genero = ""
    private var novedad = false
    private var leido = false

    init {
        listaSinFiltro = listaLibros;
        recalculaFiltro()
    }

    fun setBusqueda(busqueda: String) {
        this.busqueda = busqueda.toLowerCase()
        recalculaFiltro()
    }

    fun setGenero(genero: String) {
        this.genero = genero
        recalculaFiltro()
    }

    fun setNovedad(novedad: Boolean) {
        this.novedad = novedad
        recalculaFiltro()
    }

    fun setLeido(leido: Boolean) {
        this.leido = leido
        recalculaFiltro()
    }

    fun recalculaFiltro() {
        listaLibros = ArrayList()
        indiceFiltro = ArrayList()
        for (i in listaSinFiltro.indices) {
            val libro = listaSinFiltro[i]
            if ((libro.titulo.toLowerCase().contains(busqueda) || libro.autor.toLowerCase().contains(busqueda)) && libro.genero.startsWith(genero)
                    && (!novedad || novedad && libro.novedad)
                    && (!leido || leido && libro.leido)) {
                listaLibros.add(libro)
                indiceFiltro!!.add(i)
            }
        }
    }

    fun getItem(posicion: Int): Libro {
        return listaSinFiltro[indiceFiltro!![posicion]]
    }

    override fun getItemId(posicion: Int): Long {
        return indiceFiltro!![posicion].toLong()
    }

    fun borrar(posicion: Int) {
        listaSinFiltro.removeAt(getItemId(posicion).toInt())
        recalculaFiltro()
    }

    fun insertar(libro: Libro) {
        listaSinFiltro.add(0, libro)
        recalculaFiltro()
    }


}