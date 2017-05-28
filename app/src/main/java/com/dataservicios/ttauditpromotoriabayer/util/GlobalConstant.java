package com.dataservicios.ttauditpromotoriabayer.util;
/**
 * Created by usuario on 11/11/2014.
 */

public final class GlobalConstant {
    public static String dominio = "http://ttaudit.com";
   // public static String dominio = "http://appfiliaibk.com";
    public static final String LOGIN_URL = dominio + "/loginUser";
    public static final String KEY_USERNAME = "username";
    public static String inicio,fin;
    public static  double latitude_open, longitude_open;
    public static  int global_close_audit =0;
    public static int company_id = 77;
    // public static String albunName = "AlicorpPhoto";
    //public static String directory_images = "/Pictures/" + albunName;
    public static String directory_images = "/Pictures/" ;
    public static String type_aplication = "android";
    public static int[] poll_id = new int[]{
        1353, // 0	Se encuentra la persona?
        1354, // 1	Recibio Premio ?
} ;

    public static int[] audit_id = new int[]{
            54,	// 0 "Entrega de Premios"
    } ;

    public static final String JPEG_FILE_PREFIX = "_bayer_promo_";
    public static final String JPEG_FILE_SUFFIX = ".jpg";
}

