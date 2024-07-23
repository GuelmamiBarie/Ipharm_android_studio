package com.example.ipharm;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.ipharm.Models.Medicine;
import com.example.ipharm.Models.Patient;

public class CalculActivity extends   AppCompatActivity
                            implements ViewPatients2Fragment.OnPatient2SelectedListener,
                                       CalculFragment.OnViewPatientsListener,
                                       ViewMedicines2Fragment.OnMedicine2SelectedListener,
                                       CalculFragment.OnViewMedicinesListener
{
    private static final String TAG = "CalculActivity";

    @Override
    public void OnPatient2Selected(Patient patient)
    {
        Log.d(TAG, "OnPatient2Selected: patient selected from:"
                +getString(R.string.view_patients2_fragment)
                +""+patient.getNameP());

        CalculFragment fragment = new CalculFragment();
        Bundle args = new Bundle();
        args.putParcelable(getString(R.string.patient), patient);
        fragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container2, fragment);
        transaction.addToBackStack(getString(R.string.patient_fragment));
        transaction.commit();
    }

    @Override
    public void OnMedicine2Selected(Medicine medicine)
    {
        Log.d(TAG, "OnMedicine2Selected: medicine selected from:"
                +getString(R.string.view_medicines2_fragment)
                +""+medicine.getNameM());

        CalculFragment fragment = new CalculFragment();
        Bundle args = new Bundle();
        args.putParcelable(getString(R.string.medicine), medicine);
        fragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container2, fragment);
        transaction.addToBackStack(getString(R.string.medicine_fragment));
        transaction.commit();
    }

    @Override
    public void OnViewPatients()
    {
        Log.d(TAG, "ViewPatients: Navigate to "+getString(R.string.view_patients2_fragment));

        ViewPatients2Fragment fragment = new ViewPatients2Fragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container2, fragment);
        transaction.addToBackStack(getString(R.string.view_patients2_fragment));
        transaction.commit();
    }

    @Override
    public void OnViewMedicines()
    {
        Log.d(TAG, "ViewMedicines: Navigate to "+getString(R.string.view_medicines2_fragment));

        ViewMedicines2Fragment fragment = new ViewMedicines2Fragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container2, fragment);
        transaction.addToBackStack(getString(R.string.view_medicines2_fragment));
        transaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calcul);
        Log.d(TAG, "onCreate: CalcActivity Started.");
        Init();
    }

    private void Init()
    {
        CalculFragment fragment = new CalculFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container2, fragment);
       // transaction.addToBackStack(null);
        transaction.commit();
    }
}
