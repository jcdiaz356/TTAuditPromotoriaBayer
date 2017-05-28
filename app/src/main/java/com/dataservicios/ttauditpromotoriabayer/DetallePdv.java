package com.dataservicios.ttauditpromotoriabayer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.dataservicios.ttauditpromotoriabayer.Bayer.EncuentraPersona;
import com.dataservicios.ttauditpromotoriabayer.Model.Audit;
import com.dataservicios.ttauditpromotoriabayer.SQLite.DatabaseHelper;
import com.dataservicios.ttauditpromotoriabayer.app.AppController;
import com.dataservicios.ttauditpromotoriabayer.util.AuditAlicorp;
import com.dataservicios.ttauditpromotoriabayer.util.GPSTracker;
import com.dataservicios.ttauditpromotoriabayer.util.GlobalConstant;
import com.dataservicios.ttauditpromotoriabayer.util.SessionManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


/**
 * Created by usuario on 14/01/2015.
 */
public class DetallePdv extends FragmentActivity {
    private static final String LOG_TAG = DetallePdv.class.getSimpleName();
    private GoogleMap map;
    // Log tag
    private static final String TAG = MainActivity.class.getSimpleName();
    private ViewGroup linearLayout;
    private Button bt;
    private double latitude ;
    private double longitude ;
    private double lat ;
    private double lon ;
    private Marker MarkerNow;
    private ProgressDialog pDialog;

    private JSONObject params,paramsCordenadas;
    private SessionManager session;
    private String email_user, id_user, name_user;
    private int store_id, IdRuta, idCompany;
    private String fechaRuta,typeBodega;
    EditText pdvs1,pdvsAuditados1,porcentajeAvance1;
    TextView tvStoreId,tvTienda,tvDireccion ,tvDistrito, tvReferencia , tvPDVSdelDia ; //tvLong, tvLat;
    Button btCerrarAudit; //, btEditStore; btGuardarLatLong,
    Activity MyActivity = (Activity) this;


    private DatabaseHelper db;

