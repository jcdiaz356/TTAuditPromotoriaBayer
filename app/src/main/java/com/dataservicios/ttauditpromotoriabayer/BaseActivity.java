package com.dataservicios.ttauditpromotoriabayer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.dataservicios.ttauditpromotoriabayer.Model.NavDrawerItem;
import com.dataservicios.ttauditpromotoriabayer.Repositories.MediaRepo;
import com.dataservicios.ttauditpromotoriabayer.adapter.NavDrawerListAdapter;
import com.dataservicios.ttauditpromotoriabayer.util.SessionManager;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by usuario on 26/11/2014.
 */
public class BaseActivity extends Activity {
    private static final String LOG_TAG = BaseActivity.class.getSimpleName();
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    // nav drawer title
    private CharSequence mDrawerTitle;
    // used to store app title
    private CharSequence mTitle;
    // slide menu items
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;
    private SessionManager session;
    private String email_user, id_user, name_user;

    private String pathFile ;
    private File filePath;

    final Activity MyActivity = this;

    private AlbumStorageDirFactory mAlbumStorageDirFactory = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paneladmin);

        session = new SessionManager(getApplicationContext());

        HashMap<String, String> user = session.getUserDetails();
        name_user = user.get(SessionManager.KEY_NAME);
        email_user = user.get(SessionManager.KEY_EMAIL);
        id_user = user.get(SessionManager.KEY_ID_USER);
        mTitle = mDrawerTitle = getTitle();
        // load slide menu items
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
        navDrawerItems = new ArrayList<NavDrawerItem>();

        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId( 0, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId( 1 , -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId( 2 , -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId( 3 , -1), true , "0"));

        navMenuIcons.recycle();
        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
        // setting the nav drawer list adapter
        adapter = new NavDrawerListAdapter(getApplicationContext(),navDrawerItems);
        mDrawerList.setAdapter(adapter);
        // enabling action bar app icon and behaving it as toggle button
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        )
        {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {


                getActionBar().setTitle(mDrawerTitle);
                Log.i(LOG_TAG, String.valueOf(navDrawerItems.get(3).getCount() )) ;

                MediaRepo mr = new MediaRepo(MyActivity);
                long Total = mr.getAllMedias().size();

                navDrawerItems.get(3).setCount(String.valueOf(Total));
                adapter.notifyDataSetChanged();
                //notifyDataSetChanged();


                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            // on first time display view for first nav item
            displayView(0);
        }
    }

    /**
     * Slide menu item click listener
     * */
    private class SlideMenuClickListener implements  ListView.OnItemClickListener
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // display view for selected nav drawer item
            displayView(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        // toggle nav drawer on selecting action bar app icon/title
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action bar actions click
        switch (item.getItemId()) {
            case R.id.action_change_password:
                Log.i("Hola:", "sdfsdf");

                return true;
            default:
                return super.onOptionsItemSelected(item);

            case R.id.action_salir:
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //super.onBackPressed();
                                //session.logoutUser();
                                //finish();
                                session.logoutUser();
                                //finish();
                                // Termina Toda las actividades abiertas
                                //for api 16+ use finishAffinity(); and for api <16 use ActivityCompat.finishAffinity(this); (with import import android.support.v4.app.ActivityCompat)
                                ActivityCompat.finishAffinity(MyActivity);
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(this,2);
                builder.setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Cerar aplicación")
                        .setMessage("Está seguro que desea cerrar sesión ?")
                        .setPositiveButton("Si", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();



                return true;


            case R.id.action_about:
                Intent intent = new Intent(MyActivity,About.class);
                startActivity(intent);
                //finish();
                return true;
        }
    }
    /***
     * Called when invalidateOptionsMenu() is triggered
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        //menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {

        Log.i(LOG_TAG,"onMenuOpened") ;
        return super.onMenuOpened(featureId, menu);


    }

    /**
     * Diplaying fragment view for selected nav drawer list item
     * */
    private void displayView(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        switch (position) {
            case 0:
//
                fragment = new FragmentRutas();
//                Intent intent = new Intent("com.dataservicios.systemauditor.PUNTOVENTA");
//                startActivity(intent);
                break;

            case 1:

                pathFile = Environment.getExternalStorageDirectory().toString()+"/Pictures/" + getAlbunNameTemp()  ;

                //File filePath = new File(pathFile);
                filePath = new File(pathFile);

                if (filePath.isDirectory()) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    Uri myUri = Uri.parse(String.valueOf(filePath));
                    intent.setDataAndType(myUri , "resource/folder");
                    startActivity(intent);
                } else {
                    Toast.makeText(MyActivity,"El directorio no existe, no hay imagenes temporales", Toast.LENGTH_LONG).show();
                }

                break;
            case 2:
                //fragment = new GraficosFragment();
                //fragment = new PhotosFragment();

                pathFile = Environment.getExternalStorageDirectory().toString()+"/Pictures/" + getAlbunNameBackup()  ;

                //File filePath = new File(pathFile);
                filePath = new File(pathFile);

                if (filePath.isDirectory()) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    Uri myUri = Uri.parse(String.valueOf(filePath));
                    intent.setDataAndType(myUri , "resource/folder");
                    startActivity(intent);
                } else {
                    Toast.makeText(MyActivity,"El directorio no existe, no hay backup de imágenes", Toast.LENGTH_LONG).show();
                }
                break;
            case 3:
                //fragment = new CommunityFragment();
                Intent intent;
                intent = new Intent(MyActivity, RegistroMedia.class);
                //intent.putExtras(argRuta);
                startActivity(intent);
                break;
            case 4:
                //fragment = new PagesFragment();
                break;
            case 5:
                //fragment = new WhatsHotFragment();
                break;

            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment).commit();

            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(navMenuTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }



    private String getAlbunNameTemp(){
        return  getString(R.string.album_name_temp);
    }

    private String getAlbunNameBackup(){
        return  getString(R.string.album_name_backup);
    }
}
