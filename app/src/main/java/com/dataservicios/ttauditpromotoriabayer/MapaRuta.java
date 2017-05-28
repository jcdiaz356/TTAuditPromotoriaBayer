package com.dataservicios.ttauditpromotoriabayer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.dataservicios.ttauditpromotoriabayer.app.AppController;
import com.dataservicios.ttauditpromotoriabayer.util.DirectionsJSONParser;
import com.dataservicios.ttauditpromotoriabayer.util.GlobalConstant;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by usuario on 26/01/2015.
 */
public class MapaRuta extends FragmentActivity {
    GoogleMap map;
    ArrayList<LatLng> markerPoints;
    ArrayList<Marker> myMarkersPointsInterbank = new ArrayList<Marker>();
    ArrayList<Marker> myMarkersPointsAlicorp = new ArrayList<Marker>();
    ArrayList<Marker> myMarkersPointsBayer = new ArrayList<Marker>();
    MarkerOptions options;
    private int IdRuta ;
    private ProgressDialog pDialog;
    private JSONObject params;
    private double lat ;
    private double lon ;
    private Button btn_MarkeInter, btn_MarkeAlicorp, btn_MarkeBayer ;
    private boolean isPressedInterbak = false,  isPressedAlicorp = false , isPressedBayer ;
    Activity MyActivity= (Activity) this;
    Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapa_rutas);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("Mapa de Rutas");
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);

        btn_MarkeInter = (Button) findViewById(R.id.btnMarkeInter);
        btn_MarkeAlicorp = (Button) findViewById(R.id.btnMarkeAlicorp);
        btn_MarkeBayer = (Button) findViewById(R.id.btnMarkeBayer);

        Bundle bundle = getIntent().getExtras();
        IdRuta= bundle.getInt("id");

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Cargando...");
        pDialog.setCancelable(false);



        //Recogiendo paramentro del anterior Activity
        //Bundle bundle = savedInstanceState.getArguments();
        params = new JSONObject();
        try {
            params.put("id", IdRuta);
            //params.put("id_pdv",idPDV);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Initializing
        markerPoints = new ArrayList<LatLng>();
        // Getting reference to SupportMapFragment of the activity_main
        SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        // Getting reference to Button
        //Button btnDraw = (Button) findViewById(R.id.btn_draw);
        // Obtener mapas para el SupportMapFragment
        map = fm.getMap();
        // Habilitar Botón MyLocation en el Mapa
        map.setMyLocationEnabled(true);


//        // Adición de un nuevo elemento a la ArrayList
//        markerPoints.add(new LatLng(-11.925391, -77.043209));
//        // Crear MarkerOptions
//        options = new MarkerOptions();
//        // Ajuste de la posición del marcador
//        options.position(new LatLng(-11.925391, -77.043209));
//        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
//        //}
//        //Añadir nuevo marcador para la API V2 Google Map Android
//        map.addMarker(options);
//
//
//
//        markerPoints.add(new LatLng(-11.928750, -77.044325));
//        // Crear MarkerOptions
//        options = new MarkerOptions();
//        // Ajuste de la posición del marcador
//        options.position(new LatLng(-11.928750, -77.044325));
//        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
//        //Añadir nuevo marcador para la API V2 Google Map Android
//        map.addMarker(options);
//
//        markerPoints.add(new LatLng(-11.928751, -77.044326));
//        // Crear MarkerOptions
//        options = new MarkerOptions();
//        // Ajuste de la posición del marcador
//        options.position(new LatLng(-11.928751, -77.044326));
//        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
//        //}
//        //Añadir nuevo marcador para la API V2 Google Map Android
//        map.addMarker(options);


        btn_MarkeInter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPressedInterbak) {

                    Drawable img = MyActivity.getResources().getDrawable( R.drawable.ic_inter_active);
                    img.setBounds( 0, 0, 95, 95 );  // set the image size
                    //txtVw.setCompoundDrawables( img, null, null, null );
                    btn_MarkeInter.setCompoundDrawables(null, img, null, null);
                    // btn_MarkeInter.setBackgroundResource(R.drawable.ic_inter_active);

                    //markerPoints.

                    for (Marker m : myMarkersPointsInterbank) {
                        m.setVisible(true);
                    }

                    toast = Toast.makeText(MyActivity, "Activado puntos de interbank", Toast.LENGTH_LONG);
                    toast.show();
                } else {

                    Drawable img = MyActivity.getResources().getDrawable( R.drawable.ic_inter_desactive);
                    img.setBounds( 0, 0, 95, 95 );  // set the image size
                    //txtVw.setCompoundDrawables( img, null, null, null );
                    btn_MarkeInter.setCompoundDrawables(null,img,null,null);
                    //btn_MarkeInter.setBackgroundResource(R.drawable.ic_inter_desactive);

                    for (Marker m : myMarkersPointsInterbank) {
                        m.setVisible(false);
                    }
                    toast = Toast.makeText(MyActivity, "", Toast.LENGTH_LONG);
                    toast = Toast.makeText(MyActivity, "Desactivado puntos de interbank", Toast.LENGTH_LONG);
                    toast.show();
                }
                isPressedInterbak = !isPressedInterbak;
            }
        });


        btn_MarkeAlicorp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPressedAlicorp) {

                    Drawable img = MyActivity.getResources().getDrawable( R.drawable.ic_alicorp_active);
                    img.setBounds( 0, 0, 95, 95 );  // set the image size
                    //txtVw.setCompoundDrawables( img, null, null, null );
                    btn_MarkeAlicorp.setCompoundDrawables(null, img, null, null);
                    // btn_MarkeInter.setBackgroundResource(R.drawable.ic_inter_active);

                    //markerPoints.

                    for (Marker m : myMarkersPointsAlicorp) {
                        m.setVisible(true);
                    }

                    toast = Toast.makeText(MyActivity, "Activado puntos de Alicorp", Toast.LENGTH_LONG);
                    toast.show();
                } else {

                    Drawable img = MyActivity.getResources().getDrawable( R.drawable.ic_alicorp_desactive);
                    img.setBounds( 0, 0, 95, 95 );  // set the image size
                    //txtVw.setCompoundDrawables( img, null, null, null );
                    btn_MarkeAlicorp.setCompoundDrawables(null,img,null,null);
                    //btn_MarkeInter.setBackgroundResource(R.drawable.ic_inter_desactive);

                    for (Marker m : myMarkersPointsAlicorp) {
                        m.setVisible(false);
                    }
                    toast = Toast.makeText(MyActivity, "", Toast.LENGTH_LONG);
                    toast = Toast.makeText(MyActivity, "Desactivado puntos de Alicorp", Toast.LENGTH_LONG);
                    toast.show();
                }
                isPressedAlicorp = !isPressedAlicorp;
            }
        });

        btn_MarkeBayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPressedBayer) {

                    Drawable img = MyActivity.getResources().getDrawable( R.drawable.ic_bayer_active);
                    img.setBounds( 0, 0, 95, 95 );  // set the image size
                    //txtVw.setCompoundDrawables( img, null, null, null );
                    btn_MarkeBayer.setCompoundDrawables(null, img, null, null);
                    // btn_MarkeInter.setBackgroundResource(R.drawable.ic_inter_active);

                    //markerPoints.

                    for (Marker m : myMarkersPointsBayer) {
                        m.setVisible(true);
                    }

                    toast = Toast.makeText(MyActivity, "Activado puntos de Bayer", Toast.LENGTH_LONG);
                    toast.show();
                } else {

                    Drawable img = MyActivity.getResources().getDrawable( R.drawable.ic_bayer_desactive);
                    img.setBounds( 0, 0, 95, 95 );  // set the image size
                    //txtVw.setCompoundDrawables( img, null, null, null );
                    btn_MarkeBayer.setCompoundDrawables(null,img,null,null);
                    //btn_MarkeInter.setBackgroundResource(R.drawable.ic_inter_desactive);

                    for (Marker m : myMarkersPointsBayer) {
                        m.setVisible(false);
                    }
                    toast = Toast.makeText(MyActivity, "", Toast.LENGTH_LONG);
                    toast = Toast.makeText(MyActivity, "Desactivado puntos de Bayer", Toast.LENGTH_LONG);
                    toast.show();
                }
                isPressedBayer = !isPressedBayer;
            }
        });


        showpDialog();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST , GlobalConstant.dominio + "/JsonRoadsMap" ,params,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        Log.d("DATAAAA", response.toString());
                        //adapter.notifyDataSetChanged();
                        try {
                            //String agente = response.getString("agentes");
                            int success =  response.getInt("success");
                            if (success == 1) {
//
                                JSONArray ObjJson;
                                ObjJson = response.getJSONArray("storeMaps");
                                // looping through All Products
                                if(ObjJson.length() > 0) {
                                    for (int i = 0; i < ObjJson.length(); i++) {
                                        try {
                                            JSONObject obj = ObjJson.getJSONObject(i);
                                            if(obj.getInt("company_id") == 12 ){
                                                myMarkersPointsInterbank.add(map.addMarker(new MarkerOptions()
                                                        .position(new LatLng(Double.valueOf(obj.getString("latitude")), Double.valueOf(obj.getString("longitude"))))
                                                        .title("Shop")
                                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_interbank))
                                                        .title(obj.getString("fullname"))));
                                            }

                                            if(obj.getInt("company_id") == GlobalConstant.company_id){
                                                myMarkersPointsBayer.add(map.addMarker(new MarkerOptions()
                                                        .position(new LatLng(Double.valueOf(obj.getString("latitude")), Double.valueOf(obj.getString("longitude"))))
                                                        .title("Shop")
                                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_alicorp))
                                                        .title(obj.getString("fullname"))));
                                            }

                                            if(obj.getInt("company_id") == 13 ){
                                                myMarkersPointsAlicorp.add(map.addMarker(new MarkerOptions()
                                                        .position(new LatLng(Double.valueOf(obj.getString("latitude")), Double.valueOf(obj.getString("longitude"))))
                                                        .title("Shop")
                                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_bayer))
                                                        .title(obj.getString("fullname"))));
                                            }


