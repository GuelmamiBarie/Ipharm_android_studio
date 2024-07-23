package com.example.ipharm;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.ipharm.Models.Medicine;
import com.example.ipharm.Models.Patient;
import com.example.ipharm.Models.Selled;
import com.example.ipharm.Utils.DbMedicinesHelper;
import com.example.ipharm.Utils.DbSalesHelpler;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;

public class CalculFragment extends Fragment
{
    private static final String TAG = "CalculFragment";
    String mNameM,mLabName,mPhoneNumberLab;
    Double mPresentBottle,InitConst,MinConst,MaxConst,mPrice,mRemainingPart;
    int mStability,mQuantity;
    private Toolbar toolBar;
    private Button btnGetclient, btnGetMedicines, btnCalculation, btnValidation,btnReset;
    ImageView ivBackArrow;
    TextView heading,mTvDoseToBeAdministered,mTvFinalVolume,mTvNumberOfBottlesNeeded,mTvPocketTypeCalc,mTvRemainderCalc;
    private EditText mBodySurface,mPosologie, mInitConst,mMinConst,mMaxConst,mPresntation;
    private Patient mPatient;
    private Medicine mMedicine;
    private ScrollView mMainScrollView;

    public interface OnViewPatientsListener
    {
        public void OnViewPatients();
    }
    OnViewPatientsListener mOnViewPatients;

    public interface OnViewMedicinesListener
    {
        public void OnViewMedicines();
    }
    OnViewMedicinesListener mOnViewMedicines;

    //This will evade the NullPointer exception when adding to a new bundle from PatientActivity.
    public CalculFragment()
    {
        super();
        setArguments(new Bundle());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_calcul, container, false);

        //Initialization the widgets.
        toolBar = view.findViewById(R.id.calculToolbar);
        heading = view.findViewById(R.id.textClacuToolbar);
        ivBackArrow = view.findViewById(R.id.ivBackArrow);
        //btnGetclient = view.findViewById(R.id.btnGetClient);
        mBodySurface = view.findViewById(R.id.edSurfaceCorpo);
        mPosologie = view.findViewById(R.id.edPosologie);
        mInitConst = view.findViewById(R.id.edInitConstCalc);
        mMinConst = view.findViewById(R.id.edMinConstCalc);
        mMaxConst = view.findViewById(R.id.edMaxConstCalc);
        mPresntation = view.findViewById(R.id.edPrésentationCalc);
        btnCalculation = view.findViewById(R.id.buttonCalc);
        btnGetMedicines = view.findViewById(R.id.btnGetMedicine);
        btnValidation= view.findViewById(R.id.btnValidation);
        btnReset = view.findViewById(R.id.buttonReset);
        mTvDoseToBeAdministered = view.findViewById(R.id.tvDoseToBeAdministered);
        mTvFinalVolume =  view.findViewById (R.id.tvFinalVolume);
        mTvNumberOfBottlesNeeded = view.findViewById (R.id.tvNumberOfBottlesNeeded);
        mTvPocketTypeCalc = view.findViewById (R.id.tvPocketTypeCalc);
        mTvRemainderCalc = view.findViewById (R.id.tvRemainderCalc);
        mMainScrollView = view.findViewById(R.id.mainScrollView);
        Log.d(TAG, "onCreateView: Calculation Activity Started.");

        //Disable the validation Button.
        btnValidation.setEnabled(false);

        //Set the heading for the toolbar.
        heading.setText(getString(R.string.calculation));

        /*//Get the Patient from the bundle.
        mPatient = getPatientFromBundle();
        if(mPatient != null)
        {
            Init();
        }*/

        //Get the Medicine from the bundle.
        mMedicine = getMedicineFromBundle();
        if (mMedicine != null)
        {
            Init2();
        }

