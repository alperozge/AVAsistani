package com.example.alperozge.avasistani.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;

import android.os.AsyncTask;
import android.os.Bundle;


import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.alperozge.avasistani.R;
import com.example.alperozge.avasistani.dbmanager.ShoppingDB;
import com.example.alperozge.avasistani.dbmanager.AssistantDBHelper;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;


public class SplashActivity extends AppCompatActivity {

    TextView mTextView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this, R.layout.activity_splash);
//        setContentView(R.layout.activity_splash);


        Locale.setDefault(Locale.forLanguageTag("tr-TR"));
        Log.d("Splash", "onCreate: Locale:" + Locale.getDefault());
        if(needsDatabaseCreation()) {


            try {
                createDatabase();//
//                migrateToRoomAndComplete();
            } catch (Exception e) {
                showAlertAndClose(getString(R.string.db_not_created));
            }
        } else {
//            mTextView.setText("No database creation necessary");
            completeSplash();
        }

    }


    @Override
    protected void onStart() {
        super.onStart();
    }



    private void completeSplash(){
        SharedPreferences prf = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prf.edit();
        editor.putBoolean(getString(R.string.need_create_db),false);
        editor.apply();
        this.startApp();
        super.finish(); // Don't forget to finish this SplashActivity so the user can't return to it!
    }

    private void startApp() {
        Intent intent = new Intent(SplashActivity.this, AllListsActivity.class);
        startActivity(intent);
    }

    /*TODO: to account for possible database version updating, add logic to check the current version of database
       and update here when necessary*/
    private boolean needsDatabaseCreation() {
        SharedPreferences prf = getPreferences(Context.MODE_PRIVATE);
        return prf.getBoolean(getString(R.string.need_create_db),true);
    }

    private void createDatabase() {
        SplashActivity.DatabaseCreator creator = new SplashActivity.DatabaseCreator(this);
        try {
            creator.execute(getAssets().open("db/" + getString(R.string.db_name)));
        } catch (IOException e) {
            showAlertAndClose(getString(R.string.db_not_created));
        }
    }
    private void showAlertAndClose(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false)
                .setMessage(message)
                .setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                    }
                })
                .show();
    }

    public void setText(String text) {
        mTextView.setText(text);
    }

    private void migrateToRoomAndComplete() {

            RoomDatabase db = Room.databaseBuilder(getApplicationContext(), ShoppingDB.class,getString(R.string.db_name))
                                .createFromAsset( "db/" + getString(R.string.db_name))
//                    .addMigrations(ShoppingDB.MIGRATION1_2)
                    .build(); // no need for migration

//            db.close();
            completeSplash();

    }
    /*
    * AsynchTask subclass to copy pre-populated SQLite dbase file from project assests to application database path.
    * */
    private static class DatabaseCreator extends AsyncTask<InputStream,String,Boolean> {
        SplashActivity mContext;

        public DatabaseCreator(SplashActivity cont) {
            this.mContext = cont;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if(!aBoolean) {
                showAlertAndClose(mContext.getString(R.string.db_not_created));
            }
            else {
                mContext.completeSplash();
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            mContext.setText(values[0]);
        }

        @Override
        protected Boolean doInBackground(InputStream... inputStreams) {
            boolean returnValue = true;


//            try {
//                Thread.sleep(10000);
//            } catch (InterruptedException e) {
//                mContext.finishAffinity();
//            }


            AssistantDBHelper helper = new AssistantDBHelper(mContext);
            SQLiteDatabase dBase = helper.getWritableDatabase();
            dBase.close();
            BufferedOutputStream buffOut = null;
            BufferedInputStream buffIn = null;
            File outFile = null;
            try {
                buffIn = new BufferedInputStream(inputStreams[0]);
                outFile = mContext.getDatabasePath(mContext.getString(R.string.db_name));
                outFile.delete();
                FileOutputStream out = new FileOutputStream(outFile,false);
                buffOut = new BufferedOutputStream(out);
                int numBytesRead;
                byte bytes[] = new byte[2048];
                while((numBytesRead = buffIn.read(bytes)) >= 0) {
                    buffOut.write(bytes,0,numBytesRead);
                }
            } catch (IOException e) {
                if(outFile != null) {

                    outFile.delete();
                    returnValue = false;
                }
            } finally {
                try {
                    if (buffIn!=null) {
                        buffIn.close();
                    }
                    if (buffOut!=null) {

                        buffOut.flush();
                        buffOut.close();

                    }

                    if(dBase.isOpen()) {
                        dBase.close();
                    }
                    //Lastly, change the SharedPreference to check necessity of dbase creation to false

                    SharedPreferences pref = mContext.getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putBoolean(mContext.getString(R.string.need_create_db),false);
                    editor.apply();

                } catch (IOException e) {
                    throw new RuntimeException("Some error happened during closing DBase transfer stream closing");
                }
            }
            return returnValue;
        }

        private void showAlertAndClose(String message) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setCancelable(false)
                    .setMessage(message)
                    .setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mContext.finishAffinity();
                        }
                    })
                    .show();
        }


    }
}
