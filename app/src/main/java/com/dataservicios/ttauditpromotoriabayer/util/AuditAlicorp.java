package com.dataservicios.ttauditpromotoriabayer.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;

import com.dataservicios.ttauditpromotoriabayer.Model.Audit;
import com.dataservicios.ttauditpromotoriabayer.Model.Media;
import com.dataservicios.ttauditpromotoriabayer.Model.Pdv;
import com.dataservicios.ttauditpromotoriabayer.Model.PhoneDetail;
import com.dataservicios.ttauditpromotoriabayer.Model.PollDetail;
import com.dataservicios.ttauditpromotoriabayer.Model.User;

import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by Jaime on 28/08/2016.
 */
public class AuditAlicorp {
    public static final String LOG_TAG = AuditAlicorp.class.getSimpleName();
    //private AlbumStorageDirFactory mAlbumStorageDirFactory = null;
    private Context context;

    public AuditAlicorp(Context context) {
        this.context = context ;
    }

    /**
     * uploadMedia send media
     * @param media Objeto media
     * @param typeSend Type send: 0 = imagen para preguntas, 1 = imagen para publicitis,  2 = imagen para soad
     * @return
     */
    public boolean uploadMedia(Media media, int typeSend){
        HttpURLConnection httpConnection = null;
        final String url_upload_image = GlobalConstant.dominio + "/insertImagesMayorista";

        String imag = media.getFile();
        int id = media.getId();
        int company_id = media.getCompany_id();
        int poll_id = media.getPoll_id();
        int product_id = media.getProduct_id();
        int publicity_id = media.getPublicity_id();
        int category_product_id = media.getCategory_product_id();
        int store_id= media.getStore_id();
        int type = media.getType();
        String monto = media.getMonto();
        String razon_social = media.getRazonSocial();
        String hora_sistema = media.getCreated_at();
        String created_at = media.getCreated_at();

        File file = new File(BitmapLoader.getAlbumDirTemp(context).getAbsolutePath() + "/" + imag);
        if(!file.exists()){
            return true;
        }
        Bitmap bbicon = null;
        Bitmap scaledBitmap;
        bbicon = BitmapLoader.loadBitmap(file.getAbsolutePath(),300,300);

//        if(Build.MODEL.equals("MotoG3")){
//            scaledBitmap = FileImagenManager.rotateImage(FileImagenManager.scaleDown(bbicon, 450 , true),0);
//        } else {
//            scaledBitmap = FileImagenManager.rotateImage(FileImagenManager.scaleDown(bbicon, 450 , true),90);
//        }

        if(Build.MODEL.equals("MotoG3")){
            //scaledBitmap = BitmapLoader.scaleDown() BitmapLoader.rotateImage(bbicon,0);
            scaledBitmap = BitmapLoader.rotateImage(BitmapLoader.scaleDown(bbicon, 540 , true),0);
        } else {
            //scaledBitmap = BitmapLoader.rotateImage(bbicon,90);
            scaledBitmap = BitmapLoader.rotateImage(BitmapLoader.scaleDown(bbicon, 540 , true),90);
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        scaledBitmap.compress(Bitmap.CompressFormat.JPEG,90, bos);
        //bbicon.compress(Bitmap.CompressFormat.JPEG,100, bos);

        try {
            ContentBody foto = new ByteArrayBody(bos.toByteArray(), file.getName());
            //ContentBody foto = new ByteArrayBody(bos.toByteArray(), file.getName());
            //MultipartEntity mpEntity = new MultipartEntity();
            AndroidMultiPartEntity mpEntity = new AndroidMultiPartEntity(new AndroidMultiPartEntity.ProgressListener() {
                @Override
                public void transferred(long num) {
                    //notification.contentView.setProgressBar(R.id.progressBar1, 100,(int) ((num / (float) totalSize) * 100), true);
                    // notificationManager.notify(1, notification);
                }
            });

            mpEntity.addPart("fotoUp", foto);
            mpEntity.addPart("archivo", new StringBody(String.valueOf(file.getName())));
            mpEntity.addPart("store_id", new StringBody(String.valueOf(store_id)));
            mpEntity.addPart("product_id", new StringBody(String.valueOf(product_id)));
            mpEntity.addPart("poll_id", new StringBody(String.valueOf(poll_id)));
            mpEntity.addPart("publicities_id", new StringBody(String.valueOf(publicity_id)));
            mpEntity.addPart("category_product_id", new StringBody(String.valueOf(category_product_id)));
            mpEntity.addPart("company_id", new StringBody(String.valueOf(company_id)));
            mpEntity.addPart("tipo", new StringBody(String.valueOf(type)));
            mpEntity.addPart("monto", new StringBody(String.valueOf(monto)));
            mpEntity.addPart("razon_social", new StringBody(String.valueOf(razon_social)));
            mpEntity.addPart("horaSistema", new StringBody(String.valueOf(hora_sistema)));

            URL url = new URL(url_upload_image);
            httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setReadTimeout(10000);
            httpConnection.setConnectTimeout(15000);
            httpConnection.setRequestMethod("POST");
            httpConnection.setUseCaches(false);
            httpConnection.setDoInput(true);
            httpConnection.setDoOutput(true);
            httpConnection.setRequestProperty("Connection", "Keep-Alive");
            httpConnection.addRequestProperty("Content-length", mpEntity.getContentLength()+"");
            httpConnection.addRequestProperty(mpEntity.getContentType().getName(), mpEntity.getContentType().getValue());
            OutputStream os = httpConnection.getOutputStream();
            mpEntity.writeTo(httpConnection.getOutputStream());
            os.close();
            httpConnection.connect();

            if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                Log.d("UPLOAD", "HTTP 200 OK." + httpConnection.getResponseCode()+" "+httpConnection.getResponseMessage()+".");
                //return readStream(httpConnection.getInputStream());
                //This return returns the response from the upload.
                return  true ;

            } else {
                Log.d("UPLOAD", "HTTP "+httpConnection.getResponseCode()+" "+httpConnection.getResponseMessage()+".");
                // String stream =  readStream(
                // );
                // Log.d("UPLOAD", "Response: "+stream);
                // return stream;
                return  false ;
            }

            //resp.getEntity().consumeContent();
            //return true;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return  false ;
        }finally {
            if(httpConnection != null){
                httpConnection.disconnect();
            }
        }
        //return true;
    }


