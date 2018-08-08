package com.project.model;

import java.util.List;

/**
 * Created by Administrator on 2018/7/11.
 */

public class FilterData
{
    List<FilterCheckData> checkDatas;
    long timeStart;
    long timeEnd;
    String searchTxt;
    float seekbarNum1Start;
    float seekbarNum1End;
    float seekbarNum2Start;
    float seekbarNum2End;

    public float getSeekbarNum1Start() {
        return seekbarNum1Start;
    }

    public void setSeekbarNum1Start(float seekbarNum1Start) {
        this.seekbarNum1Start = seekbarNum1Start;
    }

    public float getSeekbarNum1End() {
        return seekbarNum1End;
    }

    public void setSeekbarNum1End(float seekbarNum1End) {
        this.seekbarNum1End = seekbarNum1End;
    }

    public float getSeekbarNum2Start() {
        return seekbarNum2Start;
    }

    public void setSeekbarNum2Start(float seekbarNum2Start) {
        this.seekbarNum2Start = seekbarNum2Start;
    }

    public float getSeekbarNum2End() {
        return seekbarNum2End;
    }

    public void setSeekbarNum2End(float seekbarNum2End) {
        this.seekbarNum2End = seekbarNum2End;
    }

    public String getSearchTxt() {
        return searchTxt;
    }

    public void setSearchTxt(String searchTxt) {
        this.searchTxt = searchTxt;
    }

    public List<FilterCheckData> getCheckDatas() {
        return checkDatas;
    }

    public void setCheckDatas(List<FilterCheckData> checkDatas) {
        this.checkDatas = checkDatas;
    }

    public long getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(long timeStart) {
        this.timeStart = timeStart;
    }

    public long getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(long timeEnd) {
        this.timeEnd = timeEnd;
    }
}
