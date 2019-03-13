package audiolibros.example.com.audiolibros

import java.util.ArrayList

/**
 * Created by William_ST on 03/02/19.
 */
const val G_TODOS = "Todos los géneros"
const val G_EPICO = "Poema épico"
const val G_S_XIX = "Literatura siglo XIX"
const val G_SUSPENSE = "Suspense"
val G_ARRAY = arrayOf(G_TODOS, G_EPICO, G_S_XIX, G_SUSPENSE)

fun ejemploLibros(): List<Libro> {
    val SERVIDOR = "http://mmoviles.upv.es/audiolibros/"
    val libros = ArrayList<Libro>()
    libros.add(Libro("Kappa", "Akutagawa",
            SERVIDOR + "kappa.jpg", SERVIDOR + "kappa.mp3",
            G_S_XIX, false, false))
    libros.add(Libro("Avecilla", "Alas Clarín, Leopoldo",
            SERVIDOR + "avecilla.jpg", SERVIDOR + "avecilla.mp3",
            G_S_XIX, true, false))
    libros.add(Libro("Divina Comedia", "Dante",
            SERVIDOR + "divinacomedia.jpg", SERVIDOR + "divina_comedia.mp3",
            G_EPICO, true, false))
    libros.add(Libro("Viejo Pancho, El", "Alonso y Trelles, José",
            SERVIDOR + "viejo_pancho.jpg", SERVIDOR + "viejo_pancho.mp3",
            G_S_XIX, true, true))
    libros.add(Libro("Canción de Rolando", "Anónimo",
            SERVIDOR + "cancion_rolando.jpg", SERVIDOR + "cancion_rolando.mp3",
            G_EPICO, false, true))
    libros.add(Libro("Matrimonio de sabuesos", "Agata Christie",
            SERVIDOR + "matrimonio_sabuesos.jpg", SERVIDOR + "matrim_sabuesos.mp3",
            G_SUSPENSE, false, true))
    libros.add(Libro("La iliada", "Homero",
            SERVIDOR + "iliada.jpg", SERVIDOR + "la_iliada.mp3",
            G_EPICO, true, false))
    return libros
}

data class Libro(val titulo: String,
                 val autor: String,
                 val urlImagen: String,
                 val urlAudio: String,
                 val genero: String,    // Género literario
                 var novedad: Boolean,  // Es una novedad
                 var leido: Boolean) {    // Leído por el usuario

    var colorVibrante = -1
    var colorApagado = -1

}
