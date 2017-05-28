package com.dataservicios.ttauditpromotoriabayer.Services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.dataservicios.ttauditpromotoriabayer.Model.Media;
import com.dataservicios.ttauditpromotoriabayer.Repositories.MediaRepo;
import com.dataservicios.ttauditpromotoriabayer.app.AppController;
import com.dataservicios.ttauditpromotoriabayer.util.AuditAlicorp;
import com.dataservicios.ttauditpromotoriabayer.util.Connectivity;

/**
 * Created by Jaime on 25/08/2016.
 */
public class UpdateServices extends Service {
    private final String TAG = UpdateServices.class.getSimpleName();
    private final Integer contador = 0;

    private Context context = this;

    static final int DELAY = 120000; //2 minutos de espera
    //static final int DELAY = 9000; //9 segundo de espera
    private boolean runFlag = false;
    private Updater updater;

    private AppController application;

    private MediaRepo mediaRepo;
    private Media m;
    private AuditAlicorp auditAlicorp;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        application = (AppController) getApplication();
        updater = new Updater();
        mediaRepo = new MediaRepo(this);
        m = new Media();
        auditAlicorp = new AuditAlicorp(context);
        Log.d(TAG, "onCreated");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        runFlag = false;
        application.setServiceRunningFlag(false);
        updater.interrupt();
        updater = null;

        Log.d(TAG, "onDestroyed");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(!runFlag){
            runFlag = true;
            application.setServiceRunningFlag(true);
            updater.start();
        }

        Log.d(TAG, "onStarted");
        return START_STICKY;
    }

    private class Updater extends Thread {
        public Updater(){
            super("UpdaterService-UpdaterThread");
        }


        @Override
        public void run() {

            UpdateServices updaterService = UpdateServices.this;
            while (updaterService.runFlag) {
                Log.d(TAG, "UpdaterThread running");
                try{

//                    int hour = Integer.valueOf(new SimpleDateFormat("k").format(new Date()));
//                    if(hour < 6 || hour > 12){

                        if(Connectivity.isConnected(context)) {
                            if (Connectivity.isConnectedFast(context)) {

                                Log.i(TAG," Connectivity fast" );
                                m = mediaRepo.getFirstMedia();
                                if(m.getId() != 0){
                                    // Toast.makeText(context,"Segundo plano",Toast.LENGTH_SHORT);
                                    boolean response = auditAlicorp.uploadMedia(m,1);
                                    if (response) {
                                        mediaRepo.deleteForId(m.getId());
                                        Log.i(TAG," Send success images database server and delete local database and file " );
                                    }
                                } else {
                                    Log.i(TAG, "No found records in media table for send");
                                }

                            }else {
                                Log.i(TAG," Connectivity slow" );
                            }
                        } else {
                            Log.i(TAG," No internet connection" );
                        }

//                        Log.i(TAG,"Se está enviando la foto " + String.valueOf(hour));
//                    } else {
//                        Log.i(TAG,"No se envía fuera del horario" + String.valueOf(hour));
//                    }







                    Thread.sleep(DELAY);
                }catch(InterruptedException e){
                    updaterService.runFlag = false;
                    application.setServiceRunningFlag(true);
                }

            }
        }


    }
}
