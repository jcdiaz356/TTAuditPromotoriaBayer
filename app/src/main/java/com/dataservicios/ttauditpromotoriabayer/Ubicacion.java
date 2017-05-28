package com.dataservicios.ttauditpromotoriabayer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dataservicios.ttauditpromotoriabayer.util.GlobalConstant;
import com.dataservicios.ttauditpromotoriabayer.util.JSONParser;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by usuario on 18/01/2015.
 */
public class Ubicacion extends FragmentActivity {
    private Activity MyActivity = this ;
    private GoogleMap map;

    private static final String TAG_FULL_NAME = "fullname";
    private static final String TAG_DISTRICT = "district";
    private static final String TAG_REGION = "region";
    private static final String TAG_LATITUDE = "latitude";
    private static final String TAG_LONGITUDE = "longitude";
    private static final String TAG_SUCCESS = "success";

    private Button btGuardar;

    JSONArray storeJsonArray = null;

    private LocationManager locManager;
    private LocationListener locListener;
    private double latitude ;
    private double longitude ;
    private Marker MarkerNow;
    private double lat ;
    private double lon ;

    private int store_id,rout_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       // getWindow().setWindowAnimations(2);
        setContentView(R.layout.ubicacion);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        getActionBar().setTitle("Guardar Geolocalización");

        btGuardar = (Button) findViewById(R.id.btGuardar);

        Bundle bundle = getIntent().getExtras();
        store_id= bundle.getInt("store_id");
        rout_id= bundle.getInt("rout_id");




        map =((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        map.setMyLocationEnabled(true);

        map.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                lat = location.getLatitude();
                lon = location.getLongitude();
            }
        });

        btGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MyActivity);
                builder.setTitle("Guardar Georeferencia");
                builder.setMessage("Está seguro de guardar la Georeferencia: ");
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MyActivity,"Lat: " + String.valueOf(lat) +  " Long: "  + String.valueOf(lon) , Toast.LENGTH_LONG).show();
                        new SaveGeorreferencia().execute();
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
                builder.setCancelable(false);
            }
        });
        new JSONParse().execute();
    }



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

    private class JSONParse extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(MyActivity);
            pDialog.setMessage("Cargando cordenadas ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

        }

        @Override
        protected JSONObject doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("id", String.valueOf(store_id)));
            params.add(new BasicNameValuePair("idRoute", String.valueOf(rout_id)));
            params.add(new BasicNameValuePair("company_id", String.valueOf(GlobalConstant.company_id)));
            JSONParser jsonParser = new JSONParser();
            JSONObject json = jsonParser.makeHttpRequest(GlobalConstant.dominio + "/JsonRoadDetail", "POST",params);
            return json;
        }
        @Override
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();
            try {
                // Getting JSON Array
               // user = json.getJSONArray(TAG_USER);
               if (json != null) {
                   storeJsonArray = json.getJSONArray("roadsDetail");

                   if(storeJsonArray.length() > 0) {
                       for (int i = 0; i < storeJsonArray.length(); i++) {

                           JSONObject obj = storeJsonArray.getJSONObject(i);
                           String fullName = obj.getString(TAG_FULL_NAME);
                           String distric = obj.getString(TAG_DISTRICT);
                           String region = obj.getString(TAG_REGION);
                           latitude= Double.valueOf(obj.getString(TAG_LATITUDE))  ;
                           longitude= Double.valueOf(obj.getString(TAG_LONGITUDE));

                           MarkerNow = map.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title(fullName)
                                   .snippet("Región: " + region)
                                   .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_alicorp)));
                           CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(latitude, longitude)).zoom(15).build();
                           map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                       }
                   }
                } else {
                   Toast.makeText(MyActivity,"No se pudo obtener información, póngase en contácto con el administrador" , Toast.LENGTH_LONG).show();
               }


            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(MyActivity,"Server error, póngase en contácto con el administrador" , Toast.LENGTH_LONG).show();
            }

        }
    }

    private class SaveGeorreferencia extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(MyActivity);
            pDialog.setMessage("Getting Data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

        }

        @Override
        protected JSONObject doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("latitud", String.valueOf(lat)));
            params.add(new BasicNameValuePair("longitud", String.valueOf(lon)));
            params.add(new BasicNameValuePair("id", String.valueOf(store_id)));


            JSONParser jsonParser = new JSONParser();
            JSONObject json = jsonParser.makeHttpRequest(GlobalConstant.dominio + "/updatePositionStore", "POST",params);
            return json;
        }
        @Override
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();
            try {
                // Getting JSON Array
                // user = json.getJSONArray(TAG_USER);
                if (json != null) {


                    int success =  json.getInt(TAG_SUCCESS);
                    if (success == 1) {
//
                        map.clear();
                        MarkerNow = map.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title("Mi Ubicación")
                                //.snippet("Population: 4,137,400")
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_alicorp)));
                        map.setMyLocationEnabled(true);
                        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(lat, lon)).zoom(15).build();
                        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                    }
                } else {
                    Toast.makeText(MyActivity,"No se pudo obtener información, póngase en contácto con el administrador" , Toast.LENGTH_LONG).show();
                }


            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(MyActivity,"Server error, póngase en contácto con el administrador" , Toast.LENGTH_LONG).show();
            }

        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
            //Toast.makeText(MyActivity, "No se puede volver atras, los datos ya fueron guardado, para modificar pongase en contácto con el administrador", Toast.LENGTH_LONG).show();
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public void onBackPressed() {


        AlertDialog.Builder builder = new AlertDialog.Builder(MyActivity);
        builder.setTitle("Geolocalización");
        builder.setMessage("Está seguro desea salir");
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener()

        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

                // Toast.makeText(MyActivity, "No se puede volver atras, los datos ya fueron guardado, para modificar póngase en contácto con el administrador", Toast.LENGTH_LONG).show();
                // super.onBackPressed();
                //this.finish();
                finish();

                overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_right);
                dialog.dismiss();

            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                return;
            }
        });

        builder.show();
        builder.setCancelable(false);
    }

}