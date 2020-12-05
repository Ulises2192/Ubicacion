package com.ulisesdiaz.ubicacion

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.tasks.OnSuccessListener

class MainActivity : AppCompatActivity() {

    // Variables de los permisos que se van autilizar haciendo referencia a los permisos que se van autilizar
    private val permisoFineLocation = android.Manifest.permission.ACCESS_FINE_LOCATION
    private val permisoCoarseLocation = android.Manifest.permission.ACCESS_COARSE_LOCATION

    // Variable que permite identificar el permiso mediante un nummero
    private val CODIGO_SOLICITUD_PERMISO = 100

    // Variable que permite obtener los datos de la ubicacion a traves de google play service Location
    var fusedLocationClient: FusedLocationProviderClient? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Se instancia la variable que permitira obtener las coordenadas
        fusedLocationClient = FusedLocationProviderClient(this)
    }

    /**
     * Ciclo de Vida onStart
     * Se activa cada vez que se inicie la aplicacion, cambie o la app pase a segundo plano
     * Condicinal donde si hay permisos obtiene la ubicion, caso contrario solicita los permisos
     */
    override fun onStart() {
        super.onStart()

        if (validarPermisosUbicacion()){
            obtenerUbicacion()
        }else{
            pedirPermisos()
        }
    }

    /**
     * Esta funcion permite mapear si el usuario otorgo permisos, es llamada una vez que se otorgaron los permisos o se denegaron
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode){
            CODIGO_SOLICITUD_PERMISO ->{
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    // Obtener ubicacion
                    obtenerUbicacion()
                }else{
                    Toast.makeText(this, "No se otorgaron permisos para la ubicacon",
                            Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    /**
     * Funcion que valida si el usuario ya tiene permisos o se necesita pedir
     * Se compara el permiso que deseo usar con los que se declararon en el manifest
     * Regresa verdadero si estan los permisos otorgados
     */
    private fun validarPermisosUbicacion(): Boolean{
        val hayUbicacionPrecisa = ActivityCompat.checkSelfPermission(
                this, permisoFineLocation) == PackageManager.PERMISSION_GRANTED

        val hayUbicacionOrdinaria = ActivityCompat.checkSelfPermission(
                this, permisoCoarseLocation)  == PackageManager.PERMISSION_GRANTED

        return hayUbicacionPrecisa && hayUbicacionOrdinaria
    }

    /**
     * Se obtiene ubiccacion que se utilizo con anterioridad, esta la obtiene mediante la aplicacion de google maps
     * Lo realiza por medio del listener addOnSuccesListener
     */
    @SuppressLint("MissingPermission")
    private fun obtenerUbicacion(){

        fusedLocationClient?.lastLocation?.addOnSuccessListener(this, object: OnSuccessListener<Location>{
            override fun onSuccess(location: Location?) {
                if (location != null){
                    Toast.makeText(applicationContext, "Coordenadas de ultima ubicacion registrada"
                            + location.latitude.toString() + ",  " + location.longitude.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    /**
     * Si el usuario no tiene permisos o son negados entra a esta funcion para solicitarlos
     * la variable proverContexto recibe un boleano de si se otorgo el permiso o no (true o false)
     * Solo se pide el permiso de Fine Location
     */
    private fun pedirPermisos(){
        val proveerContexto = ActivityCompat.shouldShowRequestPermissionRationale(
                this, permisoFineLocation)
        if (proveerContexto){
            // Mandar mensaje con explicacion adicinal
            solicitudPermiso()
        }else{
            solicitudPermiso()
        }
    }

    /**
     * Se llama a requesPermissions para ingresar todos los permisos que funcionaran en la actividad.
     * Los permisos se ingresan por medio de un arreglo
     */
    private fun solicitudPermiso(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            requestPermissions(arrayOf(permisoFineLocation, permisoCoarseLocation), CODIGO_SOLICITUD_PERMISO)
        }
    }

}