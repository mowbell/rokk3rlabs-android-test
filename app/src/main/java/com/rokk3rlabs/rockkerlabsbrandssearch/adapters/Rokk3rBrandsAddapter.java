package com.rokk3rlabs.rockkerlabsbrandssearch.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.rokk3rlabs.rockkerlabsbrandssearch.R;

/**
 * Created by Mauricio on 06/04/2016.
 */
public class Rokk3rBrandsAddapter extends CursorAdapter {
    public Rokk3rBrandsAddapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.brands_result_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView tvBrand = (TextView) view.findViewById(R.id.textViewBrand);
        TextView tvClothing = (TextView) view.findViewById(R.id.textViewClothing);
        TextView tvResult = (TextView) view.findViewById(R.id.textViewResultQuery);

        String brand = cursor.getString(cursor.getColumnIndexOrThrow("BRAND_FOUND"));
        String clothing = cursor.getString(cursor.getColumnIndexOrThrow("CLOTHING_FOUND"));
        String query = cursor.getString(cursor.getColumnIndexOrThrow("QUERY"));

        String queryResult=query;
        if(query!=null){
            if(brand!=null)
                queryResult=query.replace(brand, "");
            if(clothing!=null)
                queryResult=queryResult.replace(clothing,"");

            queryResult=queryResult.trim();
        }



        tvBrand.setText(brand);
        tvClothing.setText(clothing);
        tvResult.setText(queryResult);
    }
}
