package com.travel.enjoyindanang.utils.helper;

import android.Manifest;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.travel.enjoyindanang.BuildConfig;
import com.travel.enjoyindanang.GlobalApplication;
import com.travel.enjoyindanang.R;
import com.travel.enjoyindanang.model.ImageData;
import com.travel.enjoyindanang.utils.Utils;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static com.travel.enjoyindanang.utils.ScreenUtils.getPixelsFromDP;

/**
 * Author: Tavv
 * Created on 27/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class PhotoHelper {
    public static final int CAPTURE_IMAGE_REQUEST_CODE = 89;
    public static final int SELECT_FROM_GALLERY_CODE = 98;
    public static final int PERMISSION_READ_EXTERNAL_CODE = 96;

    private static final float SCALE_WIDTH = 100f;

    private Fragment fragment;

    private String mCurrentPhotoPath;

    private String fullPhotoPath;

    public PhotoHelper(Fragment fragment) {
        this.fragment = fragment;
    }


    public void startCameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fragment.startActivityForResult(intent, CAPTURE_IMAGE_REQUEST_CODE);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void startGalleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");

        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);

        intent.setAction(Intent.ACTION_GET_CONTENT);
        fragment.startActivityForResult(Intent.createChooser(intent, "android.intent.action.SEND_MULTIPLE"), SELECT_FROM_GALLERY_CODE);
    }

    public Bitmap getCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = null;
        FileOutputStream fos = null;
        if (thumbnail != null) {
            bytes = new ByteArrayOutputStream();
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
            File destination = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");
            try {
                boolean isFileCreated = destination.createNewFile();
                if (isFileCreated) {
                    fos = new FileOutputStream(destination);
                    fos.write(bytes.toByteArray());
                    return thumbnail;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                try {
                    if (bytes != null) {
                        bytes.close();
                    }
                    if (fos != null) {
                        fos.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }


    public Bitmap getBitmapSelectFromGallery(Intent data) {
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(GlobalApplication.getGlobalApplicationContext().getContentResolver(), data.getData());
                return bm;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    public File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        final File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        fullPhotoPath = image.getPath();
        return image;
    }

    public Uri startIntentImageCapture() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(fragment.getContext().getPackageManager()) != null) {
            try {
                File photoFile = createImageFile();
                if (photoFile != null) {
                    Uri uri = FileProvider.getUriForFile(GlobalApplication.getGlobalApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", photoFile);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    validCamera(cameraIntent, uri);
                    fragment.startActivityForResult(cameraIntent, CAPTURE_IMAGE_REQUEST_CODE);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    public Bitmap decodeFile(Uri uriFile) throws OutOfMemoryError {
        String filePath = uriFile.getPath();
        BitmapFactory.Options bmOptions;
        Bitmap imageBitmap;
        try {
            imageBitmap = BitmapFactory.decodeFile(filePath);
        } catch (OutOfMemoryError e) {
            bmOptions = new BitmapFactory.Options();
            bmOptions.inSampleSize = 4;
            bmOptions.inPurgeable = true;
            imageBitmap = BitmapFactory.decodeFile(filePath, bmOptions);
        }
        return imageBitmap;
    }

    public String getCurrentPhotoPath() {
        return mCurrentPhotoPath;
    }

    public void setCurrentPhotoPath(String mCurrentPhotoPath) {
        this.mCurrentPhotoPath = mCurrentPhotoPath;
    }

    private void validCamera(Intent captureImage, Uri uri) {
        List<ResolveInfo> cameraActivities = fragment.getActivity()
                .getPackageManager()
                .queryIntentActivities(captureImage, PackageManager.MATCH_DEFAULT_ONLY);

        for (ResolveInfo activity : cameraActivities) {
            fragment.getActivity().grantUriPermission(activity.activityInfo.packageName,
                    uri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
    }

    public String getFullPhotoPath() {
        return fullPhotoPath;
    }

    public void setFullPhotoPath(String fullPhotoPath) {
        this.fullPhotoPath = fullPhotoPath;
    }


    public void cameraIntent() {
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePicture.resolveActivity(fragment.getActivity().getPackageManager()) != null) {

            File photoFile = null;
            try {
                photoFile = createImageFile();

            } catch (IOException e) {
                e.printStackTrace();
            }

            if (photoFile != null) {
                Uri u = FileProvider.getUriForFile(fragment.getActivity(), BuildConfig.APPLICATION_ID + ".fileprovider", photoFile);

                takePicture.putExtra(MediaStore.EXTRA_OUTPUT, u);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    takePicture.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    ClipData clip =
                            ClipData.newUri(fragment.getActivity().getContentResolver(), "A photo", u);

                    takePicture.setClipData(clip);
                    takePicture.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                } else {
                    List<ResolveInfo> resInfoList =
                            fragment.getActivity().getPackageManager()
                                    .queryIntentActivities(takePicture, PackageManager.MATCH_DEFAULT_ONLY);

                    for (ResolveInfo resolveInfo : resInfoList) {
                        String packageName = resolveInfo.activityInfo.packageName;
                        fragment.getActivity().grantUriPermission(packageName, u,
                                Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    }
                }
                fragment.startActivityForResult(takePicture, CAPTURE_IMAGE_REQUEST_CODE);
            }
        }
    }


    private void onCaptureImageResult() {
        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);

    }

    private File createNewImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "PM_" + timeStamp + "_";
        File storageDire = fragment.getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File i = File.createTempFile(imageFileName,
                ".jpg",
                storageDire);

        mCurrentPhotoPath = i.getAbsolutePath();
        return i;
    }

    @AfterPermissionGranted(PERMISSION_READ_EXTERNAL_CODE)
    public void openGallery() {
        if(fragment == null || !fragment.isAdded()) return;
        if (Build.VERSION.SDK_INT >= 23) {
            Context context = fragment.getContext() == null ? fragment.getActivity() : fragment.getContext();
//            if(context == null && fragment instanceof ProfileFragment){
//                context = ((ProfileFragment) fragment).getParentActivity();
//            }
            if (EasyPermissions.hasPermissions(context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                fragment.startActivityForResult(photoPickerIntent, SELECT_FROM_GALLERY_CODE);
            } else {
                EasyPermissions.requestPermissions(fragment, Utils.getLanguageByResId(R.string.Request_Permission_Camera), PERMISSION_READ_EXTERNAL_CODE, Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        } else {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            fragment.startActivityForResult(photoPickerIntent, SELECT_FROM_GALLERY_CODE);
        }
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = fragment.getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    public List<ImageData> parseGalleryResult(Intent intent, int limitSize) {
        if (intent != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                List<ImageData> images = new ArrayList<>();
                boolean isOverMaxSize = false;
                if(intent.getClipData() != null){
                    int numberOfImages = intent.getClipData().getItemCount();
                    isOverMaxSize = numberOfImages > limitSize;
                    numberOfImages = isOverMaxSize ? limitSize : numberOfImages;
                    try {
                        for (int i = 0; i < numberOfImages ; i++) {
                            ImageData imageData = new ImageData();
                            imageData.setUri(intent.getClipData().getItemAt(i).getUri());
                            Bitmap bitmap = getBitmap(intent.getClipData().getItemAt(i).getUri());
                            imageData.setIcon(Bitmap.createScaledBitmap(bitmap, getPixelsFromDP(SCALE_WIDTH), getPixelsFromDP(SCALE_WIDTH), false));
                            images.add(imageData);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    images.add(new ImageData(null, intent.getData(), null));
                }
                if(isOverMaxSize){
                    Toast.makeText(fragment.getContext(), Utils.getLanguageByResId(R.string.Message_Over_Size_Select), Toast.LENGTH_SHORT).show();
                }
                return images;
            }
        }
        return null;
    }

    private Bitmap getBitmap(Uri uri) {
        try {
            return MediaStore.Images.Media.getBitmap(GlobalApplication.getGlobalApplicationContext().getContentResolver(), uri);
        } catch (IOException e) {
            return null;
        }
    }
}
