package mx.edu.ittepic.ladm_u5_ejercicio1_geomapatec

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    lateinit var ubicacionCLiente : FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        ubicacionCLiente = LocationServices.getFusedLocationProviderClient(this)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
        when(item.itemId){
            android.R.id.home->{
                finish()
            }
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        var datosRecibidos = intent.extras!!
        var nombre = datosRecibidos.getString("nom")
        var latitud = datosRecibidos.getDouble("pos1")
        var longitud = datosRecibidos.getDouble("pos2")
        this.setTitle(nombre)

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isMyLocationButtonEnabled = true
        mMap.isMyLocationEnabled = true
        ubicacionCLiente.lastLocation.addOnSuccessListener {
            if(it!=null){
                val posicionActual = LatLng(it.latitude,it.longitude)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(posicionActual,17f))
            }
        }
        val marcador = LatLng(latitud, longitud)
        mMap.addMarker(MarkerOptions().position(marcador).title(nombre))

    }
}
