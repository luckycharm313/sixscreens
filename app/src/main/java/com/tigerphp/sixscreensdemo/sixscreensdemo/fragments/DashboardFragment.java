package com.tigerphp.sixscreensdemo.sixscreensdemo.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.RequestParams;
import com.tigerphp.sixscreensdemo.sixscreensdemo.R;
import com.tigerphp.sixscreensdemo.sixscreensdemo.activities.BreakTypeActivity;
import com.tigerphp.sixscreensdemo.sixscreensdemo.activities.JobActivity;
import com.tigerphp.sixscreensdemo.sixscreensdemo.api.APIInterface;
import com.tigerphp.sixscreensdemo.sixscreensdemo.api.ApiManager;
import com.tigerphp.sixscreensdemo.sixscreensdemo.app.AppConstants;
import com.tigerphp.sixscreensdemo.sixscreensdemo.app.AppHelper;
import com.tigerphp.sixscreensdemo.sixscreensdemo.app.EndPoints;
import com.tigerphp.sixscreensdemo.sixscreensdemo.app.ImageLoader;
import com.tigerphp.sixscreensdemo.sixscreensdemo.app.PreferenceManager;
import com.tigerphp.sixscreensdemo.sixscreensdemo.models.Pusher;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static com.tigerphp.sixscreensdemo.sixscreensdemo.app.AppConstants.EVENT_BUS_NEW_USER_PHOTO;
import static com.tigerphp.sixscreensdemo.sixscreensdemo.app.AppConstants.EVENT_BUS_UPDATE_BREAK;
import static com.tigerphp.sixscreensdemo.sixscreensdemo.app.AppConstants.PERMISSION_REQUEST_CODE;

/**
 * Created by luckycharm on 7/8/18.
 */

public class DashboardFragment extends Fragment  implements View.OnClickListener {

    @BindView(R.id.layout_clocked_out)
    RelativeLayout layout_clocked_out;
    @BindView(R.id.layout_clocked_in)
    RelativeLayout layout_clocked_in;
    @BindView(R.id.layout_meal_break)
    RelativeLayout layout_meal_break;

    @BindView(R.id.rl_btn_right_clockout)
    RelativeLayout rl_btn_right_clockout;
    @BindView(R.id.btn_clock_in)
    Button btn_clock_in;
    @BindView(R.id.txt_job_title)
    TextView txt_job_title_clocked_Out;
    @BindView(R.id.txt_jobName)
    TextView txt_jobName;

    @BindView(R.id.img_avatar_in)
    CircleImageView img_avatar_in;


    @BindView(R.id.txt_clockIn_time)
    TextView txt_clockIn_time;
    @BindView(R.id.rl_btn_right_clockin)
    RelativeLayout rl_btn_right_clockin;

    @BindView(R.id.txt_job_title_clocked_in)
    TextView txt_job_title_clocked_in;
    @BindView(R.id.txt_address)
    TextView txt_address;

    @BindView(R.id.txt_break_title)
    TextView txt_break_title;

