package com.example.fotokopimenuapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class MenuListAdapter extends BaseAdapter{


    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View row = view;
        ViewHolder holder = new ViewHolder();
        if(row == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);
            holder.txtName = (TextView) row.findViewById(R.id.txtName);
            holder.txtPrice = (TextView) row.findViewById(R.id.txtPrice);
            holder.imageView = (ImageView) row.findViewById(R.id.imgMenu);
            row.setTag(holder);    }
        else {
            holder = (ViewHolder) row.getTag();
        }

       Menu menu = MenuList.get(position);
        holder.txtName.setText(menu.getMenuName());
        holder.txtPrice.setText(menu.getMenuPrice());
        byte[] foodImage = menu.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(foodImage, 0, foodImage.length);
        holder.imageView.setImageBitmap(bitmap);
        return row;
    }

    private Context context;
    private  int layout;
    private ArrayList<Menu> MenuList;

    public MenuListAdapter(Context context, int layout, ArrayList<Menu> MenuList) {
        this.context = context;
        this.layout = layout;
        this.MenuList = MenuList;}

    @Override
    public int getCount() {
        return MenuList.size();}
    @Override
    public Object getItem(int position) {
        return MenuList.get(position);}

    @Override
    public long getItemId(int position) {
        return position;}

    private class ViewHolder{

        ImageView imageView;
        TextView txtName, txtPrice,txtStatus;;
    }


}
