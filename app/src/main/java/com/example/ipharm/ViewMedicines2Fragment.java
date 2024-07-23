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

import com.example.ipharm.Models.Medicine;
import com.example.ipharm.Utils.DbMedicinesHelper;
import com.example.ipharm.Utils.MedicineListAdapter;
import com.google.android.material.appbar.AppBarLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

public class ViewMedicines2Fragment extends Fragment
{
    private static final String TAG = "ViewMedicines2Fragment";

    public interface OnMedicine2SelectedListener
    {
        public void OnMedicine2Selected(Medicine pat);
    }
    OnMedicine2SelectedListener mMedicine2Listener;

    private static final int STANDARD_APPBAR = 0;
    private static final int SEARCH_APPBAR = 1;
    private int mAppBarState;

    private AppBarLayout viewMedicineBar ,searchBar;
    private MedicineListAdapter adapter;
    private ListView medicinesList;
    private EditText mSearchMedicines;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_viewmedicines2,container,false);
        viewMedicineBar = view.findViewById(R.id.viewMedicinesToolBar);
        searchBar =  view.findViewById(R.id.searchToolBar);
        medicinesList =  view.findViewById(R.id.medicinesList2);
        mSearchMedicines = view.findViewById(R.id.etSearch);
        Log.d(TAG, "onCreateView: Started.");

        setAppBarState(STANDARD_APPBAR);

        setupMedicinesList();

        ImageView ivSearchMedicine =  view.findViewById(R.id.ivSearchIcon);
        ivSearchMedicine.setOnClickListener(new View.OnClickListener()
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
                setupMedicinesList();
            }
        });

        ImageView ivBackArrow2 = view.findViewById(R.id.ivBackArrow);
        ivBackArrow2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Log.d(TAG, "onClick: clicked back arrow of the fragment to go back to the main activity.");
                //remove previous fragment from the BackStack (therefor navigating back).
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            mMedicine2Listener = (OnMedicine2SelectedListener) getActivity();
        }catch(ClassCastException e)
        {
            Log.e(TAG, "onAttach: ClassCastException:"+e.getMessage());
        }

    }

    private void setupMedicinesList() {
        final ArrayList<Medicine> medicines = new ArrayList<>();
//      patients.add(new Patient("Medicine", "(213) 665-4777", 15, 18, 18, testImageURL));

        DbMedicinesHelper dbMedicinesHelper = new DbMedicinesHelper(getActivity());
        Cursor cursor = dbMedicinesHelper.getAllMedicines();
        //Iterate through all the rows in the database.
        if (!cursor.moveToNext())
        {
            Toast.makeText(getActivity(),"il n'y a pas encore de médicament à montrer.",Toast.LENGTH_SHORT).show();
        }
        while (cursor.moveToNext())
        {
            String name = cursor.getString(1);
            String labFabName = cursor.getString(2);
            String phoneNumberLab = cursor.getString(3);
            Double presentBottle = cursor.getDouble(4);
            Double initConst = cursor.getDouble(5);
            Double minConst = cursor.getDouble(6);
            Double maxConst = cursor.getDouble(7);
            Double priceM = cursor.getDouble(8);
            Double remainingPart = cursor.getDouble(9);
            int stability = cursor.getInt(10);
            int quantity = cursor.getInt(11);

            medicines.add(new Medicine(name,labFabName,phoneNumberLab,presentBottle,initConst,minConst,maxConst,priceM,remainingPart,stability,quantity));
        }

        //Sort the array list based on the patient Name.
        Collections.sort(medicines, new Comparator<Medicine>() {
            @Override
            public int compare(Medicine o1, Medicine o2) {
                return o1.getNameM().compareToIgnoreCase(o2.getNameM());
            }
        });

        adapter = new MedicineListAdapter(getActivity(), R.layout.layout_medicineslistitems, medicines);

        mSearchMedicines.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
                String text = mSearchMedicines.getText().toString().toLowerCase(Locale.getDefault());
                adapter.filter(text);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {}
        });

        medicinesList.setAdapter(adapter);

        medicinesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Log.d(TAG, "onClick: navigation to:" + getString(R.string.medicine_fragment));
                //Pass the medicine to the interface and send it to MedicineActivity.
                mMedicine2Listener.OnMedicine2Selected(medicines.get(position));
            }
        });
        dbMedicinesHelper.close();
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
            viewMedicineBar.setVisibility(View.VISIBLE);

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
            viewMedicineBar.setVisibility(View.GONE);
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
