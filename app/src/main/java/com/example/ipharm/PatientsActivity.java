package com.example.ipharm;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;

import com.example.ipharm.Models.Patient;
import com.example.ipharm.Utils.UniversalImageLoader;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.ByteArrayOutputStream;

public class PatientsActivity extends AppCompatActivity
                              implements ViewPatientsFragment.OnPatientSelectedListener,
                                         PatientFragment.OnEditPatientListener,
                                         ViewPatientsFragment.OnAddPatientListener
{
    private static final String TAG = "PatientsActivity";
    private static final int REQUEST_CODE =1;

    @Override
    public void OnPatientSelected(Patient patient)
    {
        Log.d(TAG, "OnPatientSelected: patient selected from:"
                +getString(R.string.view_patients_fragment)
                +""+patient.getNameP());

        PatientFragment fragment = new PatientFragment();
        Bundle args = new Bundle();
        args.putParcelable(getString(R.string.patient), patient);
        fragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(getString(R.string.patient_fragment));
        transaction.commit();
    }


    @Override
    public void onEditPatientSelected(Patient patient)
    {
        Log.d(TAG, "onEditePatientSelected: patient selected edite:"
                +getString(R.string.edit_patient_fragment)
                +""+patient.getNameP());

        EditPatientFragment fragment = new EditPatientFragment();
        Bundle args = new Bundle();
        args.putParcelable(getString(R.string.patient), patient);
        fragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(getString(R.string.edit_patient_fragment));
        transaction.commit();
    }

    @Override
    public void OnAddPatient()
    {
        Log.d(TAG, "OnAddPatient: navigating to: "+getString(R.string.add_patient_fragment));

        AddPatientFragment fragment = new AddPatientFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(getString(R.string.add_patient_fragment));
        transaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patients);
        Log.d(TAG, "onCreate: started. ");
        InitImageLoader();
        Init();
    }

    /**
     * Initialize the first fragment (ViewPatientsFragment).
     * **/
    private void Init()
    {
        ViewPatientsFragment fragment = new ViewPatientsFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container,fragment);
        //transaction.addToBackStack(null);
        transaction.commit();
    }

    private void InitImageLoader()
    {
        UniversalImageLoader universalImageLoader = new UniversalImageLoader(PatientsActivity.this);
        ImageLoader.getInstance().init(universalImageLoader.getConfig());
    }

    /*
    * Compress the bitmap by the quality.
    * Quality can be anywhere from 1-100 : 100 is the highest quality.
    * */
    public Bitmap compressBitmap(Bitmap bitmap, int quality)
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,quality,stream);
        return bitmap;
    }

    /*
    * Generalized method for asking permission. Can pass any array of permissions.
    * */
    public void verifyPermissions (String[] permissions)
    {
        Log.d(TAG, "verifyPermissions: asking user for permissions.");
        ActivityCompat.requestPermissions(PatientsActivity.this,permissions,REQUEST_CODE);
    }


    /*
    * Checking if the permission was granted for the passed permission.
    * ONLY ONE PERMISSION MAYT BE CHECKED AT A TIME.
    * */
    public boolean checkPermission (String[] permission)
    {
        Log.d(TAG, "checkinPermission: checking permission for:"+permission[0]);

        int permissionRequest = ActivityCompat.checkSelfPermission(PatientsActivity.this,permission[0]);

        if(permissionRequest != PackageManager.PERMISSION_GRANTED)
        {
            Log.d(TAG, "checkinPermission: \n Permissions was not granted for:"+ permission[0]);
            return false;
        }else
            return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        Log.d(TAG, "onRequestPermissionsResult: requestCode:"+requestCode);
        switch(requestCode)
        {
            case REQUEST_CODE :
                for (int i=0; i<permissions.length; i++)
                {
                    if(grantResults[i] == PackageManager.PERMISSION_GRANTED)
                    {
                        Log.d(TAG, "onRequestPermissionsResult: User has allowed permission to access:"+permissions[i]);
                    }
                    else
                        break;
                }
                break;
        }
    }
}
