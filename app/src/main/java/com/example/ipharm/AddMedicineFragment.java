package com.example.ipharm;

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

import com.example.ipharm.Models.Medicine;
import com.example.ipharm.Utils.DbMedicinesHelper;

public class AddMedicineFragment extends Fragment
{
    private static final String TAG = "AddMedicineFragment";

    private Medicine mMedicine;
    private EditText mNameM,mLabName,mPhoneNumberLab,mPresentBottle,mInitConst,mMinConst,mMaxConst,mPrice,mQuantity,mStability,mRemainingPart;
    private Toolbar toolBar;
    private int mPreviousKeyStroke;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_addmedicine, container,false);
        mNameM = view.findViewById(R.id.etMedicineName);
        mLabName = view.findViewById(R.id.etLaboName);
        mPhoneNumberLab = view.findViewById(R.id.etLaboPhone);
        mPresentBottle = view.findViewById(R.id.etPresentBottle);
        mInitConst = view.findViewById(R.id.etInitConst);
        mMinConst = view.findViewById(R.id.etMinConst);
        mMaxConst = view.findViewById(R.id.etMaxConst);
        mPrice = view.findViewById(R.id.etPriceM);
        mQuantity = view.findViewById(R.id.etQuantity);
        mStability = view.findViewById(R.id.etStability);
        mRemainingPart = view.findViewById(R.id.etRemainingPart);
        toolBar =  (Toolbar) view.findViewById(R.id.editMedicinetoolbar);
        Log.d(TAG, "onCreateView: Started.");

        //Set the heading for the toolbar.
        TextView heading = (TextView) view.findViewById(R.id.textMedicineToolbar);
        heading.setText(getString(R.string.add_medicine));

        //required for settings the toolbar.
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolBar);
        setHasOptionsMenu(true);

        Log.d(TAG, "onCreateView: here ? ");
        //Navigation for the backArrow.
        ImageView ivBackArrow1 = view.findViewById(R.id.ivBackArrow1);
        Log.d(TAG, "onCreateView: Or here ? ");
        ivBackArrow1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.d(TAG, "onClick: clicked back arrow.");
                //remove previous fragment from the BackStack (therefor navigating back).
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        Log.d(TAG, "onCreateView: Or here2 ? ");

        //Save new medicine, 'Set OnClickListener to the 'CheckMark' icon for saving a medicine'.
        ImageView ivCheckMark = (ImageView) view.findViewById(R.id.ivCheckMark1);
        ivCheckMark.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.d(TAG, "onClick: Saving the medicine.");

                //Testing if Edit texts are Empty.
                if (TestEmpty())
                {
                    //Execute the save method for the database.
                    if (checkStringIfNull(mNameM.getText().toString()))
                    {
                        Log.d(TAG, "onClick: saving the medicine"+ mNameM.getText().toString());

                        DbMedicinesHelper dbMedicineHelper = new DbMedicinesHelper(getActivity());

                        String name = mNameM.getText().toString();
                        String LabName = mLabName.getText().toString();
                        String phoneNumberLab = mPhoneNumberLab.getText().toString();
                        double presentB = Double.parseDouble(mPresentBottle.getText().toString());
                        double initC = Double.parseDouble(mInitConst.getText().toString());
                        double minC = Double.parseDouble(mMinConst.getText().toString());
                        double MaxC = Double.parseDouble(mMaxConst.getText().toString());
                        double price = Double.parseDouble(mPrice.getText().toString());
                        double rest = Double.parseDouble(mRemainingPart.getText().toString());
                        int quantity = Integer.parseInt(mQuantity.getText().toString());
                        int stability = Integer.parseInt(mStability.getText().toString());

                        Medicine medicine = new Medicine(name,LabName,phoneNumberLab,presentB,initC,minC,MaxC,price,rest,stability,quantity);

                        if (dbMedicineHelper.addMedicine(medicine))
                        {
                            Toast.makeText(getActivity(),"Médicament Ajouté",Toast.LENGTH_SHORT).show();
                            getActivity().getSupportFragmentManager().popBackStack();
                        }else
                        {
                            Toast.makeText(getActivity(),"Erreur De Sauvegarde",Toast.LENGTH_SHORT).show();
                        }
                    }
                }else
                {
                    Toast.makeText(getActivity(),"Tu oublies d'entrer quelque chose",Toast.LENGTH_SHORT).show();
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
}
