package com.dataservicios.ttauditpromotoriabayer.Services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.dataservicios.ttauditpromotoriabayer.Model.PhoneDetail;
import com.dataservicios.ttauditpromotoriabayer.Model.User;
import com.dataservicios.ttauditpromotoriabayer.Repositories.UserRepo;
import com.dataservicios.ttauditpromotoriabayer.app.AppController;
import com.dataservicios.ttauditpromotoriabayer.util.AuditAlicorp;
import com.dataservicios.ttauditpromotoriabayer.util.Connectivity;
import com.dataservicios.ttauditpromotoriabayer.util.GPSTracker;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * Created by Jaime on 3/01/2017.
 */

public class MonitoGPSServices extends Service {
    private final String TAG = MonitoGPSServices.class.getSimpleName();
    private final Integer contador = 0;

    private Context context = this;

   static final int DELAY = 240000; //4 minutos de espera
    //static final int DELAY = 20000; //20 segundos
    private boolean runFlag = false;
    private Updater updater;
    private PhoneDetail m;

    private AppController application;
    private UserRepo userRepo;

    GPSTracker gpsTracker ;


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

        gpsTracker = new GPSTracker(context);
        m = new PhoneDetail();
        userRepo = new UserRepo(getApplicationContext());

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

            MonitoGPSServices monitoGPSServices = MonitoGPSServices.this;
            while (monitoGPSServices.runFlag) {
                Log.d(TAG, "UpdaterThread running");
                try{

                    int hour = Integer.valueOf(new SimpleDateFormat("k").format(new Date()));
                    if(hour > 8 || hour < 20){

                        if(Connectivity.isConnected(context)) {
                            if (Connectivity.isConnectedFast(context)) {

                                Log.i(TAG," Connectivity fast" );
                                //////////////////
                                double latitude = gpsTracker.getLatitude();
                                double longitude = gpsTracker.getLongitude();
                                String simSerialNumber;

                                try {
                                    TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
                                    // get IMEI
                                    String imei = tm.getDeviceId();
                                    String imei1 = tm.getLine1Number();
                                    // get SimSerialNumber
                                    simSerialNumber = tm.getSimSerialNumber();
                                } catch (Exception e){
                                    simSerialNumber = "";
                                    Log.e(TAG,"Error de permiso al obtener serial number" + String.valueOf(hour));
                                }


                                int user_id = 0;
                                if(userRepo.getUserCount() > 0) {
                                    //User users = new User();
                                    List<User> usersList = userRepo.getAllUser();
                                    if(usersList.size()>0) {
                                        User users = new User();
                                        users=usersList.get(0);
                                        user_id=users.getId();

                                    }
                                }

                                m.setLatitude(latitude);
                                m.setLongitude(longitude);
                                m.setPhone(simSerialNumber);
                                m.setSdk(String.valueOf(Build.VERSION.SDK_INT));
                                m.setUser_id(user_id);

                                boolean response = AuditAlicorp.savePhoneDetails(m);
                                if (response) {

                                    Log.i(TAG," Send location phone success " );
                                }

                            }else {
                                Log.i(TAG," Connectivity slow" );
                            }
                        } else {
                            Log.i(TAG," No internet connection" );
                        }

                    } else {
                        Log.i(TAG,"No se envía fuera del horario" + String.valueOf(hour));
                    }

                    Thread.sleep(DELAY);
                }catch(InterruptedException e){
                    monitoGPSServices.runFlag = false;
                    application.setServiceRunningFlag(true);
                }

            }
        }


    }
}

