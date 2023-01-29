package com.example.fotokopimenuapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;


import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class MenuList extends AppCompatActivity {
    GridView gridView;
    ArrayList<Menu> list;
    MenuListAdapter adapter = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_list);

        gridView = (GridView) findViewById(R.id.gridView);
        list = new ArrayList<>();
        adapter = new MenuListAdapter(this, R.layout.activity_cust_menu, list);
        gridView.setAdapter(adapter);    // get all data from sqlite

        Cursor cursor = adminPage.sqLiteHelper.getData("SELECT * FROM COFFEE");//mainActivity.java
        list.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String price = cursor.getString(2);
            byte[] image = cursor.getBlob(3);
            list.add(new Menu( id, name, price, image));
        }
        adapter.notifyDataSetChanged();


        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

            CharSequence[] items = {"Update", "Delete"};
            AlertDialog.Builder dialog = new AlertDialog.Builder(MenuList.this);

            dialog.setTitle("Choose Selection");
            dialog.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    if (item == 0) {
                        //update
                        Cursor c = adminPage.sqLiteHelper.getData("SELECT id FROM COFFEE");
                        ArrayList<Integer> arrID = new ArrayList<Integer>();
                        while (c.moveToNext()) {
                            arrID.add(c.getInt(0));
                        }
                        //show dialog update at here
                        showDialogUpdate(MenuList.this, arrID.get(position));
                    } else {
                        //delete
                        Cursor c = adminPage.sqLiteHelper.getData("SELECT id FROM COFFEE");
                        ArrayList<Integer> arrID = new ArrayList<Integer>();
                        while (c.moveToNext()) {
                            arrID.add(c.getInt(0));
                        }
                        showDialogDelete(arrID.get(position));
                    }
                }

            });
            dialog.show();
            return true;
            }
        });

}
    ImageView imageViewMenu;
    private void showDialogUpdate(Activity activity, int position){

        Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.update_menu_activity);
        dialog.setTitle("Update");

        imageViewMenu = (ImageView)  dialog.findViewById(R.id.imageViewMenu);
        final EditText edtName_update = (EditText)  dialog.findViewById(R.id.edtName_update);
        final EditText edtPrice_update = (EditText)  dialog.findViewById(R.id.edtPrice_update);
        Button buttonUpdate = (Button)  dialog.findViewById(R.id.buttonUpdate);

        //set width for dialog
        int width = (int) (activity.getResources().getDisplayMetrics().widthPixels*0.95);
        //set height for dialog
        int height = (int) (activity.getResources().getDisplayMetrics().heightPixels*0.7);
        dialog.getWindow().setLayout(width, height);
        dialog.show();

        imageViewMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // request photo library
                ActivityCompat.requestPermissions(
                        MenuList.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        888
                );
            }
        });

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    adminPage.sqLiteHelper.updateData(
                            edtName_update.getText().toString().trim(),
                            edtPrice_update.getText().toString().trim(),
                            adminPage.imageViewToByte(imageViewMenu),
                            position
                    );
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Update successfully!!!", Toast.LENGTH_SHORT).show();
                }
                catch (Exception error){
                    Log.e("Update error", error.getMessage());
                }
                updateMenuList();
            }
        });
    }
    private void updateMenuList(){
        Cursor cursor = adminPage.sqLiteHelper.getData("SELECT * FROM COFFEE");
        list.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String price = cursor.getString(2);
            byte[] image = cursor.getBlob(3);
            list.add(new Menu(id, name, price, image));
        }
        adapter.notifyDataSetChanged();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 888) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 888);
            } else {
                Toast.makeText(getApplicationContext(), "You don't have permission to access file location!", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 888 && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imageViewMenu.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showDialogDelete(int idMenu){
        AlertDialog.Builder dialogDelete = new AlertDialog.Builder(MenuList.this);

        dialogDelete.setTitle("Warning!");
        dialogDelete.setMessage("Are you sure you want to delete this?");
        dialogDelete.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try{
                    adminPage.sqLiteHelper.deleteData(idMenu);
                    Toast.makeText(getApplicationContext(), "Deleted successfully!", Toast.LENGTH_LONG).show();
                } catch (Exception e){
                    Log.e("error", e.getMessage());
                }
                updateMenuList();
            }
        });
        dialogDelete.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialogDelete.show();
    }
}