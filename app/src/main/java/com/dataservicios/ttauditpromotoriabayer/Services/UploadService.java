package com.dataservicios.ttauditpromotoriabayer.Services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;

import com.dataservicios.ttauditpromotoriabayer.AlbumStorageDirFactory;
import com.dataservicios.ttauditpromotoriabayer.Model.Media;
import com.dataservicios.ttauditpromotoriabayer.Repositories.MediaRepo;
import com.dataservicios.ttauditpromotoriabayer.util.GlobalConstant;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

//Subida de Archivos


public class  UploadService extends IntentService {
    private static final String LOG_TAG = UploadService.class.getSimpleName();
    public static final int NOTIFICATION_ID=1;
    private int totalMessages = 0;
    private NotificationManager mNotificationManager;
    private Notification notification;
    private Context context = this;

    ArrayList<String> names_file = new ArrayList<String>();
   // private static final String url_upload_image = GlobalConstant.dominio + "/uploadImagesAudit";
    private String url_insert_image ;
    private AlbumStorageDirFactory mAlbumStorageDirFactory = null;
    String store_id,publicities_id,invoices_id,tipo,product_id,sod_ventana_id, company_id, poll_id,category_product_id,monto,razon_social;

    public UploadService(String name) {
        super(name);
    }
    public UploadService(){
        super("UploadService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        names_file =intent.getStringArrayListExtra("names_file");
        store_id=intent.getStringExtra("store_id");

        publicities_id=intent.getStringExtra("publicities_id");
        product_id=intent.getStringExtra("product_id");
        poll_id=intent.getStringExtra("poll_id");
        company_id = intent.getStringExtra("company_id");
        category_product_id =intent.getStringExtra("category_product_id");
        sod_ventana_id = intent.getStringExtra("sod_ventana_id");
        url_insert_image=intent.getStringExtra("url_insert_image");
        monto = intent.getStringExtra("monto");
        razon_social = intent.getStringExtra("razon_social");
        tipo=intent.getStringExtra("tipo");

        for (int i = 0; i < names_file.size(); i++) {
            String foto = names_file.get(i);
            //String pathFile =getAlbumDirTemp().getAbsolutePath() + "/" + foto ;
            String created_at = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss").format(new Date());
            MediaRepo mr = new MediaRepo(context);
            Media m = new Media();
            m.setStore_id(Integer.valueOf(store_id));
            m.setPoll_id(Integer.valueOf(poll_id));
            m.setCompany_id(GlobalConstant.company_id);
            m.setCategory_product_id(Integer.valueOf(category_product_id));
            m.setPublicity_id(Integer.valueOf(publicities_id));
            m.setMonto(monto);
            m.setRazonSocial(razon_social);
            m.setCreated_at(created_at);
            m.setType(1);
            m.setFile(foto);
            mr.insert(m);
        }
    }



}
