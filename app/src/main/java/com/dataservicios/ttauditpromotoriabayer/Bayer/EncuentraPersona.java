package com.dataservicios.ttauditpromotoriabayer.Bayer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.dataservicios.ttauditpromotoriabayer.AndroidCustomGalleryActivity;
import com.dataservicios.ttauditpromotoriabayer.DetallePdv;
import com.dataservicios.ttauditpromotoriabayer.MainActivity;
import com.dataservicios.ttauditpromotoriabayer.Model.Audit;
import com.dataservicios.ttauditpromotoriabayer.Model.PollDetail;
import com.dataservicios.ttauditpromotoriabayer.R;
import com.dataservicios.ttauditpromotoriabayer.SQLite.DatabaseHelper;
import com.dataservicios.ttauditpromotoriabayer.util.AuditAlicorp;
import com.dataservicios.ttauditpromotoriabayer.util.GlobalConstant;
import com.dataservicios.ttauditpromotoriabayer.util.JSONParserX;
import com.dataservicios.ttauditpromotoriabayer.util.SessionManager;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Jaime on 19/03/2016.
 */
public class EncuentraPersona extends Activity {
    private Activity MyActivity = this ;
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private SessionManager session;

    private Switch swSiNo ;
    private Button bt_photo, bt_guardar;
    private EditText etComentario;
    private TextView tv_Pregunta;


    private String tipo,cadenaruc, fechaRuta, comentario="", type, region,typeBodega;

    private Integer user_id, company_id,store_id,rout_id,audit_id, poll_id;

    int  isSiNo=0 ;


    private DatabaseHelper db;

    private ProgressDialog pDialog;


    private PollDetail mPollDetail, pollDetail2;
    private Audit mAudit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.encuentra_persona);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("Persona");

        swSiNo = (Switch) findViewById(R.id.swSiNo);

        tv_Pregunta = (TextView) findViewById(R.id.tvPregunta);
        bt_guardar = (Button) findViewById(R.id.btGuardar);
        bt_photo = (Button) findViewById(R.id.btPhoto);
        //et_Comentario = (EditText) findViewById(R.id.etComentario);
        etComentario = (EditText) findViewById(R.id.etComentario);

        Bundle bundle = getIntent().getExtras();
        company_id = GlobalConstant.company_id;
        store_id = bundle.getInt("store_id");
        rout_id = bundle.getInt("rout_id");
        region = bundle.getString("region");
        fechaRuta = bundle.getString("fechaRuta");
        type = bundle.getString("type");
        typeBodega = bundle.getString("typeBodega");



        audit_id = GlobalConstant.audit_id[0]; //Inicio de auditoría Alicorp


        poll_id = GlobalConstant.poll_id[0] ;// 5 "Se encuentra Abierto el punto?"

        pDialog = new ProgressDialog(MyActivity);
        pDialog.setMessage("Cargando...");
        pDialog.setCancelable(false);

        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        // id
        user_id = Integer.valueOf(user.get(SessionManager.KEY_ID_USER)) ;

        //tv_Pregunta.setText("¿Se encuentra la persona ?");





        swSiNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isSiNo = 1;
