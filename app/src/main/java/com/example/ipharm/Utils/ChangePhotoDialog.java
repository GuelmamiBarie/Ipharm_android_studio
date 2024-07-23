package com.example.ipharm.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.ipharm.R;

import java.io.File;

public class ChangePhotoDialog extends DialogFragment
{
    private static final String TAG = "ChangePhotoDialog";

    public interface OnPhotoReceivedListenner{
        public void getBitmapImage(Bitmap bitmap);
        public void getImagePath(String imagepath);
    }

    OnPhotoReceivedListenner mOnPhotoReceived;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.dialog_changephoto, container, false);

        //Initialize the TextView for starting the camera.
        TextView takePhoto = (TextView) view.findViewById(R.id.dialogTakePhoto);
        takePhoto.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.d(TAG, "onClick: Starting the camera.");
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent,Init.CAMERA_REQUEST_CODE);
            }
        });

        //Initialize the TextView for choosing an image from the memory.
        TextView selectPhoto = (TextView) view.findViewById(R.id.dialogChoosePhoto);
        selectPhoto.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.d(TAG, "onClick: Accessing phones memory.");
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, Init.PICKFILE_REQUEST_CODE);
            }
        });

        //Cancel Button for closing the dialog.
        TextView cancelDialog = (TextView) view.findViewById(R.id.dialogCancel);
        cancelDialog.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.d(TAG, "onClick: Closing dialog");
                getDialog().dismiss();
            }
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context)
    {
        super.onAttach(context);
        try {
            mOnPhotoReceived = (OnPhotoReceivedListenner) getTargetFragment();
        }catch(ClassCastException e){
            Log.d(TAG, "onAttach: ClassCastException:"+e.getMessage());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        /*
         * Results whene taking a new photo with the camera.
         * */
        if (requestCode == Init.CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK)
        {
            Log.d(TAG, "onActivityResult: Done taking a Picture.");

            //Get the new image bitmap.
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");

            Log.d(TAG, "onActivityResult: received bitmap:"+ bitmap);

            //send the bitmap and fragment to the interface.
            mOnPhotoReceived.getBitmapImage(bitmap);
            getDialog().dismiss();
        }

        /*
         * Results when selecting a new image from the memory.
         * */
        if (requestCode == Init.PICKFILE_REQUEST_CODE && resultCode == Activity.RESULT_OK)
        {
            Uri selectedImageUri = data.getData();
            File file = new File(selectedImageUri.toString());
            Log.d(TAG, "onActivityResult: imged:"+ file.getPath());

            //Send the bitmap and the fragment to the interface.
            mOnPhotoReceived.getImagePath(file.getPath());
            getDialog().dismiss();

        }

    }
}