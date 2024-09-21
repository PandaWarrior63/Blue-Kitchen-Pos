package com.lectus.blue.Config;

public class URL {
    private static String base_URL = "http://178.16.139.46/api/method/";
    //private static String base_URL = "http://134.209.210.1/api/method/";
    public static String URL_LOGIN = base_URL+"sopos.api.v1.auth.login";
    public static String URL_RESTAURANT_INIT_DATA = base_URL+"sopos.api.v1.restaurant.data.init_data";
    public static String URL_RESTAURANT_TABLE_DATA = base_URL+"sopos.api.v1.restaurant.data.tables";
    public static String URL_RESTAURANT_UPDATE_QUANTITY = base_URL+"sopos.api.v1.restaurant.order.update_quantity_app";
    public static String URL_COMMON_OPEN_ENTRIES = base_URL+"sopos.api.common.data.pos_open_list";
}
