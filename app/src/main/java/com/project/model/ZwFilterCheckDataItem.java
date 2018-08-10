package com.project.model;

/**
 * Created by Administrator on 2018/7/2.
 */

public class ZwFilterCheckDataItem
{
    String showName;
    String id;
    Boolean isChecked;//是否选中
    Object params;//自定义参数

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }

    public Object getParams() {
        return params;
    }

    public void setParams(Object params) {
        this.params = params;
    }
}
