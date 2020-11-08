package com.example.sns_project.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.sns_project.R;
import com.example.sns_project.activity.TourDTO;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.example.sns_project.fragment.DayTimeFragment.checkinDate;
import static com.example.sns_project.fragment.DayTimeFragment.checkoutDate;

public class NowLocation_V2 extends Fragment implements OnMapReadyCallback {

    private FragmentActivity mContext;

    private static final String TAG = NowLocation_V2.class.getSimpleName();
    private GoogleMap mMap;
    private MapView mapView = null;
    private Marker currentMarker = null;

    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LocationRequest locationRequest;
    private Location mCurrentLocation;

    private final LatLng mDefaultLocation = new LatLng(37.56, 126.97);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int UPDATE_INTERVAL_MS = 1000 * 60 * 1;
    private static final int FASTEST_UPDATE_INTERVAL_MS = 1000 * 30;

    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    private static int checkinMonth;
    private static int checkoutMonth;
    private static String eventStartDate;
    private static String eventEndDate;

    public static String mapx;
    public static String mapy;
    public static TextView placename;
    public static TextView Startdate;
    public static ImageView imageView2;
    public static ImageButton imgButton;
    private RelativeLayout LLayout;
    public static String Title;
    public static String tts;

    public NowLocation_V2() {
    }

    @Override
    public void onAttach(Activity activity) { // Fragment 가 Activity에 attach 될 때 호출된다.
        mContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        // Layout 을 inflate 하는 곳이다.
        if (savedInstanceState != null) {
            mCurrentLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            CameraPosition mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }
        View layout = inflater.inflate(R.layout.fragment_festival_map, container, false);
        LLayout = layout.findViewById(R.id.Llayout);
        LLayout.setVisibility(View.INVISIBLE);
        mapView = layout.findViewById(R.id.mapView);

        if (mapView != null) {
            mapView.onCreate(savedInstanceState);
        }
        mapView.getMapAsync(this);
        checkinMonth = DayTimeFragment.checkinMonth;
        checkoutMonth = DayTimeFragment.checkoutMonth;

        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MapsInitializer.initialize(mContext);

        locationRequest = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL_MS)
                .setFastestInterval(FASTEST_UPDATE_INTERVAL_MS);

        LocationSettingsRequest.Builder builder =
                new LocationSettingsRequest.Builder();

        builder.addLocationRequest(locationRequest);

