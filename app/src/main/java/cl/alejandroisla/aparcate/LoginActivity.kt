package cl.alejandroisla.aparcate

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.widget.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.form_search.*
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jetbrains.anko.doAsync

class LoginActivity : AppCompatActivity() {

    var parent = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnLogin.setOnClickListener {

            doAsync {

                val error = parent.findViewById(R.id.errorLogin) as TextView

                error.text = ""

                try {

                    //val srt = doPost("http://nominacarrera.tucreativa.cl/aparcate/parkings/create")
                    val srt = doPost("http://nominacarrera.tucreativa.cl/aparcate/login")

                    if(srt == "1"){

                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish();

                    }else{

                       /* val builder = AlertDialog.Builder(this@LoginActivity)

                        builder.setTitle("Selecciona opciones de filtro")
                        builder.setMessage("El usuario o contraseña no coinciden.")

                        builder.setNegativeButton("OK") { dialog, whichButton ->
                            dialog.cancel()
                        }
                        builder.show()*/

                        error.text = "El usuario o contraseña no coinciden."

                    }

                } catch (ex: Exception) {
                    Log.d("Error", "Error la pm " + ex.message)
                }

            }
        }

    }

    fun doPost(url: String): String {

        val email = this.findViewById(R.id.emailLogin) as EditText
        val pass = this.findViewById(R.id.passLogin) as EditText

        try {

            val client = OkHttpClient.Builder().build()

            val formBody = FormBody.Builder()
                .add("email", email.text.toString())
                .add("password", pass.text.toString())
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
