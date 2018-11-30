package cl.alejandroisla.aparcate

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.CardView
import android.util.Log
import android.view.*
import android.widget.*
import com.bumptech.glide.Glide
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.detail.*
import kotlinx.android.synthetic.main.form_search.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.email
import org.jetbrains.anko.makeCall


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    //private var listaEstacionamientos = ArrayList<Estacionamiento>()
    private var listaEstacionamientos = ArrayList<ParkingUser.Parking>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            filterEstacionamientos()
        }

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)


       /* listaEstacionamientos.add(Estacionamiento(1, "https://picsum.photos/200/300", "Estacionamieno grande", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s",, "Valparaiso","Diario", "4.000", "1", null, null , "1")) */
        updateListView()

        getAllParking(null, null, null, null)

    }

    private fun getAllParking(price : String?, date :  String?, city : String?, period : String?){



        var progressDoalog = ProgressDialog (this)
        progressDoalog.setMax(100)
        progressDoalog.setMessage("Its loading....")
        progressDoalog.setTitle("ProgressDialog bar example")
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
        progressDoalog.show()

        var urlParkings = "http://nominacarrera.tucreativa.cl/aparcate/parkings?"

        if(city != null){
            urlParkings = urlParkings + "city=" + city + "&"
        }

        if(period != null){
            urlParkings = urlParkings + "period=" + period + "&"
        }

        if(price != null){
            urlParkings = urlParkings + "price=" + price + "&"
        }

        if(date != null){
            urlParkings = urlParkings + "date=" + date + "&"
        }

        doAsync {

            try {
                var response = APIRequest(urlParkings).run()

                var gson = Gson()
                var dataReponsed = gson.fromJson(response, ParkingUser.Data::class.java)

                listaEstacionamientos = dataReponsed.entity

                println(listaEstacionamientos)

               // uiThread { longToast("Peticion realizada") }
            } catch (ex: Exception) {
                Log.d("Error", "Error la pm " +  ex.message)
            }
        }

        val handler = Handler()
        handler.postDelayed({ delayInit() }, 2000)

        progressDoalog.dismiss()

        //updateListView()
    }

    private fun delayInit() {

        updateListView()
    }


    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {

            R.id.nav_buscar_arriendo ->{
                filterEstacionamientos()
            }

            R.id.nav_arrendar -> {
                var intent = Intent(this@MainActivity, CreateParkActivity::class.java)
                startActivity(intent)
            }


            R.id.nav_share -> {

            }
            R.id.nav_about -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun updateListView(){
        var notesAdapter = ParkingAdapter(this, listaEstacionamientos)
        lvPublicaciones.adapter = notesAdapter
    }


    inner class ParkingAdapter : BaseAdapter {
        private var estaList = ArrayList<ParkingUser.Parking>()
        private var context: Context? = null

        constructor(context: Context, estaList: ArrayList<ParkingUser.Parking>) : super() {
            this.estaList = estaList
            this.context = context
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
            val view: View?
            val vh: ViewHolder
            if (convertView == null) {
                view = layoutInflater.inflate(R.layout.estacionamiento, parent, false)
                vh = ViewHolder(view)
                view.tag = vh
            } else {
                view = convertView
                vh = view.tag as ViewHolder
            }

            println("ADAPTER LIST")
            println(estaList)
            //vh.iv = estaList[position].imagen
            vh.tvCiudad.text = estaList[position].city
            vh.tvTipo.text = estaList[position].period
            vh.tvTitulo.text = estaList[position].title
            //vh.tvContenido.text = estaList[position].contenido
            vh.tvPrecio.text = estaList[position].price

            val cvShow = view?.findViewById(R.id.cardEstacionamiento) as CardView

            Glide.with(this@MainActivity).load(estaList[position].image).into(vh.ivImagen)


            cvShow.setOnClickListener {

                showDetail(position)
                //Toast.makeText(this@MainActivity, "Estacionamiento" + estaList[position].id , Toast.LENGTH_SHORT).show()
            }

            return view
        }
        override fun getItem(position: Int): Any {
            return estaList[position]
        }
        override fun getItemId(position: Int): Long {
            return position.toLong()
        }
        override fun getCount(): Int {
            return estaList.size
        }
    }


    private fun showDetail(position : Int?){

        val builder = AlertDialog.Builder(this)

        val viewInflated = LayoutInflater.from(this).inflate(R.layout.detail, detail , false)
        builder.setView(viewInflated)

        var Parking = listaEstacionamientos[position!!]

        //val image = viewInflated.findViewById(R.id.txtNombre) as TextView
        val image = viewInflated.findViewById(R.id.dImage) as ImageView
        val title = viewInflated.findViewById(R.id.dTitle) as TextView
        val content = viewInflated.findViewById(R.id.dContent) as TextView
        val city = viewInflated.findViewById(R.id.dCity) as TextView
        val period = viewInflated.findViewById(R.id.dPeriod) as TextView
        val price = viewInflated.findViewById(R.id.dPrice) as TextView
        val names = viewInflated.findViewById(R.id.dNames) as TextView


        val llamar = viewInflated.findViewById(R.id.dLlamar) as Button
        val email = viewInflated.findViewById(R.id.dEmail) as Button

        title.text = Parking.title
        content.text = Parking.content
        city.text = Parking.city
        period.text = Parking.period
        price.text = Parking.price
        names.text = Parking.user!!.names

        Glide.with(this@MainActivity).load(Parking.image).into(image)

        try {
            llamar.setOnClickListener {
                makeCall("+56" + Parking.user!!.phone)
            }

            email.setOnClickListener {
                email(Parking.user!!.email,
                    "Consulta por " +  Parking.title,
                    "Hola " + Parking.user!!.names + ", Quería saber mas detalles de este anuncio.")
            }
        }catch (exx: Exception){
            Toast.makeText(this@MainActivity, exx.message.toString(), Toast.LENGTH_SHORT).show()
        }

        builder.show()

    }


    private class ViewHolder(view: View?) {
        val ivImagen : ImageView // image
        val tvCiudad: TextView // city
        val tvTipo: TextView // periodo
        val tvTitulo: TextView // title
       // val tvContenido: TextView
        val tvPrecio: TextView // price


        init {
            this.ivImagen = view?.findViewById(R.id.ivImagen) as ImageView
            this.tvCiudad = view.findViewById(R.id.tvCiudad) as TextView
            this.tvTipo = view.findViewById(R.id.tvTipo) as TextView
            this.tvTitulo = view.findViewById(R.id.tvTitulo) as TextView
           // this.tvContenido = view?.findViewById(R.id.tvContenido) as TextView
            this.tvPrecio = view.findViewById(R.id.tvPrecio) as TextView


        }
    }

    private fun filterEstacionamientos(){
        val builder = AlertDialog.Builder(this)

        builder.setTitle("Selecciona opciones de filtro")
        val viewInflated = LayoutInflater.from(this).inflate(R.layout.form_search, form_search, false)
        builder.setView(viewInflated)


        val city = viewInflated.findViewById(R.id.fCity) as Spinner




        builder.setPositiveButton("Buscar") { dialog, whichButton ->

            if(!city.equals("Todas")){

                getAllParking(null, null, city.selectedItem.toString(), null)

            }

            if(city.equals("Todas")){
                getAllParking(null, null, null,  null)
            }



            dialog.dismiss()
        }
        builder.setNegativeButton("Cancelar") { dialog, whichButton ->
            dialog.cancel()
        }
        builder.show()
    }



}

