package com.project.model;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/7/4.
 */

public class Params
{
    int comIndex;
    int sort;
    FilterData filterData;
    ArrayList<FilterCheckDataItem> markData;

    public ArrayList<FilterCheckDataItem> getMarkData() {
        return markData;
    }

    public void setMarkData(ArrayList<FilterCheckDataItem> markData) {
        this.markData = markData;
    }

    public int getComIndex() {
        return comIndex;
    }

    public void setComIndex(int comIndex) {
        this.comIndex = comIndex;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public FilterData getFilterData() {
        return filterData;
    }

    public void setFilterData(FilterData filterData) {
        this.filterData = filterData;
    }
}
