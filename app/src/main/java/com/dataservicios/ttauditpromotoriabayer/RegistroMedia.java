package com.dataservicios.ttauditpromotoriabayer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dataservicios.ttauditpromotoriabayer.Model.Media;
import com.dataservicios.ttauditpromotoriabayer.Repositories.MediaRepo;
import com.dataservicios.ttauditpromotoriabayer.SQLite.DatabaseHelper;
import com.dataservicios.ttauditpromotoriabayer.adapter.MediaAdapter;
import com.dataservicios.ttauditpromotoriabayer.util.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Jaime on 29/08/2016.
 */
public class RegistroMedia extends Activity {
    private Activity MyActivity = this ;
    private static final String LOG_TAG = RegistroMedia.class.getSimpleName();
    private SessionManager session;
    private ListView listView;
    private MediaAdapter adapter;
    private DatabaseHelper db;
    private MediaRepo mr ;
    private ProgressDialog pDialog;
    private List<Media> mediaList = new ArrayList<Media>();

    private String tipo,cadenaruc,fechaRuta;
    private Integer user_id, company_id,store_id,rout_id,audit_id;

    private TextView tv_contador;
    private Button bt_finalizar;

    private int awards = 0 ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.registro_media);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        //getActionBar().setTitle("Productos");

        tv_contador = (TextView) findViewById(R.id.tvContador);
        //bt_finalizar = (Button) findViewById(R.id.btFinalizar);

//        Bundle bundle = getIntent().getExtras();
//        company_id = bundle.getInt("company_id");
//        store_id = bundle.getInt("idPDV");
//        tipo = bundle.getString("tipo");
//        cadenaruc = bundle.getString("cadenaruc");
//        rout_id = bundle.getInt("idRuta");
//        fechaRuta = bundle.getString("fechaRuta");
//        audit_id = bundle.getInt("idAuditoria");


        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        // id
        user_id = Integer.valueOf(user.get(SessionManager.KEY_ID_USER)) ;


        pDialog = new ProgressDialog(MyActivity);
        pDialog.setMessage("Cargando...");
        pDialog.setCancelable(false);

        db = new DatabaseHelper(getApplicationContext());
        mr = new MediaRepo(MyActivity);

        int total_products = mr.getAllMedias().size();
        tv_contador.setText(String.valueOf(total_products));

        listView = (ListView) findViewById(R.id.listProducts);
        mediaList =  mr.getAllMedias();
        // adapter = new PublicityAdapter(this, db.getAllPublicity());
        adapter = new MediaAdapter(MyActivity,  mediaList);
        listView.setAdapter(adapter);
        Log.d(LOG_TAG, String.valueOf(mr.getAllMedias()));
        adapter.notifyDataSetChanged();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                // selected item
                String product_id = ((TextView) view.findViewById(R.id.tvId)).getText().toString();
                Toast toast = Toast.makeText(getApplicationContext(), product_id, Toast.LENGTH_SHORT);
                toast.show();


            }

        });



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

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
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
}

