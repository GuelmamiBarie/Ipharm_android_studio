package com.example.ipharm.Utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ipharm.PatientsActivity;
import com.example.ipharm.R;

import java.util.List;

public class PatientPropertyListAdapter extends ArrayAdapter<String>
{
    private static final String TAG = "PatientPropertyListAdap";
    private LayoutInflater mInflater;
    private List<String> mProperties = null;
    private int layoutResource;
    private Context mContext;

    public PatientPropertyListAdapter(@NonNull Context context, int resource, @NonNull List<String> properties)
    {
        super(context, resource, properties);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutResource = resource;
        this.mContext = context;
        this.mProperties = properties;
    }
    //************** here where you can change.  ***************************************
    public static class ViewHolder
    {
        TextView property;
        ImageView rightIcon;
        ImageView leftIcon;
    }
    //***************************************************************************

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

            //************** here where you can change.  ***************************************
            holder.property = (TextView) convertView.findViewById(R.id.tvMiddleCardView);
            holder.rightIcon = (ImageView) convertView.findViewById(R.id.iconRightCardView);
            holder.leftIcon = (ImageView) convertView.findViewById(R.id.iconLeftCardView);
            //***************************************************************************

            convertView.setTag(holder);
        }else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        //******* here where you can change.  ***************************************
        final String property = getItem(position);
        holder.property.setText(property);

        //We should to have a test here but am gonna to do it without a test.
        if (property.contains("@"))
        {

        }else
        if ((property.length())!=0)
        {
            //Phone Call
            holder.leftIcon.setImageResource(mContext.getResources().getIdentifier("@drawable/ic_phone", null, mContext.getPackageName()));
            holder.leftIcon.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (((PatientsActivity)mContext).checkPermission(Init.PHONE_PERMISSIONS))
                    {
                        Log.d(TAG, "onClick: Initiating phone call...");
                        Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.fromParts("tel",property,null));
                        mContext.startActivity(callIntent);
                    }else
                    {
                        ((PatientsActivity)mContext).verifyPermissions(Init.PHONE_PERMISSIONS);
                    }
                }
            });

            //Setting the icon for sending text messages.
            holder.rightIcon.setImageResource(mContext.getResources().getIdentifier("@drawable/ic_message", null, mContext.getPackageName()));
            holder.rightIcon.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Log.d(TAG, "onClick: Initiating Text Message...");

                    //The number that we want to send SMS.
                    Intent smsIntent = new Intent(Intent.ACTION_VIEW,Uri.fromParts("sms",property,null));
                    mContext.startActivity(smsIntent);
                }
            });


        }

        //***************************************************************************

        return convertView;
    }
}
