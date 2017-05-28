package com.dataservicios.ttauditpromotoriabayer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.dataservicios.ttauditpromotoriabayer.Model.Audit;
import com.dataservicios.ttauditpromotoriabayer.SQLite.DatabaseHelper;
import com.dataservicios.ttauditpromotoriabayer.app.AppController;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Jaime on 13/10/2016.
 */
public class MercadoDetalle extends FragmentActivity {
    // private static final String URL_PDVS = "http://www.dataservicios.com/webservice/pdvs.php";
    private GoogleMap map;
    // Log tag
    private static final String LOG_TAG = MercadoDetalle.class.getSimpleName();
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
    private int idPDV, IdRuta, idCompany, level;
    private String fechaRuta;
    EditText pdvs1,pdvsAuditados1,porcentajeAvance1;
    TextView tvStoreId,tvTienda,tvDireccion ,tvDistrito, tvReferencia , tvPDVSdelDia ; //tvLong, tvLat;
    Button btCerrarAudit , btEditStore, btGuardarLatLong,btContinuar;
    Activity MyActivity = (Activity) this;


    private DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.mercado_detalle);
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
         btGuardarLatLong = (Button) findViewById(R.id.btGuardarLatLong);
        btContinuar = (Button) findViewById(R.id.btContinuar);

       // btCerrarAudit = (Button) findViewById(R.id.btCerrarAuditoria);
        //btEditStore = (Button) findViewById(R.id.btEditStore);
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        // name
        name_user = user.get(SessionManager.KEY_NAME);
        // email
        email_user = user.get(SessionManager.KEY_EMAIL);
        // id
        id_user = user.get(SessionManager.KEY_ID_USER);
        level = 0;

        Bundle bundle = getIntent().getExtras();
        IdRuta = bundle.getInt("idRuta");
        idPDV = bundle.getInt("store_id");
        //level = bundle.getInt("level");
        fechaRuta = bundle.getString("fechaRuta");


        db = new DatabaseHelper(getApplicationContext());

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Cargando...");
        pDialog.setCancelable(false);


        btGuardarLatLong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                paramsCordenadas = new JSONObject();
                try {
                    paramsCordenadas.put("latitud", lat);
                    paramsCordenadas.put("longitud", lon);
                    paramsCordenadas.put("id", idPDV);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                guardarCoordenadas();
            }
        });

        btContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bolsa = new Bundle();
                bolsa.putInt("idRuta", Integer.valueOf(IdRuta));
                bolsa.putInt("level", level);
                bolsa.putString("fechaRuta", fechaRuta);

                //Intent intent = new Intent( "com.dataservicios.ttauditpromotoriabayer.PUNTOSVENTA");
                Intent intent = new Intent(MyActivity,PuntosVenta.class);
                intent.putExtras(bolsa);
                startActivity(intent);
                finish();
                //Intent intent = new Intent(getActivity().getApplicationContext(),MercadoDetalle.class);
            }
        });


        linearLayout = (ViewGroup) findViewById(R.id.lyControles);

        params = new JSONObject();

        tvPDVSdelDia.setText(fechaRuta);


        try {
            params.put("id", idPDV);
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
        //cargarAditorias();

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
                        Log.d("DATAAAA", response.toString());
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
                                            tvStoreId.setText(String.valueOf(idPDV));
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
                        Log.d("DATAAAA", response.toString());
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
                                    int totalAudit = db.getCountAuditForIdForStoreId(Integer.valueOf(idAuditoria), Integer.valueOf(idPDV));
                                    List<Audit> audits1 = new ArrayList<Audit>();
                                    audits1=db.getAllAudits();
                                    if(totalAudit==0) {
                                        Audit audit = new Audit();
                                        audit.setId(Integer.valueOf(idAuditoria));
                                        audit.setName(auditoria);
                                        audit.setStore_id(idPDV);
                                        audit.setScore(0);
                                        db.createAudit(audit);
                                    }

                                    audits1=db.getAllAudits();

                                    int status = obj.getInt("state");

                                    Integer audit_id ;
                                    audit_id = Integer.valueOf(idAuditoria);
                                    // Solo para auditorias diferente del id 4 y 14
                                    if( (audit_id != 4 ) && (audit_id != 15)  ){
                                        bt = new Button(MyActivity);
                                        LinearLayout ly = new LinearLayout(MyActivity);
                                        ly.setOrientation(LinearLayout.VERTICAL);
                                        ly.setId(i+'_');
                                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                                LinearLayout.LayoutParams.FILL_PARENT,
                                                LinearLayout.LayoutParams.FILL_PARENT
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
                                        bt.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
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
                                                argRuta.putInt("store_id",idPDV);
                                                argRuta.putInt("rout_id", IdRuta );
                                                argRuta.putString("fechaRuta",fechaRuta);
                                                argRuta.putInt("audit_id",idAuditoria);


                                                switch (idAuditoria) {

                                                    case 3:
//                                                        intent = new Intent(MyActivity, PresenciaMaterial.class);
//                                                        intent.putExtras(argRuta);
//                                                        startActivity(intent);

                                                        break;
                                                    case 1:
//                                                        intent = new Intent(MyActivity, CategoriasSOD.class);
//                                                        intent.putExtras(argRuta);
//                                                        startActivity(intent);
//                                                        break;

                                                    case 37:
//                                                        intent = new Intent(MyActivity, ClientePerfecto.class);
//                                                        intent.putExtras(argRuta);
//                                                        startActivity(intent);
//                                                        break;

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

   // @Override
  //  public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            onBackPressed();
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
    //}
//    @Override
//    public void onBackPressed() {
//       // Toast.makeText(MyActivity, "No se puede volver atras, los datos ya fueron guardado, para modificar póngase en contácto con el administrador", Toast.LENGTH_LONG).show();
//
//    }

}
