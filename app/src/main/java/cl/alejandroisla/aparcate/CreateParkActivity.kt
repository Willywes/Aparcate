package cl.alejandroisla.aparcate

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.activity_create_park.*
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jetbrains.anko.doAsync
import org.json.JSONObject
import android.R.attr.password
import android.support.v7.app.AlertDialog
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import okhttp3.FormBody
import okhttp3.RequestBody



class CreateParkActivity : AppCompatActivity() {

    private val PICK_IMAGE_REQUEST = 1
    private var listaEstacionamientos = ArrayList<ParkingUser.Parking>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_park)



        btnChoose.setOnClickListener {

            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Seleccionar Foto"), PICK_IMAGE_REQUEST)

        }

        btnSave.setOnClickListener{
            doAsync {

                try {

                    val srt = doPost("http://nominacarrera.tucreativa.cl/aparcate/parkings/store")



                    // uiThread { longToast("Peticion realizada") }
                } catch (ex: Exception) {
                    Log.d("Error", "Error la pm " +  ex.message)
                }
            }

            alertaListo()
        }


    }

    fun alertaListo(){

        val builder = AlertDialog.Builder(this@CreateParkActivity)
        builder.setTitle("Registro Creado")
        builder.setMessage("Se ha publicado correctamente tu anuncio.")

        builder.setNegativeButton("OK") { dialog, whichButton ->
            val intent = Intent(this@CreateParkActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        builder.show()
    }

    fun doPost(url: String): String {



        val title = this.findViewById(R.id.nTitle) as EditText
        val content = this.findViewById(R.id.nContent) as EditText
        val city = this.findViewById(R.id.nCity) as Spinner
        val period = this.findViewById(R.id.nPeriod) as Spinner
        val price = this.findViewById(R.id.nPrice) as EditText

        try {

            val client = OkHttpClient.Builder().build()

            val formBody = FormBody.Builder()
                .add("title", title.text.toString())
                .add("content", content.text.toString())
                .add("city", city.selectedItem.toString())
                .add("period", period.selectedItem.toString())
                .add("price", price.text.toString())
                .add("user_id", "1")
                .build()

            val request = Request.Builder()
                .url(url)
                .post(formBody)
                .build()

            val response = client.newCall(request).execute()
            return response.body()!!.string()

        } catch (ex: Exception) {
            return ("PICOOOO NO PASO" + ex.message)
        }
    }
}
