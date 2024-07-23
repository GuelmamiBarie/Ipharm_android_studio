package com.example.ipharm.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ipharm.Models.Medicine;
import com.example.ipharm.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MedicineListAdapter extends ArrayAdapter<Medicine>
{
    private LayoutInflater mInflater;
    private List<Medicine> mMedicine = null;
    private ArrayList<Medicine> arrayList; // used for the search bar.
    private int layoutResource;
    private Context mContext;

    public MedicineListAdapter(@NonNull Context context, int resource, @NonNull List<Medicine> medicines)
    {
        super(context, resource, medicines);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutResource = resource;
        this.mContext = context;
        this.mMedicine = medicines;
        arrayList = new ArrayList<>();
        this.arrayList.addAll(mMedicine);
    }

    public static class ViewHolder
    {
        TextView name;
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
            holder.name = (TextView) convertView.findViewById(R.id.medicineName);
            //***************************************************************************

            convertView.setTag(holder);
        }else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        //******* here where you can change.  ***************************************
        String name_ = getItem(position).getNameM();
        holder.name.setText(name_);
        //***************************************************************************

        return convertView;
    }

    //Filter Class
    public void filter(String characterText)
    {
        characterText = characterText.toLowerCase(Locale.getDefault());
        mMedicine.clear();
        if (characterText.length() == 0)
        {
            mMedicine.addAll(arrayList);
        }else
        {
            mMedicine.clear();
            for (Medicine medicine: arrayList)
            {
                if (medicine.getNameM().toLowerCase(Locale.getDefault()).contains(characterText))
                {
                    mMedicine.add(medicine);
                }
            }
        }
        notifyDataSetChanged();
    }
}
