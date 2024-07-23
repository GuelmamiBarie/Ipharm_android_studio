package com.example.ipharm;


import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.ipharm.Models.Medicine;
import com.example.ipharm.Utils.DbMedicinesHelper;
import com.example.ipharm.Utils.MedicinePropertyListAdapter;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class MedicineFragment extends Fragment

{
    private static final String TAG = "MedicineFragment";

    public interface OnEditMedicineListener
    {
        public void onEditMedicineSelected(Medicine medicine);
    }
    OnEditMedicineListener mOnEditMedicineListener;

    //This will evade the NullPointer exception when adding to a new bundle from MedicineActivity.
    public MedicineFragment()
    {
        super();
        setArguments(new Bundle());
    }

    private Medicine mMedicine;
    private TextView mMedicineName,mMedicineLaboName,mConstIni,mConstMin,mConstMax,mPres,mPrice,mStability,mQtn,mRmP;
    private ListView mListView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        Toolbar toolbar;
        View view = inflater.inflate(R.layout.fragment_medicine, container, false);
        toolbar =  view.findViewById(R.id.medicinetoolbar);
        mMedicineName =  view.findViewById(R.id.medicineName);
        mMedicineLaboName = view.findViewById(R.id.medicineLaboName);
        mListView =  view.findViewById(R.id.lvMedicineProperties);
        mConstIni = view.findViewById(R.id.tvCI2);
        mConstMin = view.findViewById(R.id.tvCMi2);
        mConstMax = view.findViewById(R.id.tvCMa2);
        mPres = view.findViewById(R.id.tvPre2);
        mPrice = view.findViewById(R.id.tvPri2);
        mStability = view.findViewById(R.id.tvStb2);
        mQtn = view.findViewById(R.id.tvQtn2);
        mRmP = view.findViewById(R.id.tvRmP2);
        Log.d(TAG, "onCreateView: Started the MedicineFragment.");

        //required for settings the toolbar.
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

        mMedicine = getMedicineFromBundle();

        Init();
        
        //Navigation for the backArrow.
        ImageView ivBackArrow1 = view.findViewById(R.id.ivBackArrow);
        ivBackArrow1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Log.d(TAG, "onClick: clicked back arrow.");
                //remove previous fragment from the backStack (therefor navigating back).
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        //Navigate to the edit medicine fragment to edit the medicine selected.
        ImageView ivEdit = view.findViewById(R.id.ivEdit);
        ivEdit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.d(TAG, "onClick: clicked the Edit Icon.");
                mOnEditMedicineListener.onEditMedicineSelected(mMedicine);
            }
        });

        return view;
    }

    private void Init()
    {
        mMedicineName.setText(mMedicine.getNameM());
        mMedicineLaboName.setText(mMedicine.getLaboFabM());
        mConstIni.setText(mMedicine.getInitConst()+"");
        mConstMin.setText(mMedicine.getMinConst()+"");
        mConstMax.setText(mMedicine.getMaxConst()+"");
        mPres.setText(mMedicine.getPresentBottle()+"");
        mPrice.setText(mMedicine.getPriceM()+"");
        mRmP.setText(new DecimalFormat("##.##").format(mMedicine.getRemainingPartM()));
        mStability.setText(mMedicine.getStabilityM()+"");
        mQtn.setText(mMedicine.getQuantityM()+"");
        /*
        *
        *
        * Here where i can add the other properties to the CardView.
        *
        *
        * */
        ArrayList<String> properties = new ArrayList<>();
        properties.add(mMedicine.getPhoneNumberLabo());
        MedicinePropertyListAdapter adapter = new MedicinePropertyListAdapter(getActivity(),R.layout.layout_cardview,properties);
        mListView.setAdapter(adapter);
        mListView.setDivider(null);
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater)
    {
        inflater.inflate(R.menu.delete_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    /*@Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.menuitem_delete:
                Log.d(TAG, "onOptionsItemSelected: deleting medicine.");

                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
                alertBuilder.setTitle("Confirmation")
                        .setMessage("êtes-vous sûr ?")
                        .setPositiveButton("Oui", new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        //delete selled.
                                        DbMedicinesHelper dbMedicinesHelper = new DbMedicinesHelper(getActivity());
                                        Cursor cursor = dbMedicinesHelper.getMedicineId(mMedicine);
                                        int medicineID = -1;
                                        while(cursor.moveToNext())
                                        {
                                            medicineID = cursor.getInt(0);
                                        }
                                        if (medicineID > -1)
                                        {
                                            if (dbMedicinesHelper.deleteMedicine(medicineID) > 0)
                                            {
                                                Toast.makeText(getActivity(), "Médicament Supprimé", Toast.LENGTH_SHORT).show();
                                                //Clear the arguments on the current bundle since the Medicine is deleted.
                                                getArguments().clear();
                                                //Remove previous fragment from the backStack (therefore navigating back).
                                                getActivity().getSupportFragmentManager().popBackStack();
                                            } else
                                                {
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
    * Retrieves the selected medicine from the bundle (coming from MedicinesActivity).
    * */
    private Medicine getMedicineFromBundle()
    {
        Log.d(TAG, "getMedicineFromBundle: arguments:"+getArguments());

        Bundle bundle = this.getArguments();
        if(bundle != null)
        {
            return bundle.getParcelable(getString(R.string.medicine));
        }else
            return null;
    }

    @Override
    public void onAttach(@NonNull Context context)
    {
        super.onAttach(context);
        try {
            mOnEditMedicineListener = (OnEditMedicineListener) getActivity();
        }catch(ClassCastException e)
        {
            Log.d(TAG, "onAttach: ClassCastException:"+ e.getMessage());
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        DbMedicinesHelper dbMedicinesHelper = new DbMedicinesHelper(getActivity());
        Cursor cursor = dbMedicinesHelper.getMedicineId(mMedicine);

        int medicineID = -1;
        while(cursor.moveToNext())
        {
            medicineID = cursor.getInt(0);
        }
        if (medicineID > -1)
        {
            //Testing if the medicine doesn't still exists and navigating back by popping the stack.
            Init();// it's not really necessary to do that.
        }else
        {
            getArguments().clear();
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }
}