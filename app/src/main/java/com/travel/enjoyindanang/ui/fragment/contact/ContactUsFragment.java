package com.travel.enjoyindanang.ui.fragment.contact;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.travel.enjoyindanang.MvpFragment;
import com.travel.enjoyindanang.R;
import com.travel.enjoyindanang.annotation.DialogType;
import com.travel.enjoyindanang.constant.AppError;
import com.travel.enjoyindanang.model.Contact;
import com.travel.enjoyindanang.utils.DialogUtils;
import com.travel.enjoyindanang.utils.Utils;
import com.travel.enjoyindanang.utils.helper.LanguageHelper;
import com.travel.enjoyindanang.utils.helper.SoftKeyboardManager;

/**
 * Author: Tavv
 * Created on 26/10/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public class ContactUsFragment extends MvpFragment<ContactUsPresenter> implements ContactUsView, View.OnTouchListener {

    private final String LABEL_REQUIRE_TEMPLATE = "%s (<font color=#e51c23>*</font>)";

    @BindView(R.id.edtName)
    EditText edtName;

    @BindView(R.id.lrlContactUs)
    LinearLayout lrlContactUs;

    @BindView(R.id.edtPhone)
    EditText edtPhone;

    @BindView(R.id.edtEmail)
    EditText edtEmail;

    @BindView(R.id.edtTitle)
    EditText edtTitle;

    @BindView(R.id.edtAriaContent)
    EditText edtAriaContent;

    @BindView(R.id.txtNameLabel)
    TextView txtNameLabel;

    @BindView(R.id.txtAddress)
    TextView txtAddress;
    @BindView(R.id.txtEmail)
    TextView txtEmail;
    @BindView(R.id.txtPhone)
    TextView txtPhone;
    @BindView(R.id.txtContactAddress)
    TextView txtContactAddress;
    @BindView(R.id.txtContactEmail)
    TextView txtContactEmail;
    @BindView(R.id.txtContactPhone)
    TextView txtContactPhone;
    @BindView(R.id.lbPhone)
    TextView lbPhone;
    @BindView(R.id.lbEmail)
    TextView lbEmail;
    @BindView(R.id.lbTitle)
    TextView lbTitle;
    @BindView(R.id.lbContent)
    TextView lbContent;

    @BindView(R.id.lbBasicInfor)
    TextView lbBasicInfor;

    @BindView(R.id.lbContact)
    TextView lbContact;

    @BindView(R.id.btnSendContact)
    AppCompatButton btnSendContact;


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mvpPresenter = createPresenter();
        mvpPresenter.getInformation();
    }

    @Override
    public void showToast(String desc) {

    }

    @Override
    public void unKnownError() {

    }

    @Override
    public void sendContactSuccess() {
        Utils.clearForm(edtName, edtEmail, edtPhone, edtTitle, edtAriaContent);
        DialogUtils.showDialog(getContext(), DialogType.SUCCESS, DialogUtils.getTitleDialog(1), Utils.getLanguageByResId(R.string.Message_Submit_Contact_Success));
    }

    @Override
    public void sendContactFailure(AppError error) {
        DialogUtils.showDialog(getContext(), DialogType.WRONG, DialogUtils.getTitleDialog(3), error.getMessage());
    }

    @Override
    public void onGetInformation(Contact contact) {
        if (contact != null) {
            txtContactAddress.setText(contact.getAddress());
            txtContactEmail.setText(contact.getEmail());
            txtContactPhone.setText(contact.getPhone());
        }
    }

    @Override
    public void onGetInforFailure(AppError error) {

    }

    @Override
    protected ContactUsPresenter createPresenter() {
        return new ContactUsPresenter(this);
    }

    @Override
    protected void init(View view) {
        String lblNameRequire = String.format(Locale.getDefault(), LABEL_REQUIRE_TEMPLATE, Utils.getLanguageByResId(R.string.Name));
        if (Build.VERSION.SDK_INT >= 24) {
            txtNameLabel.setText(Html.fromHtml(lblNameRequire, Html.FROM_HTML_MODE_LEGACY));
        } else {
            txtNameLabel.setText(Html.fromHtml(lblNameRequire));
        }
    }

    @Override
    protected void setEvent(View view) {
        edtAriaContent.setOnTouchListener(this);
        lrlContactUs.setOnTouchListener(this);
    }

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_contact_us;
    }

    @Override
    public void bindView(View view) {
        ButterKnife.bind(this, view);
    }

    @Override
    public void initViewLabel(View view) {
        LanguageHelper.getValueByViewId(txtAddress, txtEmail, txtPhone, lbPhone, lbEmail, lbTitle,
                lbContent, lbBasicInfor, lbContact, btnSendContact);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() == R.id.lrlContactUs) {
            SoftKeyboardManager.hideSoftKeyboard(getContext(), v.getWindowToken(), 0);
        } else {
            v.getParent().requestDisallowInterceptTouchEvent(true);
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_UP:
                    v.getParent().requestDisallowInterceptTouchEvent(false);
                    break;
            }
        }
        return false;
    }

    @OnClick(R.id.btnSendContact)
    void onClick(View view) {
        sendContact();
    }

    private void sendContact() {
        String name = edtName.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String title = edtTitle.getText().toString().trim();
        String content = edtAriaContent.getText().toString().trim();
        if (StringUtils.isEmpty(name)) {
            Toast.makeText(mMainActivity, Utils.getLanguageByResId(R.string.Message_NameEmpty), Toast.LENGTH_SHORT).show();
        } else {
            mvpPresenter.sendContact(name, phone, email, title, content);
        }
    }

}
