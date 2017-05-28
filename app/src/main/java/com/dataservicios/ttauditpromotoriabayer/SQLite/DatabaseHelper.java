package com.dataservicios.ttauditpromotoriabayer.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.dataservicios.ttauditpromotoriabayer.Model.Audit;
import com.dataservicios.ttauditpromotoriabayer.Model.Encuesta;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by usuario on 12/02/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    // Logcat tag
    private static final String LOG = "DatabaseHelper";
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "db_bayer_promo";
    // Table Names
    protected static final String TABLE_POLL = "poll";
    protected static final String TABLE_USER = "user";
    protected static final String TABLE_PRODUCTS = "products";
    protected static final String TABLE_PUBLICITY = "publicity";
    protected static final String TABLE_PRESENCE_PRODUCTS = "presense_products";
    protected static final String TABLE_PRESENCE_PUBLICITY = "presence_publicity";
    protected static final String TABLE_AUDITS = "audits";
    protected static final String TABLE_SOD_VENTANAS = "sodventanas";
    protected static final String TABLE_MEDIAS = "medias";
    protected static final String TABLE_CATEGORIAS = "categorias";
    protected static final String TABLE_REG_INVOICE= "registrar_factura";

    //Name columns common
    protected static final String KEY_ID = "id";
    protected static final String KEY_NAME = "name";
    protected static final String KEY_STORE = "store_id";
    protected static final String KEY_POLL_ID = "poll_id";
    protected static final String KEY_DATE_CREATED= "created_at";
    protected static final String KEY_DATE_UPDATE= "update_at";

    //Name columns user
    protected static final String KEY_EMAIL = "email";
    protected static final String KEY_PASSWORD = "password";

    //Name columns poll
    protected static final String KEY_QUESTION = "name_question";
    protected static final String KEY_ID_AUDITORIA = "auditoria_id";

    //Name columns Products
    protected static final String KEY_CODE = "code";
    protected static final String KEY_IMAGEN = "image";
    protected static final String KEY_COMPANY_ID = "company_id";

    protected static final String KEY_CATEGORY_ID = "category_id";
    protected static final String KEY_CATEGORY_NAME = "category_name";
    protected static final String KEY_PRECIO = "precio";
    protected static final String KEY_STATUS = "status";



    //Name column Presense Product
    protected static final String KEY_STORE_ID = "store_id";
    protected static final String KEY_PRODUCT_ID = "product_id";
    protected static final String KEY_PRECIO_CHECK = "precio_check";
    protected static final String KEY_PRECIO_VISIBLE = "precio_visible";

    //Name column Presense Publicity
    protected static final String KEY_PUBLICITY_ID = "publicity_id";
    protected static final String KEY_FOUND = "found";
    protected static final String KEY_VISIBLE = "visible";
    protected static final String KEY_LAYOUT_CORRECT = "layout_correct";
    protected static final String KEY_CONTAMINATED = "contaminated";
    protected static final String KEY_COMMENT = "comment";

    //Name column Table publicity
    protected   static final String KEY_ACTIVE = "active";
    //Name column Audit
    protected static final String KEY_SCORE = "score";
    //Name column Table medias
    protected   static final String KEY_TIPO = "tipo";
    protected   static final String KEY_NAME_FILE = "archivo";
    protected   static final String KEY_TYPE = "type";
    protected   static final String KEY_MONTO = "monto";
    protected   static final String KEY_RAZON_SOCIAL = "razon_social";

    protected   static final String KEY_CATEGORY_PRODUCT_ID = "category_product_id";


    // Table Create Statements
    // eNCUESTA table create statement
    private static final String CREATE_TABLE_POLL = "CREATE TABLE "
            + TABLE_POLL + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_ID_AUDITORIA + " INTEGER,"
            + KEY_QUESTION  + " TEXT " + ")";

    // User table create statement
    private static final String CREATE_TABLE_USER = "CREATE TABLE "
            + TABLE_USER + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_NAME + " TEXT,"
            + KEY_EMAIL + " TEXT,"
            + KEY_PASSWORD + " TEXT " + ")";


    // Publicity table create statement

    private static final String CREATE_TABLE_PUBLICITY = "CREATE TABLE "
            + TABLE_PUBLICITY + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_NAME + " TEXT,"
            + KEY_IMAGEN + " TEXT, "
            + KEY_COMPANY_ID + " INTEGER, "
            + KEY_CATEGORY_ID + " INTEGER, "
            + KEY_CATEGORY_NAME + " TEXT, "
            + KEY_ACTIVE + " INTEGER " + ")";


    // Publicity table create statement

    private static final String CREATE_TABLE_CATEGORIAS = "CREATE TABLE "
            + TABLE_CATEGORIAS + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_NAME + " TEXT,"
            + KEY_ACTIVE + " INTEGER " + ")";

    // Products table create statement
    private static final String CREATE_TABLE_PRODUCTS = "CREATE TABLE "
            + TABLE_PRODUCTS + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_NAME + " TEXT,"
            + KEY_CODE + " TEXT,"
            + KEY_IMAGEN + " TEXT, "
            + KEY_PRECIO + " TEXT, "
            + KEY_STATUS + " INTEGER, "
            + KEY_COMPANY_ID + " INTEGER, "
            + KEY_CATEGORY_ID + " INTEGER, "
            + KEY_CATEGORY_NAME + " TEXT ,"
            + KEY_ACTIVE + " INTEGER " + ")";


    // Presence product create table statemnt
    private static final String CREATE_TABLE_PRESENCE_PRODUCTS  = "CREATE TABLE "
            + TABLE_PRESENCE_PRODUCTS + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_STORE_ID + " INTEGER, "
            + KEY_CATEGORY_ID + " INTEGER, "
            + KEY_CODE + " STRING, "
            + KEY_STATUS + " INTEGER, "
            + KEY_PRECIO_CHECK + " STRING, "
            + KEY_PRECIO_VISIBLE + " INTEGER, "
            + KEY_PRODUCT_ID + " INTEGER )";

    // Presence product create table statemnt
    private static final String CREATE_TABLE_PRESENCE_PUBLICITY  = "CREATE TABLE "
            + TABLE_PRESENCE_PUBLICITY + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_STORE_ID + " INTEGER, "
            + KEY_CATEGORY_ID + " INTEGER, "
            + KEY_FOUND + " INTEGER, "
            + KEY_VISIBLE + " INTEGER, "
            + KEY_LAYOUT_CORRECT + " INTEGER, "
            + KEY_CONTAMINATED + " INTEGER, "
            + KEY_COMMENT + " TEXT, "
            + KEY_PUBLICITY_ID + " INTEGER )";

    // Audit create table statement
    private static final String CREATE_TABLE_AUDITS  = "CREATE TABLE "
            + TABLE_AUDITS + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_NAME + " TEXT ,"
            + KEY_STORE_ID + " INTEGER, "
            + KEY_SCORE + " INTEGER )";

    private static final String CREATE_TABLE_SOD_VENTANAS  = "CREATE TABLE "
            + TABLE_SOD_VENTANAS + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_NAME + " TEXT ,"
            + KEY_IMAGEN + " TEXT, "
            + KEY_STATUS+ " INTEGER )";
    private static final String CREATE_TABLE_MEDIAS  = "CREATE TABLE "
            + TABLE_MEDIAS + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_STORE + " INTEGER ,"
            + KEY_POLL_ID + " INTEGER, "
            + KEY_PUBLICITY_ID + " INTEGER, "
            + KEY_PRODUCT_ID + " INTEGER, "
            + KEY_CATEGORY_PRODUCT_ID + " INTEGER, "
            + KEY_COMPANY_ID + " INTEGER, "
            + KEY_NAME_FILE + " TEXT, "
            + KEY_MONTO + " TEXT, "
            + KEY_RAZON_SOCIAL + " TEXT, "
            + KEY_TYPE + " INTEGER, "
            + KEY_DATE_CREATED + " TEXT )";

    private static final String CREATE_TABLE_REG_INVOICE  = "CREATE TABLE "
            + TABLE_REG_INVOICE + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_STORE + " INTEGER ,"
            + KEY_NAME_FILE + " TEXT, "
            + KEY_MONTO + " TEXT, "
            + KEY_RAZON_SOCIAL + " TEXT ) ";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

