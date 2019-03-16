package audiolibros.example.com.audiolibros

import android.app.AlertDialog
import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.NavigationView
import android.support.design.widget.TabLayout
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast

import audiolibros.example.com.audiolibros.fragments.DetalleFragment
import audiolibros.example.com.audiolibros.fragments.SelectorFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var adaptador: AdaptadorLibrosFiltro
    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adaptador = (applicationContext as Aplicacion).adaptador

        if (findViewById<View>(R.id.contenedor_pequeno) != null && fragmentManager.findFragmentById(R.id.contenedor_pequeno) == null) {
            val primerFragment = SelectorFragment()
            fragmentManager.beginTransaction().add(R.id.contenedor_pequeno, primerFragment).commit()
        }

        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        // Navigation Drawer
        toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        toggle.toolbarNavigationClickListener = View.OnClickListener { onBackPressed() }

        val navigationView = findViewById<View>(
                R.id.nav_view) as NavigationView
        navigationView.setNavigationItemSelectedListener(this)

        val fab = findViewById<View>(R.id.fab) as FloatingActionButton
        fab.setOnClickListener { irUltimoVisitado() }

        //tabs = findViewById<View>(R.id.tabs) as TabLayout
        tabs.addTab(tabs.newTab().setText("Todos"))
        tabs.addTab(tabs.newTab().setText("Nuevos"))
        tabs.addTab(tabs.newTab().setText("Leidos"))
        tabs.tabMode = TabLayout.MODE_SCROLLABLE
        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 //Todos
                    -> {
                        adaptador.setNovedad(false)
                        adaptador.setLeido(false)
                    }
                    1 //Nuevos
                    -> {
                        adaptador.setNovedad(true)
                        adaptador.setLeido(false)
                    }
                    2 //Leidos
                    -> {
                        adaptador.setNovedad(false)
                        adaptador.setLeido(true)
                    }
                }
                adaptador.notifyDataSetChanged()
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        when (id) {
            R.id.nav_todos -> {
                adaptador.setGenero("")
                adaptador.notifyDataSetChanged()
            }
            R.id.nav_epico -> {
                adaptador.setGenero(G_EPICO)
                adaptador.notifyDataSetChanged()
            }
            R.id.nav_XIX -> {
                adaptador.setGenero(G_S_XIX)
                adaptador.notifyDataSetChanged()
            }
            R.id.nav_suspense -> {
                adaptador.setGenero(G_SUSPENSE)
                adaptador.notifyDataSetChanged()
            }
            R.id.nav_preferencias -> {
                val i = Intent(this, PreferenciasActivity::class.java)
                startActivity(i)
            }
        }

        drawer!!.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.menu_preferencias -> {
                val i = Intent(this, PreferenciasActivity::class.java)
                startActivity(i)
                return true
            }
            R.id.menu_acerca -> {
                val builder = AlertDialog.Builder(this)
                builder.setMessage("Mensaje de Acerca De")
                builder.setPositiveButton(android.R.string.ok, null)
                builder.create().show()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    fun irUltimoVisitado() {
        val pref = getSharedPreferences("com.example.audiolibros_internal", Context.MODE_PRIVATE)
        val id = pref.getInt("ultimo", -1)
        if (id >= 0) {
            mostrarDetalle(id)
        } else {
            Toast.makeText(this, "Sin última vista", Toast.LENGTH_LONG).show()
        }
    }

    fun mostrarDetalle(id: Int) {

        val fragmentDetail : DetalleFragment? = fragmentManager.findFragmentById(R.id.detalle_fragment) as? DetalleFragment

        if (fragmentDetail == null) {
            val fragment = DetalleFragment()
            val args = Bundle()
            args.putInt(DetalleFragment.ARG_ID_LIBRO, id)
            fragment.arguments = args
            val transaccion = fragmentManager.beginTransaction()
            transaccion.replace(R.id.contenedor_pequeno, fragment)
            transaccion.addToBackStack(null)
            transaccion.commit()
        } else {
            fragmentDetail.ponInfoLibro(id)
        }

        val pref = getSharedPreferences("com.example.audiolibros_internal", Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putInt("ultimo", id)
        editor.apply()
    }

    fun mostrarElementos(mostrar: Boolean) {
        appBarLayout.setExpanded(mostrar)
        toggle.isDrawerIndicatorEnabled = mostrar
        if (mostrar) {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            tabs.visibility = View.VISIBLE
        } else {
            tabs.visibility = View.GONE
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        }
    }
}
