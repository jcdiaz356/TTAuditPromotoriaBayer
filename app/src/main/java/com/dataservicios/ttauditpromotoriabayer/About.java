package com.dataservicios.ttauditpromotoriabayer;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dataservicios.ttauditpromotoriabayer.util.GlobalConstant;
import com.dataservicios.ttauditpromotoriabayer.util.JSONParserX;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


/**
 * Created by Jaime Eduardo on 13/11/2015.
 */
public class About extends Activity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private Activity MyActivity;
    private Button bt_Salir;
    private TextView tv_version,tv_version_android, tv_log;
    LinearLayout ly_ProgresBar;
    String cadena="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);

        MyActivity = (Activity) this;
        ly_ProgresBar = (LinearLayout) findViewById(R.id.lyProgresBar);

        tv_log = (TextView) findViewById(R.id.tvLog);
        tv_version = (TextView) findViewById(R.id.tvVersion);
        tv_version_android = (TextView) findViewById(R.id.tvVersionAndroid);
        bt_Salir = (Button) findViewById(R.id.cbSalir);


        tv_version_android.setText("SDK: " +  String.valueOf(Build.VERSION.SDK_INT + "(" +  String.valueOf(Build.MODEL) +  "  "  + " "  + String.valueOf(Build.BRAND) + " )"));

        PackageInfo pinfo;
        try {
            pinfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String versionName = pinfo.versionName;
            tv_version.setText("Ver. " + versionName);
            //ET2.setText(versionNumber);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // String textLog;
        //textLog =  String.valueOf(R.string.version_text) ;
        //tv_log.setText(R.string.version_text);
        //tv_log.setText(Html.fromHtml(textLog));
        bt_Salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        new Task().execute();
    }


    class Task extends AsyncTask<String, Integer, Boolean> {
        @Override
        protected void onPreExecute() {
            ly_ProgresBar.setVisibility(View.VISIBLE);
            // lv_lista.setVisibility(View.GONE);
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            ly_ProgresBar.setVisibility(View.GONE);
            tv_log.setText(cadena);
            // lv_lista.setVisibility(View.VISIBLE);
            //  adapter.notifyDataSetChanged();
            super.onPostExecute(result);
        }

        @Override
        protected Boolean doInBackground(String... params) {
//            stringValues.add("String 1");
//            stringValues.add("String 2");
//            stringValues.add("String 3");
//            stringValues.add("String 4");
//            stringValues.add("String 5");

//            try {
//                Thread.sleep(3000);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }


            jsonVersion(GlobalConstant.company_id,GlobalConstant.type_aplication);
            return null;
        }
    }

    private void jsonVersion(Integer company_id, String type) {
        int success;
        try {
            HashMap<String, String> params = new HashMap<>();


            params.put("company_id", String.valueOf(company_id));
            params.put("type", String.valueOf(type));


            JSONParserX jsonParser = new JSONParserX();
            // getting product details by making HTTP request
            JSONObject json = jsonParser.makeHttpRequest(GlobalConstant.dominio + "/versiones" ,"GET", params);
            // check your log for json response
            Log.d("Login attempt", json.toString());
            // json success, tag que retorna el json
            success = json.getInt("success");

            if (success == 1) {
                JSONArray ObjJson;
                ObjJson = json.getJSONArray("version");
                if(ObjJson.length() > 0) {
                    for (int i = 0; i < ObjJson.length(); i++) {
                        try {
                            JSONObject obj = ObjJson.getJSONObject(i);
                            String version = obj.getString("version");
                            String content = obj.getString("content");
                            String Fecha = obj.getString("created_at");


//                            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
//                            Date birthDate = sdf.parse(Fecha);

                            //cadena =   String.valueOf(birthDate) + " " +  version + " " + content + System.getProperty ("line.separator") + cadena;
                            cadena =   Fecha.substring(0,10) + "  (Ver. " +  version + ") " + content + System.getProperty ("line.separator") + cadena;
                           // tv_log.setText(String.valueOf(birthDate) + " " +  version + " " + content  );



                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }

                   // Log.d(LOG_TAG, String.valueOf(db.getAllAudits()));
                }
            }else{
                Log.d(LOG_TAG, json.getString("message"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
