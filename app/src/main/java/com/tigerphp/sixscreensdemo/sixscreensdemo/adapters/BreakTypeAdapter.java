package com.tigerphp.sixscreensdemo.sixscreensdemo.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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
 * Created by luckycharm on 7/10/18.
 */

public class BreakTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    private final Activity mActivity;
    private List<JSONObject> mBreakTypeList;

    public BreakTypeAdapter(@NonNull Activity mActivity, List<JSONObject> mJobList) {
        this.mActivity = mActivity;
        this.mBreakTypeList = mJobList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mActivity).inflate(R.layout.row_break_type, parent, false);
        return new BreakTypeAdapter.BreakTypeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof BreakTypeAdapter.BreakTypeViewHolder) {
            final BreakTypeAdapter.BreakTypeViewHolder breakTypeViewHolder = (BreakTypeAdapter.BreakTypeViewHolder) holder;

            /* resolution start*/
            if (holder.itemView.getMinimumWidth() == 1)
                ResolutionSet._instance.iterateChild(holder.itemView);

            final JSONObject mObj = this.mBreakTypeList.get(position);
            String breakType= null;
            try {
                breakType = mObj.getString("title");
                breakTypeViewHolder.setType(breakType);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            final String finalBreakType = breakType;

            breakTypeViewHolder.setOnClickListener(view -> {
                PreferenceManager.setBreakType(mActivity, finalBreakType);
                mActivity.finish();
            });
        }
    }

    @Override
    public int getItemCount() {
        return mBreakTypeList.size() > 0 ? mBreakTypeList.size() : 0;
    }

    public class BreakTypeViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_type_item)
        TextView txt_type_item;
        @BindView(R.id.rl_row_break_type_item)
        RelativeLayout rl_row_break_type_item;

        BreakTypeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void setType(String type) {
            txt_type_item.setText(type);
        }

        void setOnClickListener(View.OnClickListener listener) {
            rl_row_break_type_item.setOnClickListener(listener);
        }
    }
}
