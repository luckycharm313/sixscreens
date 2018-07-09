package com.tigerphp.sixscreensdemo.sixscreensdemo.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tigerphp.sixscreensdemo.sixscreensdemo.R;
import com.tigerphp.sixscreensdemo.sixscreensdemo.app.ImageLoader;
import com.tigerphp.sixscreensdemo.sixscreensdemo.app.PreferenceManager;
import com.tigerphp.sixscreensdemo.sixscreensdemo.models.Pusher;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.tigerphp.sixscreensdemo.sixscreensdemo.app.AppConstants.EVENT_BUS_NEW_USER_PHOTO;
import static com.tigerphp.sixscreensdemo.sixscreensdemo.app.AppConstants.EVENT_BUS_UPDATE_BREAK;

/**
 * Created by luckycharm on 7/8/18.
 */

public class DashboardFragment extends Fragment {

    @BindView(R.id.layout_clocked_out)
    RelativeLayout layout_clocked_out;
    @BindView(R.id.layout_clocked_in)
    RelativeLayout layout_clocked_in;
    @BindView(R.id.layout_meal_break)
    RelativeLayout layout_meal_break;

    @BindView(R.id.img_avatar_in)
    CircleImageView img_avatar_in;

    @BindView(R.id.txt_jobName)
    TextView txt_jobName;
    @BindView(R.id.txt_clockIn_time)
    TextView txt_clockIn_time;
    @BindView(R.id.txt_job_title)
    TextView txt_job_title_clocked_Out;
    @BindView(R.id.txt_job_title_clocked_in)
    TextView txt_job_title_clocked_in;
    @BindView(R.id.txt_address)
    TextView txt_address;

    @BindView(R.id.txt_break_title)
    TextView txt_break_title;

    // break
    @BindView(R.id.txt_break_type_name)
    TextView txt_break_type_name;
    @BindView(R.id.txt_break_time)
    TextView txt_break_time;
    @BindView(R.id.txt_address_break)
    TextView txt_address_break;
    @BindView(R.id.txt_job_title_break)
    TextView txt_job_title_break;
    @BindView(R.id.img_avatar_break)
    CircleImageView img_avatar_break;


