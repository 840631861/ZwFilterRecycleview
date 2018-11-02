package com.zw.filterrv.view;

/**
 * Created by Administrator on 2018/6/28.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.library.R;

public class BarCheckText extends android.support.v7.widget.AppCompatTextView{

    private boolean isChecked=false;
    private int checkedColor,color;

    private static final int[] CHECKED_STATE_SET = {
            android.R.attr.state_checked
    };

    public BarCheckText(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs,R.styleable.BarCheckText);
        checkedColor = ta.getColor(R.styleable.BarCheckText_checkedColor,context.getResources().getColor(R.color.black));
        color = ta.getColor(R.styleable.BarCheckText_textColor,context.getResources().getColor(R.color.gray_8));
    }


    @Override
    public boolean performClick() {

        toggle();

        return super.performClick();
    }

    public void setCheckedColor(int checkedColor,int color) {
        this.checkedColor = checkedColor;
        this.color = color;
    }

    public void setChecked(boolean checked)
    {
        if( checked )
        {
            setTextColor(checkedColor);
        }
        else
        {
            setTextColor(color);
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
