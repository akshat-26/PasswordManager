package monkey.d.luffy.passwordmanager;

import android.arch.persistence.room.Room;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;

import monkey.d.luffy.passwordmanager.db.helperdb.AppDatabase;
import monkey.d.luffy.passwordmanager.db.model.LoginDetails;

public class TableActivity extends AppCompatActivity {

    TableLayout tl;
    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.edit().putBoolean("locked", false).apply();
        tl = findViewById(R.id.tl);
        if (!prefs.getBoolean("locked", false)) {
            TableRow tr = new TableRow(TableActivity.this);
            tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

            TextView b = new TextView(TableActivity.this);
            b.setText("Username");
            b.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            b.setTextColor(Color.BLACK);
            tr.addView(b);

            TextView c = new TextView(TableActivity.this);
            c.setText("Password");
            c.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            c.setTextColor(Color.BLACK);
            tr.addView(c);

            tl.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
            prefs.edit().putBoolean("locked", true).apply();
        }

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "user-database").build();
        new GetAll().execute();
    }

    private class GetAll extends AsyncTask<Void, Void, Void> {
        List<LoginDetails> loginDetailsList;

        @Override
        protected Void doInBackground(Void... voids) {
            loginDetailsList = db.userDao().getAll();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            for (LoginDetails loginDetails : loginDetailsList) {
                TableRow tr = new TableRow(TableActivity.this);
                tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

                TextView b = new TextView(TableActivity.this);
                b.setText(loginDetails.getUsername());
                b.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                tr.addView(b);

                TextView c = new TextView(TableActivity.this);
                c.setText(loginDetails.getPassword());
                c.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

                tr.addView(c);
                tl.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
            }
        }
    }
}
