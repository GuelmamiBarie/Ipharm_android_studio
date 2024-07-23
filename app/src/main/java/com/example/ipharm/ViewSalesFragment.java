package com.example.ipharm;

import android.content.Context;
import android.content.Intent;
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

import com.example.ipharm.Models.Selled;
import com.example.ipharm.Utils.DbSalesHelpler;
import com.example.ipharm.Utils.SalesListAdapter;
import com.google.android.material.appbar.AppBarLayout;

import java.util.ArrayList;
import java.util.Locale;

public class ViewSalesFragment extends Fragment
{
    private static final String TAG = "ViewSalesFragment";

    public interface OnSellSelectedListener
    {
        public void OnSellSelected(Selled sell);
    }
    OnSellSelectedListener mSellListener;

    private static final int STANDARD_APPBAR = 0;
    private static final int SEARCH_APPBAR = 1;
    private int mAppBarState;

    private AppBarLayout viewSalesBar ,searchBar;
    private SalesListAdapter adapter;
    private ListView salesList;
    private EditText mSearchsell;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_viewsales,container,false);
        viewSalesBar = view.findViewById(R.id.viewSalesToolBar);
        searchBar =  view.findViewById(R.id.searchToolBar);
        salesList =  view.findViewById(R.id.salesList);
        mSearchsell = view.findViewById(R.id.etSearch);
        Log.d(TAG, "onCreateView: Started.");

        setAppBarState(STANDARD_APPBAR);

        setupSalesList();

        ImageView ivSearchSelled =  view.findViewById(R.id.ivSearchIcon);
        ivSearchSelled.setOnClickListener(new View.OnClickListener()
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
                setupSalesList();
            }
        });

        ImageView ivBackArrow2 = view.findViewById(R.id.ivBackArrow);
        ivBackArrow2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Log.d(TAG, "onClick: clicked back arrow of the fragment to go back to the main activity.");
                //Create a new Intent with a key to back to the mainActivity, and deactivate the splash using the key.
                goBackToMainActivity();
            }
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context)
    {
        super.onAttach(context);

        try {
            mSellListener = (OnSellSelectedListener) getActivity();
        }catch(ClassCastException e)
        {
            Log.e(TAG, "onAttach: ClassCastException:"+e.getMessage());
        }
    }

    public void setupSalesList()
    {
        final ArrayList<Selled> sell = new ArrayList<>();

        DbSalesHelpler dbSalesHelpler = new DbSalesHelpler(getActivity());
        Cursor cursor = dbSalesHelpler.getAllSales();

        //Iterate through all the rows in the database.
        if (!cursor.moveToNext())
        {
            Toast.makeText(getActivity(),"il n'y a pas encore de reliquat à montrer.",Toast.LENGTH_SHORT).show();
        }
        while (cursor.moveToNext())
        {
            String name = cursor.getString(1);
            String date = cursor.getString(2);
            int stability = cursor.getInt(3);
            int nbbottles = cursor.getInt(4);
            Double remainingPart = cursor.getDouble(5);
            Double amount = cursor.getDouble(6);

            sell.add(new Selled(name,date,stability,nbbottles,remainingPart,amount));
        }

        adapter = new SalesListAdapter(getActivity(),R.layout.layout_saleslistitems, sell);

        mSearchsell.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
                String text = mSearchsell.getText().toString().toLowerCase(Locale.getDefault());
                adapter.filter(text);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {}
        });

        salesList.setAdapter(adapter);

        salesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Log.d(TAG, "onClick: navigation to:" + getString(R.string.sell_fragment));
                //Pass the sell to the interface and send it to SalesActivity.
                mSellListener.OnSellSelected(sell.get(position));
            }
        });
        dbSalesHelpler.close();
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
            viewSalesBar.setVisibility(View.VISIBLE);

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
            viewSalesBar.setVisibility(View.GONE);
            searchBar.setVisibility(View.VISIBLE);

            //Open The Keyboard
            InputMethodManager Imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            Imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0 );
        }
    }

    private void goBackToMainActivity()
    {
        Intent myIntent = new Intent(getActivity(), MainActivity.class);
        myIntent.putExtra("key", "a");
        startActivity(myIntent);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        setAppBarState(STANDARD_APPBAR);
    }
}
