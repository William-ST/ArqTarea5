package audiolibros.example.com.audiolibros;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.List;

import audiolibros.example.com.audiolibros.util.GlideApp;

/**
 * Created by William_ST on 03/02/19.
 */

public class AdaptadorLibros extends RecyclerView.Adapter<AdaptadorLibros.ViewHolder> {

    private LayoutInflater inflador; //Crea Layouts a partir del XML
    protected List<Libro> listaLibros; //Lista de libros a visualizar
    private Context contexto;
    private View.OnClickListener onClickListener;
    private View.OnLongClickListener onLongClickListener;

    public AdaptadorLibros(Context contexto, List<Libro> listaLibros) {
        inflador = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.listaLibros = listaLibros;
        this.contexto = contexto;
    }

    //Creamos nuestro ViewHolder, con los tipos de elementos a modificar
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView portada;
        public TextView titulo;

        public ViewHolder(View itemView) {
            super(itemView);
            portada = (ImageView) itemView.findViewById(R.id.portada);
            titulo = (TextView) itemView.findViewById(R.id.titulo);
        }
    }

    // Creamos el ViewHolder con las vista de un elemento sin personalizar
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) { // Inflamos la vista desde el xml
        View v = inflador.inflate(R.layout.elemento_selector, null);
        v.setOnClickListener(onClickListener);
        v.setOnLongClickListener(onLongClickListener);
        return new ViewHolder(v);
    }

    // Usando como base el ViewHolder y lo personalizamos
    @Override
    public void onBindViewHolder(final ViewHolder holder, int posicion) {
        final Libro libro = listaLibros.get(posicion);

        GlideApp.with(contexto).asBitmap().load(libro.getUrlImagen()).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                holder.portada.setImageBitmap(bitmap);
                if (libro.getColorVibrante() != -1 && libro.getColorApagado() != -1) {
                    holder.itemView.setBackgroundColor(libro.getColorApagado());
                    holder.titulo.setBackgroundColor(libro.getColorVibrante());
                    holder.portada.invalidate();
                } else {
                    Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                        public void onGenerated(Palette palette) {
                            libro.setColorVibrante(palette.getLightVibrantColor(0));
                            libro.setColorApagado(palette.getLightMutedColor(0));

                            holder.itemView.setBackgroundColor(libro.getColorApagado());
                            holder.titulo.setBackgroundColor(libro.getColorVibrante());
                            holder.portada.invalidate();
                        }
                    });
                }
            }

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                super.onLoadFailed(errorDrawable);
                holder.portada.setImageResource(R.drawable.books);
            }
        });

        holder.titulo.setText(libro.getTitulo());
        holder.itemView.setScaleX(1);
        holder.itemView.setScaleY(1);
    }

    // Indicamos el número de elementos de la lista
    @Override
    public int getItemCount() {
        return listaLibros.size();
    }

    public void setOnItemClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setOnItemLongClickListener(View.OnLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }

}