//                    bt_photo.setVisibility(View.INVISIBLE);
//                    bt_photo.setEnabled(false);

                } else {
                    isSiNo = 0;

                }
            }
        });

        bt_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });

        bt_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!swSiNo.isChecked()){

                } else {

                }

                AlertDialog.Builder builder = new AlertDialog.Builder(MyActivity);
                builder.setTitle("Guardar Encuesta");
                builder.setMessage("Está seguro de guardar todas las encuestas: ");
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        comentario = String.valueOf(etComentario.getText()) ;

                        mPollDetail = new PollDetail();
                        mPollDetail.setPoll_id(poll_id);
                        mPollDetail.setStore_id(store_id);
                        mPollDetail.setSino(1);
                        mPollDetail.setOptions(0);
                        mPollDetail.setLimits(0);
                        mPollDetail.setMedia(0);
                        mPollDetail.setComment(1);
                        mPollDetail.setResult(isSiNo);
                        mPollDetail.setLimite("0");
                        mPollDetail.setComentario(comentario);
                        mPollDetail.setAuditor(user_id);
                        mPollDetail.setProduct_id(0);
                        mPollDetail.setCategory_product_id(0);
                        mPollDetail.setPublicity_id(0);
                        mPollDetail.setCompany_id(GlobalConstant.company_id);
                        mPollDetail.setCommentOptions(0);
                        mPollDetail.setSelectdOptions("");
                        mPollDetail.setSelectedOtionsComment("");
                        mPollDetail.setPriority("0");

                        new loadPoll().execute(mPollDetail);

                       // new loadPoll().execute();
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

    }

    private void takePhoto() {

        Intent i = new Intent( MyActivity, AndroidCustomGalleryActivity.class);
        Bundle bolsa = new Bundle();

        bolsa.putString("store_id", String.valueOf(store_id));
        bolsa.putString("product_id", String.valueOf("0"));
        bolsa.putString("publicities_id", String.valueOf("0"));
        bolsa.putString("poll_id", String.valueOf(poll_id));
        bolsa.putString("sod_ventana_id", String.valueOf("0"));
        bolsa.putString("company_id", String.valueOf(GlobalConstant.company_id));
        bolsa.putString("category_product_id", "0");
        bolsa.putString("monto","");
        bolsa.putString("razon_social","");
        bolsa.putString("url_insert_image", GlobalConstant.dominio + "/insertImagesProductPollAlicorp");
        bolsa.putString("tipo", "1");
        i.putExtras(bolsa);
        startActivity(i);
    }





    class loadPoll extends AsyncTask<PollDetail, Integer , Boolean> {
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
        protected Boolean doInBackground(PollDetail... params) {
            // TODO Auto-generated method stub

            PollDetail mPD = params[0] ;

            String time_close = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss").format(new Date());
            mAudit = new Audit();
            mAudit.setCompany_id(GlobalConstant.company_id);
            mAudit.setStore_id(store_id);
            mAudit.setId(audit_id);
            mAudit.setRoute_id(rout_id);
            mAudit.setUser_id(user_id);
            mAudit.setLatitude_close("");
            mAudit.setLongitude_close("");
            mAudit.setLatitude_open(String.valueOf(GlobalConstant.latitude_open));
            mAudit.setLongitude_open(String.valueOf(GlobalConstant.longitude_open));
            mAudit.setTime_open(GlobalConstant.inicio);
            mAudit.setTime_close(time_close);

            if(isSiNo == 1) {
                if(!AuditAlicorp.insertPollDetail(mPD)) return false;

            } else{
                if(!AuditAlicorp.insertPollDetail(mPD)) return false;

                if(!AuditAlicorp.closeAuditRoadStore(mAudit)) return false;
                // if(!AuditAlicorp.closeAuditRoadAll(mAudit)) return false;
            }


            return true;
        }
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(Boolean result) {
            // dismiss the dialog once product deleted

            if (result){
                // loadLoginActivity();
                if(isSiNo==1) {

                        Bundle argRuta = new Bundle();
                        argRuta.clear();
                        argRuta.putInt("idPDV", store_id);
                        argRuta.putString("fechaRuta", fechaRuta);
                        argRuta.putInt("idAuditoria", audit_id);
                        argRuta.putInt("rout_id", rout_id);
                        argRuta.putString("typeBodega", typeBodega);

                        Intent intent;
                        //intent = new Intent(MyActivity, Product.class);
                        //intent = new Intent(MyActivity, TipoDex.class);
                        intent = new Intent(MyActivity, RecibeCertificado.class);
                        intent.putExtras(argRuta);
                        startActivity(intent);
                        finish();

                } else if(isSiNo==0){
//
                    finish();
                }



            } else {
                Toast.makeText(MyActivity , "No se pudo guardar la información intentelo nuevamente", Toast.LENGTH_LONG).show();
            }
            hidepDialog();
        }
    }

    private Boolean InsertAuditPollsProduct(int store_id, int publicity_id, int poll_id, int status , int result, String options, String comentario , String comentario_options) {
        int success;
        try {

            HashMap<String, String> params = new HashMap<>();
            params.put("poll_id", String.valueOf(poll_id));
            params.put("store_id", String.valueOf(store_id));
            if (result == 1) {
                params.put("media", "0");
            } else if(result == 0){
                params.put("media", "1");
            }
            params.put("coment", "1");
            params.put("options", "1");
            params.put("opcion", options);
            params.put("sino", "1");
            params.put("comentario", String.valueOf(comentario));
            params.put("result", String.valueOf(result));
            params.put("company_id", String.valueOf(GlobalConstant.company_id));
            params.put("idroute", String.valueOf(rout_id));
            params.put("idaudit", String.valueOf(audit_id));
            params.put("user_id", String.valueOf(user_id));

            params.put("publicity_id", String.valueOf(publicity_id));
            params.put("coment_options", "0");
            params.put("comentario_options", comentario_options);


            params.put("status", String.valueOf(status));


            JSONParserX jsonParser = new JSONParserX();
            // getting product details by making HTTP request
            //JSONObject json = jsonParser.makeHttpRequest(GlobalConstant.dominio + "/JsonInsertPollsAlicorp" ,"POST", params);
            JSONObject json = jsonParser.makeHttpRequest(GlobalConstant.dominio + "/JsonInsertPollsAlicorp" ,"POST", params);
            // check your log for json response
            Log.d("Login attempt", json.toString());
            // json success, tag que retorna el json
            if (json == null) {
                Log.d("JSON result", "Está en nullo");
                return false;
            } else{
                success = json.getInt("success");
                if (success == 1) {
                   // Log.d(LOG_TAG, json.getString("Ingresado correctamente"));
                    Log.d(LOG_TAG, "Ingresado correctamente");

                }else{
                  //  Log.d(LOG_TAG, json.getString("message"));
                    // return json.getString("message");
                    Log.d(LOG_TAG, "Error al ingresar registro");
                   // return false;
                }
            }

        } catch (Exception e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
            e.printStackTrace();
            return  false;
        }

        return  true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
//                this.finish();
//                Intent a = new Intent(this,PanelAdmin.class);
//                //a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(a);
//                overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_right);
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
        //return super.onOptionsItemSelected(item);
    }




    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}