    private static final int LOCATION_PERMISSION_REQUEST_CODE = 11;
    View mView;
    private long startTime = 0L;
    private Handler customHandler = new Handler();
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;
    String jobSelectedTitle = "Basement Slab Preparation";
    String typeSelectedBreak = "Lunch";
    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    Geocoder geocoder;
    String currentAddress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.tab_dashboard, container, false);
        ButterKnife.bind(this, mView);
        initializerView();
        return mView;
    }

    private void initializerView() {
        layout_clocked_out.setVisibility(View.VISIBLE);
        layout_clocked_in.setVisibility(View.GONE);
        layout_meal_break.setVisibility(View.GONE);
    }


    @Override
    public void onStart() {
        EventBus.getDefault().register(this);
        super.onStart();
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();

        String jobTitle = PreferenceManager.getTest(getActivity());
        if(jobTitle != null){
            jobSelectedTitle = jobTitle;
        }
        txt_job_title_clocked_Out.setText(jobSelectedTitle);
        txt_job_title_clocked_in.setText(jobSelectedTitle);
        txt_job_title_break.setText(jobSelectedTitle);

        String breakType = PreferenceManager.getBreakType(getActivity());
        if(breakType != null)
            typeSelectedBreak = breakType;

        txt_break_title.setText(typeSelectedBreak);

    }

    public void currentLocation(int flag) {

        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    mMap = googleMap;
                    enableMyLocationIfPermitted();
//                    mMap.getUiSettings().setZoomControlsEnabled(true);
                    mMap.setMinZoomPreference(10);
                    geocoder = new Geocoder(getActivity(), Locale.getDefault());

                    mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {

                        @Override
                        public void onMyLocationChange(Location arg0) {


                            List<Address> addresses = new ArrayList<>();
                            try {
                                addresses = geocoder.getFromLocation(arg0.getLatitude(), arg0.getLongitude(),1);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if (addresses.size() > 0) {
                                currentAddress = addresses.get(0).getFeatureName() + ", " + addresses.get(0).getLocality() +", " + addresses.get(0).getAdminArea() + ", " + addresses.get(0).getCountryName();
                            }

                            if(flag == 1){
                                txt_address.setText(currentAddress);
                            }
                            else{
                                txt_address_break.setText(currentAddress);
                            }

                            // TODO Auto-generated method stub
                            LatLng latLng = new LatLng(arg0.getLatitude(), arg0.getLongitude());
                            mMap.addMarker(new MarkerOptions().position(latLng)
                                    .title(currentAddress));
                            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                        }
                    });
                }
            });
        }

        if(flag == 1){
            getChildFragmentManager().beginTransaction().replace(R.id.mapView_clockedIn, mapFragment).commit();
        }
        else{
            getChildFragmentManager().beginTransaction().replace(R.id.mapView_break, mapFragment).commit();
        }

    }

    private void enableMyLocationIfPermitted() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else if (mMap != null) {
            mMap.setMyLocationEnabled(true);
        }
    }

    /**
     * method of EventBus
     *
     * @param pusher this is parameter of onEventMainThread method
     */
    @SuppressWarnings("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(Pusher pusher) {
        switch (pusher.getAction()) {
            case EVENT_BUS_NEW_USER_PHOTO:
                boolean isClockIn = pusher.isClockIn();

                if(isClockIn){
                    customHandler.removeCallbacks(updateTimerThread);
                    mapFragment = null;
                    geocoder = null;
                    mMap = null;

                    layout_clocked_out.setVisibility(View.GONE);
                    layout_meal_break.setVisibility(View.GONE);
                    layout_clocked_in.setVisibility(View.VISIBLE);

                    String photoUrl = pusher.getPhotoURL();
                    String job_title = pusher.getJobName();

                    startTime = SystemClock.uptimeMillis();
                    customHandler.postDelayed(updateTimerThread, 0);
                    txt_jobName.setText(job_title);
                    ImageLoader.loadCircleImage(getActivity().getApplicationContext(), photoUrl, img_avatar_in, R.drawable.avatar);

                    currentLocation(1);
                }
                else{
                    customHandler.removeCallbacks(updateTimerThread);
                    layout_clocked_out.setVisibility(View.VISIBLE);
                    layout_clocked_in.setVisibility(View.GONE);
                    layout_meal_break.setVisibility(View.GONE);
                }
                break;
            case EVENT_BUS_UPDATE_BREAK:
                customHandler.removeCallbacks(updateTimerThread);
                mapFragment = null;
                geocoder = null;
                mMap = null;

                layout_clocked_out.setVisibility(View.GONE);
                layout_clocked_in.setVisibility(View.GONE);
                layout_meal_break.setVisibility(View.VISIBLE);
                String photoUrl = pusher.getPhotoURL();
                String _breakType = pusher.getBreakType();

                startTime = SystemClock.uptimeMillis();
                customHandler.postDelayed(updateTimerThreadBreak, 0);

                txt_break_type_name.setText(_breakType);
                ImageLoader.loadCircleImage(getActivity().getApplicationContext(), photoUrl, img_avatar_break, R.drawable.avatar);
                currentLocation(2);



                break;
//            case EVENT_BUS_UPDATE_JOB_TITLE:
//                boolean _isClockIn = pusher.isClockIn();
//                Log.e("tag", "job Name : "+ pusher.getJobName());
//                txt_job_title.setText(pusher.getJobName());
//                break;
        }
    }

    private Runnable updateTimerThread = new Runnable() {

        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updatedTime = timeSwapBuff + timeInMilliseconds;
            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            int hours = mins / 60;
            secs = secs % 60;
            txt_clockIn_time.setText("" + hours + "h:" + mins + "m:" + String.format("%02d", secs)+"s");
            customHandler.postDelayed(this, 0);
        }
    };

    private Runnable updateTimerThreadBreak = new Runnable() {

        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updatedTime = timeSwapBuff + timeInMilliseconds;
            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            int hours = mins / 60;
            secs = secs % 60;
            txt_break_time.setText("" + hours + "h:" + mins + "m:" + String.format("%02d", secs)+"s");
            customHandler.postDelayed(this, 0);
        }
    };
}
