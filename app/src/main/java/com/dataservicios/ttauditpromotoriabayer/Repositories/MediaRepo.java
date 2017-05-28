package com.dataservicios.ttauditpromotoriabayer.Repositories;

/**
 * Created by Webmaster on 27/08/2016.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.dataservicios.ttauditpromotoriabayer.Model.Media;
import com.dataservicios.ttauditpromotoriabayer.SQLite.DatabaseHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MediaRepo extends DatabaseHelper {
     private static final String LOG = MediaRepo.class.getSimpleName();

    public MediaRepo(Context context) {
        super(context);
    }


    public int update(Media media) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, media.getId());
        // updating row
        return db.update(TABLE_MEDIAS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(media.getId()) });
    }

    /**
     *
     * @param media Object media
     * @return
     */
    public long insert(Media media) {
        long todo_id;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = df.format(Calendar.getInstance().getTime());
        media.setCreated_at(date);

        // values.put(KEY_ID, audit.getId());
        values.put(KEY_STORE_ID, media.getStore_id());
        values.put(KEY_POLL_ID, media.getPoll_id());
        values.put(KEY_COMPANY_ID, media.getCompany_id());
        values.put(KEY_PRODUCT_ID, media.getProduct_id());
        values.put(KEY_CATEGORY_PRODUCT_ID, media.getCategory_product_id());
        values.put(KEY_PUBLICITY_ID, media.getPublicity_id());
        values.put(KEY_NAME_FILE, media.getFile());
        values.put(KEY_MONTO, media.getMonto());
        values.put(KEY_RAZON_SOCIAL, media.getRazonSocial());
        values.put(KEY_TYPE, media.getType());
        values.put(KEY_DATE_CREATED, media.getCreated_at());

        todo_id = db.insert(TABLE_MEDIAS, null, values);
        db.close();
        return todo_id;
    }

    /**
     * Get all Medias return lis object media
     * @return
     */
    public List<Media> getAllMedias() {
        List<Media> medias = new ArrayList<Media>();
        String selectQuery = "SELECT  * FROM " + TABLE_MEDIAS;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Media pd = new Media();
                int id = Integer.parseInt(c.getString(c.getColumnIndex(KEY_ID)));
                pd.setId(id);
                pd.setStore_id(c.getInt(c.getColumnIndex(KEY_STORE_ID)));
                pd.setPoll_id((c.getInt(c.getColumnIndex(KEY_POLL_ID))));
                pd.setCompany_id((c.getInt(c.getColumnIndex(KEY_COMPANY_ID))));
                pd.setProduct_id((c.getInt(c.getColumnIndex(KEY_PRODUCT_ID))));
                pd.setCategory_product_id((c.getInt(c.getColumnIndex(KEY_CATEGORY_PRODUCT_ID))));
                pd.setPublicity_id(c.getInt(c.getColumnIndex(KEY_PUBLICITY_ID)));
                pd.setFile((c.getString(c.getColumnIndex(KEY_NAME_FILE))));
                pd.setMonto((c.getString(c.getColumnIndex(KEY_MONTO))));
                pd.setRazonSocial((c.getString(c.getColumnIndex(KEY_RAZON_SOCIAL))));
                pd.setType((c.getInt(c.getColumnIndex(KEY_TYPE))));
                pd.setCreated_at((c.getString(c.getColumnIndex(KEY_DATE_CREATED))));
                medias.add(pd);
            } while (c.moveToNext());
        }
        return medias;
    }

    /**
     * Get Media for idMedia
     * @param idMedia
     * @return
     */
    public Media getMedia(long idMedia) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_MEDIAS + " WHERE "
                + KEY_ID + " = " + idMedia;
        Log.e(LOG, selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);
        Media pd = new Media();
        if (c.moveToFirst()) {

            int id = Integer.parseInt(c.getString(c.getColumnIndex(KEY_ID)));
            pd.setId(id);
            pd.setStore_id(c.getInt(c.getColumnIndex(KEY_STORE_ID)));
            pd.setPoll_id((c.getInt(c.getColumnIndex(KEY_POLL_ID))));
            pd.setCompany_id((c.getInt(c.getColumnIndex(KEY_COMPANY_ID))));
            pd.setProduct_id((c.getInt(c.getColumnIndex(KEY_PRODUCT_ID))));
            pd.setCategory_product_id((c.getInt(c.getColumnIndex(KEY_CATEGORY_PRODUCT_ID))));
            pd.setPublicity_id(c.getInt(c.getColumnIndex(KEY_PUBLICITY_ID)));
            pd.setType((c.getInt(c.getColumnIndex(KEY_TYPE))));
            pd.setFile((c.getString(c.getColumnIndex(KEY_NAME_FILE))));
            pd.setMonto((c.getString(c.getColumnIndex(KEY_MONTO))));
            pd.setRazonSocial((c.getString(c.getColumnIndex(KEY_RAZON_SOCIAL))));
            pd.setCreated_at((c.getString(c.getColumnIndex(KEY_DATE_CREATED))));
        }
        c.close();
        db.close();
        return pd;
    }

    /**
     * Get first record Media
     * @return
     */
    public Media getFirstMedia() {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_MEDIAS  + " LIMIT 1  ";
        Log.e(LOG, selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);

        Media pd = new Media();
        if(c != null)
        {
            if (c.moveToFirst()) {
                int id = Integer.parseInt(c.getString(c.getColumnIndex(KEY_ID)));
                pd.setId(id);
                pd.setStore_id(c.getInt(c.getColumnIndex(KEY_STORE_ID)));
                pd.setPoll_id((c.getInt(c.getColumnIndex(KEY_POLL_ID))));
                pd.setCompany_id((c.getInt(c.getColumnIndex(KEY_COMPANY_ID))));
                pd.setProduct_id((c.getInt(c.getColumnIndex(KEY_PRODUCT_ID))));
                pd.setPublicity_id(c.getInt(c.getColumnIndex(KEY_PUBLICITY_ID)));
                pd.setCategory_product_id(c.getInt(c.getColumnIndex(KEY_CATEGORY_PRODUCT_ID)));
                pd.setFile((c.getString(c.getColumnIndex(KEY_NAME_FILE))));
                pd.setMonto((c.getString(c.getColumnIndex(KEY_MONTO))));
                pd.setRazonSocial((c.getString(c.getColumnIndex(KEY_RAZON_SOCIAL))));
                pd.setType((c.getInt(c.getColumnIndex(KEY_TYPE))));
                pd.setCreated_at((c.getString(c.getColumnIndex(KEY_DATE_CREATED))));
            }
        }

        c.close();
        db.close();
        return pd;
    }

    /**
     * Delete All Media
     */
    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MEDIAS, null, null );

    }

    /**
     * Delete Media for Id
     * @param id
     */
    public void deleteForId(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MEDIAS, KEY_ID + " = ? ",  new String[] { String.valueOf(id) } );

    }

}

