package com.travel.enjoyindanang.ui.activity.scan;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.Result;

import org.apache.commons.lang3.StringUtils;

import java.text.NumberFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.refactor.lib.colordialog.PromptDialog;
import me.dm7.barcodescanner.core.IViewFinder;
import me.dm7.barcodescanner.core.ViewFinderView;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import com.travel.enjoyindanang.MvpActivity;
import com.travel.enjoyindanang.R;
import com.travel.enjoyindanang.annotation.DialogType;
import com.travel.enjoyindanang.constant.AppError;
import com.travel.enjoyindanang.constant.Constant;
import com.travel.enjoyindanang.model.HistoryCheckin;
import com.travel.enjoyindanang.model.Partner;
import com.travel.enjoyindanang.model.UserInfo;
import com.travel.enjoyindanang.utils.DialogUtils;
import com.travel.enjoyindanang.utils.ImageUtils;
import com.travel.enjoyindanang.utils.Utils;
import com.travel.enjoyindanang.utils.helper.LanguageHelper;
import com.travel.enjoyindanang.utils.widget.NumberTextWatcher;

/**
 * Author: Tavv
 * Created on 30/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class ScanActivity extends MvpActivity<ScanQRCodePresenter> implements ScanQRCodeView, ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;

    private UserInfo userInfo;

    @BindView(R.id.scan_container)
    ViewGroup contentFrame;

    private AlertDialog alertDialog;

    @Override
    public void handleResult(Result result) {
        if (result != null) {
            vibrate();
            mvpPresenter.getInfoScanQr(result.getText());
        }
    }

    @Override
    public void showToast(String desc) {

    }

    @Override
    public void unKnownError() {

    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }


    private void showDialogInfo(@NonNull final Partner partner) {
        final String formatPartnerName = Utils.getLanguageByResId(R.string.Name).concat(": %s");
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_scan_qr_layout, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(ScanActivity.this);
        builder.setView(dialogView);
        alertDialog = builder.create();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        AppCompatButton btnOk = (AppCompatButton) dialogView.findViewById(R.id.btnSubmit);
        AppCompatButton btnCancel = (AppCompatButton) dialogView.findViewById(R.id.btnCancel);
        TextView txtPartnerName = (TextView) dialogView.findViewById(R.id.txtPartnerName);
        ImageView imgPartner = (ImageView) dialogView.findViewById(R.id.imgPartner);
        TextView txtDiscount = (TextView) dialogView.findViewById(R.id.txtDiscount);
        final EditText edtAmount = (EditText) dialogView.findViewById(R.id.edtAmount);
        final EditText edtPwd = (EditText) dialogView.findViewById(R.id.edtPassWord);
        LanguageHelper.getValueByViewId(edtAmount, txtPartnerName, txtDiscount, btnCancel, btnOk, edtPwd);
        edtAmount.addTextChangedListener(new NumberTextWatcher(edtAmount));
        txtPartnerName.setText(String.format(Locale.getDefault(), formatPartnerName, partner.getName()));
        txtDiscount.setText(Utils.getLanguageByResId(R.string.Discount) + ": " + partner.getDiscount() + " (%)");
        ImageUtils.loadImageNoRadius(ScanActivity.this, imgPartner, partner.getPicture());
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int amount;
                String strPwd;
                try {
                    amount = Integer.parseInt(getNumber(edtAmount.getText().toString()));
                    strPwd = String.valueOf(edtPwd.getText());
                } catch (Exception e) {
                    DialogUtils.showDialog(ScanActivity.this, DialogType.WARNING, Utils.getLanguageByResId(R.string.Dialog_Title_Warning),
                            Utils.getLanguageByResId(R.string.Message_Wrong_Amount));
                    return;
                }
                if (amount != 0 && StringUtils.isNotBlank(strPwd)) {
                    v.setEnabled(false);
                    if (Utils.hasLogin()) {
                        mvpPresenter.requestOrder(partner.getId(), userInfo.getUserId(), amount, strPwd);
                    } else {
                        mvpPresenter.requestOrder(partner.getId(), 0, amount, strPwd);
                    }
                } else {
                    DialogUtils.showDialog(ScanActivity.this, DialogType.WARNING, Utils.getLanguageByResId(R.string.Dialog_Title_Warning),
                            Utils.getLanguageByResId(R.string.Validate_Message_All_Field_Empty));
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                mScannerView.resumeCameraPreview(ScanActivity.this);
            }
        });
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(alertDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        alertDialog.show();
        alertDialog.getWindow().setAttributes(lp);
    }

    @Override
    public void onFetchInfoSuccess(Partner partner) {
        showDialogInfo(partner);
    }

    @Override
    public void onRequestOrderSuccess(HistoryCheckin response) {
        String resultPattern = "%s\n%s: %s\n%s: %s%s\n%s: %s";
        String strAmount = NumberFormat.getInstance().format(response.getAmount());
        String strPayment = NumberFormat.getInstance().format(response.getPayment());
        String result = String.format(Locale.getDefault(), resultPattern,
                Utils.getLanguageByResId(R.string.Message_Payment_Success),
                Utils.getLanguageByResId(R.string.Amount), strAmount,
                Utils.getLanguageByResId(R.string.Discount), response.getDiscount(), "%",
                Utils.getLanguageByResId(R.string.Message_Payment_Paid), strPayment);
        DialogUtils.showDialog(ScanActivity.this, 3, Constant.TITLE_SUCCESS,
                result, new PromptDialog.OnPositiveListener() {
                    @Override
                    public void onClick(PromptDialog promptDialog) {
                        promptDialog.dismiss();
                        if (alertDialog != null && alertDialog.isShowing()) {
                            alertDialog.dismiss();
                            mScannerView.resumeCameraPreview(ScanActivity.this);
                        }
                    }
                });
    }

    private String getNumber(String value) {
        if (!value.contains(".") && !value.contains(",")) {
            return value;
        }
        return value.replaceAll("\\.|,", "");
    }

    @Override
    public void onFetchError(AppError appError) {
        DialogUtils.showDialog(ScanActivity.this, DialogType.WRONG, Utils.getLanguageByResId(R.string.Dialog_Title_Wrong), appError.getMessage());
        mScannerView.resumeCameraPreview(ScanActivity.this);
    }

    @Override
    public void onRequestOrderSuccessError(AppError appError) {
        DialogUtils.showDialog(ScanActivity.this, DialogType.WRONG, Utils.getLanguageByResId(R.string.Dialog_Title_Wrong), appError.getMessage());
        mScannerView.resumeCameraPreview(ScanActivity.this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransitionExit();
    }

    @Override
    protected ScanQRCodePresenter createPresenter() {
        return new ScanQRCodePresenter(this);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.fragment_scan);
    }

    @Override
    public void init() {
        userInfo = Utils.getUserInfo();
        mScannerView = new ZXingScannerView(this) {
            @Override
            protected IViewFinder createViewFinderView(Context context) {
                return new CustomViewFinderView(context);
            }
        };
        contentFrame.addView(mScannerView);
    }


    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void bindViews() {
        ButterKnife.bind(this);
    }

    @Override
    public void setValue(Bundle savedInstanceState) {

    }

    @Override
    public void setEvent() {
    }


    private static class CustomViewFinderView extends ViewFinderView {
        public static final int TRADE_MARK_TEXT_SIZE_SP = 40;
        public final Paint PAINT = new Paint();

        public CustomViewFinderView(Context context) {
            super(context);
            init();
        }

        public CustomViewFinderView(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        private void init() {
            setSquareViewFinder(true);
        }

        @Override
        public void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            drawTradeMark(canvas);
        }

        private void drawTradeMark(Canvas canvas) {
            Rect framingRect = getFramingRect();
            float tradeMarkTop;
            float tradeMarkLeft;
            if (framingRect != null) {
                tradeMarkTop = framingRect.bottom + PAINT.getTextSize() + 10;
                tradeMarkLeft = framingRect.left;
            } else {
                tradeMarkTop = 10;
                tradeMarkLeft = canvas.getHeight() - PAINT.getTextSize() - 10;
            }
            canvas.drawText(StringUtils.EMPTY, tradeMarkLeft, tradeMarkTop, PAINT);
        }
    }

    private void resumeScan() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScannerView.resumeCameraPreview(ScanActivity.this);
            }
        }, 1000);
    }
}