    Audit mAudit ;
    GPSTracker gpsTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.detalle_pdv);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("Detalle de PDV");
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
        session = new SessionManager(MyActivity);
        map =((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        pdvs1 = (EditText)  findViewById(R.id.etPDVS);
        pdvsAuditados1 = (EditText)  findViewById(R.id.etPDVSAuditados);
        porcentajeAvance1 = (EditText)  findViewById(R.id.etPorcentajeAvance);
        tvTienda = (TextView)  findViewById(R.id.tvTienda);
        tvStoreId = (TextView)  findViewById(R.id.tvStoreId);
        tvDireccion = (TextView)  findViewById(R.id.tvDireccion);
        tvReferencia = (TextView)  findViewById(R.id.tvReferencia);
        tvDistrito= (TextView)  findViewById(R.id.tvDistrito);
        tvPDVSdelDia = (TextView)  findViewById(R.id.tvPDVSdelDia);
        // btGuardarLatLong = (Button) findViewById(R.id.btGuardarLatLong);
        btCerrarAudit = (Button) findViewById(R.id.btCerrarAuditoria);
        //btEditStore = (Button) findViewById(R.id.btEditStore);
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        // name
        name_user = user.get(SessionManager.KEY_NAME);
        // email
        email_user = user.get(SessionManager.KEY_EMAIL);
        // id
        id_user = user.get(SessionManager.KEY_ID_USER);

        db = new DatabaseHelper(getApplicationContext());
        gpsTracker = new GPSTracker(MyActivity);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Cargando...");
        pDialog.setCancelable(false);



        btCerrarAudit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder = new AlertDialog.Builder(MyActivity);
                builder.setTitle("Guardar Encuesta");
                builder.setMessage("Está seguro de cerrar la auditoría: ");
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener()

                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        Calendar c = Calendar.getInstance();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String strDate = sdf.format(c.getTime());
                        GlobalConstant.fin = strDate;

                        new loadPoll().execute();

                        dialog.dismiss();

                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                    }
                });

                builder.show();
                builder.setCancelable(false);


            }
        });


        linearLayout = (ViewGroup) findViewById(R.id.lyControles);

        params = new JSONObject();

        Bundle bundle = getIntent().getExtras();
        store_id= bundle.getInt("store_id");
        IdRuta= bundle.getInt("idRuta");
        fechaRuta= bundle.getString("fechaRuta");
        typeBodega = bundle.getString("typeBodega");


        tvPDVSdelDia.setText(fechaRuta);


        try {
            params.put("id", store_id);
            params.put("idRoute", IdRuta);
            params.put("company_id", GlobalConstant.company_id);
            //Enviando

            params.put("iduser", id_user);

            //params.put("id_pdv",idPDV);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        cargaPdvs();
        //cargarAditorias();
        cargarAditorias();

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

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
    private void cargaPdvs(){
        showpDialog();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST , GlobalConstant.dominio + "/JsonRoadDetail" ,params,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        Log.d(LOG_TAG, response.toString());
                        //adapter.notifyDataSetChanged();
                        try {
                            //String agente = response.getString("agentes");
                            int success =  response.getInt("success");


                            if (success == 1) {
//
                                JSONArray ObjJson;
                                ObjJson = response.getJSONArray("roadsDetail");
                                // looping through All Products
                                if(ObjJson.length() > 0) {
                                    for (int i = 0; i < ObjJson.length(); i++) {
                                        try {
                                            JSONObject obj = ObjJson.getJSONObject(i);
                                            tvTienda.setText(obj.getString("fullname"));

                                            tvDireccion.setText(obj.getString("address"));
                                            tvDistrito.setText(obj.getString("district"));
                                            tvReferencia.setText(obj.getString("urbanization"));
                                            tvStoreId.setText(String.valueOf(store_id));
                                            //tvLat.setText(obj.getString("latitude"));
                                           // tvLong.setText(obj.getString("longitude"));
                                            latitude= Double.valueOf(obj.getString("latitude"))  ;
                                            longitude= Double.valueOf(obj.getString("longitude"));
                                            map.clear();
                                            map.setMyLocationEnabled(true);
                                            MarkerNow = map.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("Mi Ubicación")
                                                    //.snippet("Population: 4,137,400")
                                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_alicorp)));
                                            CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(latitude, longitude)).zoom(15).build();
                                            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                                            // Establezco un listener para ver cuando cambio de posicion
                                            map.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                                                public void onMyLocationChange(Location pos) {
                                                    // TODO Auto-generated method stub
                                                    // Extraigo la Lat y Lon del Listener
                                                    lat = pos.getLatitude();
                                                    lon = pos.getLongitude();
                                                    // Muevo la camara a mi posicion
                                                    //CameraUpdate cam = CameraUpdateFactory.newLatLng(new LatLng(lat, lon));
                                                    //map.moveCamera(cam);
                                                    // Notifico con un mensaje al usuario de su Lat y Lon
                                                    //Toast.makeText(MyActivity,"Lat: " + lat + "\nLon: " + lon, Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }

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
    }


    private void cargarAditorias(){
        showpDialog();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST ,  GlobalConstant.dominio + "/JsonAuditsForStore" ,params,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        Log.d(LOG_TAG, response.toString());
                        //adapter.notifyDataSetChanged();
                        try {
                            //String agente = response.getString("agentes");
                            int success =  response.getInt("success");
                            idCompany =response.getInt("company");
                            if (success == 1) {
                                JSONArray agentesObjJson;
                                //db.deleteAllAudits();
                                agentesObjJson = response.getJSONArray("audits");
                                // looping through All Products
                                for (int i = 0; i < agentesObjJson.length(); i++) {
                                    JSONObject obj = agentesObjJson.getJSONObject(i);
                                    // Storing each json item in variable
                                    String idAuditoria = obj.getString("id");
                                    String auditoria = obj.getString("fullname");

                                    //int totalAudit = db.getCountAuditForId(Integer.valueOf(idAuditoria));
                                    int totalAudit = db.getCountAuditForIdForStoreId(Integer.valueOf(idAuditoria), Integer.valueOf(store_id));
                                    List<Audit> audits1 = new ArrayList<Audit>();
                                    audits1=db.getAllAudits();
                                    if(totalAudit==0) {
                                        Audit audit = new Audit();
                                        audit.setId(Integer.valueOf(idAuditoria));
                                        audit.setName(auditoria);
                                        audit.setStore_id(store_id);
                                        audit.setScore(0);
                                        db.createAudit(audit);
                                    }

                                    audits1=db.getAllAudits();

                                    int status = obj.getInt("state");

                                    Integer audit_id ;
                                    audit_id = Integer.valueOf(idAuditoria);
                                    // Solo para auditorias diferente del id 4 y 14
                                    if(typeBodega.equals("6D") && (audit_id == 41) ) {

                                        break;
                                    };

                                    if( (audit_id != 4 ) && (audit_id != 42)  ){
                                            bt = new Button(MyActivity);
                                            LinearLayout ly = new LinearLayout(MyActivity);
                                            ly.setOrientation(LinearLayout.VERTICAL);
                                            ly.setId(i+'_');
                                            LayoutParams params = new LayoutParams(
                                                    LayoutParams.FILL_PARENT,
                                                    LayoutParams.FILL_PARENT
                                            );
                                            params.setMargins(0, 10, 0, 10);
                                            ly.setLayoutParams(params);
                                            bt.setBackgroundColor(getResources().getColor(R.color.color_base));
                                            bt.setTextColor(getResources().getColor(R.color.counter_text_bg));
                                            bt.setText(auditoria);


                                            if(status==1) {
                                                Drawable img = MyActivity.getResources().getDrawable( R.drawable.ic_check_on);
                                                img.setBounds( 0, 0, 60, 60 );  // set the image size
                                                bt.setCompoundDrawables( img, null, null, null );
                                                bt.setBackgroundColor(getResources().getColor(R.color.color_bottom_buttom_pressed));
                                                bt.setTextColor(getResources().getColor(R.color.color_base));
                                                bt.setEnabled(false);
                                            }  else {
                                                Drawable img = MyActivity.getResources().getDrawable( R.drawable.ic_check_off);
                                                img.setBounds( 0, 0, 60, 60 );  // set the image size
                                                bt.setCompoundDrawables( img, null, null, null );
                                            }
                                            if(GlobalConstant.global_close_audit==1){

                                                bt.setBackgroundColor(getResources().getColor(R.color.color_bottom_buttom_pressed));
                                                bt.setTextColor(getResources().getColor(R.color.color_base));
                                                bt.setEnabled(false);
                                            }
                                            //bt.setBackground();
                                            bt.setId(Integer.valueOf(idAuditoria));
                                            bt.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
                                            bt.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    // Toast.makeText(getActivity(), j  , Toast.LENGTH_LONG).show();
                                                    Button button1 = (Button) v;
                                                    String texto = button1.getText().toString();
                                                    //Toast toast=Toast.makeText(getActivity(), selected, Toast.LENGTH_SHORT);
                                                    Toast toast;
                                                    toast = Toast.makeText(MyActivity, texto + ":" + button1.getId(), Toast.LENGTH_LONG);
                                                    toast.show();
                                                    //int idBoton = Integer.valueOf(idAuditoria);
                                                    Intent intent;
                                                    int idAuditoria = button1.getId();

                                                    Bundle argRuta = new Bundle();
                                                    argRuta.clear();
                                                    argRuta.putInt("store_id",store_id);
                                                    argRuta.putInt("rout_id", IdRuta );
                                                    argRuta.putString("fechaRuta",fechaRuta);
                                                    argRuta.putInt("audit_id",idAuditoria);
                                                    argRuta.putString("typeBodega",typeBodega);

                                                    switch (idAuditoria) {

                                                        case 54:
                                                            intent = new Intent(MyActivity, EncuentraPersona.class);
                                                            intent.putExtras(argRuta);
                                                            startActivity(intent);

                                                            break;
                                                        case 40:
//                                                            intent = new Intent(MyActivity, PresenciaProducto.class);
//                                                            intent.putExtras(argRuta);
//                                                            startActivity(intent);
//                                                            break;

                                                        case 41:
//                                                            intent = new Intent(MyActivity, CategoriasCumpleCuotas.class);
//                                                            intent.putExtras(argRuta);
//                                                            startActivity(intent);
//                                                            break;

                                                    }
                                                }
                                            });
                                            ly.addView(bt);
                                            linearLayout.addView(ly);
                                    }

                                }
                                GlobalConstant.global_close_audit=0;
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

    }


    private void guardarCoordenadas(){

        showpDialog();


        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST , GlobalConstant.dominio+"/updatePositionStore" ,paramsCordenadas,
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
                                map.clear();
                                MarkerNow = map.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title("Mi Ubicación")
                                        //.snippet("Population: 4,137,400")
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_map)));
//        Mostrando información del Market ni bien carga el mapa
                                // MarkerNow.showInfoWindow();