//
    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(CREATE_TABLE_POLL);
        db.execSQL(CREATE_TABLE_USER);

        db.execSQL(CREATE_TABLE_PRESENCE_PRODUCTS);
        db.execSQL(CREATE_TABLE_PRESENCE_PUBLICITY);
        db.execSQL(CREATE_TABLE_PRODUCTS);
        db.execSQL(CREATE_TABLE_AUDITS);
        db.execSQL(CREATE_TABLE_PUBLICITY);
        db.execSQL(CREATE_TABLE_SOD_VENTANAS);
        db.execSQL(CREATE_TABLE_MEDIAS);
        db.execSQL(CREATE_TABLE_REG_INVOICE);
        db.execSQL(CREATE_TABLE_CATEGORIAS);
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_POLL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRESENCE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRESENCE_PUBLICITY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AUDITS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PUBLICITY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SOD_VENTANAS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEDIAS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REG_INVOICE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIAS);

        // create new tables
        onCreate(db);
    }




    /**
     * getting all products
     * */








    public int updateProductStatusForCode(String code, int status) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_STATUS, status);

        // updating row
        return db.update(TABLE_PRODUCTS, values, KEY_CODE + " = ?",  new String[] { String.valueOf(code) });
    }

    public int updateProductStatusForCategory(int category_id,int status) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_STATUS, status);

        // updating row
        return db.update(TABLE_PRODUCTS, values, KEY_CATEGORY_ID + " = ?",  new String[] { String.valueOf(category_id) });
    }


    public int updateProductStatus(int status) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_STATUS, status);

        // updating row
        return db.update(TABLE_PRODUCTS, values, null,  null);
    }


    /*
     * Deleting a User
     */
    public void deleteProduct(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRODUCTS, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
    }

    /*
     * Deleting all User
     */
    public void deleteAllProducts() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRODUCTS, null, null );

    }

    public int updateProductDesactive() {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_ACTIVE, 1);



        // updating row
        return db.update(TABLE_PRODUCTS, values, null,  null);
    }


    //---------------------------------------------------------------
    // ------------------------ "POLS" table methods ----------------//
