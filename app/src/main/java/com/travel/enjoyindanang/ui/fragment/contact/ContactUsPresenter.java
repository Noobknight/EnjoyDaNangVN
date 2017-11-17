package com.travel.enjoyindanang.ui.fragment.contact;

import com.travel.enjoyindanang.BasePresenter;
import com.travel.enjoyindanang.api.ApiCallback;
import com.travel.enjoyindanang.api.model.Repository;
import com.travel.enjoyindanang.constant.AppError;
import com.travel.enjoyindanang.model.Contact;
import com.travel.enjoyindanang.utils.Utils;

/**
 * Author: Tavv
 * Created on 26/10/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public class ContactUsPresenter extends BasePresenter<ContactUsView> {

    public ContactUsPresenter(ContactUsView view) {
        super(view);
    }

    void sendContact(String name, String phone, String email, String title, String content) {
        addSubscription(apiStores.sendContact(name, phone, email, title, content), new ApiCallback<Repository>() {

            @Override
            public void onSuccess(Repository model) {
                if(Utils.isResponseError(model)){
                    mvpView.sendContactFailure(new AppError(new Throwable(model.getMessage())));
                    return;
                }
                mvpView.sendContactSuccess();
            }

            @Override
            public void onFailure(String msg) {
                mvpView.sendContactFailure(new AppError(new Throwable(msg)));
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }

    void getInformation(){
        addSubscription(apiStores.getInformation(), new ApiCallback<Repository<Contact>>() {

            @Override
            public void onSuccess(Repository<Contact> model) {
                if(Utils.isResponseError(model)){
                    mvpView.sendContactFailure(new AppError(new Throwable(model.getMessage())));
                    return;
                }
                mvpView.onGetInformation(model.getData().get(0));
            }

            @Override
            public void onFailure(String msg) {
                mvpView.sendContactFailure(new AppError(new Throwable(msg)));
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }
}
