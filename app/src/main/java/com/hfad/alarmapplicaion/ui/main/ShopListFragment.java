package com.hfad.alarmapplicaion.ui.main;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.hfad.alarmapplicaion.DatabaseSystem.FirebaseSystem;
import com.hfad.alarmapplicaion.MainActivity;
import com.hfad.alarmapplicaion.R;
import com.hfad.alarmapplicaion.model.Shop;
import com.hfad.alarmapplicaion.model.User;

import java.util.ArrayList;

public class ShopListFragment extends Fragment implements UpdateListView {

    ArrayList<Shop> shops;
    FirebaseSystem mFirebaseSystem;
    private User myUserInfo;

    GridView gridview;

    String[] shopItemNameList = null;
    String[] shopItemPriceList = null;
    String[] shopItemImageUrlList = null;

    String ItemPrice;
    String ItemName;
    int userPoint;

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
        mFirebaseSystem.getMyPoint(myUserInfo);

        myUserInfo = ((MainActivity)getActivity()).myUserInfo;



        gridview = view.findViewById(R.id.shop_gridview);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                ItemName = shops.get(position).itemName;
                ItemPrice = shops.get(position).price;
                userPoint = myUserInfo.point;
                buyDialogShow();
            }
        });

        IntentFilter filter = new IntentFilter();
        filter.addAction("shopList");
        getContext().registerReceiver(broadcastReceiver, filter);

        IntentFilter pointfilter = new IntentFilter();
        filter.addAction("myPoint");
        getContext().registerReceiver(broadcastReceiver, pointfilter);
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

            /*rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // -------------- 상점 아이템 클릭 ----------------
                    //buyDialogShow();
                    Toast.makeText(getContext(), ItemPrice + ItemName, Toast.LENGTH_SHORT).show();
                }
            });*/
            return rowView;
        }
    }

    public void buyItem(){
        String tmpPrice = ItemPrice.replace(",", "");
        int itemPrice = Integer.parseInt(tmpPrice);
        if(userPoint >= itemPrice){
            Toast.makeText(getContext(), "구매 성공!", Toast.LENGTH_SHORT).show();
            /*---디비로 포인트 변경 값 전송? 설정?---*/
        } else{
            Toast.makeText(getContext(), "포인트가 부족합니다. 나의 포인트 잔액 : " + userPoint, Toast.LENGTH_SHORT).show();
        }
        ItemPrice = null;
    }

    public void buyDialogShow() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(ItemName + " 구매!");
        builder.setMessage(ItemName + " 을(를) 정말 구매하시겠습니까? \n" + "구매시 " + ItemPrice + "p가 차감됩니다.");
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        buyItem();
                    }
                });
        builder.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(), "다음에 다시 이용해주세요!", Toast.LENGTH_SHORT).show();
                    }
                });
        builder.show();
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals("shopList")) {
                shops = (ArrayList<Shop>)intent.getSerializableExtra("shopList");
                //Toast.makeText(getContext(), shops.get(0).itemName, Toast.LENGTH_SHORT).show();
                gridview.setAdapter(new ShopAdapter(getContext(), shops));
            }
            else if(action.equals("getMyPoint")) {
                Bundle bundle = intent.getExtras();
                userPoint = bundle.getInt("getMyPoint");
            }
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        getContext().unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void updateListView() {

    }
}