//------------------------------------------------------------------
    /*
     * Creating a Encuesta
     */
    public long createEncuesta(Encuesta encuesta) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_ID, encuesta.getId());
        values.put(KEY_ID_AUDITORIA, encuesta.getIdAuditoria());
        values.put(KEY_QUESTION, encuesta.getQuestion());

        // insert row
        //long todo_id = db.insert(TABLE_PEDIDO, null, values);
         db.insert(TABLE_POLL, null, values);

        long todo_id = encuesta.getId();
        return todo_id;
    }



    /*
     * get single Encuesta
     */
    public Encuesta getEncuesta(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_POLL + " WHERE "
                + KEY_ID + " = " + id;
        Log.e(LOG, selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);
        if (c != null)
            c.moveToFirst();
        Encuesta pd = new Encuesta();
        pd.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        pd.setIdAiditoria(c.getInt(c.getColumnIndex(KEY_ID_AUDITORIA)));
        pd.setQuestion((c.getString(c.getColumnIndex(KEY_QUESTION))));
        return pd;
    }

    /*
     * get single Encuesta por Auditoria
     */
    public Encuesta getEncuestaAuditoria(long idAuditoria) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_POLL + " WHERE "
                + KEY_ID_AUDITORIA + " = " + idAuditoria;
        Log.e(LOG, selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);
        if (c != null)
            c.moveToFirst();
        Encuesta pd = new Encuesta();
        pd.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        pd.setIdAiditoria(c.getInt(c.getColumnIndex(KEY_ID_AUDITORIA)));
        pd.setQuestion((c.getString(c.getColumnIndex(KEY_QUESTION))));
        return pd;
    }

    /**
     * getting all Encuesta
     * */
    public List<Encuesta> getAllEncuesta() {
        List<Encuesta> encuesta = new ArrayList<Encuesta>();
        String selectQuery = "SELECT  * FROM " + TABLE_POLL;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Encuesta pd = new Encuesta();
                pd.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                pd.setIdAiditoria(c.getInt((c.getColumnIndex(KEY_ID_AUDITORIA))));
                pd.setQuestion((c.getString(c.getColumnIndex(KEY_QUESTION))));

                // adding to todo list
                encuesta.add(pd);
            } while (c.moveToNext());
        }
        return encuesta;
    }

    /*
     * getting Encuesta count
     */
    public int getEncuestaCount() {
        String countQuery = "SELECT  * FROM " + TABLE_POLL;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        // return count
        return count;
    }
    /*
     * Updating a Encuesta
     */
    public int updateEncuesta(Encuesta encuesta) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_QUESTION, encuesta.getQuestion());
        // updating row
        return db.update(TABLE_POLL, values, KEY_ID + " = ?",
                new String[] { String.valueOf(encuesta.getId()) });
    }

    /*
     * Deleting a Encuesta
     */
    public void deleteEncuesta(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_POLL, KEY_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    /*
     * Deleting all Encuesta
     */
    public void deleteAllEncuesta() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_POLL, null, null);

    }