        //Navigation for the backArrow.
        ivBackArrow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.d(TAG, "onClick: clicked back arrow from CalcluActivity to MainActivity.");
                goBackToMainActivity();
            }
        });

        /*//Get a patient from the listView 'ViewPatient2Fragment'
        btnGetclient.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.d(TAG, "onClick: onClick GetClients.");
                mOnViewPatients.OnViewPatients();
            }
        });*/

        //get a medicine from the listView
        btnGetMedicines.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.d(TAG, "onClick: onClick getMedicine button.");
                mOnViewMedicines.OnViewMedicines();
            }
        });

        //Calculation
        btnCalculation.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.d(TAG, "onClick: Calculation Button.");
                if (TestEmpty())
                {
                    //Hide The Keyboard
                    View view = getView();
                    InputMethodManager Imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    try {
                        Imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }catch (NullPointerException e){
                        Log.d(TAG, e.getMessage());
                    }
                    CalculationMethod();
                    //Disable the validation button again.
                    btnValidation.setEnabled(true);
                    //moving to the end of the Scrollview.
                    View lastChild = mMainScrollView.getChildAt(mMainScrollView.getChildCount() - 1);
                    int bottom = lastChild.getBottom() + mMainScrollView.getPaddingBottom();
                    int sy = mMainScrollView.getScrollY();
                    int sh = mMainScrollView.getHeight();
                    int delta = bottom - (sy + sh);
                    mMainScrollView.smoothScrollBy(0, delta);
                }
                else
                    Toast.makeText(getActivity(),"Tu oublies d'entrer quelque chose",Toast.LENGTH_SHORT).show();
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mInitConst.setText("");
                mMinConst.setText("");
                mMaxConst.setText("");
                mPresntation.setText("");
                mBodySurface.setText("");
                mPosologie.setText("");
            }
        });

        btnValidation.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Double getNbrBottles = Double.parseDouble(mTvNumberOfBottlesNeeded.getText().toString());
                if (getNbrBottles > mMedicine.getQuantityM())
                {
                    Toast.makeText(getActivity(),"Vous n'avez pas ce nombre de bouteilles en stock",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    UpdateMedicine();
                    AddNewSell();
                    ClearAll();
                    Toast.makeText(getActivity(),"Terminé",Toast.LENGTH_SHORT).show();
                    mMainScrollView.smoothScrollTo(0,0);
                    btnValidation.setEnabled(false);
                }
            }
        });

        return view;
    }

    private void AddNewSell()
    {
        Log.d(TAG, "AddNewSell: Beginning.");
        mNameM = mMedicine.getNameM();
        mStability = mMedicine.getStabilityM();
        mPrice = mMedicine.getPriceM();
        mPresentBottle = mMedicine.getPresentBottle();
        //Get real time date.
        Date mNewDateS = Calendar.getInstance().getTime();

        //Execute the save method for the database.
        if (checkStringIfNull(mNameM))
        {
            Log.d(TAG, "onClick: saving sell" + mNameM);

            DbSalesHelpler dbSalesHelper = new DbSalesHelpler(getActivity());

            String name = mNameM;
            String Date = mNewDateS.toString();
            int stability = mStability;
            int quantity = Integer.parseInt(mTvNumberOfBottlesNeeded.getText().toString());
            double remainingPart = Double.parseDouble(mTvRemainderCalc.getText().toString());
            double amount = (remainingPart * mPrice)/mPresentBottle;

            Selled sell = new Selled(name, Date, stability, quantity, remainingPart, amount);

            if (dbSalesHelper.addsell(sell))
            {
                Log.d(TAG, "AddNewSell: reliquat added.");
                Toast.makeText(getActivity(), "Reliqua Ajouté", Toast.LENGTH_SHORT).show();
            } else
                {
                    Log.d(TAG, "AddNewSell: Adding Error.");
                    Toast.makeText(getActivity(), "Erreur De Sauvegarde", Toast.LENGTH_SHORT).show();
                }
        }
    }

    private void UpdateMedicine()
    {
        Double NewRemainingPart;
        int NewQuantity;
        mNameM = mMedicine.getNameM();
        mLabName = mMedicine.getLaboFabM();
        mPhoneNumberLab = mMedicine.getPhoneNumberLabo();
        mPresentBottle = mMedicine.getPresentBottle();
        InitConst = mMedicine.getInitConst();
        MinConst = mMedicine.getMinConst();
        MaxConst = mMedicine.getMaxConst();
        mPrice = mMedicine.getPriceM();
        mStability = mMedicine.getStabilityM();
        mRemainingPart = mMedicine.getRemainingPartM();
        mQuantity = mMedicine.getQuantityM();

        //Calculation the new Remaining part and the new quantity after we confirm the sell.
        NewRemainingPart = Double.parseDouble(mTvRemainderCalc.getText().toString())+ mRemainingPart;
        NewQuantity = mQuantity - Integer.parseInt(mTvNumberOfBottlesNeeded.getText().toString());

        if(checkStringIfNull(mNameM))
        {
            Log.d(TAG, "onClick: Update the medicine." + mNameM);
            //Get the databaseHelper and Update the Medicine.
            DbMedicinesHelper db = new DbMedicinesHelper(getActivity());
            Cursor cursor = db.getMedicineId(mMedicine);

            int medicineID = -1;
            while(cursor.moveToNext())
            {
                medicineID = cursor.getInt(0);
            }
            if (medicineID > -1)
            {
                mMedicine.setNameM(mNameM);
                mMedicine.setLaboFabM(mLabName);
                mMedicine.setPhoneNumberLabo(mPhoneNumberLab);
                mMedicine.setPresentBottle(mPresentBottle);
                mMedicine.setInitConst(InitConst);
                mMedicine.setMinConst(MinConst);
                mMedicine.setMaxConst(MaxConst);
                mMedicine.setPriceM(mPrice);
                mMedicine.setRemainingPartM(NewRemainingPart);
                mMedicine.setQuantityM(NewQuantity);
                mMedicine.setStabilityM(mStability);

                db.updateMedicine(mMedicine, medicineID);
                Log.d(TAG, "UpdateMedicine: Updated.");
                Toast.makeText(getActivity(),"Médicament Actualisé",Toast.LENGTH_SHORT).show();
            }
            else
                {
                    Log.d(TAG, "UpdateMedicine: DB problem.");
                    Toast.makeText(getActivity(),"Erreur De La Base De Données",Toast.LENGTH_SHORT).show();
                }
        }
    }


    private boolean TestEmpty()
    {
        String testBodySurface = mBodySurface.getText().toString();
        String testPosologie = mPosologie.getText().toString();
        String testInitConst = mInitConst.getText().toString();
        String testMinConst = mMinConst.getText().toString();
        String testMaxConst = mMaxConst.getText().toString();
        String testPresntation = mPresntation.getText().toString();

        if (TextUtils.isEmpty(testBodySurface))
        {
            return false;
        }
        else
            if (TextUtils.isEmpty(testPosologie))
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
                                return true;
    }

    private void CalculationMethod()
    {
        Double getBodySurface = Double.parseDouble(mBodySurface.getText().toString());
        Double getPosologie = Double.parseDouble(mPosologie.getText().toString());
        Double getInitConst = Double.parseDouble(mInitConst.getText().toString());
        Double getMinConst = Double.parseDouble(mMinConst.getText().toString());
        Double getMaxConst = Double.parseDouble(mMaxConst.getText().toString());
        Double getPresntation = Double.parseDouble(mPresntation.getText().toString());

        Double Dose,Reste,PresntationTest;
        Double Volume=0.0;
        int BottlesNumber=0;
        String PocheType;


        Dose = getBodySurface*getPosologie;
        Volume = Dose/getInitConst;

        PresntationTest = 0.0;


        while(PresntationTest <= Volume )
        {
            PresntationTest = getPresntation;
            BottlesNumber++;
            PresntationTest = PresntationTest*BottlesNumber;
        }

        Reste = (BottlesNumber*getPresntation)-Volume;

        if ((getMinConst*250<=Dose && getMaxConst*250>=Dose)&&(getMinConst*500<=Dose && getMaxConst*500>=Dose))
        {
            PocheType = "250 et 500";
        }else
            if (getMinConst*250<=Dose && getMaxConst*250>=Dose)
            {
                PocheType = "250";
            }else
                if (getMinConst*500<=Dose && getMaxConst*500>=Dose)
                {
                    PocheType = "500";
                }
                else
                {
                    PocheType = "500";
                }

                mTvDoseToBeAdministered.setText(new DecimalFormat("##.##").format(Dose));
                mTvFinalVolume.setText(new DecimalFormat("##.##").format(Volume));
                mTvNumberOfBottlesNeeded.setText(BottlesNumber+"");
                mTvPocketTypeCalc.setText(PocheType);
                mTvRemainderCalc.setText(new DecimalFormat("##.##").format(Reste));
    }

    private void goBackToMainActivity()
    {
        Intent myIntent = new Intent(getActivity(), MainActivity.class);
        myIntent.putExtra("key", "a");
        startActivity(myIntent);
    }

    private void ClearAll()
    {
        mInitConst.setText("");
        mMinConst.setText("");
        mMaxConst.setText("");
        mPresntation.setText("");
        mBodySurface.setText("");
        mPosologie.setText("");
        mTvDoseToBeAdministered.setText("");
        mTvFinalVolume.setText("");
        mTvNumberOfBottlesNeeded.setText("");
        mTvPocketTypeCalc.setText("");
        mTvRemainderCalc.setText("");
    }

    /*
     * Retrieves the selected patient from the bundle (coming from CalculActivity).
     * */
    /*private Patient getPatientFromBundle()
    {
        Log.d(TAG, "getPatientFromBundle: Arguments:"+ getArguments());

        Bundle bundle = this.getArguments();
        if(bundle != null)
        {
            return bundle.getParcelable(getString(R.string.patient));
        }else
            return null;
    }*/

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

    /*private void Init ()
    {
        mBodySurface.setText(mPatient.getBodySurfaceP()+"");
    }*/

    private void Init2()
    {
        mInitConst.setText(mMedicine.getInitConst()+"");
        mMinConst.setText(mMedicine.getMinConst()+"");
        mMaxConst.setText(mMedicine.getMaxConst()+"");
        mPresntation.setText(mMedicine.getPresentBottle()+"");

    }

    public void onAttach(@NonNull Context context)
    {
        super.onAttach(context);

        try {
            //mOnViewPatients = (OnViewPatientsListener) getActivity();
            mOnViewMedicines = (OnViewMedicinesListener) getActivity();
        } catch (ClassCastException e) {
            Log.e(TAG, "onAttach: ClassCastException:" + e.getMessage());
        }
    }

    private boolean checkStringIfNull(String string)
    {
        if (string.equals(""))
        {
            return false;
        }else
            return true;
    }

}