    @BindView(R.id.btn_clock_in_transfor)
    Button btn_clock_in_transfor;
    @BindView(R.id.btn_break)
    Button btn_break;
    @BindView(R.id.btn_check_out_clockin)
    Button btn_check_out_clockin;
    @BindView(R.id.rl_btn_right_type)
    RelativeLayout rl_btn_right_type;


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
    @BindView(R.id.btn_check_out_break)
    Button btn_check_out_break;
    @BindView(R.id.btn_clock_resume)
    Button btn_clock_resume;
    @BindView(R.id.rl_btn_right_break)
    RelativeLayout rl_btn_right_break;

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
    int flag = 0;
    JSONObject userData = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.tab_dashboard, container, false);
        ButterKnife.bind(this, mView);
        initializerView();
        return mView;
    }

    private void initializerView() {
        userData = PreferenceManager.getUserData(getActivity());

        img_avatar_break.setOnClickListener(this);
        btn_check_out_break.setOnClickListener(this);
        btn_clock_in.setOnClickListener(this);
        txt_job_title_clocked_Out.setOnClickListener(this);
        rl_btn_right_clockout.setOnClickListener(this);

        txt_job_title_clocked_in.setOnClickListener(this);
        rl_btn_right_clockin.setOnClickListener(this);
        btn_clock_in_transfor.setOnClickListener(this);
        btn_break.setOnClickListener(this);
        btn_check_out_clockin.setOnClickListener(this);
        txt_break_title.setOnClickListener(this);
        rl_btn_right_type.setOnClickListener(this);


        txt_job_title_break.setOnClickListener(this);
        rl_btn_right_break.setOnClickListener(this);
        btn_clock_resume.setOnClickListener(this);


        timeSwapBuff = PreferenceManager.getWorkTime(getActivity());

        try {
            String currentJob = PreferenceManager.getTest(getActivity());
            String currentBreak = PreferenceManager.getBreakType(getActivity());
            String url = userData.getString("photoURL");

            if(currentBreak != null && !currentBreak.equals("null")){
                breakEvent(url, currentBreak);
            }
            else{

                if(currentJob != null && !currentJob.equals("null")){
                    clockedIn(url, currentJob);
                }
                else{
                    layout_clocked_out.setVisibility(View.VISIBLE);
                    layout_clocked_in.setVisibility(View.GONE);
                    layout_meal_break.setVisibility(View.GONE);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        String currentJob = PreferenceManager.getTest(getActivity());
        String currentBreak = PreferenceManager.getBreakType(getActivity());

        Log.e("tag", "currentJob : "+currentJob);
        Log.e("tag", "currentBreak : "+currentBreak);

        if(currentBreak != null && !currentBreak.equals("null")){
            typeSelectedBreak = currentBreak;
        }

        if(currentJob != null && !currentJob.equals("null")){
            jobSelectedTitle = currentJob;
        }

        Log.e("tag", "jobSelectedTitle : "+jobSelectedTitle);
        Log.e("tag", "typeSelectedBreak : "+typeSelectedBreak);

        txt_job_title_clocked_Out.setText(jobSelectedTitle);
        txt_job_title_clocked_in.setText(jobSelectedTitle);

        txt_job_title_break.setText(jobSelectedTitle);
        txt_break_title.setText(typeSelectedBreak);
        txt_break_type_name.setText(typeSelectedBreak);
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
                Log.e("tag", " EVENT_BUS_NEW_USER_PHOTO :"+isClockIn);
                if(isClockIn){
                    String photoUrl = pusher.getPhotoURL();
                    String job_title = pusher.getJobName();
                    clockedIn(photoUrl, job_title);
                }
                else{
                    customHandler.removeCallbacks(updateTimerThread);
                    layout_clocked_out.setVisibility(View.VISIBLE);
                    layout_clocked_in.setVisibility(View.GONE);
                    layout_meal_break.setVisibility(View.GONE);
                }
                break;
            case EVENT_BUS_UPDATE_BREAK:
                String photoUrl = pusher.getPhotoURL();
                String _breakType = pusher.getBreakType();

                breakEvent(photoUrl, _breakType);

                break;
        }
    }

    private void clockedIn(String photoUrl, String job_title){
        customHandler.removeCallbacks(updateTimerThread);
        mapFragment = null;
        geocoder = null;
        mMap = null;

        layout_clocked_out.setVisibility(View.GONE);
        layout_meal_break.setVisibility(View.GONE);
        layout_clocked_in.setVisibility(View.VISIBLE);



        startTime = SystemClock.uptimeMillis();
        customHandler.postDelayed(updateTimerThread, 0);
        txt_jobName.setText(job_title);
        ImageLoader.loadCircleImage(getActivity().getApplicationContext(), photoUrl, img_avatar_in, R.drawable.avatar);

        currentLocation(1);
    }

    private void breakEvent(String photoUrl, String _breakType){
        customHandler.removeCallbacks(updateTimerThread);
        mapFragment = null;
        geocoder = null;
        mMap = null;

        layout_clocked_out.setVisibility(View.GONE);
        layout_clocked_in.setVisibility(View.GONE);
        layout_meal_break.setVisibility(View.VISIBLE);


        startTime = SystemClock.uptimeMillis();
        customHandler.postDelayed(updateTimerThreadBreak, 0);

        txt_break_type_name.setText(_breakType);
        ImageLoader.loadCircleImage(getActivity().getApplicationContext(), photoUrl, img_avatar_break, R.drawable.avatar);
        currentLocation(2);
    }
    private Runnable updateTimerThread = new Runnable() {

        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updatedTime = timeSwapBuff + timeInMilliseconds;
            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            int hours = mins / 60;
            secs = secs % 60;
            mins = mins % 60;
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
            mins = mins % 60;
            txt_break_time.setText("" + hours + "h:" + mins + "m:" + String.format("%02d", secs)+"s");
            customHandler.postDelayed(this, 0);
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_check_out_break:
            case R.id.btn_check_out_clockin:
                flag = 4;
                onClockIn(1);
                break;
            case R.id.btn_clock_in:
                flag = 0;
                onClockIn(1);
                break;
            case R.id.btn_clock_in_transfor:
                flag = 2;
                timeSwapBuff = 0L;
                onClockIn(1);
                break;
            case R.id.btn_clock_resume:
                flag = 3;
                timeSwapBuff = 0L;
                onClockIn(1);
                break;
            case R.id.btn_break:
                timeSwapBuff = 0L;
                onClockIn(2);
                break;
            case R.id.txt_break_title:
            case R.id.rl_btn_right_type:
                onChangeType();
                break;
            case R.id.txt_job_title:
            case R.id.rl_btn_right_clockout:
            case R.id.txt_job_title_clocked_in:
            case R.id.rl_btn_right_clockin:
            case R.id.txt_job_title_break:
            case R.id.rl_btn_right_break:
                onChangeJob();
                break;
        }
    }


    /** clock in button in layout_clocked_out **/
    public void onClockIn(int _isBreak){


        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA, }, PERMISSION_REQUEST_CODE);
        }
        else{
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
            }
            else{
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                    intent.putExtra("android.intent.extras.LENS_FACING_FRONT", 1); }
                else { intent.putExtra("android.intent.extras.CAMERA_FACING", 1); }

                if(_isBreak == 1){
                    startActivityForResult(intent, AppConstants.UPLOAD_PICTURE_REQUEST_CODE);
                }
                else{
                    startActivityForResult(intent, AppConstants.UPLOAD_PICTURE_REQUEST_CODE_BREAK);
                }

            }

        }
    }

    public void onChangeJob(){
        Intent mIntent = new Intent(getActivity(), JobActivity.class);
        this.startActivity(mIntent);
    }

    public void onChangeType(){
        Intent mIntent = new Intent(getActivity(), BreakTypeActivity.class);
        this.startActivity(mIntent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case AppConstants.UPLOAD_PICTURE_REQUEST_CODE:

                    //Uri _uri = data.getData();
                    try {
                        Bundle extras = data.getExtras();
                        Bitmap imageBitmap = (Bitmap) extras.get("data");

                        // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                        Uri tempUri = AppHelper.getImageUri(getActivity(), imageBitmap);

                        String FilePath = AppHelper.getFilePath(getActivity(), tempUri);
                        final String jobName = jobSelectedTitle;

                        try {
                            InputStream targetStream = new FileInputStream(FilePath);
                            RequestParams params    = new RequestParams();
                            params.put("username", userData.getString("username"));
                            params.put("jobName", jobName);
                            params.put("photo", targetStream);

                            String endpoint = null;

                            switch (flag) {
                                case 0:
                                    endpoint = EndPoints.clockInEndpoint;
                                    break;
                                case 2:
                                    endpoint = EndPoints.transferEndpoint;
                                    break;
                                case 3:
                                    endpoint = EndPoints.resumeWorkEndpoint;
                                    break;
                                case 4:
                                    endpoint = EndPoints.clockOutEndpoint;
                                    break;
                            }

                            ApiManager.callApi(endpoint, params, new APIInterface() {
                                @Override
                                public void onSuccess(JSONObject response) throws JSONException {
                                    Boolean status      = response.getBoolean("success");

                                    if(status){
                                        JSONObject user_data = response.getJSONObject("data");
                                        String photoURL = user_data.getString("photoURL");

                                        if( flag == 4){
                                            EventBus.getDefault().post(new Pusher(AppConstants.EVENT_BUS_NEW_USER_PHOTO, photoURL, jobName, false));
                                        }
                                        else{
                                            EventBus.getDefault().post(new Pusher(AppConstants.EVENT_BUS_NEW_USER_PHOTO, photoURL, jobName, true));
                                        }

                                    }
                                    else{
                                        String message = response.getString("message");
                                        AppHelper.CustomToast(getActivity(), message);
                                    }
                                }

                                @Override
                                public void onFailure(JSONObject response) throws JSONException {
                                    AppHelper.CustomToast(getActivity(), "Network Error!");
                                }
                            });
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;
                case AppConstants.UPLOAD_PICTURE_REQUEST_CODE_BREAK:

                    try {
                        Bundle extras = data.getExtras();
                        Bitmap imageBitmap = (Bitmap) extras.get("data");

                        // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                        Uri tempUri = AppHelper.getImageUri(getActivity(), imageBitmap);

                        String FilePath = AppHelper.getFilePath(getActivity(), tempUri);

                        final String breakName = typeSelectedBreak;

                        try {
                            InputStream __targetStream = new FileInputStream(FilePath);
                            RequestParams __params    = new RequestParams();
                            __params.put("username", userData.getString("username"));
                            __params.put("breakName", breakName);
                            __params.put("photo", __targetStream);

                            String endpoint = EndPoints.startBreakEndpoint;

                            ApiManager.callApi(endpoint, __params, new APIInterface() {
                                @Override
                                public void onSuccess(JSONObject response) throws JSONException {
                                    Boolean status      = response.getBoolean("success");

                                    if(status){
                                        JSONObject user_data = response.getJSONObject("data");
                                        String photoURL = user_data.getString("photoURL");

                                        EventBus.getDefault().post(new Pusher(AppConstants.EVENT_BUS_UPDATE_BREAK, photoURL, breakName));
                                    }
                                    else{
                                        String message = response.getString("message");
                                        AppHelper.CustomToast(getActivity(), message);
                                    }
                                }

                                @Override
                                public void onFailure(JSONObject response) throws JSONException {
                                    AppHelper.CustomToast(getActivity(), "Network Error!");
                                }
                            });
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;
                default:

            }
        }
    }
}
