package cl.alejandroisla.aparcate

import android.util.Log
import org.json.JSONObject
import java.net.URL

class APIRequest(val url: String) {

    fun run(): String {
        try {
            val srt = URL(url).readText()
            //Log.d("RESpuesto", JSONObject(srt).toString())
            return JSONObject(srt).toString()

        } catch (ex: Exception) {
           return ex.message.toString()
        }
    }
}