//        Ajuste de la cámara en el mayor nivel de zoom es posible que incluya los límites
                                map.setMyLocationEnabled(true);
                                CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(lat, lon)).zoom(15).build();
                                map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                                //tvLat.setText(String.valueOf(lat));
                                //tvLong.setText(String.valueOf(lon));

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

    }

    private void insertaTiemporAuditoria(JSONObject parametros) {
        showpDialog();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST , GlobalConstant.dominio + "/insertaTiempo" ,parametros,
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
//                                Log.d("DATAAAA", response.toString());
//                                Toast.makeText(MyActivity, "Se ", Toast.LENGTH_LONG).show();
//
//
//                                Bundle argument = new Bundle();
//                                argument.clear();
//                                argument.putInt("idPDV",idPDV);
//
//                                Intent intent = new Intent("dataservicios.com.ttauditalicorp.PREMIACION");
//                                intent.putExtras(argument);
//                                startActivity(intent);

                                finish();
                            } else {
                                Toast.makeText(MyActivity, "No se ha podido enviar la información, intentelo mas tarde ", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(MyActivity, "No se ha podido enviar la información, intentelo mas tarde ", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
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


    }


    class loadPoll extends AsyncTask<Void , Integer , Boolean> {
        /**
         * Antes de comenzar en el hilo determinado, Mostrar progresión
         * */
        boolean failure = false;
        @Override
        protected void onPreExecute() {
            //tvCargando.setText("Cargando Product...");
            pDialog.show();
            super.onPreExecute();
        }
        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO Auto-generated method stub



            String time_close = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss").format(new Date());
            mAudit = new Audit();
            mAudit.setCompany_id(GlobalConstant.company_id);
            mAudit.setStore_id(store_id);
            mAudit.setId(0);
            mAudit.setRoute_id(IdRuta);
            mAudit.setUser_id(Integer.valueOf(id_user));
            mAudit.setLatitude_close(String.valueOf(gpsTracker.getLatitude()));
            mAudit.setLongitude_close(String.valueOf(gpsTracker.getLongitude()));
            mAudit.setLatitude_open(String.valueOf(GlobalConstant.latitude_open));
            mAudit.setLongitude_open(String.valueOf(GlobalConstant.longitude_open));
            mAudit.setTime_open(GlobalConstant.inicio);
            mAudit.setTime_close(time_close);


            if(!AuditAlicorp.closeAuditRoadAll(mAudit)) return false;


            return true;
        }
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(Boolean result) {
            // dismiss the dialog once product deleted

            if (result){
                finish();
            } else {
                Toast.makeText(MyActivity , "No se pudo guardar la información intentelo nuevamente", Toast.LENGTH_LONG).show();
            }
            hidepDialog();
        }
    }



    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    public void onRestart() {
        super.onRestart();
        //When BACK BUTTON is pressed, the activity on the stack is restarted
        //Do what you want on the refresh procedure here
        finish();
        startActivity(getIntent());
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
        Toast.makeText(MyActivity, "No se puede volver atras, los datos ya fueron guardado, para modificar póngase en contácto con el administrador", Toast.LENGTH_LONG).show();
//        super.onBackPressed();
//        this.finish();
//
//        overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_right);
    }

}
