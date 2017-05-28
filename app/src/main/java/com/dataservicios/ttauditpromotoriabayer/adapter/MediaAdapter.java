package com.dataservicios.ttauditpromotoriabayer.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dataservicios.ttauditpromotoriabayer.AlbumStorageDirFactory;
import com.dataservicios.ttauditpromotoriabayer.BaseAlbumDirFactory;
import com.dataservicios.ttauditpromotoriabayer.FroyoAlbumDirFactory;
import com.dataservicios.ttauditpromotoriabayer.Model.Media;
import com.dataservicios.ttauditpromotoriabayer.R;
import com.dataservicios.ttauditpromotoriabayer.util.BitmapLoader;

import java.io.File;
import java.util.List;

/**
 * Created by Jaime on 29/08/2016.
 */
public class MediaAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Media> mediaItems;
    //ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private AlbumStorageDirFactory mAlbumStorageDirFactory = null;

    public MediaAdapter(Activity activity, List<Media> mediaItems) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            mAlbumStorageDirFactory = new FroyoAlbumDirFactory();
        } else {
            mAlbumStorageDirFactory = new BaseAlbumDirFactory();
        }

        this.activity = activity;
        this.mediaItems = mediaItems;
    }




    @Override
    public int getCount() {
        return mediaItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mediaItems.get(position);

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //View view = convertView;
        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_row_media, null);

       // if (imageLoader == null)  imageLoader = AppController.getInstance().getImageLoader();

        //NetworkImageView thumbNail = (NetworkImageView) convertView.findViewById(R.id.thumbnail);
        ImageView thumbNail = (ImageView) convertView.findViewById(R.id.thumbnail) ;
        TextView tvId = (TextView) convertView.findViewById(R.id.tvId);
        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        TextView tvFecha = (TextView) convertView.findViewById(R.id.tvFecha);

        //ImageView imgStatus = (ImageView) convertView.findViewById(R.id.imgStatus);

        Media m = mediaItems.get(position);

        //thumbNail.setImageUrl(m.getImage(), imageLoader);
        String pathFile = BitmapLoader.getAlbumDirTemp(activity).getAbsolutePath() + "/" + m.getFile() ;
        File imgFile = new File(pathFile);


        if(imgFile.exists()){
//            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
//            thumbNail.setImageBitmap(myBitmap);
//            thumbNail.setImageBitmap(myBitmap);
//            //thumbNail.setImageURI(Uri.fromFile(imgFile));

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(), options);
            thumbNail.setImageBitmap(myBitmap);

        }


        tvId.setText(String.valueOf(m.getId()));

        tvName.setText(m.getFile());

        tvFecha.setText(m.getCreated_at().toString());

        return convertView;
    }

    @Override
    public boolean isEnabled(int position) {

        // Deshabilitando los items del adptador segun el statu
//        if( mediaItems.get(position).getActive()==1){
//
//            return false;
//
//        }
        return true;
    }



}
