package com.example.shivamdhammi.friends;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder>{

    Context context;
    SQLiteDatabase mDatabase;

    private ArrayList<String> IDList;
    private ArrayList<String> NameList;
    private ArrayList<String> ContactList;

    class SearchViewHolder extends RecyclerView.ViewHolder{

        TextView name,contact,id;
        RelativeLayout parentLayout;
        Button edit,delete;

        public SearchViewHolder(@NonNull View itemView){

            super(itemView);

            name = (TextView)itemView.findViewById(R.id.listName);
            contact = (TextView)itemView.findViewById(R.id.ListContact);
            id = (TextView)itemView.findViewById(R.id.ID);
            parentLayout = (RelativeLayout)itemView.findViewById(R.id.relative);
            edit = (Button)itemView.findViewById(R.id.Edit_Button);
            delete = (Button)itemView.findViewById(R.id.Delete_Button);

        }

    }


public SearchAdapter(Context context, ArrayList<String> IDList, ArrayList<String> nameList, ArrayList<String>contactList, SQLiteDatabase mDatabase){

    this.context= context;
    this.IDList=IDList;
    this.NameList = nameList;
    this.ContactList = contactList;
    this.mDatabase = mDatabase;
}


    @NonNull
    @Override
    public SearchAdapter.SearchViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.list_layout,viewGroup,false);

        return new SearchAdapter.SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SearchViewHolder searchViewHolder,final int i) {

        searchViewHolder.id.setText(IDList.get(i));
        searchViewHolder.name.setText(NameList.get(i));
        searchViewHolder.contact.setText(ContactList.get(i));

      /*  searchViewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,SProfile.class);

                intent.putExtra("Uid",profilepicList.get(i));
                context.startActivity(intent);
            }
        });*/

        searchViewHolder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(context);

                dialog.setContentView(R.layout.edit);

                dialog.show();

                final EditText name = (EditText)dialog.findViewById(R.id.update_name);
                final EditText contact = (EditText)dialog.findViewById(R.id.update_contact);
                Button update = (Button)dialog.findViewById(R.id.update_button);
                ImageView cancel = (ImageView) dialog.findViewById(R.id.cancel);

                name.setText(NameList.get(i));
                contact.setText(ContactList.get(i));


                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        if(!name.getText().toString().isEmpty()){
                            if(contact.length()==10){

                                String sql = "UPDATE squad SET Name = ?, Contact_No = ? WHERE Id =?;";

                                mDatabase.execSQL(sql,new String[]{name.getText().toString().trim(),contact.getText().toString().trim(),IDList.get(i)});

                                Toast.makeText(context,"Updated Successfully",Toast.LENGTH_LONG).show();

                                LoadFriendsAgain();

                                dialog.dismiss();
                            }
                            else{
                                contact.setError("Enter a valid number");
                                contact.requestFocus();
                            }
                        }
                        else{
                            name.setError("Enter a name");
                            name.requestFocus();
                        }


                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

            }
        });


        searchViewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(context);

                dialog.setContentView(R.layout.delete);

                dialog.show();

                Button yes  = dialog.findViewById(R.id.yes);
                Button no  = dialog.findViewById(R.id.no);

                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String sql = "DELETE FROM squad WHERE Id = ?;";
                        mDatabase.execSQL(sql,new String[]{IDList.get(i)});
                        LoadFriendsAgain();
                        dialog.dismiss();

                    }
                });

                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });

    }

    private void LoadFriendsAgain(){
        String sql = "SELECT * FROM squad";

        Cursor cursor = mDatabase.rawQuery(sql,null);

        IDList.clear();
        NameList.clear();
        ContactList.clear();

        if(cursor.moveToFirst()){
            do{
                IDList.add(cursor.getString(0));
                NameList.add(cursor.getString(1));
                ContactList.add(cursor.getString(2));

            }while(cursor.moveToNext());

            notifyDataSetChanged();
        }
    }


    @Override
    public int getItemCount() {

        return NameList.size();
    }
}
