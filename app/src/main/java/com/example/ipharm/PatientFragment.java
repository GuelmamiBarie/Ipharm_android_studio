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

import com.example.ipharm.Models.Patient;
import com.example.ipharm.Utils.DbPatientsHelper;
import com.example.ipharm.Utils.PatientPropertyListAdapter;
import com.example.ipharm.Utils.UniversalImageLoader;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PatientFragment extends Fragment
{
    private static final String TAG = "PatientFragment";

    public interface OnEditPatientListener
    {
        public void onEditPatientSelected(Patient patient);
    }
    OnEditPatientListener mOnEditPatientListener;

    //This will evade the NullPointer exception when adding to a new bundle from PatientActivity.
    public PatientFragment ()
    {
        super();
        setArguments(new Bundle());
    }

    private Patient mPatient;
    private TextView mPatientName,mSize,mWeight,mSurface;
    private CircleImageView mPatientImage;
    private ListView mListView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        Toolbar toolbar;
        View view = inflater.inflate(R.layout.fragment_patient, container, false);
        toolbar = view.findViewById(R.id.patienttoolbar);
        mPatientName = view.findViewById(R.id.patientName);
        mPatientImage = view.findViewById(R.id.patientImage);
        mListView = view.findViewById(R.id.lvPatientProperties);
        mSize = view.findViewById(R.id.tvSize2);
        mWeight = view.findViewById(R.id.tvWeight2);
        mSurface = view.findViewById(R.id.tvBodySurfaceP2);
        Log.d(TAG, "onCreateView: Started.");
        mPatient = getPatientFromBundle();

        //required for settings the toolbar.
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

        Init();

        //Navigation for the backArrow.
        ImageView ivBackArrow = (ImageView) view.findViewById(R.id.ivBackArrow);
        ivBackArrow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.d(TAG, "onClick: clicked back arrow.");
                //remove previous fragment from the backstack (therefor navigating back).
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        //Navigate to the edit patient fragment to edit the patient selected.
        ImageView ivEdit = (ImageView) view.findViewById(R.id.ivEdit);
        ivEdit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.d(TAG, "onClick: clicked the Edit Icon.");
                mOnEditPatientListener.onEditPatientSelected(mPatient);
            }
        });
        return view;
    }

    private void Init()
    {
        mPatientName.setText(mPatient.getNameP());
        mSurface.setText(mPatient.getBodySurfaceP()+"");
        mSize.setText(mPatient.getSizeP()+"");
        mWeight.setText(mPatient.getWeightP()+"");
        UniversalImageLoader.setImage(mPatient.getProfileImageP(),mPatientImage,null,"");

        /*
        *
        *
        * Here where i can add the other properties to the CardView.
        *
        * */
        ArrayList<String> properties = new ArrayList<>();
        properties.add(mPatient.getPhonenumber());
        PatientPropertyListAdapter adapter = new PatientPropertyListAdapter(getActivity(),R.layout.layout_cardview,properties);
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
                Log.d(TAG, "onOptionsItemSelected: deleting patient.");
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
                alertBuilder.setTitle("Confirmation")
                        .setMessage("êtes-vous sûr ?")
                        .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //delete selled.
                                DbPatientsHelper dbPatientsHelper = new DbPatientsHelper(getActivity());
                                Cursor cursor = dbPatientsHelper.getPatientId(mPatient);
                                int patientID = -1;
                                while (cursor.moveToNext()) {
                                    patientID = cursor.getInt(0);
                                }
                                if (patientID > -1) {
                                    if (dbPatientsHelper.deletePatient(patientID) > 0) {
                                        Toast.makeText(getActivity(), "Patient Supprimé", Toast.LENGTH_SHORT).show();
                                        //Clear the arguments on the current bundle since the patient is deleted.
                                        getArguments().clear();
                                        //Remove previous fragment from the backStack (therefore navigating back).
                                        getActivity().getSupportFragmentManager().popBackStack();
                                    } else {
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
    * Retrieves the selected patient from the bundle (coming from PatientActivity).
    * */
    private Patient getPatientFromBundle()
    {
        Log.d(TAG, "getPatientFromBundle: Arguments:"+ getArguments());

        Bundle bundle = this.getArguments();
        if(bundle != null)
        {
            return bundle.getParcelable(getString(R.string.patient));
        }else
            return null;
    }

    @Override
    public void onAttach(@NonNull Context context)
    {
        super.onAttach(context);
        try {
            mOnEditPatientListener = (OnEditPatientListener) getActivity();
        }catch(ClassCastException e)
        {
            Log.d(TAG, "onAttach: ClassCastException:"+ e.getMessage());
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        DbPatientsHelper dbPatientsHelper = new DbPatientsHelper(getActivity());
        Cursor cursor = dbPatientsHelper.getPatientId(mPatient);

        int patientID = -1;
        while(cursor.moveToNext())
        {
            patientID = cursor.getInt(0);
        }
        if (patientID > -1)
        {
            //Testing if the patient doesn't still exists and navigating back by popping the stack.
            Init();// it's not really necessary to do that.
        }else
        {
            getArguments().clear();
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }
}
