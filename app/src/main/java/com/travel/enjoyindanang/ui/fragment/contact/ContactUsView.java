package com.travel.enjoyindanang.ui.fragment.contact;

import com.travel.enjoyindanang.constant.AppError;
import com.travel.enjoyindanang.iBaseView;
import com.travel.enjoyindanang.model.Contact;

/**
 * Author: Tavv
 * Created on 26/10/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public interface ContactUsView extends iBaseView {

    void sendContactSuccess();

    void sendContactFailure(AppError error);

    void onGetInformation(Contact contact);

    void onGetInforFailure(AppError error);
}
