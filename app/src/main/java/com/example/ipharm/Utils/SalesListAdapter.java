package com.example.ipharm.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ipharm.Models.Selled;
import com.example.ipharm.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SalesListAdapter extends ArrayAdapter<Selled>
{
    private LayoutInflater mInflater;
    private List<Selled> mSell = null;
    private ArrayList<Selled> arrayList; // used for the search bar.
    private int layoutResource;
    private Context mContext;

    public SalesListAdapter(@NonNull Context context, int resource, @NonNull List<Selled> sales)
    {
        super(context, resource, sales);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutResource = resource;
        this.mContext = context;
        this.mSell = sales;
        arrayList = new ArrayList<>();
        this.arrayList.addAll(mSell);
    }

    public static class ViewHolder
    {
        TextView name,date,stability,remainingPart;
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
            holder.name = convertView.findViewById(R.id.tvItemMedicineNameS2);
            holder.date = convertView.findViewById(R.id.tvItemDateS2);
            holder.remainingPart = convertView.findViewById(R.id.tvItemRemainingPartS2);
            holder.stability = convertView.findViewById(R.id.tvItemStabilityS2);
            //***************************************************************************

            convertView.setTag(holder);
        }else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        //******* here where you can change.  ***************************************
        String name_ = getItem(position).getNameMS();
        String date_ = getItem(position).getDateOfStoringS();
        Double remainingPart_ = getItem(position).getRemainingPartMS();
        int stability_ = getItem(position).getStabilityMS();
        holder.name.setText(name_);
        holder.date.setText(date_);
        holder.remainingPart.setText(remainingPart_+"");
        holder.stability.setText(stability_+"");
        //***************************************************************************

        return convertView;
    }

    //Filter Class
    public void filter(String characterText)
    {
        characterText = characterText.toLowerCase(Locale.getDefault());
        mSell.clear();
        if (characterText.length() == 0)
        {
            mSell.addAll(arrayList);
        }else
        {
            mSell.clear();
            for (Selled sell: arrayList)
            {
                if (sell.getNameMS().toLowerCase(Locale.getDefault()).contains(characterText))
                {
                    mSell.add(sell);
                }
            }
        }
        notifyDataSetChanged();
    }
}
