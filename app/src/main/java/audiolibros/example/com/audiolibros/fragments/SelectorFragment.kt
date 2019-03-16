package audiolibros.example.com.audiolibros.fragments

import android.animation.Animator
import android.animation.AnimatorInflater
import android.app.AlertDialog
import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.view.MenuItemCompat
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup

import audiolibros.example.com.audiolibros.AdaptadorLibrosFiltro
import audiolibros.example.com.audiolibros.Aplicacion
import audiolibros.example.com.audiolibros.MainActivity
import audiolibros.example.com.audiolibros.R
import kotlinx.android.synthetic.main.fragment_selector.view.*

/**
 * Created by William_ST on 05/02/19.
 */

class SelectorFragment : Fragment(), Animator.AnimatorListener {

    lateinit var adaptador: AdaptadorLibrosFiltro

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adaptador = (activity.application as Aplicacion).adaptador
    }

    override fun onResume() {
        (activity as MainActivity).mostrarElementos(true)
        super.onResume()
    }

    override fun onCreateView(inflador: LayoutInflater, contenedor: ViewGroup?, savedInstanceState: Bundle?): View? {
        val vista = inflador.inflate(R.layout.fragment_selector, contenedor, false)
        vista.recycler_view.layoutManager = GridLayoutManager(activity, 2)
        vista.recycler_view.adapter = adaptador
        val animator = DefaultItemAnimator()
        animator.addDuration = 2000
        animator.moveDuration = 2000
        vista.recycler_view.itemAnimator = animator

        setHasOptionsMenu(true)

        adaptador.setOnItemClickListener(View.OnClickListener { v ->
            (activity as MainActivity).mostrarDetalle(
                    // recyclerView.getChildAdapterPosition(v));
                    adaptador.getItemId(vista.recycler_view.getChildAdapterPosition(v)).toInt())
        })

        adaptador.setOnItemLongClickListener(View.OnLongClickListener { v ->
            val id = vista.recycler_view.getChildAdapterPosition(v)
            val menu = AlertDialog.Builder(activity)
            val opciones = arrayOf<CharSequence>("Compartir", "Borrar ", "Insertar")
            menu.setItems(opciones) { _, opcion ->
                when (opcion) {
                    0 //Compartir
                    -> {
                        val anim = AnimatorInflater.loadAnimator(activity, R.animator.share)
                        anim.addListener(object : Animator.AnimatorListener {
                            override fun onAnimationStart(animation: Animator) {

                            }

                            override fun onAnimationEnd(animation: Animator) {
                                val (titulo, _, _, urlAudio) = (activity.application as Aplicacion).listaLibros[id]
                                val i = Intent(Intent.ACTION_SEND)
                                i.type = "text/plain"
                                i.putExtra(Intent.EXTRA_SUBJECT, titulo)
                                i.putExtra(Intent.EXTRA_TEXT, urlAudio)
                                startActivity(Intent.createChooser(i, "Compartir"))
                            }

                            override fun onAnimationCancel(animation: Animator) {

                            }

                            override fun onAnimationRepeat(animation: Animator) {

                            }
                        })
                        anim.setTarget(v)
                        anim.start()
                    }
                    1 //Borrar
                    -> Snackbar.make(v, "¿Estás seguro?", Snackbar.LENGTH_LONG)
                            .setAction("SI") {
                                //app.getListaLibros().remove(id);

                                //adaptador.borrar(id);
                                //adaptador.notifyDataSetChanged();

                                //Animation anim = AnimationUtils.loadAnimation(actividad, R.anim.menguar);
                                //anim.setAnimationListener(SelectorFragment.this);
                                //v.startAnimation(anim);

                                val anim = AnimatorInflater.loadAnimator(activity, R.animator.menguar)
                                anim.addListener(this@SelectorFragment)
                                anim.setTarget(v)
                                anim.start()

                                adaptador.borrar(id)
                            }
                            .show()
                    2 //Insertar
                    -> {
                        //app.getListaLibros().add(app.getListaLibros().get(id));
                        val posicion = vista.recycler_view.getChildLayoutPosition(v)
                        adaptador.insertar(adaptador.getItem(posicion))
                        //adaptador.notifyDataSetChanged();
                        adaptador.notifyItemInserted(0)
                        Snackbar.make(v, "Libro insertado", Snackbar.LENGTH_INDEFINITE).setAction("OK") { }.show()
                    }
                }
            }
            menu.create().show()
            true
        })

        return vista
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_selector, menu)
        val searchItem = menu.findItem(R.id.menu_buscar)
        val searchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(
                object : SearchView.OnQueryTextListener {
                    override fun onQueryTextChange(query: String): Boolean {
                        adaptador.setBusqueda(query)
                        adaptador.notifyDataSetChanged()
                        return false
                    }

                    override fun onQueryTextSubmit(query: String): Boolean {
                        return false
                    }
                })
        MenuItemCompat.setOnActionExpandListener(searchItem, object : MenuItemCompat.OnActionExpandListener {
            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                adaptador.setBusqueda("")
                adaptador.notifyDataSetChanged()
                return true // Para permitir cierre
            }

            override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                return true // Para permitir expansión
            }
        })
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_ultimo -> {
                (activity as MainActivity).irUltimoVisitado()
                return true
            }
            R.id.menu_buscar -> return true
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onAnimationStart(animation: Animator) {

    }

    override fun onAnimationEnd(animation: Animator) {
        adaptador.notifyDataSetChanged()
    }

    override fun onAnimationCancel(animation: Animator) {

    }

    override fun onAnimationRepeat(animation: Animator) {

    }
}