package com.dataservicios.ttauditpromotoriabayer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dataservicios.ttauditpromotoriabayer.Model.User;
import com.dataservicios.ttauditpromotoriabayer.Repositories.UserRepo;
import com.dataservicios.ttauditpromotoriabayer.util.AuditAlicorp;
import com.dataservicios.ttauditpromotoriabayer.util.Connectivity;
import com.dataservicios.ttauditpromotoriabayer.util.JSONParser;
import com.dataservicios.ttauditpromotoriabayer.util.SessionManager;

import java.util.List;


/**
 * Created by usuario on 05/11/2014.
 */
public class LoginActivity extends Activity implements View.OnClickListener {
    private Button ingresar;
    private EditText etUsuario,etPassword;
    //private DatabaseHelper db;
    private UserRepo userRepo ;
    private ProgressDialog pDialog;
    private Activity myActivity = (Activity) this;
    private SessionManager session;
    private JSONParser jsonParser = new JSONParser();
    private String userLogin, passwordLogin, simSNLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        ingresar = (Button) findViewById(R.id.btIngresar);
        etUsuario =   (EditText) findViewById(R.id.etUsuario);
        etPassword = (EditText) findViewById(R.id.etContrasena);

        etPassword.setText("123456");

        ingresar.setOnClickListener(this);
        session = new SessionManager(getApplicationContext());
        userRepo = new UserRepo(myActivity);

        if(Connectivity.isConnected(myActivity)) {
            if (Connectivity.isConnectedFast(myActivity)) {
                Toast.makeText(myActivity, "Conexion a internet rapida", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(myActivity, "Conexion a internet lenta", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(myActivity, "No hay conexión a internet", Toast.LENGTH_SHORT).show();
        }

        //db = new DatabaseHelper(getApplicationContext());
        if(userRepo.getUserCount() > 0) {
            //User users = new User();
            List<User> usersList = userRepo.getAllUser();
            if(usersList.size()>0) {
                User users = new User();
                users=usersList.get(0);
                etUsuario.setText(users.getEmail());

            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btIngresar:

                if (etUsuario.getText().toString().trim().equals("") )
                {
                    Toast toast = Toast.makeText(this, "Ingrese un Usuario", Toast.LENGTH_SHORT);
                    toast.show();
                    etUsuario.requestFocus();
                }else if (etPassword.getText().toString().trim().equals("")) {
                    Toast toast = Toast.makeText(this, "Ingrese una Contraseña ", Toast.LENGTH_SHORT);
                    toast.show();
                    etPassword.requestFocus();
                }else {

                    TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
                    tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    ///String imei = tm.getDeviceId();
                    //String imei1 = tm.getLine1Number();
                    simSNLogin = tm.getSimSerialNumber();
                    userLogin = etUsuario.getText().toString();
                    passwordLogin = etPassword.getText().toString();
                    new AttemptLogin().execute();
                }
                break;
        }
    }

    class AttemptLogin extends AsyncTask<Void, String, User> {

        /**
         * Antes de comenzar en el hilo determinado, Mostrar progresión
         * */
        boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage(getString(R.string.text_loading));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected User doInBackground(Void... params) {
            // TODO Auto-generated method stub

            User user = new User();
            user = AuditAlicorp.userLogin(userLogin, passwordLogin, simSNLogin);
            return user;

        }
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(User user) {
            // dismiss the dialog once product deleted

            if( user.getId() == 0) {

                Toast.makeText(myActivity, R.string.message_login_error, Toast.LENGTH_LONG).show();
            } else {

                userRepo.deleteAllUser();
                userRepo.createUser(user);
                session.createLoginSession(user.getName().toString(), user.getEmail(), String.valueOf(user.getId()));
                Intent i = new Intent(LoginActivity.this, PanelAdmin.class);
                startActivity(i);
                finish();

                Toast.makeText(myActivity, R.string.message_login_success, Toast.LENGTH_LONG).show();
            }

            pDialog.dismiss();


        }

    }
}
