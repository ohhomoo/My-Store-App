package com.myezgenius.mystoreapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by moo on 5/29/2016 AD.
 */
public class MyAdapter extends BaseAdapter {

    // Explicit
    private Context context;
    private String[] nameStrings, descripString, iconStrings;

    public MyAdapter(Context context,
                     String[] nameStrings,
                     String[] descripString,
                     String[] iconStrings) {
        this.context = context;
        this.nameStrings = nameStrings;
        this.descripString = descripString;
        this.iconStrings = iconStrings;
    }   // Constructor

    @Override
    public int getCount() {
        return nameStrings.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view1 = layoutInflater.inflate(R.layout.my_listview, viewGroup, false);

        TextView nameTextView = (TextView) view1.findViewById(R.id.textView6);
        nameTextView.setText(nameStrings[i]);

        TextView descripTextView = (TextView) view1.findViewById(R.id.textView7);
        descripTextView.setText(descripString[i]);

        ImageView iconImageView = (ImageView) view1.findViewById(R.id.imageView2);
        Picasso.with(context).load(iconStrings[i]).resize(150, 150).into(iconImageView);

        return view1;
    }
}   // Main Class
