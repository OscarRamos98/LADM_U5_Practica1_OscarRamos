package mx.edu.ittepic.ladm_u5_ejercicio1_geomapatec

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.core.app.ActivityCompat
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var baseRemota = FirebaseFirestore.getInstance()
    var listaData = ArrayList<Data>()
    var ubicaciones = ArrayList<String>()

    lateinit var locacion : LocationManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),1)
        }
        baseRemota.collection("tecnologico").addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            if(firebaseFirestoreException != null){
                return@addSnapshotListener
            }

            listaData.clear()
            ubicaciones.clear()
            for (document in querySnapshot!!){
                var data = Data()
                data.nombre = document.getString("nombre").toString()
                data.posicion1 = document.getGeoPoint("posicion1")!!
                data.posicion2 = document.getGeoPoint("posicion2")!!
                data.centro = document.getGeoPoint("centro")!!
                listaData.add(data)
                ubicaciones.add(document.getString("nombre").toString())
            }
            var adaptador = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,ubicaciones)
            ubicacionLista.adapter = adaptador
        }

        ubicacionLista.setOnItemClickListener { parent, view, position, id ->
            var intent : Intent = Intent(this,MapsActivity::class.java)
            var nom = ubicaciones[position]
            var pos1 = listaData[position].centro.latitude
            var pos2 =listaData[position].centro.longitude

            intent.putExtra("nom",nom)
            intent.putExtra("pos1",pos1)
            intent.putExtra("pos2",pos2)

            startActivity(intent)

        }
        locacion = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var oyente = Oyente(this)
        locacion.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,01f,oyente)
    }
}

class Oyente(puntero:MainActivity) : android.location.LocationListener{
    var p = puntero
    override fun onLocationChanged(location: Location) {
        var geoPosicion = GeoPoint(location.latitude,location.longitude)
        for(item in p.listaData){
            if(item.estoyEn(geoPosicion)){
                p.ubicacionActual.setText("Tu ubicacion actual es: "+item.nombre)
            }
        }
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

    }

    override fun onProviderEnabled(provider: String?) {

    }

    override fun onProviderDisabled(provider: String?) {

    }

}