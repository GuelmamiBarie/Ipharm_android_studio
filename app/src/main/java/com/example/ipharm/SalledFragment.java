package com.example.ipharm;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.ipharm.Models.Medicine;
import com.example.ipharm.Models.Selled;
import com.example.ipharm.Utils.DbMedicinesHelper;
import com.example.ipharm.Utils.DbSalesHelpler;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class SalledFragment extends Fragment {
    private static final String TAG = "SalledFragment";

    //This will evade the NullPointer exception when adding to a new bundle from SalesActivity.
    public SalledFragment() {
        super();
        setArguments(new Bundle());
    }

    int mStability, mQuantity;
    String mNameM, mLabName, mPhoneNumberLab;
    Double mPresentBottle, InitConst, MinConst, MaxConst, mPrice, mRemainingPart;
    Selled mSelled;
    private TextView namedMSa, reliquat, stability, mdate, nbBottles, price;
    ImageView ivBackArrow, deleteimage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Toolbar toolbar;
        View view = inflater.inflate(R.layout.fragment_salled, container, false);
        toolbar = view.findViewById(R.id.calculToolbar);
        ivBackArrow = view.findViewById(R.id.ivBackArrow);
        deleteimage = view.findViewById(R.id.deleteImage);
        namedMSa = view.findViewById(R.id.medicineNameSa);
        reliquat = view.findViewById(R.id.tvRemainingPartSa2);
        stability = view.findViewById(R.id.tvStabilitySa2);
        mdate = view.findViewById(R.id.tvDateSa2);
        nbBottles = view.findViewById(R.id.tvNbBottlesSa2);
        price = view.findViewById(R.id.tvPriceSa2);

        Log.d(TAG, "onCreateView: ");

        //required for settings the toolbar.
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(false);

        mSelled = getSelledFromBundle();
        if (mSelled != null) {
            Init();
        } else {
            Toast.makeText(getActivity(), "Erreur d'obtention reliquat from the bundle", Toast.LENGTH_SHORT).show();
        }


        //Navigation for the backArrow.
        ivBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked back arrow from CalcluActivity to MainActivity.");
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        //Deleting the remaining part form the db.
        deleteimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowAlert();
            }
        });

        return view;
    }

    /*
     * Retrieves the selected medicine from the bundle (coming from MedicinesActivity).
     * */
    private Selled getSelledFromBundle() {
        Log.d(TAG, "getSelledFromBundle: " + getArguments());

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            return bundle.getParcelable(getString(R.string.selled));
        } else
            return null;
    }

    private void Init() {
        namedMSa.setText(mSelled.getNameMS());
        reliquat.setText(new DecimalFormat("##.##").format(mSelled.getRemainingPartMS()));
        stability.setText(mSelled.getStabilityMS() + "");
        mdate.setText(mSelled.getDateOfStoringS());
        nbBottles.setText(mSelled.getNbBottlesS() + "");
        price.setText(new DecimalFormat("##.##").format(mSelled.getTheAmount()));
    }

    // Show alert to confirm deleting.
    private void ShowAlert() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
        alertBuilder.setTitle("Confirmation")
                .setMessage("êtes-vous sûr ?")
                .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //delete selled.
                        DbSalesHelpler dbSalesHelpler = new DbSalesHelpler(getActivity());
                        Cursor cursor = dbSalesHelpler.getSellId(mSelled);
                        int selledID = -1;
                        while (cursor.moveToNext()) {
                            selledID = cursor.getInt(0);
                        }
                        if (selledID > -1) {
                            UpdateMedicine();
                            if (dbSalesHelpler.deleteSelled(selledID) > 0) {
                                Toast.makeText(getActivity(), "Reliquat Supprimé", Toast.LENGTH_SHORT).show();
                                //Clear the arguments on the current bundle since the Medicine is deleted.
                                getArguments().clear();
                                //Remove previous fragment from the backStack (therefore navigating back).
                                getActivity().getSupportFragmentManager().popBackStack();
                            } else {
                                Toast.makeText(getActivity(), "Erreur De La Base De Données", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }).setNegativeButton("Non", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = alertBuilder.create();
        dialog.show();
    }

    private void UpdateMedicine() {
        //I have to get all the medicines from the DB.
        final ArrayList<Medicine> medicines = new ArrayList<>();
        String Search;

        DbMedicinesHelper db = new DbMedicinesHelper(getActivity());
        Cursor cursor = db.getAllMedicines();

        //Iterate through all the rows in the database.
        if (!cursor.moveToNext()) {
            Toast.makeText(getActivity(), "il n'y a pas encore de médicament à montrer.", Toast.LENGTH_SHORT).show();
        }
        while (cursor.moveToNext()) {
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

            medicines.add(new Medicine(name, labFabName, phoneNumberLab, presentBottle, initConst, minConst, maxConst, priceM, remainingPart, stability, quantity));
        }

        //and Here i have to search for the same medicine that i want to delete his remaining part.
        Search = mSelled.getNameMS();
        for (Medicine mMedicine : medicines) {
            if (mMedicine.getNameM() != null && mMedicine.getNameM().contains(Search)) {
                Double NewRemainingPart;
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

                //Calculation the new Remaining part after we confirm the deleting the sell.
                NewRemainingPart = mRemainingPart - mSelled.getRemainingPartMS();

                if (checkStringIfNull(mNameM)) {
                    Log.d(TAG, "onClick: Update the medicine." + mNameM);
                    //Get the databaseHelper and Update the Medicine.
                    DbMedicinesHelper db1 = new DbMedicinesHelper(getActivity());
                    Cursor cursor1 = db1.getMedicineId(mMedicine);

                    int medicineID = -1;
                    while (cursor1.moveToNext()) {
                        medicineID = cursor1.getInt(0);
                    }
                    if (medicineID > -1) {
                        mMedicine.setNameM(mNameM);
                        mMedicine.setLaboFabM(mLabName);
                        mMedicine.setPhoneNumberLabo(mPhoneNumberLab);
                        mMedicine.setPresentBottle(mPresentBottle);
                        mMedicine.setInitConst(InitConst);
                        mMedicine.setMinConst(MinConst);
                        mMedicine.setMaxConst(MaxConst);
                        mMedicine.setPriceM(mPrice);
                        mMedicine.setRemainingPartM(NewRemainingPart);
                        mMedicine.setQuantityM(mQuantity);
                        mMedicine.setStabilityM(mStability);

                        db.updateMedicine(mMedicine, medicineID);
                        Toast.makeText(getActivity(), "Médicament Actualisé", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(getActivity(), "Erreur De La Base De Données", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "Médicament non trouvez", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean checkStringIfNull(String string) {
        if (string.equals("")) {
            return false;
        } else
            return true;
    }
}
