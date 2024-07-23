package com.example.ipharm;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.ipharm.Models.Selled;

public class SalesActivity extends AppCompatActivity
                            implements ViewSalesFragment.OnSellSelectedListener
{
    private static final String TAG = "SalesActivity";

    @Override
    public void OnSellSelected(Selled sell)
    {
        Log.d(TAG, "OnSellSelected: selled selected from:"
                                        +getString(R.string.view_sales_fragment)
                                        +""+sell.getNameMS());

        SalledFragment fragment = new SalledFragment();
        Bundle args = new Bundle();
        args.putParcelable(getString(R.string.selled),sell);
        fragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container3, fragment);
        transaction.addToBackStack(getString(R.string.sell_fragment));
        transaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);
        Log.d(TAG, "onCreate: ");
        Init();
    }

    /**
     * Initialize the first fragment (ViewSalesFragment).
     * **/
    private void Init()
    {
        ViewSalesFragment fragment = new ViewSalesFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container3,fragment);
        //We should not add the first fragment to the BackStack.
        //transaction.addToBackStack(null);
        transaction.commit();
    }
}