//                                            markerPoints.add(new LatLng(Double.valueOf(obj.getString("latitude")), Double.valueOf(obj.getString("longitude"))));
//                                            // Crear MarkerOptions
//                                            options = new MarkerOptions();
//                                            // Ajuste de la posición del marcador
//                                            options.position(new LatLng(Double.valueOf(obj.getString("latitude")), Double.valueOf(obj.getString("longitude"))));
//                                            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
//                                            options.title(obj.getString("fullname"));
//                                            //Añadir nuevo marcador para la API V2 Google Map Android
//                                            map.addMarker(options);
//
                                            lat = Double.valueOf(obj.getString("latitude")) ;
                                            lon = Double.valueOf(obj.getString("longitude"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                    CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(lat, lon)).zoom(15).build();
                                    map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        hidepDialog();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //VolleyLog.d(TAG, "Error: " + error.getMessage());
                        hidepDialog();
                    }
                }
        );

        AppController.getInstance().addToRequestQueue(jsObjRequest);


        // Configuración onclick detector de eventos para el mapa
//        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//            @Override
//            public void onMapClick(LatLng point) {
//                //Ya 10 localidades con 8 puntos de referencia y 1 punto de inicio y 1 ubicación final.
//                // Hasta 8 puntos de ruta son permitidos en una consulta para los usuarios no comerciales
//                if (markerPoints.size() >= 10) {
//                    return;
//                }
//
//                // Adición de un nuevo elemento a la ArrayList
//                markerPoints.add(point);
//
//                // Crear MarkerOptions
//                MarkerOptions options = new MarkerOptions();
//
//                // Ajuste de la posición del marcador
//                options.position(point);
//
//                /**
//                 * Para la ubicación de inicio, el color del marcador es verde y
//                 * Para la ubicación final, el color del marcador es ROJO y
//                 * Para el resto de los marcadores, el color es AZURE
//                 */
//                if (markerPoints.size() == 1) {
//                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
//                } else if (markerPoints.size() == 2) {
//                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
//                } else {
//                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
//                }
//
//                //Añadir nuevo marcador para la API V2 Google Map Android
//                map.addMarker(options);
//            }
//        });

        // El mapa se borrará en la pulsación larga
