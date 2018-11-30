package cl.alejandroisla.aparcate

class Estacionamiento {

    var id: Int? = null
    var image: String? = null
    var title: String? = null
    var content: String? = null
    var city: String? = null
    var period: String? = null
    var price: String? = null
    var status: String? = null
    var created_at: String? = null
    var updated_at: String? = null
    var user: String? = null

    constructor(
        id: Int?,
        image: String?,
        title: String?,
        content: String?,
        city: String?,
        period: String?,
        price: String?,
        status: String?,
        created_at: String?,
        updated_at: String?,
        user: String?
    ) {
        this.id = id
        this.image = image
        this.title = title
        this.content = content
        this.city = city
        this.period = period
        this.price = price
        this.status = status
        this.created_at = created_at
        this.updated_at = updated_at
        this.user = user
    }
}