package com.dataservicios.ttauditpromotoriabayer;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.dataservicios.ttauditpromotoriabayer.Model.Audit;
import com.dataservicios.ttauditpromotoriabayer.SQLite.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jaime Eduardo on 10/10/2015.
 */
public class Premiacion extends Activity {
    private Activity MyActivity;
    private Button bt_guardar, bt_registrar;
    private TextView tv_mensaje, tv_auditScore;
    private Integer pdv_id;
    private DatabaseHelper db;
    private List<Audit> audits = new ArrayList<Audit>() ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.premiacion);
        MyActivity = (Activity) this;

        bt_guardar = (Button) findViewById(R.id.btGuardar);
       // bt_registrar = (Button) findViewById(R.id.btRegistrar);
        tv_mensaje = (TextView) findViewById(R.id.tvMensaje);
        tv_auditScore = (TextView) findViewById(R.id.tvAuditScore);

        Bundle bundle = getIntent().getExtras();
        pdv_id = bundle.getInt("idPDV");

        db =  new DatabaseHelper(MyActivity);
        String mensaje ="";
        long total=0;
       // audits=db.getAllAudits();
        audits = db.getAllAuditsForStoreId(pdv_id);
        for (Audit audit : audits) {
            mensaje = mensaje + audit.getName() + " : " + String.valueOf(audit.getScore())  +  Html.fromHtml("<br />");
            total =+ (long) audit.getScore();
        }

        tv_auditScore.setText(mensaje);
        tv_mensaje.setText(String.valueOf(total) + " Pts.");
        bt_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //db.deleteAllAudits();
                db.deleteAllAuditForStoreId(pdv_id);
                finish();

            }
        });

//        bt_registrar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                //Creamos el Intent para llamar a la Camara
//                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                //Creamos una carpeta en la memeria del terminal
//                File imagesFolder = new File( Environment.getExternalStorageDirectory(), "jaiem-fotos-colgate");
//                imagesFolder.mkdirs();
//                //añadimos el nombre de la imagen
//                File image = new File(imagesFolder, "foto.jpg");
//                Uri uriSavedImage = Uri.fromFile(image);
//                //Le decimos al Intent que queremos grabar la imagen
//                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
//                //Lanzamos la aplicacion de la camara con retorno (forResult)
//                startActivityForResult(cameraIntent, 1);
//
//            }
//        });
    }




}
