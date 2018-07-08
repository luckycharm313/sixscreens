package com.tigerphp.sixscreensdemo.sixscreensdemo.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tigerphp.sixscreensdemo.sixscreensdemo.R;
import com.tigerphp.sixscreensdemo.sixscreensdemo.app.ImageLoader;
import com.tigerphp.sixscreensdemo.sixscreensdemo.models.Pusher;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.tigerphp.sixscreensdemo.sixscreensdemo.app.AppConstants.EVENT_BUS_NEW_USER_PHOTO;

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


    View mView;
    private long startTime = 0L;
    private Handler customHandler = new Handler();
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;

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
                    layout_clocked_out.setVisibility(View.GONE);
                    layout_clocked_in.setVisibility(View.VISIBLE);

                    String photoUrl = pusher.getPhotoURL();
                    String job_title = pusher.getJobName();

                    startTime = SystemClock.uptimeMillis();
                    customHandler.postDelayed(updateTimerThread, 0);
                    txt_jobName.setText(job_title);
                    ImageLoader.loadCircleImage(getActivity().getApplicationContext(), photoUrl, img_avatar_in, R.drawable.avatar);
                }
                else{
                    customHandler.removeCallbacks(updateTimerThread);
                    layout_clocked_out.setVisibility(View.VISIBLE);
                    layout_clocked_in.setVisibility(View.GONE);
                }
                break;

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

}
