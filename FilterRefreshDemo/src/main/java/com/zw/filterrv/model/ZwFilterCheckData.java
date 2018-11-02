package com.zw.filterrv.model;

import java.util.List;

/**
 * 筛选条件中数据格式
 * Created by Administrator on 2018/7/2.
 */

public class ZwFilterCheckData
{
    public static String sortName = "sort";
    int type;//单选、多选  FilterManager.FILTER_TYPE_SIN_ELECTION，FilterManager.FILTER_TYPE_MUL_ELECTION
    String title;
    String id;
    int sort;//排序状态，ViewBarManager.SORT_STATUS_DEFAULT，ViewBarManager.SORT_STATUS_ASC，ViewBarManager.SORT_STATUS_DESC 默认 正序 倒序
    List<ZwFilterCheckDataItem> list;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public List<ZwFilterCheckDataItem> getList() {
        return list;
    }

    public void setList(List<ZwFilterCheckDataItem> list) {
        this.list = list;
    }
}
