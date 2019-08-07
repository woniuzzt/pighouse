package com.example.timple.zdpigapp;

import rxhttp.wrapper.annotation.DefaultDomain;

public class Url {
    @DefaultDomain() //设置为默认域名
    public static String baseUrl = "http://cpgroup.savld.com/";

    public static String LOGIN = "api/account/login";

    public static String REGISTER = "api/account/register";

    public static String PIG_HOUSE_LIST = "api/hog_house/getList";

    public static String PIG_CREATE = "api/hog/create";

    public static String PIG_STATUS = "api/hog/getCurrentStatus";

    public static String PIG_FULLINFO = "api/hog/getFullInfo";

    public static String PIG_CHANGE_STATUS = "api/hog/changeStatus";

    public static String UPDATE_VERSION = "api/app/getLastVersion";

    public static String ACCOUNT_TOKEN = "api/account/checkToken";

   public static  String TURN_GROUP = "/api/hog/changeHogHouse";

   public static  String UPDATEINFOBYTYPE = "/api/hog/updateInfoByType";
}
