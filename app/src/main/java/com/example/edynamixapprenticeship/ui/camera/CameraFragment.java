package com.example.edynamixapprenticeship.ui.camera;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.edynamixapprenticeship.R;
import com.example.edynamixapprenticeship.databinding.CameraFragmentBinding;

import java.io.OutputStream;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CameraFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CameraFragment extends Fragment {
    private CameraFragmentBinding binding;
    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int VIDEO_RECORD_CODE = 101;
    private static final int PICTURE_CAPTURE_CODE = 102;
    MediaController mediaController;
    int takingPictureOrVideo;
    private Uri videoPath;
    private Uri picturePath;

    private static final int PICTURE_CODE =1;
    private static final int REQUEST_CODE =1;
    private static final int VIDEO_CODE = 2;

    public CameraFragment() {
        // Required empty public constructor
    }
    public static CameraFragment newInstance(String param1, String param2) {
        CameraFragment fragment = new CameraFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = CameraFragmentBinding.inflate(inflater,container,false);
        takingPictureOrVideo = 0;
        binding.imageViewId.setVisibility(View.INVISIBLE);
        if (isCameraPresentInPhone()){
            Log.i("VIDEO_RECORD_TAG","Camera is detected");
            getCameraPermission();
        }else {
            Log.i("VIDEO_RECORD_TAG","No camera is detected");
        }



        binding.takeVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecordVideoButtonPressed(view);
            }
        });
        binding.btnCameraPictureId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TakePictureButtonPressed();
            }
        });
        return binding.getRoot();
    }
    public void RecordVideoButtonPressed(View view){
        binding.imageViewId.setVisibility(View.INVISIBLE);
        binding.videoViewId.setVisibility(View.VISIBLE);
        takingPictureOrVideo = VIDEO_CODE;
        recordVideo();
    }
    public void TakePictureButtonPressed(){
        binding.imageViewId.setVisibility(View.VISIBLE);
        binding.videoViewId.setVisibility(View.INVISIBLE);
        takingPictureOrVideo  =PICTURE_CODE;
        takePicture();
    }
    private boolean isCameraPresentInPhone(){
        if (requireActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)){
            return true;
        }
        return false;
    }

    private void getCameraPermission(){
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA)
                ==PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(requireActivity(), new String[]
                    {Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }
    }
    private void recordVideo(){
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(intent,VIDEO_RECORD_CODE);
    }
    private void takePicture(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,PICTURE_CAPTURE_CODE);

    }
    private void savePicture(){
        Uri image;
        ContentResolver contentResolver =requireActivity().getContentResolver();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            image = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
        }else {
            image = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME,System.currentTimeMillis()+".jpg");
        contentValues.put(MediaStore.Images.Media.MIME_TYPE,"images/*");
        Uri uri = contentResolver.insert(image,contentValues);

        try {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) binding.imageViewId.getDrawable();
            Bitmap bitmap = bitmapDrawable.getBitmap();
//            Bitmap original = BitmapFactory.decodeStream(getAssets().open("1024x768.jpg"));

            OutputStream outputStream = contentResolver.openOutputStream(Objects.requireNonNull(uri));
            bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);
            Objects.requireNonNull(outputStream);


//            ByteArrayOutputStream out = new ByteArrayOutputStream();
//            original.compress(Bitmap.CompressFormat.PNG, 100, out);
//            Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));



        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK &&requestCode == VIDEO_RECORD_CODE){
            videoPath = data.getData();
            Log.i("VIDEO_RECORD_TAG","Video is recorded and available at path "+videoPath);
        }else if (resultCode == RESULT_CANCELED){
            Log.i("VIDEO_RECORD_TAG","Recording video is cancelled");
        }else if (requestCode ==VIDEO_RECORD_CODE){
            Log.i("VIDEO_RECORD_TAG","Recording video has some error");
        }if (resultCode == RESULT_OK &&requestCode == PICTURE_CAPTURE_CODE){
            picturePath = data.getData();
            Log.i("IMAGE_TAKING_TAG","Picture is taken and available at path "+videoPath);
        }else if (resultCode == RESULT_CANCELED){
            Log.i("IMAGE_TAKING_TAG","Taking picture is cancelled");
        }else if (requestCode ==VIDEO_RECORD_CODE){
            Log.i("IMAGE_TAKING_TAG","Taking picture has some error");
        }

        if (takingPictureOrVideo ==PICTURE_CODE){
            assert data != null;
            Bitmap photo =(Bitmap) data.getExtras().get("data");
            binding.imageViewId.setImageBitmap(photo);

            savePicture();

        }else if (takingPictureOrVideo ==VIDEO_CODE){

            mediaController = new MediaController(requireActivity());
            binding.videoViewId.setVideoURI(videoPath);
            binding.videoViewId.start();
            mediaController.setAnchorView(binding.videoViewId);
            binding.videoViewId.setMediaController(mediaController);


        }

        takingPictureOrVideo = 0;
    }



}