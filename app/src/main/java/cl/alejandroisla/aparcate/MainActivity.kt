package cl.alejandroisla.aparcate

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.form_search.*
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup

import com.bumptech.glide.Glide



class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var listaEstacionamientos = ArrayList<Estacionamiento>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)


        listaEstacionamientos.add(Estacionamiento(1, "https://picsum.photos/200/300", "Estacionamieno grande", "$ 4.000", "Valparaiso","Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s","Diario"))

        listaEstacionamientos.add(Estacionamiento(2, "https://picsum.photos/200/300", "Estacionamieno puerto", "$120.000", "Valparaiso","Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s","Mensual"))

        listaEstacionamientos.add(Estacionamiento(2, "https://picsum.photos/200/300", "Estacionamieno centro", "$ 12.000", "Santiago","Lorem Ikl..","Mensual"))
        listaEstacionamientos.add(Estacionamiento(1, "https://picsum.photos/200/300", "Estacionamieno grande", "$ 4.000", "Valparaiso","Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s","Diario"))

        listaEstacionamientos.add(Estacionamiento(2, "https://picsum.photos/200/300", "Estacionamieno puerto", "$120.000", "Valparaiso","Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s","Mensual"))

        listaEstacionamientos.add(Estacionamiento(2, "https://picsum.photos/200/300", "Estacionamieno centro", "$ 12.000", "Santiago","Lorem Ikl..","Mensual"))
        listaEstacionamientos.add(Estacionamiento(1, "https://picsum.photos/200/300", "Estacionamieno grande", "$ 4.000", "Valparaiso","Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s","Diario"))

        listaEstacionamientos.add(Estacionamiento(2, "https://picsum.photos/200/300", "Estacionamieno puerto", "$120.000", "Valparaiso","Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s","Mensual"))

        listaEstacionamientos.add(Estacionamiento(2, "https://picsum.photos/200/300", "Estacionamieno centro", "$ 12.000", "Santiago","Lorem Ikl..","Mensual"))

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
        private var estaList = ArrayList<Estacionamiento>()
        private var context: Context? = null
        constructor(context: Context, estaList: ArrayList<Estacionamiento>) : super() {
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

            //vh.iv = estaList[position].imagen
            vh.tvCiudad.text = estaList[position].ciudad
            vh.tvTipo.text = estaList[position].tipo
            vh.tvTitulo.text = estaList[position].titulo
            //vh.tvContenido.text = estaList[position].contenido
            vh.tvPrecio.text = estaList[position].precio

            Glide.with(this@MainActivity).load(estaList[position].imagen).into(vh.ivImagen)

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
    private class ViewHolder(view: View?) {
        val ivImagen : ImageView
        val tvCiudad: TextView
        val tvTipo: TextView
        val tvTitulo: TextView
       // val tvContenido: TextView
        val tvPrecio: TextView

        init {
            this.ivImagen = view?.findViewById(R.id.ivImagen) as ImageView
            this.tvCiudad = view?.findViewById(R.id.tvCiudad) as TextView
            this.tvTipo = view?.findViewById(R.id.tvTipo) as TextView
            this.tvTitulo = view?.findViewById(R.id.tvTitulo) as TextView
           // this.tvContenido = view?.findViewById(R.id.tvContenido) as TextView
            this.tvPrecio = view?.findViewById(R.id.tvPrecio) as TextView

        }
    }

    private fun filterEstacionamientos(){
        val builder = AlertDialog.Builder(this)

        builder.setTitle(title)
        val viewInflated = LayoutInflater.from(this).inflate(R.layout.form_search, form_search, false)
        builder.setView(viewInflated)

        builder.setPositiveButton("Buscar") { dialog, whichButton ->

            dialog.dismiss()
        }
        builder.setNegativeButton("Cancelar") { dialog, whichButton ->
            dialog.cancel()
        }
        builder.show()
    }
}
