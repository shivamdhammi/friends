package com.example.shivamdhammi.friends;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import static com.example.shivamdhammi.friends.Main.DATABASE_NAME;

public class List extends AppCompatActivity {

    SQLiteDatabase mDatabse;
    private ArrayList<String> IDList;
    private ArrayList<String> NameList;
    private ArrayList<String> ContactList;

    private RecyclerView recyclerView;
    private SearchAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getSupportActionBar().hide();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        mDatabse = openOrCreateDatabase(DATABASE_NAME,MODE_PRIVATE,null);
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        IDList = new ArrayList<>();
        NameList = new ArrayList<>();
        ContactList = new ArrayList<>();

        LoadFriends();
    }

    private void LoadFriends(){

        String sql = "SELECT * FROM squad";

        Cursor cursor = mDatabse.rawQuery(sql,null);

        if(cursor.moveToFirst()){
            do{
                IDList.add(cursor.getString(0));
                NameList.add(cursor.getString(1));
                ContactList.add(cursor.getString(2));

            }while(cursor.moveToNext());

            adapter = new SearchAdapter(List.this,IDList,NameList,ContactList,mDatabse);
            recyclerView.setAdapter(adapter);
        }


    }
}
