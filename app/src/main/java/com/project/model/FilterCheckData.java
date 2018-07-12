package com.project.model;

import java.util.List;

/**
 * 筛选条件中数据格式
 * Created by Administrator on 2018/7/2.
 */

public class FilterCheckData
{
    public static String sortName = "sort";
    int type;
    String title;
    String id;
    int sort;
    List<FilterCheckDataItem> list;

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

    public List<FilterCheckDataItem> getList() {
        return list;
    }

    public void setList(List<FilterCheckDataItem> list) {
        this.list = list;
    }
}
