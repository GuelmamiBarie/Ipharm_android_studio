package com.example.ipharm.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ipharm.Models.Patient;
import com.example.ipharm.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class PatientListAdapter extends ArrayAdapter<Patient>
{
    private LayoutInflater mInflater;
    private List<Patient> mPatients = null;
    private ArrayList<Patient> arrayList; // used for the search bar.
    private int layoutResource;
    private Context mContext;
    private String mAppend;

    public PatientListAdapter(@NonNull Context context, int resource, @NonNull List<Patient> patients, String append)
    {
        super(context, resource, patients);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutResource = resource;
        this.mContext = context;
        mAppend = append;
        this.mPatients = patients;
        arrayList = new ArrayList<>();
        this.arrayList.addAll(mPatients);
    }

    public static class ViewHolder
    {
        TextView name;
        CircleImageView patientImage;
        ProgressBar mProgressBar;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        /*
        ******* ViewHolder Build Pattern Start *****
        * */
        final ViewHolder holder;

        if (convertView == null)
        {
            convertView = mInflater.inflate(layoutResource,parent,false);
            holder = new ViewHolder();

            //******* here where you can change.  ***************************************
            holder.name =  convertView.findViewById(R.id.patientName);
            holder.patientImage =  convertView.findViewById(R.id.patientImage);
            //***************************************************************************

            holder.mProgressBar = (ProgressBar) convertView.findViewById(R.id.patientProgressBar);

            convertView.setTag(holder);
        }else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        //******* here where you can change.  ***************************************
        String name_ = getItem(position).getNameP();
        String imagePath = getItem(position).getProfileImageP();
        holder.name.setText(name_);

        ImageLoader imageLoader = ImageLoader.getInstance();

        imageLoader.displayImage(mAppend + imagePath, holder.patientImage, new ImageLoadingListener()
        {
            @Override
            public void onLoadingStarted(String imageUri, View view)
            {
                holder.mProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason)
            {
                holder.mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage)
            {
                holder.mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view)
            {
                holder.mProgressBar.setVisibility(View.GONE);
            }
        });
        //***************************************************************************
        return convertView;
    }

    //Filter Class
    public void filter(String characterText)
    {
        characterText = characterText.toLowerCase(Locale.getDefault());
        mPatients.clear();
        if (characterText.length() == 0)
        {
            mPatients.addAll(arrayList);
        }else
        {
            mPatients.clear();
            for (Patient patient: arrayList)
            {
                if (patient.getNameP().toLowerCase(Locale.getDefault()).contains(characterText))
                {
                    mPatients.add(patient);
                }
            }
        }

        notifyDataSetChanged();
    }
}
