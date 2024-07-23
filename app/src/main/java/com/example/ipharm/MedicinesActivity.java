package com.example.ipharm;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;

import com.example.ipharm.Models.Medicine;

public class MedicinesActivity extends AppCompatActivity 
                               implements ViewMedicinesFragment.OnMedicineSelectedListener,
                                          MedicineFragment.OnEditMedicineListener,
                                          ViewMedicinesFragment.OnAddMedicineListener
{
    private static final String TAG = "MedicinesActivity";
    private static final int REQUEST_CODE =1;

    @Override
    public void OnMedicineSelected(Medicine medicine)
    {
        Log.d(TAG, "OnMedicineSelected: medicine selected from:"
                +getString(R.string.view_medicines_fragment)
                +""+medicine.getNameM());

        MedicineFragment fragment = new MedicineFragment();
        Bundle args = new Bundle();
        args.putParcelable(getString(R.string.medicine), medicine);
        fragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container1, fragment);
        transaction.addToBackStack(getString(R.string.medicine_fragment));
        transaction.commit();
    }
    
    @Override
    public void onEditMedicineSelected(Medicine medicine)
    {
        Log.d(TAG, "onEditMedicineSelected: medicine selected edit:"
                +getString(R.string.edit_medicine_fragment)
                +""+medicine.getNameM());

        EditMedicineFragment fragment = new EditMedicineFragment();
        Bundle args = new Bundle();
        args.putParcelable(getString(R.string.medicine), medicine);
        fragment.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container1, fragment);
        transaction.addToBackStack(getString(R.string.edit_medicine_fragment));
        transaction.commit();
    }

    @Override
    public void OnAddMedicine()
    {
        Log.d(TAG, "OnAddMedicine: navigating to:" + getString(R.string.add_medicine_fragment));

        AddMedicineFragment fragment = new AddMedicineFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container1, fragment);
        transaction.addToBackStack(getString(R.string.add_medicine_fragment));
        transaction.commit();
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicines);
        Log.d(TAG, "onCreate: started. ");
        Init();
    }

    /**
     * Initialize the first fragment (ViewMedicinesFragment).
     * **/
    private void Init()
    {
        ViewMedicinesFragment fragment = new ViewMedicinesFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container1,fragment);
        //transaction.addToBackStack(null);
        transaction.commit();
    }

    public void verifyPermissions (String[] permissions)
    {
        Log.d(TAG, "verifyPermissions: asking user for permissions.");
        ActivityCompat.requestPermissions(MedicinesActivity.this,permissions,REQUEST_CODE);
    }


    /*
     * Checking if the permission was granted for the passed permission.
     * ONLY ONE PERMISSION MAYT BE CHECKED AT A TIME.
     * */
    public boolean checkPermission (String[] permission)
    {
        Log.d(TAG, "checkinPermission: checking permission for:"+permission[0]);

        int permissionRequest = ActivityCompat.checkSelfPermission(MedicinesActivity.this,permission[0]);

        if(permissionRequest != PackageManager.PERMISSION_GRANTED)
        {
            Log.d(TAG, "checkingPermission: \n Permissions was not granted for:"+ permission[0]);
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
