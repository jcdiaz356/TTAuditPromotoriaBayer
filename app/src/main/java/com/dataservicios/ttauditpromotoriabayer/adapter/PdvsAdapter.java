package com.dataservicios.ttauditpromotoriabayer.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.dataservicios.ttauditpromotoriabayer.Model.Pdv;
import com.dataservicios.ttauditpromotoriabayer.R;
import com.dataservicios.ttauditpromotoriabayer.app.AppController;

import java.util.List;


/**
 * Created by usuario on 12/01/2015.
 */
public class PdvsAdapter extends BaseAdapter {
    private Context activityParent ;
    private Activity activity;
    private LayoutInflater inflater;
    private List<Pdv> pdvItems;
    private int idRuta;
    //final Pdv m ;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public PdvsAdapter(Activity activity, List<Pdv> rutaItems, int idRuta) {
        this.activity = activity;
        this.pdvItems = rutaItems;
        this.idRuta = idRuta;
    }

    @Override
    public int getCount() {
        return pdvItems.size();
    }

    @Override
    public Object getItem(int location) {
        return pdvItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {

//
//        if (inflater == null)
//            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        if (convertView == null)
//            convertView = inflater.inflate(R.layout.list_row_pdvs, null);

        View view =null;
        convertView = null;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.list_row_pdvs, parent, false);
        }
        activityParent= parent.getContext();
       NetworkImageView thumbNail = (NetworkImageView) convertView.findViewById(R.id.thumbnail);
        TextView idPdv = (TextView) convertView.findViewById(R.id.tvId);
        TextView pdv = (TextView) convertView.findViewById(R.id.tvPdv);
        TextView direccion = (TextView) convertView.findViewById(R.id.tvDireccion);
        TextView distrito = (TextView) convertView.findViewById(R.id.tvDistrito);
        TextView region = (TextView) convertView.findViewById(R.id.tvRegion);
        TextView storeId = (TextView) convertView.findViewById(R.id.tvStoreId);
        TextView type = (TextView) convertView.findViewById(R.id.tvType);
        TextView typeBodega = (TextView) convertView.findViewById(R.id.tvTypeBodega);
        ImageView imgStatus = (ImageView) convertView.findViewById(R.id.imgStatus);
        Button bt_do = (Button) convertView.findViewById(R.id.bt_do);

        // getting ruta data for the row
        final Pdv m = pdvItems.get(position);
        idPdv.setText(String.valueOf(m.getId()));
        // thumbnail image
       // thumbNail.setImageUrl(m.getThumbnailUrl(), imageLoader);
        // rutaDia
        pdv.setText(m.getPdv());
        // pdvs
        direccion.setText( m.getDireccion());
        distrito.setText(m.getDistrito());
        region.setText(m.getRegion());
        type.setText(m.getType());
        typeBodega.setText(m.getTypeBodega());

        storeId.setText("ID: " + m.getId());

        if(m.getStatus()==0){

            imgStatus.setImageResource(R.drawable.ic_check_off);

        } else if(m.getStatus()==1){
            imgStatus.setImageResource(R.drawable.ic_check_on);

        }

        if (m.getTypeBodega().equals("CONGLOMERADO") || m.getTypeBodega().equals("6D")  ){

            bt_do.setVisibility(View.VISIBLE);
            bt_do.setEnabled(true);

        } else {
            bt_do.setVisibility(View.INVISIBLE);
            bt_do.setEnabled(false);
        }


//        bt_do.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Bundle argRuta = new Bundle();
//                argRuta.clear();
//                argRuta.putInt("store_id", m.getId());
//                argRuta.putInt("idRuta",idRuta);
//
//                Intent intent;
//                intent = new Intent(parent.getContext(), AceptoPremio.class);
//                intent.putExtras(argRuta);
//                parent.getContext().startActivity(intent);
//
//                // Toast.makeText(activityParent,  String.valueOf(m.getId()), Toast.LENGTH_SHORT).show();
//                Toast.makeText(activityParent,  String.valueOf("Premiación no está disponible en este momento"), Toast.LENGTH_SHORT).show();
//            }
//        });
        return convertView;
    }

    @Override
    public boolean isEnabled(int position) {

        // Deshabilitando los items del adptador segun el statu
        if( pdvItems.get(position).getStatus()==1){
            return false;
        }
        return true;
    }

}
