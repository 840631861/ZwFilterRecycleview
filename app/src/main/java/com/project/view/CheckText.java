package com.project.view;

/**
 * Created by Administrator on 2018/6/28.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.project.R;

public class CheckText extends android.support.v7.widget.AppCompatTextView{

    private boolean isChecked=false;

    private static final int[] CHECKED_STATE_SET = {
            android.R.attr.state_checked
    };

    public CheckText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean performClick() {

        toggle();

        return super.performClick();
    }

    public void setChecked(boolean checked)
    {
        if( checked )
        {
            setBackgroundResource(R.drawable.bg_filter_check);
            setTextColor(getResources().getColor(R.color.colorAccent));
        }
        else
        {
            setBackgroundResource(R.drawable.bg_filter_uncheck);
            setTextColor(getResources().getColor(R.color.gray_8));
        }
        setPadding(0,0,0,0);

        if(checked==isChecked){
            return;
        }
        this.isChecked=checked;
        refreshDrawableState();
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void toggle() {
        setChecked(!isChecked);
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
//      invalidate();
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        int []drawalbeState=super.onCreateDrawableState(extraSpace+1);
        if(isChecked()){
            mergeDrawableStates(drawalbeState, CHECKED_STATE_SET);
        }
        return drawalbeState;
    }


}
