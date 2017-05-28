package com.dataservicios.ttauditpromotoriabayer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dataservicios.ttauditpromotoriabayer.SQLite.DatabaseHelper;
import com.dataservicios.ttauditpromotoriabayer.util.GlobalConstant;
import com.dataservicios.ttauditpromotoriabayer.util.JSONParserX;
import com.dataservicios.ttauditpromotoriabayer.util.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Jaime on 28/06/2016.
 */
public class EditStore extends Activity {
    private static final String LOG_TAG = EditStore.class.getSimpleName();
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private Integer store_id,   user_id ;
    private Button btGuardar, btCancelar;
    private DatabaseHelper db ;
    private Activity MyActivity = this ;
    private String direccion,referencia,comentario, userEmail, storeName, userName;
    private TextView tvCategoria;
    private EditText etDireccion, etReferencia, etComentario;
    private ProgressDialog pDialog;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_store);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        etDireccion = (EditText) findViewById(R.id.etDireccion);
        etReferencia = (EditText) findViewById(R.id.etReferencia);
        etComentario = (EditText) findViewById(R.id.etComentario);
        btGuardar = (Button) findViewById(R.id.btGuardar);
        btCancelar = (Button) findViewById(R.id.btCancelar);

        db = new DatabaseHelper(getApplicationContext());

        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        // id
        user_id = Integer.valueOf(user.get(SessionManager.KEY_ID_USER)) ;
        userEmail = String.valueOf(user.get(SessionManager.KEY_EMAIL));
        userName = String.valueOf(user.get(SessionManager.KEY_NAME));


        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Cargando...");
        pDialog.setCancelable(false);

        Bundle bundle = getIntent().getExtras();
        store_id = bundle.getInt("store_id");
        direccion = bundle.getString("direccion");
        referencia = bundle.getString("referencia");
        storeName = bundle.getString("storeName");

        etReferencia.setText(referencia);
        etDireccion.setText(direccion);


        btGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MyActivity);
                builder.setTitle("Guardar Ventana");
                builder.setMessage("Está seguro de guardar todas los datos: ");
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        direccion = etDireccion.getText().toString();
                        referencia = etReferencia.getText().toString();
                        comentario = etComentario.getText().toString();
                        new ejecutarConsulta().execute();
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

        btCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MyActivity);
                builder.setTitle("Guardar Ventana");
                builder.setMessage("Está seguro que desea salir sin guardar ");
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        finish();

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



    class ejecutarConsulta extends AsyncTask<Void, Integer ,JSONObject> {
        @Override
        protected void onPreExecute() {
            showpDialog();
            super.onPreExecute();
        }
        @Override
        protected JSONObject doInBackground(Void... params) {
            try {
                HashMap<String, String> paramsData = new HashMap<>();
                paramsData.put("direccion", direccion);
                paramsData.put("referencia", referencia);
                paramsData.put("comentario", comentario);
                paramsData.put("storeName", storeName);
                paramsData.put("userName", userName);
                paramsData.put("user_id", String.valueOf(user_id));
                paramsData.put("store_id", String.valueOf(store_id));
                //paramsData.put("store_id","99999999");
                paramsData.put("company_id", String.valueOf(GlobalConstant.company_id));
                Log.d("request", "starting");
                JSONParserX jsonParser = new JSONParserX();
                // getting product details by making HTTP request
                //JSONObject json = jsonParser.makeHttpRequest(GlobalConstant.dominio + "/json/prueba.json", "POST", paramsData);
                JSONObject json = jsonParser.makeHttpRequest(GlobalConstant.dominio + "/JsonUpdateStoreAddress", "POST", paramsData);

                if (json != null) {
                    Log.d("JSON result", "");
                    return json;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(JSONObject json) {
            int success = 0;
            String message = "";
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }
            if (json != null) {
                //Toast.makeText(MyActivity, json.toString(), Toast.LENGTH_LONG).show();

                try {
                    success = json.getInt(TAG_SUCCESS);
                    if (success > 0) {
                        Toast.makeText(MyActivity, "Se guradó Correctamente los datos", Toast.LENGTH_LONG).show();
                        MyActivity.finish();
                    } else {
                        Toast.makeText(MyActivity, "No se puede actualizar, error al actualizar los datos", Toast.LENGTH_LONG).show();
                        // message = json.getString(TAG_MESSAGE);

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(MyActivity, "No se ha recibido respuesta del servidor", Toast.LENGTH_LONG).show();
            }

            if (success == 1) {
                Log.d("Success!", message);
            }else{
                Log.d("Failure", message);
            }
            hidepDialog();
        }
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
            //Toast.makeText(MyActivity, "No se puede volver atras, los datos ya fueron guardado, para modificar pongase en contácto con el administrador", Toast.LENGTH_LONG).show();
            //onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
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