//        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
//
//            @Override
//            public void onMapLongClick(LatLng point) {
//                // Elimina todos los puntos del mapa de Google
//                map.clear();
//
//                // Elimina todos los puntos en el ArrayList
//                markerPoints.clear();
//            }
//        });




        // Haga clic en controlador de eventos para el botón btn_draw
//        btnDraw.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // Checks, whether start and end locations are captured
//                if (markerPoints.size() >= 2) {
//                    LatLng origin = markerPoints.get(0);
//                    LatLng dest = markerPoints.get(1);
//
//                    // Getting URL to the Google Directions API
//                    String url = getDirectionsUrl(origin, dest);
//
//                    DownloadTask downloadTask = new DownloadTask();
//
//                    // Start downloading json data from Google Directions API
//                    downloadTask.execute(url);
//                }
//            }
//        });
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origen de la ruta
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destino de ruta
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor habilitado
        String sensor = "sensor=false";

        // Waypoints = Puntos de paso
        String waypoints = "";
        for (int i = 2; i < markerPoints.size(); i++) {
            LatLng point = (LatLng) markerPoints.get(i);
            if (i == 2)
                waypoints = "waypoints=";
            waypoints += point.latitude + "," + point.longitude + "|";
        }

        // La construcción de los parámetros para el servicio web
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + waypoints;

        // Output format
        String output = "json";

        // construcción de los parámetros para el servicio web
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        return url;
    }

    /**
     * Un método para descargar datos JSON de url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creación de una conexión HTTP para comunicarse con url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Conexión a url
            urlConnection.connect();

            // Lectura de datos de url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception while downloading url", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    //Obtiene datos desde una URL
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread =  descarga de datos en el hilo
        @Override
        protected String doInBackground(String... url) {

            // Para el almacenamiento de datos de servicio web

            String data = "";

            try {
                // Extrayendo los datos de servicio web
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {

            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(2);
                lineOptions.color(Color.RED);
            }

            // Drawing polyline in the Google Map for the i-th route
            map.addPolyline(lineOptions);
        }
    }

    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
        //return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //this.finish();
        //Intent a = new Intent(this, PuntosVenta.class);
        //a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
        //startActivity(a);
    }
///METODOS------

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}