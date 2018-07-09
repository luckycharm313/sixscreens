package com.tigerphp.sixscreensdemo.sixscreensdemo.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tigerphp.sixscreensdemo.sixscreensdemo.R;
import com.tigerphp.sixscreensdemo.sixscreensdemo.app.AppConstants;
import com.tigerphp.sixscreensdemo.sixscreensdemo.app.PreferenceManager;
import com.tigerphp.sixscreensdemo.sixscreensdemo.models.Pusher;
import com.tigerphp.sixscreensdemo.sixscreensdemo.utils.ResolutionSet;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by luckycharm on 7/9/18.
 */

public class JobAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private final Activity mActivity;
    private List<JSONObject> mJobList;

    public JobAdapter(@NonNull Activity mActivity, List<JSONObject> mJobList) {
        this.mActivity = mActivity;
        this.mJobList = mJobList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mActivity).inflate(R.layout.row_job, parent, false);
        return new JobViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof JobAdapter.JobViewHolder) {
            final JobAdapter.JobViewHolder jobViewHolder = (JobAdapter.JobViewHolder) holder;

            /* resolution start*/
            if (holder.itemView.getMinimumWidth() == 1)
                ResolutionSet._instance.iterateChild(holder.itemView);

            final JSONObject mObj = this.mJobList.get(position);
            String jobTitle = null;
            try {
                jobTitle = mObj.getString("title");
                jobViewHolder.setJob(jobTitle);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            final String finalJobTitle = jobTitle;
            jobViewHolder.setOnClickListener(view -> {

                EventBus.getDefault().post(new Pusher(AppConstants.EVENT_BUS_UPDATE_JOB_TITLE, finalJobTitle, true));
                PreferenceManager.setTest(mActivity, finalJobTitle);
                mActivity.finish();
            });
        }
    }

    @Override
    public int getItemCount() {
        return mJobList.size() > 0 ? mJobList.size() : 0;
    }

    public class JobViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_job_item)
        TextView txt_job_item;
        @BindView(R.id.rl_row_job_item)
        RelativeLayout rl_row_job_item;

        JobViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void setJob(String job) {
            txt_job_item.setText(job);
        }

        void setOnClickListener(View.OnClickListener listener) {
            rl_row_job_item.setOnClickListener(listener);
        }
    }
}
