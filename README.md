# Ubicacion
En este ejemplo se realiza la solicitud de permisos de riesgo al usuario como lo son de la ubicación ya que utiliza el GPS para proporcionar las coordenadas geograficas del dispositvo.
Se verifica cual fue la ultima ubicación que el dispositivo registro y se manda por medio de un Toast, esto por medio del permiso COARSE LOCATION

En el archivo AndroidManifest.xml vrificar que estén los siguientes permisos:
```xml
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
```
