package com.tigerphp.sixscreensdemo.sixscreensdemo.utils;

import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.v7.widget.CardView;
/**
 * Created by luckycharm on 7/7/18.
 */

public class ResolutionSet {
    public static float fXpro = 1;
    public static float fYpro = 1;
    public static float fPro  = 1;
    public static int nWidth = 750;
    public static int nHeight = 1570;// state bar height : 54

    public static ResolutionSet _instance = new ResolutionSet();

    public ResolutionSet() {}

    public void setResolution(int x, int y)
    {
        nWidth = x; nHeight = y;
        fXpro = (float)x / 750;
        fYpro = (float)y / 1624;
        fPro = Math.min(fXpro, fYpro);
    }

    public void iterateChild(View view)
    {

        if (view instanceof RecyclerView)
            return;

        if (view instanceof ViewGroup)
        {
            ViewGroup container = (ViewGroup)view;
            int nCount = container.getChildCount();
            for (int i=0; i<nCount; i++)
            {
                iterateChild(container.getChildAt(i));
            }
        }

        UpdateLayout(view);
    }

    void UpdateLayout(View view)
    {
        ViewGroup.LayoutParams lp;
        lp = (ViewGroup.LayoutParams) view.getLayoutParams();
        if ( lp == null )
            return;

        //For protect multiple call
        view.setMinimumWidth(0);
        //End Add
        if(view.getTag() != null && view.getTag().toString().equals("bubble")){

        }
        else{
            if(lp.width > 0)
                lp.width = (int)(lp.width * fXpro + 0.50001);
            if(lp.height > 0)
                lp.height = (int)(lp.height * fYpro + 0.50001);

            if(view instanceof TextView)
            {
                TextView lblView = (TextView)view;
                int txtSize = (int) (fPro * lblView.getTextSize());
                lblView.setTextSize(TypedValue.COMPLEX_UNIT_PX, txtSize);
            }
        }


        int leftPadding = (int)( fXpro * view.getPaddingLeft() + 0.50001);
        int rightPadding = (int)(fXpro * view.getPaddingRight() + 0.50001);
        int bottomPadding = (int)(fYpro * view.getPaddingBottom() + 0.50001);
        int topPadding = (int)(fYpro * view.getPaddingTop() + 0.50001);


        view.setPadding(leftPadding, topPadding, rightPadding, bottomPadding);

        if(lp instanceof ViewGroup.MarginLayoutParams)
        {
            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams)lp;

            mlp.leftMargin = (int)(mlp.leftMargin * fXpro + 0.50001);
            mlp.rightMargin = (int)(mlp.rightMargin * fXpro+ 0.50001);
            mlp.topMargin = (int)(mlp.topMargin * fYpro+ 0.50001);
            mlp.bottomMargin = (int)(mlp.bottomMargin * fYpro+ 0.50001);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.setElevation((int) (fPro * view.getElevation()));
        }

        if(view instanceof CardView)
        {
            CardView tmpView = (CardView) view;
            int radius = (int)(fPro * tmpView.getRadius());
            int elevation = (int)(fPro * tmpView.getCardElevation());

            tmpView.setRadius(radius);
            tmpView.setCardElevation(elevation);
        }

    }
}
