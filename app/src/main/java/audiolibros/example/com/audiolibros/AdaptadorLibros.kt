package audiolibros.example.com.audiolibros

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.support.annotation.Nullable
import android.support.v4.content.ContextCompat
import android.support.v7.graphics.Palette
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.android.volley.VolleyError
import com.android.volley.toolbox.ImageLoader
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition

import audiolibros.example.com.audiolibros.util.GlideApp
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target


/**
 * Created by William_ST on 03/02/19.
 */

open class AdaptadorLibros(private val contexto: Context,
                           protected var listaLibros: List<Libro> //Lista de libros a visualizar
) : RecyclerView.Adapter<AdaptadorLibros.ViewHolder>() {

    private val inflador: LayoutInflater //Crea Layouts a partir del XML
    private var onClickListener: View.OnClickListener? = null
    private var onLongClickListener: View.OnLongClickListener? = null

    init {
        inflador = contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    //Creamos nuestro ViewHolder, con los tipos de elementos a modificar
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var portada: ImageView
        var titulo: TextView

        init {
            portada = itemView.findViewById(R.id.portada) as ImageView
            titulo = itemView.findViewById(R.id.titulo) as TextView
        }
    }

    // Creamos el ViewHolder con las vista de un elemento sin personalizar
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder { // Inflamos la vista desde el xml
        val v = inflador.inflate(R.layout.elemento_selector, null)
        v.setOnClickListener(onClickListener)
        v.setOnLongClickListener(onLongClickListener)
        return ViewHolder(v)
    }

    // Usando como base el ViewHolder y lo personalizamos
    override fun onBindViewHolder(holder: ViewHolder, posicion: Int) {
        val libro = listaLibros[posicion]
        holder.itemView.setBackgroundColor(ContextCompat.getColor(contexto, android.R.color.white))
        holder.titulo.setBackgroundColor(ContextCompat.getColor(contexto, android.R.color.white))
        holder.portada.setImageResource(R.drawable.books)

        GlideApp.with(contexto).asBitmap().load(libro.urlImagen)
                .listener(object : RequestListener<Bitmap> {
            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>?, isFirstResource: Boolean): Boolean {
                holder.portada.setImageResource(R.drawable.books)
                holder.portada.invalidate()
                return true;
            }

            override fun onResourceReady(resource: Bitmap?, model: Any?, target: Target<Bitmap>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                resource?.let {
                    holder.portada.setImageBitmap(resource)
                    if (libro.colorVibrante != -1 && libro.colorApagado != -1) {
                        holder.itemView.setBackgroundColor(libro.colorApagado)
                        holder.titulo.setBackgroundColor(libro.colorVibrante)
                        holder.portada.invalidate()
                    } else {
                        Palette.from(resource).generate(object : Palette.PaletteAsyncListener {
                            override fun onGenerated(palette: Palette) {
                                libro.colorVibrante = palette.getLightVibrantColor(0);
                                libro.colorApagado = palette.getLightMutedColor(0);

                                holder.itemView.setBackgroundColor(libro.colorApagado);
                                holder.titulo.setBackgroundColor(libro.colorVibrante);
                                holder.portada.invalidate();
                            }
                        })
                    }
                }
                return true;
            }
        }).into(holder.portada)//.onLoadFailed(ContextCompat.getDrawable(contexto, R.drawable.books))

        holder.titulo.text = libro.titulo
        holder.itemView.scaleX = 1f
        holder.itemView.scaleY = 1f
    }

    // Indicamos el número de elementos de la lista
    override fun getItemCount(): Int {
        return listaLibros.size
    }

    fun setOnItemClickListener(onClickListener: View.OnClickListener) {
        this.onClickListener = onClickListener
    }

    fun setOnItemLongClickListener(onLongClickListener: View.OnLongClickListener) {
        this.onLongClickListener = onLongClickListener
    }

}