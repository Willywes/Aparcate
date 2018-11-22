package cl.alejandroisla.aparcate

class Estacionamiento {

    var id: Int? = null
    var imagen: String? = null
    var titulo: String? = null
    var precio: String? = null
    var ciudad: String? = null
    var contenido: String? = null
    var tipo: String? = null

    constructor(id: Int?, imagen: String?,  titulo: String?, precio: String?, ciudad: String?, contenido: String?, tipo: String?) {
        this.id = id
        this.imagen = imagen
        this.titulo = titulo
        this.precio = precio
        this.ciudad = ciudad
        this.contenido = contenido
        this.tipo = tipo
    }

}