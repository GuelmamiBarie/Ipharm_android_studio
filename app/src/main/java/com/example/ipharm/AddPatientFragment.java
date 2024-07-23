package com.example.ipharm;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.ipharm.Models.Patient;
import com.example.ipharm.Utils.ChangePhotoDialog;
import com.example.ipharm.Utils.DbPatientsHelper;
import com.example.ipharm.Utils.Init;
import com.example.ipharm.Utils.UniversalImageLoader;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddPatientFragment extends Fragment implements ChangePhotoDialog.OnPhotoReceivedListenner
{
    private static final String TAG = "AddPatientFragment";

    //private Patient mPatient;
    private EditText mName,mPhoneNumber,mSize,mWeight,mBodySurface;
    private CircleImageView mPatienImage;
    private Toolbar toolBar;
    private String mSelectedImagePath;
    private int mPreviousKeyStroke;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_addpatient, container,false);
        mPhoneNumber = (EditText) view.findViewById(R.id.etPatientPhone);
        mName = (EditText) view.findViewById(R.id.etPatientName);
        mSize = (EditText) view.findViewById(R.id.etPatientSize);
        mWeight = (EditText) view.findViewById(R.id.etPatientWeight);
        mBodySurface = (EditText) view.findViewById(R.id.etPatientBodySurface);
        mPatienImage = (CircleImageView) view.findViewById(R.id.patientImage);
        toolBar =  (Toolbar) view.findViewById(R.id.editPatienttoolbar);
        Log.d(TAG, "onCreateView: Started.");

        mSelectedImagePath = null;

        //Loading the default patient image by causing an error.
        UniversalImageLoader.setImage(null,mPatienImage,null,"");

        //Set the heading for the toolbar.
        TextView heading = (TextView) view.findViewById(R.id.textPatientToolbar);
        heading.setText(getString(R.string.add_patient));

        //required for settings the toolbar.
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolBar);
        setHasOptionsMenu(true);

        //Navigation for the backarrow.
        ImageView ivBackArrow = (ImageView) view.findViewById(R.id.ivBackArrow);
        ivBackArrow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.d(TAG, "onClick: clicked back arrow.");
                //remove previous fragment from the BackStack (therefor navigating back).
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        //Save new patient, 'Set OnClickListener to the 'CheckMark' icon for saving a patient'.
        ImageView ivCheckMark = view.findViewById(R.id.ivCheckMark);
        ivCheckMark.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.d(TAG, "onClick: Saving the patient.");
                //testing empty.
                if (TestEmpty())
                {
                    //Execute the save method for the database.
                    if (checkStringIfNull(mName.getText().toString()))
                    {
                        Log.d(TAG, "onClick: saving the patient"+ mName.getText().toString());

                        DbPatientsHelper dbPatientsHelper = new DbPatientsHelper(getActivity());

                        String name = mName.getText().toString();
                        String phoneNumber = mPhoneNumber.getText().toString();
                        double size = Double.parseDouble(mSize.getText().toString());
                        double weight = Double.parseDouble(mWeight.getText().toString());
                        double bodySurface = Double.parseDouble(mBodySurface.getText().toString());

                        Patient patient = new Patient(name,phoneNumber,size,weight,bodySurface,mSelectedImagePath);

                        if (dbPatientsHelper.addPatient(patient))
                        {
                            Toast.makeText(getActivity(),"Patient Ajouté",Toast.LENGTH_SHORT).show();
                            getActivity().getSupportFragmentManager().popBackStack();
                        }else
                        {
                            Toast.makeText(getActivity(),"Erreur De Sauvegarde",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else
                {
                    Toast.makeText(getActivity(),"Tu oublies d'entrer quelque chose",Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Initiate the dialog box for choosing an image.
        ImageView ivCamera = (ImageView) view.findViewById(R.id.ivCamera);
        ivCamera.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                /*
                 * Make sure all permissions have been verified before opining the camera.
                 * */
                for (int i = 0; i < Init.PERMISSIONS.length; i++)
                {
                    String[] permission = {Init.PERMISSIONS[i]};

                    if (((PatientsActivity)getActivity()).checkPermission(permission))
                    {
                        if (i == Init.PERMISSIONS.length - 1)
                        {
                            Log.d(TAG, "onClick: opening the image selection dialog box.");

                            ChangePhotoDialog dialog = new ChangePhotoDialog();
                            dialog.show(getFragmentManager(), getString(R.string.change_photo_dialog));
                            dialog.setTargetFragment(AddPatientFragment.this,0);
                        }
                    }else {
                        ((PatientsActivity) getActivity()).verifyPermissions(permission);
                    }
                }
            }
        });
        //initOnTextChangeListener();

        return view;
    }

    private boolean checkStringIfNull(String string)
    {
        if (string.equals(""))
        {
            return false;
        }else
            return true;
    }


    /*
    * Initializing the OnTextChangeListener for formatting the phone number.
    *
    * It doesn't work now, in didn't find the problem, i'll check later.
    *
    * */
    /*private void initOnTextChangeListener()
    {
        mPhoneNumber.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                mPreviousKeyStroke = keyCode;
                return false;
            }
        });

        mPhoneNumber.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s)
            {
                String number = s.toString();
                Log.d(TAG, "afterTextChanged: " +number);


                if (number.length() == 3 && mPreviousKeyStroke != KeyEvent.KEYCODE_DEL
                                         && !number.contains("("))
                {
                    number = String.format("(%s",s.toString().substring(0,3));
                    mPhoneNumber.setText(number);
                    mPhoneNumber.setSelection(number.length());
                }else if (number.length() == 5 &&  mPreviousKeyStroke != KeyEvent.KEYCODE_DEL
                                               && !number.contains(")"))
                {
                    number = String.format("(%s) %s",
                            toString().substring(1,4),
                            toString().substring(4,5));
                    mPhoneNumber.setText(number);
                    mPhoneNumber.setSelection(number.length());

                }else if (number.length() == 10 && mPreviousKeyStroke != KeyEvent.KEYCODE_DEL
                                               && !number.contains("-"))
                {
                    number = String.format("(%s) %s-%s",
                            toString().substring(1,4),
                            toString().substring(6,9),
                            toString().substring(9,10));
                    mPhoneNumber.setText(number);
                    mPhoneNumber.setSelection(number.length());
                }
            }
        });
    }*/



    /*
     * Retrieves the selected picture from the bundle (Coming from ChangePhotoDialog).
     * */
    @Override
    public void getBitmapImage(Bitmap bitmap)
    {
        Log.d(TAG, "getBitmapImage: get the bitmap:" + bitmap);
        //get the bitmap from ChangePhotoDialog.
        if (bitmap != null)
        {
            //compress the image.
            ((PatientsActivity)getActivity()).compressBitmap(bitmap,70);
            mPatienImage.setImageBitmap(bitmap);
        }

    }

    @Override
    public void getImagePath(String imagepath)
    {
        Log.d(TAG, "getImagePath: Image path:"+imagepath);
        if (!imagepath.equals(""))
        {
            imagepath = imagepath.replace(":/","://");
            mSelectedImagePath = imagepath;
            UniversalImageLoader.setImage(imagepath, mPatienImage , null, "");

        }
    }

    private boolean TestEmpty()
    {
        String testName = mName.getText().toString();
        String testBodySurface = mBodySurface.getText().toString();
        String testPhone = mPhoneNumber.getText().toString();
        String testSize = mSize.getText().toString();
        String testWeight =mWeight.getText().toString();

        if (TextUtils.isEmpty(testBodySurface))
        {
            return false;
        }
        else
        if (TextUtils.isEmpty(testName))
        {
            return false;
        }else
        if (TextUtils.isEmpty(testPhone))
        {
            return false;
        }else
        if (TextUtils.isEmpty(testSize))
        {
            return false;
        }else
        if (TextUtils.isEmpty(testWeight))
        {
            return false;
        }
        else
            return true;
    }

}
