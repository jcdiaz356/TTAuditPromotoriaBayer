package com.dataservicios.ttauditpromotoriabayer;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dataservicios.ttauditpromotoriabayer.Repositories.UserRepo;
import com.dataservicios.ttauditpromotoriabayer.SQLite.DatabaseHelper;
import com.dataservicios.ttauditpromotoriabayer.util.ConexionInternet;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {
    // Logcat tag
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private int splashTime = 3000;
    private Thread thread;


    private ProgressBar mSpinner;
    private TextView tvCargando, tv_Version ;
    private ConexionInternet cnInternet ;
    private Activity MyActivity;
    //private JSONParser jsonParser;
    // Database Helper
   private DatabaseHelper db;
    private UserRepo userRepo ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyActivity = (Activity) this;
        mSpinner = (ProgressBar) findViewById(R.id.Splash_ProgressBar);
        mSpinner.setIndeterminate(true);
        tvCargando = (TextView) findViewById(R.id.tvCargando);
        tv_Version = (TextView) findViewById(R.id.tvVersion);

        db = new DatabaseHelper(getApplicationContext());
        userRepo = new UserRepo(getApplicationContext()) ;

        PackageInfo pckInfo ;
        try {
            pckInfo= getPackageManager().getPackageInfo(getPackageName(),0);
            tv_Version.setText(pckInfo.versionName);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        if (db.checkDataBase(MyActivity)){
            mSpinner = (ProgressBar) findViewById(R.id.Splash_ProgressBar);
            mSpinner.setIndeterminate(true);
            //thread = new Thread(runable);
            //thread.start();



        }else{
            userRepo.deleteAllUser();

            db.deleteAllAudits();

        }

       if(checkAndRequestPermissions()) loadLoginActivity();

    }


    private void loadLoginActivity()
    {
        Intent intent = new Intent(MyActivity, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public Runnable runable = new Runnable() {
        public void run() {
            try {
                Thread.sleep(splashTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                //startActivity(new Intent(MainActivity.this,LoginActivity.class));
                //Intent intent = new Intent(MainActivity.this,LoginActivity.class);
//                Intent intent = new Intent("com.dataservicios.systemauditor.LOGIN");
//
//                startActivity(intent);
//                finish();

                loadLoginActivity();
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    };



    //  Chequeando permisos de usuario Runtime
    private boolean checkAndRequestPermissions() {

        int locationPermission = ContextCompat.checkSelfPermission(MyActivity, Manifest.permission.ACCESS_FINE_LOCATION);
        int cameraPermission = ContextCompat.checkSelfPermission(MyActivity, Manifest.permission.CAMERA);
        int writeExternalStoragePermission = ContextCompat.checkSelfPermission(MyActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int callPhonePermission = ContextCompat.checkSelfPermission(MyActivity, Manifest.permission.CALL_PHONE);
        int readPhoneStatePermission = ContextCompat.checkSelfPermission(MyActivity, Manifest.permission.READ_PHONE_STATE);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }

        if (writeExternalStoragePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (callPhonePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CALL_PHONE);
        }

        if (readPhoneStatePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_PHONE_STATE);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(MyActivity,listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean respuestas = false ;
        if (requestCode == REQUEST_ID_MULTIPLE_PERMISSIONS) {


            if (grantResults.length > 0) {
                boolean permissionsApp = true ;
                for(int i=0; i < grantResults.length; i++) {
                    if(grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        //alertDialogBasico();
                        permissionsApp = false;
                        break;
                    }
                }

                if (permissionsApp==true)  loadLoginActivity();
                else alertDialogBasico();
            }
        }
    }

    public void alertDialogBasico() {

        // 1. Instancia de AlertDialog.Builder con este constructor
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);

        // 2. Encadenar varios métodos setter para ajustar las características del diálogo
        builder.setMessage(R.string.dialog_message_permission);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                System.exit(0);
            }
        });

        builder.show();

    }

}
