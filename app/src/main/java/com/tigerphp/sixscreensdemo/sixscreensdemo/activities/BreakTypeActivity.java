package com.tigerphp.sixscreensdemo.sixscreensdemo.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tigerphp.sixscreensdemo.sixscreensdemo.R;
import com.tigerphp.sixscreensdemo.sixscreensdemo.adapters.BreakTypeAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BreakTypeActivity extends BaseActivity {

    @BindView(R.id.breakTypeList)
    RecyclerView breakTypeList;
    private BreakTypeAdapter mBreakTypeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_break_type);
        ButterKnife.bind(this);
        initActivity(R.id.rl_breakType);
        initializerView();
    }

    private void initializerView() {
        List<JSONObject> mBreakTypeList = new ArrayList<>();
        JSONObject temp = new JSONObject();
        JSONObject temp1 = new JSONObject();
        JSONObject temp2 = new JSONObject();
        JSONObject temp3 = new JSONObject();

        try {
            mBreakTypeList.add(temp.put("title", "Lunch"));
            mBreakTypeList.add(temp1.put("title", "Rest"));
            mBreakTypeList.add(temp2.put("title", "10-minute paid"));
            mBreakTypeList.add(temp3.put("title", "30-minute paid"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mBreakTypeAdapter = new BreakTypeAdapter(this, mBreakTypeList);
        breakTypeList.setLayoutManager(mLinearLayoutManager);
        breakTypeList.setAdapter(mBreakTypeAdapter);
    }

    public void onBack(View v){
        this.finish();
    }
}
