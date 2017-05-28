package com.dataservicios.ttauditpromotoriabayer;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by usuario on 29/11/2014.
 */
public class GraficosFragment  extends Fragment {

    public GraficosFragment() {
    }
    public static MapView mapView;
    public static GoogleMap map;

    private static final LatLng MELBOURNE = new LatLng(-12.04513488,-77.02851775) ;
    private static final LatLng MELBOURNE2 = new LatLng(-11.917178, -77.041926);
    private static final LatLng MELBOURNE3 = new LatLng(-11.922590,-77.044282);
    private LatLngBounds AUSTRALIA;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_grafico, container, false);
        try {
            // Gets the MapView from the XML layout and creates it
            mapView = (MapView) v.findViewById(R.id.mapview);
            mapView.onCreate(savedInstanceState);
            mapView.onResume();
            //Añade el marker al mapa
            map.addMarker(new MarkerOptions()
                    .position(MELBOURNE2)
                    .title("Mi primer Market"));
            //Añade el marker al mapa
            Marker melbourne2 = map.addMarker(new MarkerOptions()
                    .position(MELBOURNE3)
                    .title("Melbourne")
                    .snippet("Population: 4,137,400"));

            //Mostrando información del Market ni bien carga el mapa
            melbourne2.showInfoWindow();
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(MELBOURNE, 13));
            // Gets to GoogleMap from the MapView and does initialization stuff
            map = mapView.getMap();
            // Changing map type
            map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

            // googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            // googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            // googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            // googleMap.setMapType(GoogleMap.MAP_TYPE_NONE);
            // Showing / hiding your current location
            map.setMyLocationEnabled(false);
            // Enable / Disable zooming controls
            map.getUiSettings().setZoomControlsEnabled(true);
            // Enable / Disable my location button
            map.getUiSettings().setMyLocationButtonEnabled(true);
            // Enable / Disable Compass icon
            map.getUiSettings().setCompassEnabled(true);
            // Enable / Disable Rotate gesture
            map.getUiSettings().setRotateGesturesEnabled(true);
            // Enable / Disable zooming functionality
            map.getUiSettings().setZoomGesturesEnabled(true);
           MapsInitializer.initialize(this.getActivity());
        } catch (Exception e) {
            System.out.println(e);
        }
        return v;
    }
}