package com.example.ipharm;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    ImageView bgApp, clover;
    LinearLayout textSplash, homeTitle,menu, layPatients,layMedicines,layCalculation,layClosing;
    Animation homeTitleanim;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        final Handler handler = new Handler();

        homeTitleanim = AnimationUtils.loadAnimation(this,R.anim.titleanim);
        /*
         * Getting the key from other activity so the animation not gonna work again.
         * */
        Intent intent = getIntent();
        String Ikey = intent.getStringExtra("key");

        //Initialize widgets.
        bgApp = findViewById(R.id.bgApp);
        clover =findViewById(R.id.clover);
        textSplash =findViewById(R.id.textSplash);
        homeTitle =findViewById(R.id.homeTitle);
        menu =findViewById(R.id.homemenu);
        layPatients = findViewById(R.id.layoutPatients);
        layMedicines = findViewById(R.id.layoutMedicines);
        layCalculation = findViewById(R.id.layoutCalculation);
        layClosing = findViewById(R.id.layoutClosing);
        /*
         * Home Activity Animation Part.
         * */
        if (Ikey == null)
        {
            bgApp.animate().translationY(-1900).setDuration(1100).setStartDelay(1500);
            clover.animate().alpha(0).setDuration(1150).setStartDelay(1500);
            textSplash.animate().translationY(140).alpha(0).setDuration(1100).setStartDelay(1500);
            homeTitle.startAnimation(homeTitleanim);
            menu.startAnimation(homeTitleanim);
        }
        else
        {
            bgApp.animate().translationY(-1900);
            clover.animate().alpha(0);
            textSplash.animate().translationY(140).alpha(0);
        }


        //Verification remaining part each day.
        Calendar calendar2 = Calendar.getInstance();
        int currentDay1 = calendar2.get(Calendar.DAY_OF_MONTH);
        SharedPreferences settings1 = getSharedPreferences("PREFS",0);
        int lastDay2 = settings1.getInt("day",0);
        if (lastDay2 != currentDay1)
        {
            SharedPreferences.Editor editor = settings1.edit();
            editor.putInt("day",currentDay1);
            editor.commit();

            handler.postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    //Do something after 3s
                    // Write your code to display AlertDialog here
                    ShowAlert();
                }
            }, 3000);
        }

        //Open Patients activity.
        layPatients.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                OpenPatientsActivity();
            }
        });

        //Open Medicines activity.
        layMedicines.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                OpenMedicinesActivity();
            }
        });

        //Open Calculation activity.
        layCalculation.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                OpenCalculationActivity();
            }
        });

        //Open Sales Activity.
        layClosing.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                OpenSalesActivity();
            }
        });

    }

    //Method for opening patient activity.
    public void OpenPatientsActivity()
    {
        Intent intent = new Intent(this, PatientsActivity.class);
        startActivity(intent);
    }

    //Method for opening medicines activity.
    public void OpenMedicinesActivity ()
    {
        Intent intent = new Intent(this, MedicinesActivity.class);
        startActivity(intent);
    }

    //Method for opening calculation activity.
    public void OpenCalculationActivity()
    {
        Intent intent = new Intent(this, CalculActivity.class);
        startActivity(intent);
    }

    //Method for opening sales activity.
    public void OpenSalesActivity()
    {
        Intent intent = new Intent(this, SalesActivity.class);
        startActivity(intent);
    }

    // Show alert to confirm the verification of the remaining part date.
    private void ShowAlert() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("Rappel !")
                .setMessage("Vérifiez la liste des médicaments dont les reliquats sont périmés et ceux qui ne le sont pas.")
                .setPositiveButton("Vérifié", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        OpenSalesActivity();
                    }
                }).setNegativeButton("Non,merci", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = alertBuilder.create();
        dialog.show();
    }
}