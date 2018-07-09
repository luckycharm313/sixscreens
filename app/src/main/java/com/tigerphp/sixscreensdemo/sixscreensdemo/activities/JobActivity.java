package com.tigerphp.sixscreensdemo.sixscreensdemo.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.tigerphp.sixscreensdemo.sixscreensdemo.R;
import com.tigerphp.sixscreensdemo.sixscreensdemo.adapters.JobAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class JobActivity extends BaseActivity {

    @BindView(R.id.jobList)
    RecyclerView jobList;

    private JobAdapter mJobAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job);
        ButterKnife.bind(this);
        initActivity(R.id.rl_job);
        initializerView();
    }

    private void initializerView() {
        List<JSONObject> mJobList = new ArrayList<>();
        JSONObject temp = new JSONObject();
        JSONObject temp1 = new JSONObject();
        JSONObject temp2 = new JSONObject();
        JSONObject temp3 = new JSONObject();
        JSONObject temp4 = new JSONObject();
        JSONObject temp5 = new JSONObject();
        JSONObject temp6 = new JSONObject();
        JSONObject temp7 = new JSONObject();
        JSONObject temp8 = new JSONObject();
        try {
            mJobList.add(temp.put("title", "Basement Slab Preparation"));
            mJobList.add(temp1.put("title", "Cabinetry Installation"));
            mJobList.add(temp2.put("title", "Install Carpet"));
            mJobList.add(temp3.put("title", "Install Hardwood Floor"));
            mJobList.add(temp4.put("title", "Install Roof Shingles"));
            mJobList.add(temp5.put("title", "Plumbing Rough-in"));
            mJobList.add(temp6.put("title", "Prep Trim for Prime Coat"));
            mJobList.add(temp7.put("title", "Sand Drywall"));
            mJobList.add(temp8.put("title", "Set Electric Boxes"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mJobAdapter = new JobAdapter(this, mJobList);
        jobList.setLayoutManager(mLinearLayoutManager);
        jobList.setAdapter(mJobAdapter);
    }

    public void onBack(View v){
        this.finish();
    }
}