//---------------------------------------------------------------//
    // ------------------------ "AUDIT" table methods ----------------//
    //-------------------------------------------------------------

    /**
     * Create Audit
     * @param audit
     * @return
     */
    public long createAudit(Audit audit) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

       // values.put(KEY_ID, audit.getId());
        values.put(KEY_NAME, audit.getName());
        values.put(KEY_STORE_ID, audit.getStore_id());
        values.put(KEY_SCORE, audit.getScore());

        // insert row
        //long todo_id = db.insert(TABLE_PEDIDO, null, values);
        db.insert(TABLE_AUDITS, null, values);

        long todo_id = audit.getId();
        return todo_id;
    }
    public Audit getAudit(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_AUDITS + " WHERE "
                + KEY_ID + " = " + id;
        Log.e(LOG, selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);
        if (c != null)
            c.moveToFirst();
        Audit pd = new Audit();
        pd.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        pd.setName(c.getString(c.getColumnIndex(KEY_NAME)));
        pd.setScore((c.getInt(c.getColumnIndex(KEY_SCORE))));
        pd.setStore_id((c.getInt(c.getColumnIndex(KEY_STORE_ID))));


        return pd;
    }




    /**
     * Update Audit
     * @param audit
     * @return
     */
    public int updateAudit(Audit audit) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, audit.getId());
        values.put(KEY_NAME, audit.getName());
        values.put(KEY_STORE_ID, audit.getStore_id());
        values.put(KEY_SCORE, audit.getScore());


        // updating row
        return db.update(TABLE_AUDITS, values, KEY_ID + " = ?", new String[] { String.valueOf(audit.getId()) });
    }

    public int updateAuditScore(int audit_id,int score , int store_id ) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID,  audit_id);
        values.put(KEY_SCORE, score);


        // updating row
        return db.update(TABLE_AUDITS, values, KEY_ID + " = ? and " + KEY_STORE_ID + " = ? ", new String[] { String.valueOf(audit_id) , String.valueOf(store_id)  });
    }

    /**
     * Delete all Audits
     */
    public void deleteAllAudits() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_AUDITS, null, null );
    }


    /**
     *
     * @param store_id
     */
    public void deleteAllAuditForStoreId(int store_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_AUDITS, KEY_STORE_ID + " = ? ", new String[] { String.valueOf(store_id)  } );
    }

    /**
     * .Return List Audits
     * @return
     */
    public List<Audit> getAllAudits() {
        List<Audit> audit = new ArrayList<Audit>();
        String selectQuery = "SELECT  * FROM " + TABLE_AUDITS;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Audit pd = new Audit();
                pd.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                pd.setName((c.getString(c.getColumnIndex(KEY_NAME))));
                pd.setStore_id((c.getInt(c.getColumnIndex(KEY_STORE_ID))));
                pd.setScore((c.getInt(c.getColumnIndex(KEY_SCORE))));
                // adding to todo list
                audit.add(pd);
            } while (c.moveToNext());
        }
        return audit;
    }

    /**
     *
     * @param store_id
     * @return
     */
    public List<Audit> getAllAuditsForStoreId(long store_id) {
        List<Audit> audit = new ArrayList<Audit>();
        String selectQuery = "SELECT  * FROM " + TABLE_AUDITS + " WHERE " + KEY_STORE_ID + " = " + store_id;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Audit pd = new Audit();
                pd.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                pd.setName((c.getString(c.getColumnIndex(KEY_NAME))));
                pd.setStore_id((c.getInt(c.getColumnIndex(KEY_STORE_ID))));
                pd.setScore((c.getInt(c.getColumnIndex(KEY_SCORE))));
                // adding to todo list
                audit.add(pd);
            } while (c.moveToNext());
        }
        return audit;
    }





    /**
     *
     * @param id
     * @return
     */

    public int getCountAuditForId(int id) {
        String countQuery = "SELECT  * FROM " + TABLE_AUDITS + " WHERE " +  KEY_ID  + " = " + id  ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        Log.e(LOG, countQuery);
        cursor.close();
        // return count
        return count;
    }

    public int getCountAuditForIdForStoreId(int id,int store_id) {
        String countQuery = "SELECT  * FROM " + TABLE_AUDITS + " WHERE " +  KEY_ID  + " = " + id + " AND " + KEY_STORE_ID + " = " + store_id  ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        Log.e(LOG, countQuery);
        cursor.close();
        // return count
        return count;
    }



    ///////////////////////////****END TABLAS MANTNIMENT**********////////////////////
    //////////////////////////////////////////////////////////////////////////////////


    public static boolean checkDataBase(Context context) {
        SQLiteDatabase checkDB = null;
        try {
            File database=context.getDatabasePath(DATABASE_NAME);
            if (database.exists()) {
                Log.i("Database", "Found");
                String myPath = database.getAbsolutePath();
                Log.i(LOG, myPath);
                checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
                //return true;
            } else {
                // Database does not exist so copy it from assets here
                Log.i(LOG, "Not Found");
                //return false;
            }
        } catch(SQLiteException e) {
            Log.i(LOG, "Not Found");
        } finally {
            if(checkDB != null) {
                checkDB.close();
            }
        }
        return checkDB != null ? true : false;
    }

}
