package com.example.ipharm;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.database.Cursor;

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
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.ipharm.Models.Medicine;
import com.example.ipharm.Utils.DbMedicinesHelper;

public class EditMedicineFragment extends Fragment
{
    private static final String TAG = "EditMedicineFragment";
    //This will evade the NullPointer exception when adding to a new bundle from MedicinesActivity.
    public EditMedicineFragment()
    {
        super();
        setArguments(new Bundle());
    }

    private Medicine mMedicine;
    private EditText mNameM,mLabName,mPhoneNumberLab,mPresentBottle,mInitConst,mMinConst,mMaxConst,mPrice,mRemainingPart,mStability,mQuantity;
    private Toolbar toolBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_editmedicine, container, false);
        mNameM = view.findViewById(R.id.etMedicineName);
        mLabName = view.findViewById(R.id.etLaboName);
        mPhoneNumberLab = view.findViewById(R.id.etLaboPhone);
        mPresentBottle = view.findViewById(R.id.etPresentBottle);
        mInitConst = view.findViewById(R.id.etInitConst);
        mMinConst = view.findViewById(R.id.etMinConst);
        mMaxConst = view.findViewById(R.id.etMaxConst);
        mPrice = view.findViewById(R.id.etPriceM);
        mRemainingPart = view.findViewById(R.id.etRemainingPart);
        mStability = view.findViewById(R.id.etStability);
        mQuantity = view.findViewById(R.id.etEditQuantity);

        toolBar = view.findViewById(R.id.editMedicinetoolbar);
        Log.d(TAG, "onCreateView: Started.");

        //Set the heading for the toolbar.
        TextView heading = view.findViewById(R.id.textMedicineToolbar);
        heading.setText(getString(R.string.edit_medicine));

        //required for settings the toolbar.
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolBar);
        setHasOptionsMenu(true);

        //Get the Medicine from the bundle.
        mMedicine = getMedicineFromBundle();

        if(mMedicine != null)
        {
            Init();
        }

        //Navigation for the backArrow.
        ImageView ivBackArrow1 = view.findViewById(R.id.ivBackArrow1);
        ivBackArrow1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Log.d(TAG, "onClick: clicked back arrow.");
                //remove previous fragment from the BackStack (therefor navigating back).
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        //Save Changes to the medicine.
        ImageView ivCheckMark = (ImageView) view.findViewById(R.id.ivCheckMark1);
        ivCheckMark.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.d(TAG, "onClick: Saving the editing medicine.");
                //Testing if edit texts are empty.
                if (TestEmpty())
                {
                    //Execute the save method for the database.
                    if(checkStringIfNull(mNameM.getText().toString()))
                    {
                        Log.d(TAG, "onClick: saving the changes to the medicine. " + mNameM.getText().toString());
                        //Get the databaseHelper and save the new changes to the medicine.
                        DbMedicinesHelper db = new DbMedicinesHelper(getActivity());
                        Cursor cursor = db.getMedicineId(mMedicine);

                        int medicineID = -1;
                        while(cursor.moveToNext())
                        {
                            medicineID = cursor.getInt(0);
                        }
                        if (medicineID > -1)
                        {
                            mMedicine.setNameM(mNameM.getText().toString());
                            mMedicine.setLaboFabM(mLabName.getText().toString());
                            mMedicine.setPhoneNumberLabo(mPhoneNumberLab.getText().toString());
                            mMedicine.setPresentBottle(Double.parseDouble(mPresentBottle.getText().toString()));
                            mMedicine.setInitConst(Double.parseDouble(mInitConst.getText().toString()));
                            mMedicine.setMinConst(Double.parseDouble(mMinConst.getText().toString()));
                            mMedicine.setMaxConst(Double.parseDouble(mMaxConst.getText().toString()));
                            mMedicine.setPriceM(Double.parseDouble(mPrice.getText().toString()));
                            mMedicine.setRemainingPartM(Double.parseDouble(mRemainingPart.getText().toString()));
                            mMedicine.setQuantityM(Integer.parseInt(mQuantity.getText().toString()));
                            mMedicine.setStabilityM(Integer.parseInt(mStability.getText().toString()));

                            db.updateMedicine(mMedicine, medicineID);
                            Toast.makeText(getActivity(),"Médicament Modifié ",Toast.LENGTH_SHORT).show();
                            getActivity().getSupportFragmentManager().popBackStack();
                        }
                        else
                            Toast.makeText(getActivity(),"Erreur De La Base De Données",Toast.LENGTH_SHORT).show();
                    }
                }else
                {
                    Toast.makeText(getActivity(),"Tu oublies d'entrer quelque chose",Toast.LENGTH_SHORT).show();
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
        mNameM.setText(mMedicine.getNameM());
        mLabName.setText(mMedicine.getLaboFabM());
        mPhoneNumberLab.setText(mMedicine.getPhoneNumberLabo());
        mPresentBottle.setText(mMedicine.getPresentBottle()+"");
        mInitConst.setText(mMedicine.getInitConst()+"");
        mMinConst.setText(mMedicine.getMinConst()+"");
        mMaxConst.setText(mMedicine.getMaxConst()+"");
        mPrice.setText(mMedicine.getPriceM()+"");
        mRemainingPart.setText(mMedicine.getRemainingPartM()+"");
        mStability.setText(mMedicine.getStabilityM()+"");
        mQuantity.setText(mMedicine.getQuantityM()+"");
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater)
    {
        inflater.inflate(R.menu.delete_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

   @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if (item.getItemId() == R.id.menuitem_delete) {
            // Handle delete action
            deleteMedicine();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    /*
     * Retrieves the selected medicine from the bundle (coming from MedicinesActivity).
     * */
    private Medicine getMedicineFromBundle()
    {
        Log.d(TAG, "getMedicineFromBundle: Arguments:"+ getArguments());

        Bundle bundle = this.getArguments();
        if(bundle != null)
        {
            return bundle.getParcelable(getString(R.string.medicine));
        }else
            return null;
    }

    //Testing the Edit Texts if they are Empty.
    private boolean TestEmpty()
    {
        String testName = mNameM.getText().toString();
        String testPhone = mPhoneNumberLab.getText().toString();
        String testInitConst = mInitConst.getText().toString();
        String testMinConst = mMinConst.getText().toString();
        String testMaxConst = mMaxConst.getText().toString();
        String testLabeName = mLabName.getText().toString();
        String testPresntation = mPresentBottle.getText().toString();
        String testPrice = mPrice.getText().toString();
        String testQuantity = mQuantity.getText().toString();
        String testStability = mStability.getText().toString();
        String testRemaininPart = mRemainingPart.getText().toString();

        if (TextUtils.isEmpty(testName))
        {
            return false;
        }
        else
        if (TextUtils.isEmpty(testPhone))
        {
            return false;
        }else
        if (TextUtils.isEmpty(testInitConst))
        {
            return false;
        }else
        if (TextUtils.isEmpty(testMinConst))
        {
            return false;
        }else
        if (TextUtils.isEmpty(testMaxConst))
        {
            return false;
        }
        else
        if (TextUtils.isEmpty(testPresntation))
        {
            return false;
        }else
        if (TextUtils.isEmpty(testLabeName))
        {
            return false;
        }else
        if (TextUtils.isEmpty(testPrice))
        {
            return false;
        }else
        if (TextUtils.isEmpty(testQuantity))
        {
            return false;
        }else
        if (TextUtils.isEmpty(testStability))
        {
            return false;
        }else
        if (TextUtils.isEmpty(testRemaininPart))
        {
            return false;
        }else
            return true;
    }

    private void deleteMedicine()
    {
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
}
