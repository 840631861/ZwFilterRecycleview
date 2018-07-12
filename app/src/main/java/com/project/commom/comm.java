package com.project.commom;

/**
 * Created by Administrator on 2018/7/2.
 */

public class comm
{
    public static Boolean isEmpty( String str )
    {
        if( str == null || str.equals("") || str.equalsIgnoreCase("null") || str == "" )
            return true;

        return false;
    }
}
