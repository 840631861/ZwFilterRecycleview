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
