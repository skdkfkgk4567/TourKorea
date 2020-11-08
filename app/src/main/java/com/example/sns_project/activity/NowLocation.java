package com.example.sns_project.activity;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.sns_project.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class NowLocation extends AppCompatActivity implements AutoPermissionsListener, GoogleMap.OnMarkerClickListener {

    private GoogleMap map;
    public static String tts, tts2;
    SupportMapFragment mapFragment;
    public static String mapx;
    public static String mapy;
    public static TextView placename;
    public static TextView address;
    public static TextView telnumber;
    public static ImageView imageView2;
    public static ImageView imageView3;
    public static ImageButton imgButton;
    private RelativeLayout LLayout;
    public static String Title;
    private Context mContext;
    private Handler handler = new Handler();
    GoogleMap googleMap;
    GoogleMap.OnMarkerClickListener markerClickListener;
    LocationManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        imageView2 = findViewById(R.id.imageView2);
        imageView3 = findViewById(R.id.imageView3);
        placename = findViewById(R.id.placename);
        address = findViewById(R.id.address);
        telnumber = findViewById(R.id.telnumber);
        imgButton = findViewById(R.id.imgButton);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        System.out.println("NowLocation 입니다.");
        setContentView(R.layout.activity_now);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mapFragment.getMapAsync(googleMap -> {
            map = googleMap;
            LLayout = findViewById(R.id.Llayout);
            LLayout.setVisibility(View.INVISIBLE);

            startLocationService();
            map.setMyLocationEnabled(true);
            map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

                @Override
                public boolean onMarkerClick(Marker marker) {
                    imgButton = findViewById(R.id.imgButton);
                    imgButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v)
                        {
                            LLayout.setVisibility(View.INVISIBLE);
                        }
                    });
                    Title = marker.getTitle();
                    Title = Title.replace(" ", "%20");
                    try {
                        apiParserSearch2();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return false;

                }
            });
        });



        try {
            MapsInitializer.initialize(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    apiParserSearch();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        },4000);

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

    @Override
    public boolean onMarkerClick(Marker marker) {

        return false;
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

            } catch (Exception e) {
                e.printStackTrace();
            }
            setUpMap();

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
        //map.animateCamera(CameraUpdateFactory.newLatLngZoom(curPoint, 15));
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

    public ArrayList<TourDTO> apiParserSearch2() throws Exception
    {
        URL url = new URL(getURLParam2(Title));

        System.out.println(url);
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();
        BufferedInputStream bis = new BufferedInputStream(url.openStream());
        xpp.setInput(bis, "utf-8");

        String tag = null;
        int event_type = xpp.getEventType();

        ArrayList<TourDTO> list = new ArrayList<TourDTO>();

        String xpos = null, ypos = null, name = null, addr1 = null, firstimage = null, secondimage = null, tel = null;
        while (event_type != XmlPullParser.END_DOCUMENT)
        {
            if (event_type == XmlPullParser.START_TAG)
            {
                tag = xpp.getName();
            }
            else if (event_type == XmlPullParser.TEXT)
            {
                if (tag.equals("mapx"))
                {
                    xpos = xpp.getText();
                }
                else if (tag.equals("mapy"))
                {
                    ypos = xpp.getText();
                }
                else if (tag.equals("title"))
                {
                    name = xpp.getText();
                }
                else if (tag.equals("addr1"))
                {
                    addr1 = xpp.getText();
                }
                else if (tag.equals("firstimage"))
                {
                    firstimage = xpp.getText();
                }
                else if (tag.equals("firstimage2"))
                {
                    secondimage = xpp.getText();
                }
                else if (tag.equals("tel"))
                {
                    tel = xpp.getText();
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
                    entity.setAddr(addr1);
                    entity.setTel(tel);
                    entity.setImage1(firstimage);
                    list.add(entity);
                }
            }
            event_type = xpp.next();
        }
        LLayout.setVisibility(View.VISIBLE);
        placename = findViewById(R.id.placename);
        address = findViewById(R.id.address);
        telnumber = findViewById(R.id.telnumber);
        imageView2 = findViewById(R.id.imageView2);
        imageView3 = findViewById(R.id.imageView3);
        placename.setText(name);
        address.setText(addr1);
        telnumber.setText(tel);
        if(firstimage.equals(secondimage))
        {
            imageView3.setVisibility(View.INVISIBLE);
        }
        else
        {
            Glide.with(this).load(secondimage).into(imageView3);
        }
        System.out.println("firstimage " + firstimage);
        Glide.with(this).load(firstimage).into(imageView2);

        return list;
    }

    public static String getURLParam2(String keyword)
    {
        String url = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/searchKeyword?serviceKey=yjLZTJHFbgy0YqvubJsxK1Izf%2FW%2ByFs94A%2F0M046ZxHpCCwpWQ84MCnhvwz%2FipI8kKSdJXvZ7D5qZWqCtmSVYA%3D%3D&MobileApp=AppTest&MobileOS=ETC&pageNo=1&numOfRows=10&listYN=Y&arrange=A&contentTypeId=15&areaCode=&sigunguCode=&cat1=&cat2=&cat3=&keyword=";
        if (mapx != null)
        {
            url = url + keyword;
            System.out.println("keyword : "+keyword);
        }
        return url;
    }

    public static ArrayList<TourDTO> apiParserSearch() throws Exception
    {
        System.out.println("apiParserSearch 동작 ");
        URL url = new URL(getURLParam(mapx,mapy));
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();
        BufferedInputStream bis = new BufferedInputStream(url.openStream());
        xpp.setInput(bis, "utf-8");

        String tag = null;
        int event_type = xpp.getEventType();

        ArrayList<TourDTO> list = new ArrayList<TourDTO>();

        String xpos = null, ypos = null, name = null, addr1 = null, firstimage = null, secondimage = null, tel = null;
        while (event_type != XmlPullParser.END_DOCUMENT)
        {
            if (event_type == XmlPullParser.START_TAG)
            {
                tag = xpp.getName();
            }
            else if (event_type == XmlPullParser.TEXT)
            {
                if (tag.equals("mapx"))
                {
                    xpos = xpp.getText();
                }
                else if (tag.equals("mapy"))
                {
                    ypos = xpp.getText();
                }
                else if (tag.equals("title"))
                {
                    name = xpp.getText();
                }
                else if (tag.equals("addr1"))
                {
                    addr1 = xpp.getText();
                }
                else if (tag.equals("firstimage"))
                {
                    firstimage = xpp.getText();
                }
                else if (tag.equals("firstimage2"))
                {
                    secondimage = xpp.getText();
                }
                else if (tag.equals("tel"))
                {
                    tel = xpp.getText();
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
                    entity.setAddr(addr1);
                    entity.setTel(tel);
                    entity.setImage1(firstimage);
                    list.add(entity);
                }
            }
            event_type = xpp.next();
        }
        System.out.println(list.size());

        return list;
    }

    public static String getURLParam(String mapx, String mapy)
    {
        String url = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/locationBasedList?serviceKey=yjLZTJHFbgy0YqvubJsxK1Izf%2FW%2ByFs94A%2F0M046ZxHpCCwpWQ84MCnhvwz%2FipI8kKSdJXvZ7D5qZWqCtmSVYA%3D%3D&numOfRows=100&pageNo=1&MobileOS=AND&MobileApp=AppTest&arrange=E&contentTypeId=15";
        //String url = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/searchFestival?ServiceKey=yjLZTJHFbgy0YqvubJsxK1Izf%2FW%2ByFs94A%2F0M046ZxHpCCwpWQ84MCnhvwz%2FipI8kKSdJXvZ7D5qZWqCtmSVYA%3D%3D&eventStartDate=20201101&eventEndDate=20201130&areaCode=&sigunguCode=&cat1=A02&cat2=&cat3=&listYN=Y&MobileOS=AND&MobileApp=AppTest&arrange=A&numOfRows=12&pageNo=1";
        //위 url : 위치기반 검색
        //아래url : 전국 해당기간 축제 검색
        if (mapx != null)
        {
            url = url + "&mapX=" + mapx;
            if (mapy != null)
            {
                url = url + "&mapY=" + mapy + "&radius=20000&listYN=Y&modifiedtime=&";
                tts = url;
                System.out.println("url : " + url);
            }
        }
        return url;
    }

    public static void main(String[] args)
    {
        new NowLocation();
    }

    private void setUpMap()
    {
        NowLocation parser = new NowLocation();
        ArrayList<TourDTO> list = null;
        try {
            list =parser.apiParserSearch();
        } catch (Exception e) {
            e.printStackTrace();
        }

        for(TourDTO entity : list) {
            MarkerOptions options = new MarkerOptions();
            options.position(new LatLng(entity.getYpos(), entity.getXpos()));
            options.title(entity.getName());
            options.snippet(entity.getAddr());
            options.snippet(entity.getTel());
            BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.free);
            Bitmap b=bitmapdraw.getBitmap();
            Bitmap smallMarker = Bitmap.createScaledBitmap(b, 100, 100, false);
            options.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));

            map.addMarker(options);
            map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                @Override
                public View getInfoWindow(Marker marker) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {
                    return null;
                }
            });

        }

    }



}