    public static ArrayList<Pdv> getLisStores(int rout_id, int company_id, int level){
        int success ;

        ArrayList<Pdv> listaPdv = new ArrayList<Pdv>();
        try {

            HashMap<String, String> params = new HashMap<>();

            params.put("id", String.valueOf(rout_id));
            params.put("company_id", String.valueOf(company_id));
            params.put("nivel", String.valueOf(level));

            JSONParserX jsonParser = new JSONParserX();
            // getting product details by making HTTP request
            JSONObject json = jsonParser.makeHttpRequest(GlobalConstant.dominio + "/JsonRoadsAlicorpMayorista" ,"POST", params);
            // check your log for json response
            Log.d("Login attempt", json.toString());
            // json success, tag que retorna el json
            if (json == null) {
                Log.d("JSON result", "Está en nullo");

            } else{
                success = json.getInt("success");
                if (success == 1) {
                    JSONArray ObjJson;
                    ObjJson = json.getJSONArray("roadsDetail");
                    // looping through All Products
                    if(ObjJson.length() > 0) {

                        for (int i = 0; i < ObjJson.length(); i++) {

                                JSONObject obj = ObjJson.getJSONObject(i);
                                Pdv pdv = new Pdv();
                                pdv.setId(Integer.valueOf(obj.getString("id")));
                                pdv.setPdv(obj.getString("fullname"));
                                //pdv.setThumbnailUrl(obj.getString("image"));
                                pdv.setDireccion(obj.getString("address"));
                                pdv.setDistrito(obj.getString("district"));
                                pdv.setType(obj.getString("type"));
                                pdv.setRegion(obj.getString("region"));
                                pdv.setTypeBodega(obj.getString("tipo_bodega"));
                                pdv.setStatus(Integer.valueOf(obj.getString("status")));

//
                                // adding movie to movies array
                                listaPdv.add(i,pdv);
                            }

                        }
                    Log.d(LOG_TAG, "Ingresado correctamente");
                }else{
                    Log.d(LOG_TAG, "No se ingreso el registro");
                    //return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
           // return false;
        }
        return  listaPdv;
    }


    public static Pdv getStore(int store_id){
        int success ;

        Pdv store = new Pdv();
        try {

            HashMap<String, String> params = new HashMap<>();

            params.put("id", String.valueOf(store_id));

            JSONParserX jsonParser = new JSONParserX();
            // getting product details by making HTTP request
            JSONObject json = jsonParser.makeHttpRequest(GlobalConstant.dominio + "/JsonRoadDetail" ,"POST", params);
            // check your log for json response
            Log.d("Login attempt", json.toString());
            // json success, tag que retorna el json
            if (json == null) {
                Log.d(LOG_TAG, "Está en nullo");

            } else{
                success = json.getInt("success");
                if (success == 1) {
                    JSONArray ObjJson;
                    ObjJson = json.getJSONArray("roadsDetail");
                    // looping through All Products
                    if(ObjJson.length() > 0) {

                        for (int i = 0; i < ObjJson.length(); i++) {

                            JSONObject obj = ObjJson.getJSONObject(i);
                            Pdv pdv = new Pdv();
                            pdv.setId(Integer.valueOf(obj.getString("id")));
                            pdv.setPdv(obj.getString("fullname"));
                            //pdv.setThumbnailUrl(obj.getString("image"));
                            pdv.setDireccion(obj.getString("address"));
                            pdv.setDistrito(obj.getString("district"));
                            pdv.setType(obj.getString("type"));
                            pdv.setRegion(obj.getString("region"));
                            pdv.setTypeBodega(obj.getString("tipo_bodega"));
                            pdv.setStatus(Integer.valueOf(obj.getString("status")));

                            store = pdv;

//
                            // adding movie to movies array
                            //listaPdv.add(i,pdv);
                        }

                    }
                    Log.d(LOG_TAG, "Lista de Store");
                }else{
                    Log.d(LOG_TAG, "No Hay datos");
                    //return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
             return store;
        }
        return  store;
    }

    /**
     *
     * @param audit
     * @return
     */
    public static boolean closeAuditRoadStore(Audit audit) {

        int success;
        try {

            HashMap<String, String> params = new HashMap<>();

            params.put("audit_id"       , String.valueOf(audit.getId()));
            params.put("store_id"       , String.valueOf(audit.getStore_id()));
            params.put("company_id"     , String.valueOf(audit.getCompany_id()));
            params.put("rout_id"        , String.valueOf(audit.getRoute_id()));

            JSONParserX jsonParser = new JSONParserX();
            // getting product details by making HTTP request
            JSONObject json = jsonParser.makeHttpRequest(GlobalConstant.dominio + "/closeAudit" ,"POST", params);
            // check your log for json response
            Log.d("Login attempt", json.toString());

            // json success, tag que retorna el json

            if (json == null) {
                Log.d("JSON result", "Está en nullo");
                return false;
            } else{
                success = json.getInt("success");
                if (success == 1) {
                    Log.d(LOG_TAG, "Se insertó registro correctamente");
                }else{
                    Log.d(LOG_TAG, "no insertó registro");
                    // return json.getString("message");
                    // return false;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;

    }

    /**
     *
     * @param audit
     * @return
     */
    public static boolean closeAuditRoadAll(Audit audit) {


        int success;
        try {


            HashMap<String, String> paramsData = new HashMap<>();

            paramsData.put("latitud_close", audit.getLatitude_close());
            paramsData.put("longitud_close", audit.getLongitude_close());
            paramsData.put("latitud_open", audit.getLatitude_open());
            paramsData.put("longitud_open",  audit.getLongitude_open());
            paramsData.put("tiempo_inicio",  audit.getTime_open());
            paramsData.put("tiempo_fin",  audit.getTime_close());
            paramsData.put("tduser", String.valueOf(audit.getUser_id()));
            paramsData.put("id", String.valueOf(audit.getStore_id()));
            paramsData.put("idruta", String.valueOf(audit.getRoute_id()));
            paramsData.put("company_id", String.valueOf(GlobalConstant.company_id));
            Log.d("request", "starting");
            JSONParserX jsonParser = new JSONParserX();
            // getting product details by making HTTP request
            //JSONObject json = jsonParser.makeHttpRequest(GlobalConstant.dominio + "/json/prueba.json", "POST", paramsData);
            JSONObject json = jsonParser.makeHttpRequest(GlobalConstant.dominio + "/insertaTiempoNew", "POST", paramsData);
            if (json == null) {
                Log.d("JSON result", "Está en nullo");
                return false;
            } else{
                success = json.getInt("success");
                if (success == 1) {
                    Log.d(LOG_TAG, "Se insertó registro correctamente");
                }else{
                    Log.d(LOG_TAG, "no insertó registro");
                    // return json.getString("message");
                    // return false;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;

    }

    public static boolean insertPollDetail(PollDetail pollDetail) {
        int success;
        try {

            HashMap<String, String> params = new HashMap<>();

            params.put("poll_id"                , String.valueOf(pollDetail.getPoll_id()));
            params.put("store_id"               , String.valueOf(pollDetail.getStore_id()));
            params.put("sino"                   , String.valueOf(pollDetail.getSino()));
            params.put("options"                , String.valueOf(pollDetail.getOptions()));
            params.put("limits"                 , String.valueOf(pollDetail.getLimits()));
            params.put("media"                  , String.valueOf(pollDetail.getMedia()));
            params.put("coment"                 , String.valueOf(pollDetail.getComment()));
            params.put("result"                 , String.valueOf(pollDetail.getResult()));
            params.put("limite"                 , String.valueOf(pollDetail.getLimite()));
            params.put("comentario"             , String.valueOf(pollDetail.getComentario()));
            params.put("auditor"                , String.valueOf(pollDetail.getAuditor()));
            params.put("product_id"             , String.valueOf(pollDetail.getProduct_id()));
            params.put("publicity_id"           , String.valueOf(pollDetail.getPublicity_id()));
            params.put("company_id"             , String.valueOf(pollDetail.getCompany_id()));
            params.put("category_product_id"    , String.valueOf(pollDetail.getCategory_product_id()));
            params.put("commentOptions"         , String.valueOf(pollDetail.getCommentOptions()));
            params.put("selectedOptions"        , String.valueOf(pollDetail.getSelectdOptions()));
            params.put("selectedOptionsComment" , String.valueOf(pollDetail.getSelectedOtionsComment()));
            params.put("priority"               , String.valueOf(pollDetail.getPriority()));



            JSONParserX jsonParser = new JSONParserX();
            // getting product details by making HTTP request
            //JSONObject json = jsonParser.makeHttpRequest(GlobalConstant.dominio + "/json/prueba.json" ,"POST", params);
            JSONObject json = jsonParser.makeHttpRequest(GlobalConstant.dominio + "/savePollDetailsReg" ,"POST", params);
            // check your log for json response
            Log.d("Login attempt", json.toString());
            // json success, tag que retorna el json
            if (json == null) {
                Log.d("JSON result", "Está en nullo");
                return false;
            } else{
                success = json.getInt("success");
                if (success == 1) {
                    Log.d(LOG_TAG, "Se insertó registro correctamente");
                   // return true ;
                }else{
                    Log.d(LOG_TAG, "no insertó registro");
                    // return json.getString("message");
                     return false;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true ;
    }





    public static boolean savePhoneDetails(PhoneDetail phoneDetail) {

        int success;
        try {

            HashMap<String, String> params = new HashMap<>();

            params.put("user_id"       , String.valueOf(phoneDetail.getUser_id()));
            params.put("latitud"       , String.valueOf(phoneDetail.getLatitude()));
            params.put("longitud"      , String.valueOf(phoneDetail.getLongitude()));
            params.put("phone"         , String.valueOf(phoneDetail.getPhone()));
            params.put("sdk"           , String.valueOf(phoneDetail.getSdk()));

            JSONParserX jsonParser = new JSONParserX();
            // getting product details by making HTTP request
            //JSONObject json = jsonParser.makeHttpRequest(GlobalConstant.dominio + "/json/prueba.json" ,"POST", params);
            JSONObject json = jsonParser.makeHttpRequest(GlobalConstant.dominio + "/savePhoneDetails" ,"POST", params);
            // check your log for json response
            Log.d("Login attempt", json.toString());

            // json success, tag que retorna el json

            if (json == null) {
                Log.d(LOG_TAG, "Está en nullo");
                return false;
            } else{
                success = json.getInt("success");
                if (success == 1) {
                    Log.d(LOG_TAG, "Se insertó registro correctamente");
                }else{
                    Log.d(LOG_TAG, "no insertó registro");
                    // return json.getString("message");
                    // return false;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;

    }

    public static User userLogin(String userName, String password , String imei){

        int success ;
        User user = new User();
        try {

            HashMap<String, String> params = new HashMap<>();

            params.put("username", String.valueOf(userName));
            params.put("password", String.valueOf(password));
            params.put("imei", String.valueOf(imei));

            JSONParserX jsonParser = new JSONParserX();
            // getting product details by making HTTP request
            JSONObject json = jsonParser.makeHttpRequest(GlobalConstant.dominio + "/loginMovil" ,"POST", params);
            // check your log for json response
            Log.d("Login attempt", json.toString());
            // json success, tag que retorna el json
            if (json == null) {
                Log.d("JSON result", "Está en nulo");
            } else{
                success = json.getInt("success");
                if (success == 1) {
                    user.setId(json.getInt("id"));
                    user.setEmail(userName);
                    user.setName(json.getString("fullname"));
                    user.setPassword(password);
                }else{
                    Log.d(LOG_TAG, "No se pudo iniciar sesión");
                    //return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // return false;
        }
        return  user;
    }

}