        // FusedLocationProviderClient 객체 생성
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mContext);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker marker) {
                String snippet = marker.getSnippet();
                String startdate = snippet.substring(0,8);
                String enddate = snippet.substring(11,19);
                System.out.println("startdate : "+startdate);
                System.out.println("enddate : "+enddate);
                TextView start = getView().findViewById(R.id.eventstartdate);
                start.setText("축제기간\n"+startdate+"\n~\n"+enddate);
                imgButton = getView().findViewById(R.id.imgButton);
                imgButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        LLayout.setVisibility(View.INVISIBLE);
                    }
                });
                Title = marker.getTitle();
                Title = Title.replace(" ", "%20");/*
                try {
                    apiParserSearch2();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }*/
                return false;

            }
        });
        setDefaultLocation();
        getLocationPermission();
        updateLocationUI();
        getDeviceLocation();
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mCurrentLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void setDefaultLocation() {
        if (currentMarker != null) currentMarker.remove();

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(mDefaultLocation);
        markerOptions.title("위치정보 가져올 수 없음");
        markerOptions.snippet("위치 퍼미션과 GPS 활성 여부 확인하세요");
        markerOptions.draggable(true);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        currentMarker = mMap.addMarker(markerOptions);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(mDefaultLocation, 15);
        mMap.moveCamera(cameraUpdate);
    }

    String getCurrentAddress(LatLng latlng) {
        List<Address> addressList = null;
        Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
        try {
            addressList = geocoder.getFromLocation(latlng.latitude, latlng.longitude, 1);
        } catch (IOException e) {
            Toast.makeText(mContext, "위치로부터 주소를 인식할 수 없습니다. 네트워크가 연결되어 있는지 확인해 주세요.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return "주소 인식 불가";
        }

        if (addressList.size() < 1) {
            return "해당 위치에 주소 없음";
        }

        Address address = addressList.get(0);
        StringBuilder addressStringBuilder = new StringBuilder();
        for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
            addressStringBuilder.append(address.getAddressLine(i));
            if (i < address.getMaxAddressLineIndex())
                addressStringBuilder.append("\n");
        }

        return addressStringBuilder.toString();
    }

    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);

            List<Location> locationList = locationResult.getLocations();

            if (locationList.size() > 0) {
                Location location = locationList.get(locationList.size() - 1);

                LatLng currentPosition
                        = new LatLng(location.getLatitude(), location.getLongitude());

                String markerTitle = getCurrentAddress(currentPosition);
                String markerSnippet = "위도:" + String.valueOf(location.getLatitude())
                        + " 경도:" + String.valueOf(location.getLongitude());
                mapx = String.valueOf(location.getLongitude());
                mapy = String.valueOf(location.getLatitude());
                System.out.println("mapx : "+mapx);
                System.out.println("mapy : "+mapy);
                getURLParam(mapx,mapy);

                Log.d(TAG, "Time :" + CurrentTime() + " onLocationResult : " + markerSnippet);
                setCurrentLocation(location, markerTitle, markerSnippet);
                mCurrentLocation = location;
                String eventstart = checkinDate;
                String eventend = checkoutDate;
                if(eventstart!=null)
                {
                    try {
                        apiParserSearch();
                        setUpMap();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    };

    private String CurrentTime() {
        Date today = new Date();
        SimpleDateFormat date = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat time = new SimpleDateFormat("hh:mm:ss a");
        return time.format(today);
    }

    public void setCurrentLocation(Location location, String markerTitle, String markerSnippet) {
        if (currentMarker != null) currentMarker.remove();

        LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(currentLatLng);
        markerOptions.title(markerTitle);
        markerOptions.snippet(markerSnippet);
        markerOptions.draggable(true);

        //currentMarker = mMap.addMarker(markerOptions);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(currentLatLng);
        mMap.moveCamera(cameraUpdate);

    }

    private void getDeviceLocation() {
        try {
            if (mLocationPermissionGranted) {
                mFusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(mContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(mContext,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }


    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @Override
    public void onStart() { // 유저에게 Fragment가 보이도록 해준다.
        super.onStart();
        mapView.onStart();
        Log.d(TAG, "onStart ");
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
        if (mFusedLocationProviderClient != null) {
            Log.d(TAG, "onStop : removeLocationUpdates");
            mFusedLocationProviderClient.removeLocationUpdates(locationCallback);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        if (mLocationPermissionGranted) {
            Log.d(TAG, "onResume : requestLocationUpdates");
            if (ActivityCompat.checkSelfPermission(getView().getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getView().getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mFusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
            if (mMap!=null)
                mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroyView() { // 프래그먼트와 관련된 View 가 제거되는 단계
        super.onDestroyView();
        if (mFusedLocationProviderClient != null) {
            Log.d(TAG, "onDestroyView : removeLocationUpdates");
            mFusedLocationProviderClient.removeLocationUpdates(locationCallback);
        }
    }

    @Override
    public void onDestroy() {
        // Destroy 할 때는, 반대로 OnDestroyView에서 View를 제거하고, OnDestroy()를 호출한다.
        super.onDestroy();
        mapView.onDestroy();
    }
/*
    private void setUpMap()
    {
        FestivalMap parser = new FestivalMap();
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
            options.snippet(entity.getEventStartDate()+" ~ "+entity.getEventEndDate());
            BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.free);
            Bitmap b=bitmapdraw.getBitmap();
            Bitmap smallMarker = Bitmap.createScaledBitmap(b, 100, 100, false);
            options.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));

            mMap.addMarker(options);
            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
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

    }*/
/*
    public ArrayList<TourDTO> apiParserSearch2() throws Exception
    {
        URL url = new URL(getURLParam2(Title));
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();
        BufferedInputStream bis = new BufferedInputStream(url.openStream());
        xpp.setInput(bis, "utf-8");

        String tag = null;
        int event_type = xpp.getEventType();

        ArrayList<TourDTO> list = new ArrayList<TourDTO>();

        String xpos = null, ypos = null, name = null, addr1 = null, firstimage = null, secondimage = null, eventenddate = null, eventstartdate = null;
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
                else if (tag.equals("eventstartdate"))
                {
                    eventstartdate = xpp.getText();
                }
                else if (tag.equals("eventenddate"))
                {
                    eventenddate = xpp.getText();
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
                    entity.setEventStartDate(eventstartdate);
                    entity.setEventEndDate(eventenddate);
                    entity.setImage1(firstimage);
                    list.add(entity);
                }
            }
            event_type = xpp.next();
        }
        LLayout.setVisibility(View.VISIBLE);
        placename = getView().findViewById(R.id.placename);
        //Startdate = getView().findViewById(R.id.eventstartdate);
        imageView2 = getView().findViewById(R.id.imageView2);
        placename.setText(name);
        //Startdate.setText("축제기간\n"+eventstartdate+"\n~\n"+eventenddate);
        Glide.with(this).load(firstimage).into(imageView2);

        return list;
    }

    public static String getURLParam2(String keyword)
    {
        String url = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/searchKeyword?serviceKey=axOWzdA%2Ft%2BFO89n8RY1laQLwOgKCba1RfMVxBXmfb9m4mdbSryUpClG1seyeoXHudXIHFYNT3%2F3HagA11q28YA%3D%3D&MobileApp=AppTest&MobileOS=ETC&pageNo=1&numOfRows=10&listYN=Y&arrange=A&contentTypeId=15&areaCode=&sigunguCode=&cat1=&cat2=&cat3=&keyword=";
        url = url + keyword;
        return url;
    }
*/
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

        String xpos = null, ypos = null, name = null, addr1 = null, firstimage = null, tel = null;
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

    private void setUpMap()
    {
        NowLocation_V2 parser = new NowLocation_V2();
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

            mMap.addMarker(options);
            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
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