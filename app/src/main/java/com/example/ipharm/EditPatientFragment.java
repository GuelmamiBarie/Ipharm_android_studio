package com.example.ipharm;

import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.ipharm.Models.Patient;
import com.example.ipharm.Utils.ChangePhotoDialog;
import com.example.ipharm.Utils.DbPatientsHelper;
import com.example.ipharm.Utils.Init;
import com.example.ipharm.Utils.UniversalImageLoader;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditPatientFragment extends Fragment implements ChangePhotoDialog.OnPhotoReceivedListenner
{
    private static final String TAG = "EditPatientFragment";

    //This will evade the NullPointer exception when adding to a new bundle from PatientActivity.
    public EditPatientFragment ()
    {
        super();
        setArguments(new Bundle());
    }

    private Patient mPatient;
    private EditText mName,mPhoneNumber,mSize,mWeight,mBodySurface;
    private CircleImageView mPatienImage;
    private Toolbar toolBar;
    private String mSelectedImagePath;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_editpatient, container, false);
        mPhoneNumber = view.findViewById(R.id.etPatientPhone);
        mName = view.findViewById(R.id.etPatientName);
        mSize = view.findViewById(R.id.etPatientSize);
        mWeight = view.findViewById(R.id.etPatientWeight);
        mBodySurface = view.findViewById(R.id.etPatientBodySurface);
        mPatienImage = view.findViewById(R.id.patientImage);
        toolBar = view.findViewById(R.id.editPatienttoolbar);
        Log.d(TAG, "onCreateView: Started.");

        mSelectedImagePath =null;

        //Set the heading for the toolbar.
        TextView heading = (TextView) view.findViewById(R.id.textPatientToolbar);
        heading.setText(getString(R.string.edit_patient));

        //required for settings the toolbar.
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolBar);
        setHasOptionsMenu(true);

        //Get the Patient from the bundle.
        mPatient = getPatientFromBundle();

        if(mPatient != null)
        {
            Init();
        }

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

        //Save Changes to the Patients.
        ImageView ivCheckMark = (ImageView) view.findViewById(R.id.ivCheckMark);
        ivCheckMark.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.d(TAG, "onClick: Saving the editing patient.");

                //Testing edit texts if they are empty.
                if (TestEmpty())
                {
                    //Execute the save method for the database.
                    if(checkStringIfNull(mName.getText().toString()))
                    {
                        Log.d(TAG, "onClick: saving the changes to the patient. " + mName.getText().toString() );
                        //Get the databaseHelper and save the new changes to the patient.
                        DbPatientsHelper db = new DbPatientsHelper(getActivity());
                        Cursor cursor = db.getPatientId(mPatient);

                        int patientID = -1;
                        while(cursor.moveToNext())
                        {
                            patientID = cursor.getInt(0);
                        }
                        if (patientID > -1)
                        {
                            if (mSelectedImagePath != null)
                            {
                                mPatient.setProfileImageP(mSelectedImagePath);
                            }
                            mPatient.setNameP(mName.getText().toString());
                            mPatient.setPhonenumber(mPhoneNumber.getText().toString());
                            mPatient.setSizeP(Double.parseDouble(mSize.getText().toString()));
                            mPatient.setWeightP(Double.parseDouble(mWeight.getText().toString()));
                            mPatient.setBodySurfaceP(Double.parseDouble(mBodySurface.getText().toString()));

                            db.updatePatient(mPatient, patientID);
                            Toast.makeText(getActivity(),"Patient Modifié ",Toast.LENGTH_SHORT).show();
                            getActivity().getSupportFragmentManager().popBackStack();
                        }
                        else
                            Toast.makeText(getActivity(),"Erreur De La Base De Données",Toast.LENGTH_SHORT).show();
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
                            dialog.setTargetFragment(EditPatientFragment.this,0);
                        }
                    }else {
                        ((PatientsActivity) getActivity()).verifyPermissions(permission);
                    }
                }
            }
        });
        
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

    private void Init ()
    {
        mPhoneNumber.setText(mPatient.getPhonenumber());
        mName.setText(mPatient.getNameP());
        mSize.setText(mPatient.getSizeP()+"");
        mWeight.setText(mPatient.getWeightP()+"");
        mBodySurface.setText(mPatient.getBodySurfaceP()+"");
        UniversalImageLoader.setImage(mPatient.getProfileImageP(),mPatienImage,null,"");
        /*
        * Here he setting the selected device to the spinner that i didn't use.
        * */
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater)
    {
        inflater.inflate(R.menu.delete_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

   /* @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.menuitem_delete:
                Log.d(TAG, "onOptionsItemSelected: deleting patient.");
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
                alertBuilder.setTitle("Confirmation")
                        .setMessage("êtes-vous sûr ?")
                        .setPositiveButton("Oui", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //delete selled.
                                DbPatientsHelper dbPatientsHelper = new DbPatientsHelper(getActivity());
                                Cursor cursor = dbPatientsHelper.getPatientId(mPatient);
                                int patientID = -1;
                                while (cursor.moveToNext()) {
                                    patientID = cursor.getInt(0);
                                }
                                if (patientID > -1)
                                {
                                    if (dbPatientsHelper.deletePatient(patientID) > 0)
                                    {
                                        Toast.makeText(getActivity(), "Patient Supprimé", Toast.LENGTH_SHORT).show();
                                        //Clear the arguments on the current bundle since the patient is deleted.
                                        getArguments().clear();
                                        //Remove previous fragment from the backStack (therefore navigating back).
                                        getActivity().getSupportFragmentManager().popBackStack();
                                    } else {
                                        Toast.makeText(getActivity(), "Erreur De La Base De Données", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        })
                        .setNegativeButton("Non", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                dialog.dismiss();
                            }
                        });
                AlertDialog dialog = alertBuilder.create();
                dialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    */

    /*
     * Retrieves the selected patient from the bundle (coming from PatientActivity).
     * */
    private Patient getPatientFromBundle()
    {
        Log.d(TAG, "getPatientFromBundle: Arguments:"+ getArguments());

        Bundle bundle = this.getArguments();
        if(bundle != null)
        {
            return bundle.getParcelable(getString(R.string.patient));
        }else
            return null;
    }

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
