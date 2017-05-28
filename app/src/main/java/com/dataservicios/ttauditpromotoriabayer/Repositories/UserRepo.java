package com.dataservicios.ttauditpromotoriabayer.Repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.dataservicios.ttauditpromotoriabayer.Model.User;
import com.dataservicios.ttauditpromotoriabayer.SQLite.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jcdia on 4/03/2017.
 */

public class UserRepo extends DatabaseHelper {
    private static final String LOG = UserRepo.class.getSimpleName();

    public UserRepo(Context context) {
        super(context);
    }

    //---------------------------------------------------------------//
    // ------------------------ "USER" table methods ----------------//
    //---------------------------------------------------------------//

    /*
     * Creating a USER
     */
    //---------------------------------------------------------------//
    // ------------------------ "USER" table methods ----------------//
    //---------------------------------------------------------------//
    /*
     * Creating a USER
     */
    /*
     * Creating a USER
     */
    public long createUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_ID, user.getId());
        values.put(KEY_NAME, user.getName());
        values.put(KEY_EMAIL, user.getEmail());
        values.put(KEY_PASSWORD, user.getPassword());

        // insert row
        //long todo_id = db.insert(TABLE_PEDIDO, null, values);
        db.insert(TABLE_USER, null, values);

        long todo_id = user.getId();
        return todo_id;
    }



    /*
     * get single User id
     */
    public User getUser(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_USER + " WHERE "
                + KEY_ID + " = " + id;
        Log.e(LOG, selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);
        if (c != null)
            c.moveToFirst();
        User pd = new User();
        pd.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        pd.setEmail(c.getString(c.getColumnIndex(KEY_EMAIL)));
        pd.setName((c.getString(c.getColumnIndex(KEY_NAME))));
        pd.setPassword((c.getString(c.getColumnIndex(KEY_PASSWORD))));
        return pd;
    }

    /**
     *
     * @param name
     * @return
     */
    public User getUserName(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_USER + " WHERE "
                + KEY_NAME + " = " + name;
        Log.e(LOG, selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);
        if (c != null)
            c.moveToFirst();
        User pd = new User();
        pd.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        pd.setName((c.getString(c.getColumnIndex(KEY_NAME))));
        pd.setEmail((c.getString(c.getColumnIndex(KEY_EMAIL))));
        pd.setPassword((c.getString(c.getColumnIndex(KEY_PASSWORD))));
        return pd;
    }

    /**
     *
     * @param email
     * @return
     */
    public User getUserEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_USER + " WHERE "
                + KEY_EMAIL + " = " + email;
        Log.e(LOG, selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);
        if (c != null)
            c.moveToFirst();
        User pd = new User();
        pd.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        pd.setName((c.getString(c.getColumnIndex(KEY_NAME))));
        pd.setEmail((c.getString(c.getColumnIndex(KEY_EMAIL))));
        pd.setPassword((c.getString(c.getColumnIndex(KEY_PASSWORD))));
        return pd;
    }

    /**
     * getting all User
     * */
    public List<User> getAllUser() {
        List<User> users = new ArrayList<User>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                User pd = new User();
                pd.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                pd.setName((c.getString(c.getColumnIndex(KEY_NAME))));
                pd.setEmail((c.getString(c.getColumnIndex(KEY_EMAIL))));
                pd.setPassword((c.getString(c.getColumnIndex(KEY_PASSWORD))));
                // adding to todo list
                users.add(pd);
            } while (c.moveToNext());
        }
        return users;
    }
    /*
         * getting User count
         */
    public int getUserCount() {
        String countQuery = "SELECT  * FROM " + TABLE_USER;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        Log.e(LOG, countQuery);
        cursor.close();
        // return count
        return count;
    }
    /*
     * Updating a User
     */
    public int updatePedido(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, user.getName());
        values.put(KEY_PASSWORD, user.getPassword());
        // updating row
        return db.update(TABLE_USER, values, KEY_ID + " = ?",
                new String[] { String.valueOf(user.getId()) });
    }


    /*
     * Deleting a User
     */
    public void deleteUser(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USER, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
    }

    /*
     * Deleting all User
     */
    public void deleteAllUser() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USER, null, null );

    }






}