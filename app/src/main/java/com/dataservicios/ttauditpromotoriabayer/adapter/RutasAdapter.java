package com.dataservicios.ttauditpromotoriabayer.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dataservicios.ttauditpromotoriabayer.Model.Ruta;
import com.dataservicios.ttauditpromotoriabayer.R;

import java.util.List;


/**
 * Created by usuario on 10/01/2015.
 */
public class RutasAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Ruta> rutaItems;
    //ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public RutasAdapter(Activity activity, List<Ruta> rutaItems) {
        this.activity = activity;
        this.rutaItems = rutaItems;
    }

    @Override
    public int getCount() {
        return rutaItems.size();
    }

    @Override
    public Object getItem(int location) {
        return rutaItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_row_rutas, null);
        TextView id = (TextView) convertView.findViewById(R.id.tvId);
        TextView rutaDia = (TextView) convertView.findViewById(R.id.tvRutaDia);
        TextView pdvs = (TextView) convertView.findViewById(R.id.tvPdvs);
        TextView porcentajeAvance = (TextView) convertView.findViewById(R.id.tvPorcentaje);

        // getting ruta data for the row
        Ruta m = rutaItems.get(position);

        // rutaDia
        id.setText(String.valueOf(m.getId()));
        rutaDia.setText( m.getRutaDia());
        // pdvs
        pdvs.setText("Nº de PDVS: " + String.valueOf(m.getPdvs()));
        // release year
        porcentajeAvance.setText("Auditados: " + String.valueOf(m.getPorcentajeAvance()));

        return convertView;
    }

}