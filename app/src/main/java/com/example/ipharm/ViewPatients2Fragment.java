package com.example.ipharm;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ipharm.Models.Patient;
import com.example.ipharm.Utils.DbPatientsHelper;
import com.example.ipharm.Utils.PatientListAdapter;
import com.google.android.material.appbar.AppBarLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

public class ViewPatients2Fragment extends Fragment
{

    private static final String TAG = "ViewPatients2Fragment";

    public interface OnPatient2SelectedListener
    {
        public void OnPatient2Selected(Patient pat);
    }
    OnPatient2SelectedListener mPatientListener;

    private static final int STANDARD_APPBAR = 0;
    private static final int SEARCH_APPBAR = 1;
    private int mAppBarState;
    private AppBarLayout viewPatientBar ,searchBar;
    private PatientListAdapter adapter;
    private ListView patientsList;
    private EditText mSearchPatients;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_viewpatients2,container,false);
        viewPatientBar = view.findViewById(R.id.viewPatientToolBar);
        searchBar = view.findViewById(R.id.searchToolBar);
        patientsList =  view.findViewById(R.id.patientsList2);
        mSearchPatients = view.findViewById(R.id.etSearch);
        Log.d(TAG, "onCreateView: Started.");


        setAppBarState(STANDARD_APPBAR);

        setupPatientsList();

        ImageView ivSearchPatient = view.findViewById(R.id.ivSearchIcon);
        ivSearchPatient.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.d(TAG, "onClick: clicked search icon.");
                ToggleToolBarState();
            }
        });

        ImageView ivBackArrow = view.findViewById(R.id.ivBackArrow_teal);
        ivBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Log.d(TAG, "onClick: clicked back arrow of the search bar.");
                ToggleToolBarState();
                setupPatientsList();
            }
        });

        ImageView ivBackArrow2 = view.findViewById(R.id.ivBackArrow);
        ivBackArrow2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Log.d(TAG, "onClick: clicked back arrow of the fragment.");
                //remove previous fragment from the BackStack (therefor navigating back).
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context)
    {
        super.onAttach(context);

        try {
            mPatientListener = (OnPatient2SelectedListener) getActivity();
        }catch(ClassCastException e)
        {
            Log.e(TAG, "onAttach: ClassCastException:"+e.getMessage());
        }
    }

    private void setupPatientsList()
    {
        final ArrayList<Patient> patients = new ArrayList<>();
//        patients.add(new Patient("Medicine", "(213) 665-4777", 15, 18, 18, testImageURL));

        DbPatientsHelper dbPatientsHelper = new DbPatientsHelper(getActivity());
        Cursor cursor = dbPatientsHelper.getAllPatients();
        //Iterate through all the rows in the database.
        if (!cursor.moveToNext())
        {
            Toast.makeText(getActivity(),"il n'y a pas encore de patient à montrer.",Toast.LENGTH_SHORT).show();
        }
        while (cursor.moveToNext())
        {
            String name = cursor.getString(1);
            String phoneNumber = cursor.getString(2);
            Double size = cursor.getDouble(3);
            Double weight = cursor.getDouble(4);
            Double bodySurface = cursor.getDouble(5);
            String imagePatient = cursor.getString(6);

            patients.add(new Patient(name,phoneNumber,size,weight,bodySurface,imagePatient));
        }

        //Sort the array list based on the patient Name.
        Collections.sort(patients, new Comparator<Patient>() {
            @Override
            public int compare(Patient o1, Patient o2) {
                return o1.getNameP().compareToIgnoreCase(o2.getNameP());
            }
        });

        Log.d(TAG, "setupPatientsList: Before Initialization of the adapter.\n\n");
        adapter = new PatientListAdapter(getActivity(), R.layout.layout_patientslistitems, patients, "");
        Log.d(TAG, "setupPatientsList: After Initialization of the adapter.\n\n");
        mSearchPatients.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
                String text = mSearchPatients.getText().toString().toLowerCase(Locale.getDefault());
                adapter.filter(text);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        patientsList.setAdapter(adapter);

        patientsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Log.d(TAG, "onClick: navigation to:" + getString(R.string.patient_fragment));
                //Pass the patient to the interface and send it to PatientActivity.
                mPatientListener.OnPatient2Selected(patients.get(position));
            }
        });
        dbPatientsHelper.close();
    }

    /**
     * Initiates the Appbar toggle.
     * **/
    private void ToggleToolBarState()
    {
        Log.d(TAG, "ToggleToolBarState: toggling app bar state.");
        if (mAppBarState == STANDARD_APPBAR)
        {
            setAppBarState(SEARCH_APPBAR);
        }
        else
        {
            setAppBarState(STANDARD_APPBAR);
        }
    }

    /**
     *Set The Appbar state for either the search 'mode' or the 'standard' mode.
     * **/
    private void setAppBarState(int state)
    {
        Log.d(TAG, "setAppBarState: changing app bar state to : "+ state);
        mAppBarState = state;

        if (mAppBarState == STANDARD_APPBAR)
        {
            searchBar.setVisibility(View.GONE);
            viewPatientBar.setVisibility(View.VISIBLE);

            View view = getView();

            //Hide The Keyboard
            InputMethodManager Imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            try {
                Imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }catch (NullPointerException e){
                Log.d(TAG, "setAppBarState: NullPointerException : "+e.getMessage());
            }
        }
        else if (mAppBarState == SEARCH_APPBAR)
        {
            viewPatientBar.setVisibility(View.GONE);
            searchBar.setVisibility(View.VISIBLE);

            //Open The Keyboard
            InputMethodManager Imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            Imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0 );
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        setAppBarState(STANDARD_APPBAR);
    }
}
