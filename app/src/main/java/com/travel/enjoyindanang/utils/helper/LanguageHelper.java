package com.travel.enjoyindanang.utils.helper;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import com.travel.enjoyindanang.GlobalApplication;
import com.travel.enjoyindanang.constant.AppLanguage;
import com.travel.enjoyindanang.constant.Constant;
import com.travel.enjoyindanang.utils.FileUtils;

/**
 * Author: Tavv
 * Created on 22/10/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public class LanguageHelper {

    public static String getValueByKey(String key) {
        JSONObject js = GlobalApplication.getGlobalApplicationContext().getJsLanguage() != null
                ? GlobalApplication.getGlobalApplicationContext().getJsLanguage()
                : FileUtils.readFile(Constant.FILE_NAME_LANGUAGE);
        if (js != null) {
            try {
                return js.getString(key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return StringUtils.EMPTY;
    }


    public static void getValueByViewId(View... views) {
        JSONObject js = GlobalApplication.getGlobalApplicationContext().getJsLanguage();
        if (views != null) {
            for (View view : views) {
                String value;
                if (view instanceof EditText) {
                    try {
                        value = js.getString(((EditText) view).getHint() + "".trim());
                    } catch (Exception e) {
                        value = StringUtils.EMPTY;
                    }
                    ((EditText) view).setHint(value);
                } else if (view instanceof Button) {
                    try {
                        value = js.getString(((Button) view).getText() + "".trim());
                    } catch (Exception e) {
                        value = StringUtils.EMPTY;
                    }
                    ((Button) view).setText(value);
                } else if (view instanceof TextView) {
                    try {
                        value = js.getString(((TextView) view).getText() + "".trim());
                    } catch (Exception e) {
                        value = StringUtils.EMPTY;
                    }
                    ((TextView) view).setText(value);
                } else if (view instanceof SearchView) {
                    try {
                        value = js.getString(js.getString(((SearchView) view).getQueryHint() + "".trim()));
                    } catch (Exception e) {
                        value = StringUtils.EMPTY;
                    }
                    ((SearchView) view).setQueryHint(value);
                }
            }
        }
    }

    public static String[] getTitleMenuNormal() {
        JSONObject js = GlobalApplication.getGlobalApplicationContext().getJsLanguage();
        if (js != null) {
            try {
                return new String[]{js.getString(AppLanguage.Key.Information), js.getString(AppLanguage.Key.Introduction),
                        js.getString(AppLanguage.Key.Contact_Us), js.getString(AppLanguage.Key.Term),
                        js.getString(AppLanguage.Key.Favorite), js.getString(AppLanguage.Key.Home_Account_LogCheck_in),
                        js.getString(AppLanguage.Key.Persional), js.getString(AppLanguage.Key.Home_Account_UpdateProfile),
                        js.getString(AppLanguage.Key.Home_Account_ChangePassword),
                        js.getString(AppLanguage.Key.Home_Account_Logout)};
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ;
        }
        return null;
    }

    public static String[] getTitleMenuNoLogin() {
        JSONObject js = GlobalApplication.getGlobalApplicationContext().getJsLanguage();
        if (js != null) {
            try {
                return new String[]{js.getString(AppLanguage.Key.Information), js.getString(AppLanguage.Key.Introduction),
                        js.getString(AppLanguage.Key.Contact_Us), js.getString(AppLanguage.Key.Term),
                        js.getString(AppLanguage.Key.Persional), js.getString(AppLanguage.Key.Home_Account_Login)};
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ;
        }
        return null;
    }

    public static String getSystemLanguage() {
        Locale locale = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = Resources.getSystem().getConfiguration().getLocales().get(0);
        } else {
            locale = Resources.getSystem().getConfiguration().locale;
        }
        return locale.getLanguage();
    }


    public static String getSystemLanguage(Configuration configs) {
        if (configs != null) {
            Locale locale = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                locale = configs.getLocales().get(0);
            } else {
                locale = configs.locale;
            }
            return locale.getLanguage();
        }
        return "";
    }
}
