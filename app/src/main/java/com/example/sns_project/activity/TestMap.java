package com.example.sns_project.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.sns_project.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.net.URL;
import java.util.ArrayList;

public class TestMap extends AppCompatActivity implements AutoPermissionsListener
{
    private GoogleMap map;
    public static String tts, tts2;
    SupportMapFragment mapFragment;
    public static String mapx;
    public static String mapy;
    GoogleMap.OnMarkerClickListener markerClickListener;
    LocationManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_map);
        System.out.println("TestMap 입니다.");

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                startLocationService();
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                map.setMyLocationEnabled(true);
                map.setOnMarkerClickListener(markerClickListener);

            }
        });

        try {
            MapsInitializer.initialize(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        AutoPermissions.Companion.loadAllPermissions(this, 100);
    }

    private void startLocationService() {
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        try {
            int chk1 = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
            int chk2 = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);

            Location location = null;
            if (chk1 == PackageManager.PERMISSION_GRANTED && chk2 == PackageManager.PERMISSION_GRANTED) {
                location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            } else {
                return;
            }

            if (location != null) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                String msg = "최근 위치 ->  Latitue : " + latitude + "\nLongitude : " + longitude;
                showCurrentLocation(latitude, longitude);
            }

            GPSListener gpsListener = new GPSListener();
            long minTime = 10000;
            float minDistance = 0;

            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, gpsListener);



        } catch (
                Exception e) {
            e.printStackTrace();
        }
    }


    class GPSListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            Double latitude = location.getLatitude();
            Double longitude = location.getLongitude();

            String message = "내 위치 -> Latitude : " + latitude + "\nLongitude:" + longitude;
            Log.d("Map", message);
            mapx = String.valueOf(longitude);
            mapy = String.valueOf(latitude);
            try {
                apiParserSearch();
            } catch (Exception e) {
                e.printStackTrace();
            }


            showCurrentLocation(latitude, longitude);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }
    }
    private void showCurrentLocation(double latitude, double longitude) {
        LatLng curPoint = new LatLng(latitude, longitude);
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(curPoint, 15));
    }

    @Override
    public void onDenied(int i, String[] strings) {
    }

    @Override
    public void onGranted(int i, String[] strings) {
        Toast.makeText(this, "permissions granted : " + strings.length, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AutoPermissions.Companion.parsePermissions(this, requestCode, permissions, this);
    }
    //TODO 이부분
    static ArrayList<TourDTO> apiParserSearch() throws Exception
    {
        URL url = new URL(getURLParam(mapx,mapy));
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();
        BufferedInputStream bis = new BufferedInputStream(url.openStream());
        xpp.setInput(bis, "utf-8");

        String tag = null;
        int event_type = xpp.getEventType();

        ArrayList<TourDTO> list = new ArrayList<TourDTO>();

        String xpos = null, ypos = null, name = null;
        while (event_type != XmlPullParser.END_DOCUMENT)
        {
            if (event_type == XmlPullParser.START_TAG)
            {
                tag = xpp.getName();
            }
            else if (event_type == XmlPullParser.TEXT)
            {
                if (tag.equals("longitude"))
                {
                    xpos = xpp.getText();
                    System.out.println(xpos);
                }
                else if (tag.equals("latitude"))
                {
                    ypos = xpp.getText();
                }
                else if (tag.equals("dutyName"))
                {
                    name = xpp.getText();
                }
            } else if (event_type == XmlPullParser.END_TAG)
            {
                tag = xpp.getName();
                if (tag.equals("item"))
                {
                    TourDTO entity = new TourDTO();
                    entity.setXpos(Double.valueOf(xpos));
                    entity.setYpos(Double.valueOf(ypos));
                    entity.setName(name);

                    list.add(entity);
                }
            }
            event_type = xpp.next();
        }
        System.out.println(list.size());

        return list;
    }

    static String getURLParam(String mapx, String mapy)
    {
        String url = "http://apis.data.go.kr/B552657/HsptlAsembySearchService/getHsptlMdcncLcinfoInqire?servicekey=8Px1w557AgfzJyJjw31zplaMuGSfquTk45%2Bo9%2FHdDRE4AQGNh%2BzwipiQdPyuTUkHHgSCAfjI7hKPuWpoSrXzOg%3D%3D&";
        if (mapx != null)
        {
            url = url + "WGS84_LON=" + mapx; //TODO 경도 관련 명령어
            if (mapy != null)
            {
                url = url + "&WGS84_LAT=" + mapy + "&pageNo=1&numOfRows=20";//TODO 위도 관련 명령어 + 위도 경도 입력 후 쓰여지는 주소입력
                tts = url;
                System.out.println("url : " + url);
            }
        }
        return url;
    }


}