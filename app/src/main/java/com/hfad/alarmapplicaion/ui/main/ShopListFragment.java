package com.hfad.alarmapplicaion.ui.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.hfad.alarmapplicaion.DatabaseSystem.FirebaseSystem;
import com.hfad.alarmapplicaion.R;
import com.hfad.alarmapplicaion.model.Shop;

import java.util.ArrayList;

public class ShopListFragment extends Fragment {

    ArrayList<Shop> shops;
    FirebaseSystem mFirebaseSystem;
    GridView gridview;

    String[] shopItemNameList = null;
    String[] shopItemPriceList = null;
    String[] shopItemImageUrlList = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_shop_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mFirebaseSystem = FirebaseSystem.getInstance(getContext());
        mFirebaseSystem.getShopListItem();
        gridview = view.findViewById(R.id.shop_gridview);

        IntentFilter filter = new IntentFilter();
        filter.addAction("shopList");
        getContext().registerReceiver(broadcastReceiver, filter);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public class ShopAdapter extends BaseAdapter {
        //private Context mContext;

        private LayoutInflater inflater = null;

        String[] shopItemImageUrls;
        String[] shopItemPrices;
        String[] shopItemNames;
        Context context;

        ArrayList<Shop> shops;

        public ShopAdapter(Context context, ArrayList<Shop> arrayList) {

            this.context = context;
            this.shops = arrayList;
            inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public int getCount() {
            return shops.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public class Holder{
            TextView shopItemName;
            TextView shopItemPrice;
            ImageView shopItemImage;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {

            Holder holder=new Holder();
            View rowView;

            rowView = inflater.inflate(R.layout.shopgridview, null);
            holder.shopItemName =(TextView) rowView.findViewById(R.id.shopItemName);
            holder.shopItemPrice = rowView.findViewById(R.id.shopItemPrice);
            holder.shopItemImage =(ImageView) rowView.findViewById(R.id.shopItemImage);

            holder.shopItemName.setText(shops.get(position).itemName);
            holder.shopItemPrice.setText(shops.get(position).price);


            Glide.with(context).load(shops.get(position).url).into(holder.shopItemImage);
            // holder.shopItemImage.setImageResource(shopItemImageUrls[position]);
            holder.shopItemImage.setLayoutParams(new LinearLayout.LayoutParams(300, 300));
            holder.shopItemImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
            holder.shopItemImage.setPadding(8, 8, 8, 8);

            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, " Clicked ", Toast.LENGTH_SHORT).show();
                }
            });
            return rowView;
        }
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action == "shopList") {
                shops = (ArrayList<Shop>)intent.getSerializableExtra("shopList");
                //Toast.makeText(getContext(), shops.get(0).itemName, Toast.LENGTH_SHORT).show();
                gridview.setAdapter(new ShopAdapter(getContext(), shops));
            }
        }
    };
}