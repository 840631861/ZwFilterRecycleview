package com.project.model;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/7/4.
 */

public class ZwParams
{
    ZwFilterCheckDataItem barCheckItem;
    ZwFilterData filterData;
    ArrayList<ZwFilterCheckDataItem> markData;

    public ArrayList<ZwFilterCheckDataItem> getMarkData() {
        return markData;
    }

    public void setMarkData(ArrayList<ZwFilterCheckDataItem> markData) {
        this.markData = markData;
    }

    public ZwFilterCheckDataItem getBarCheckItem() {
        return barCheckItem;
    }

    public void setBarCheckItem(ZwFilterCheckDataItem barCheckItem) {
        this.barCheckItem = barCheckItem;
    }

    public ZwFilterData getFilterData() {
        return filterData;
    }

    public void setFilterData(ZwFilterData filterData) {
        this.filterData = filterData;
    }
}
