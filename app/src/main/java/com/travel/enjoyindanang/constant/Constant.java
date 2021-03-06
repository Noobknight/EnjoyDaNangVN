package com.travel.enjoyindanang.constant;


import android.Manifest;

import com.travel.enjoyindanang.R;


/**
 * Created by chientruong on 12/29/16.
 */

public class Constant {

    public static String URL_HOST = "https://enjoyindanang.vn/API/";
    public static String URL_HOST_IMAGE = "https://enjoyindanang.vn";
    public static String URL_FORGOT_PWD = "https://enjoyindanang.vn/Account/ForgotPassword";

    public static final String URL_DIRECTION_MAPS = "http://maps.google.com/maps?f=d&hl=en&saddr=%1$,.2f,%1$,.2f&daddr=%1$,.2f,%1$,.2f";

    public static String EXCHANGE_RATE_FORMAT = "1$ = %s VND";

    public static String DISCOUNT_TEMPLATE = "%d%s OFF";

    public static final String DATE_SERVER_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    public static final int SPLASH_TIME_OUT = 1000;

    public static final String DATE_FORMAT_DMY = "dd-MM-yyyy";

    public static final String REGEX_URL = "((http)[s]?(://).*)";

    public static final String REGEX_NUMBER = "[0-9]+";

    public static final String API_OK = "200";
    public static final String API_401 = "401";
    public static final String API_402 = "402";
    public static final String API_403 = "403";
    public static final String API_404 = "404";
    public static final String API_405 = "405";
    public static final String API_500 = "500";
    public static final String API_501 = "501";
    public static final String API_502 = "502";
    public static final String MSG_SUCCESS = "success";
    public static final String MSG_WARNING = "warning";
    public static final String MSG_FAILURE = "fail";

    public static final int CONNECT_TIME_OUT = 15000;

    public static final String FILE_NAME_LANGUAGE = "languageAppVN.json";

    public static String EMBEB_YOUTUBE_FORMAT = "<html><body><iframe width=\"100%\" height=\"%d\" src=\"%s\" frameborder=\"0\" allowfullscreen></iframe></body></html>";

    public static final String TITLE_ERROR = "Error";

    public static final String TITLE_WARNING = "Warning";

    public static final String TITLE_SUCCESS = "Success";


    public static final int[] ICON_MENU_NORMAL = new int[]{0, R.drawable.ic_introduction, R.drawable.ic_contact, R.drawable.dieukhoan, R.drawable.ic_favorite,
            R.drawable.ic_log_checkin, 0, R.drawable.ic_profile, R.drawable.ic_change_password, R.drawable.ic_logout};

    public static final int[] ICON_MENU_NO_LOGIN = new int[]{0, R.drawable.ic_introduction, R.drawable.ic_contact, R.drawable.dieukhoan, 0, R.drawable.ic_logout};

    public static final Integer[] INDEX_HEADER_NORMAL = new Integer[]{0, 6};

    public static final Integer[] INDEX_HEADER_NO_LOGIN = new Integer[]{0, 4};

    public static final String FROM_DATE = "FromDate";
    public static final String TO_DATE = "ToDate";

    public static final int MAX_SIZE_GALLERY_SELECT = 3;

    public static final int FETCH_UPDATE_TIME = 60; // fetch every 1h

    public static final String KEY_EXTRAS_USER_INFO = "user_data";
    public static final String KEY_EXTRAS_CLOSE_POPUP = "popup_close_home";
    public static final String KEY_EXTRAS_DATE_CLOSE_POPUP = "date_close_popup";
    public static final String SHARED_PREFS_NAME = "EnjoySharedPrefsVN";

    public static final int SHOW_BACK_ICON = 1;
    public static final int HIDE_BACK_ICON = 2;
    public static final int SHOW_QR_CODE = 1;
    public static final int SHOW_EDIT_PROFILE = 2;
    public static final int HIDE_ALL_ITEM_MENU = 3;
    public static final int SHOW_MENU_BACK = 4;

    public static final int DEFAULT_ITEM_EACH_FETCH = 6;

    public static final String TITLE_HOME_VN = "TRANG CHỦ";


    public static final String LOCATION_NOT_FOUND = "Location_Not_Found";

    public static final int DEFAULT_RATING_STAR = 4;

    public static final String[] PERMISSION_REQUIRED = new String[]{Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
}
