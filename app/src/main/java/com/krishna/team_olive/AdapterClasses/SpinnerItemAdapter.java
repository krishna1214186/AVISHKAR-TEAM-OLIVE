package com.krishna.team_olive.AdapterClasses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

//import com.example.customspinners.inventory.Fruit;

import com.krishna.team_olive.AddingItem.Spinner.Spinner;
import com.krishna.team_olive.R;

import java.util.List;

/********************************************
 *     Created by DailyCoding on 15-May-21.  *
 ********************************************/

public class SpinnerItemAdapter extends BaseAdapter {
    private Context context;
    private List<Spinner> fruitList;

    public SpinnerItemAdapter(Context context, List<Spinner> fruitList) {
        this.context = context;
        this.fruitList = fruitList;
    }

    @Override
    public int getCount() {
        return fruitList != null ? fruitList.size() : 0;
    }

    @Override
    public Object getItem(int i) {
        return fruitList.get(i).getName();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.item_spinner, viewGroup, false);

        TextView txtName = rootView.findViewById(R.id.name);
        ImageView image = rootView.findViewById(R.id.image);

        txtName.setText(fruitList.get(i).getName());
        image.setImageResource(fruitList.get(i).getImage());

        return rootView;
    }
}