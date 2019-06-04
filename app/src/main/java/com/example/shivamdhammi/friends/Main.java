package com.example.shivamdhammi.friends;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Main extends AppCompatActivity {

    Button add,view;
    TextInputEditText name, number;
    public static final String DATABASE_NAME = "squad";

    SQLiteDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getSupportActionBar().hide();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabase = openOrCreateDatabase(DATABASE_NAME,MODE_PRIVATE,null);

        add= (Button)findViewById(R.id.button1);
        view= (Button)findViewById(R.id.button2);

        name = (TextInputEditText)findViewById(R.id.name);
        number = (TextInputEditText)findViewById(R.id.number);

        createTable();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!name.getText().toString().isEmpty()){
                    if(number.length()==10){

                            String sql = "INSERT INTO squad(Name,Contact_No) VALUES(?,?);";
                            mDatabase.execSQL(sql,new String[]{name.getText().toString().trim(),number.getText().toString().trim()});
                        Toast.makeText(getApplicationContext(),"Added Successfully",Toast.LENGTH_LONG).show();
                    }
                    else{
                        number.setError("Enter a valid number");
                        number.requestFocus();
                    }
                }
                else{
                    name.setError("Enter a name");
                    name.requestFocus();
                }
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), List.class);
                startActivity(intent);
            }
        });

    }


    private void createTable(){

        String sql = "CREATE TABLE IF NOT EXISTS squad (Id INTEGER PRIMARY KEY AUTOINCREMENT, Name varchar(30) NOT NULL,Contact_No int NOT NULL);";
        mDatabase.execSQL(sql);
    }
}
