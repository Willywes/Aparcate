package cl.alejandroisla.aparcate

class ParkingUser {

    data class Data(
        var status: String,
        var message: String,
        var entity: ArrayList<Parking>
    )

    data class Parking(
        var id: Int,
        var image: String,
        var title: String,
        var content: String,
        var city: String,
        var period: String,
        var price: String,
        var status: String,
        var created_at: String,
        var updated_at: String,
        var user: User?
    )

    data class User(
        var id: Int,
        var names: String?,
        var email: String,
        var phone: String?,
        var created_at: String,
        var updated_at: String
    )
}