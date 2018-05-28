package monkey.d.luffy.passwordmanager;

import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.List;
import java.util.concurrent.ExecutionException;

import monkey.d.luffy.passwordmanager.adapter.PasswordAdapter;
import monkey.d.luffy.passwordmanager.db.helperdb.AppDatabase;
import monkey.d.luffy.passwordmanager.db.model.LoginDetails;
import monkey.d.luffy.passwordmanager.util.EncryDercy;
import monkey.d.luffy.passwordmanager.util.RecyclerItemClickListener;

public class PasswordActivity extends AppCompatActivity {

    static AppDatabase db;
    static PasswordAdapter mPasswordAdapter;
    FloatingActionButton addButton;
    RelativeLayout parentLayout;
    static List<LoginDetails> loginDetailsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        parentLayout = findViewById(R.id.parent_layout);
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "user-database").build();
        addButton = findViewById(R.id.add);
        addListener();

        final RecyclerView recyclerView = findViewById(R.id.recv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        try {
            loginDetailsList = new GetDetails().execute().get();
            mPasswordAdapter = new PasswordAdapter(loginDetailsList);
            recyclerView.setAdapter(mPasswordAdapter);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(PasswordActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PasswordActivity.this);
                builder.setTitle(EncryDercy.decrypt(loginDetailsList.get(position).getUsername()));
                builder.setMessage(EncryDercy.decrypt(loginDetailsList.get(position).getPassword()));
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        callAlert(loginDetailsList.get(position));
                    }
                });
                builder.show();
            }
        }));
    }

    private void addListener() {
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonLoginClick();
            }
        });
    }

    private void buttonLoginClick() {
        LayoutInflater li = LayoutInflater.from(PasswordActivity.this);
        View prompt = li.inflate(R.layout.login_layout, null, false);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PasswordActivity.this);
        alertDialogBuilder.setView(prompt);
        final TextInputEditText usernameEt = prompt.findViewById(R.id.et_username);
        final TextInputEditText passwordEt = prompt.findViewById(R.id.et_password);
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String username = usernameEt.getText().toString();
                String password = passwordEt.getText().toString();
                if (username.trim().length() == 0 || password.trim().length() == 0) {
                    dialog.dismiss();
                    Snackbar.make(parentLayout, "None of the two fields can be left empty.", Snackbar.LENGTH_SHORT).show();
                } else {
                    LoginDetails loginDetails = new LoginDetails();
                    loginDetails.setUsername(EncryDercy.encrypt(username));
                    loginDetails.setPassword(EncryDercy.encrypt(password));
                    new AddDetails().execute(loginDetails);
                }
                dialog.dismiss();
            }
        });
        alertDialogBuilder.show();
    }

    private void callAlert(final LoginDetails loginDetails) {
        AlertDialog.Builder builder = new AlertDialog.Builder(PasswordActivity.this);
        builder.setMessage("Delete "+EncryDercy.decrypt(loginDetails.getUsername()));
        builder.setTitle("Really??");
        builder.setPositiveButton("Yes, I am sure", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new DeleteUser().execute(loginDetails);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("NO. Absolutely not.", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private static class AddDetails extends AsyncTask<LoginDetails, Void, Void> {
        LoginDetails loginDetails;
        @Override
        protected Void doInBackground(LoginDetails... loginDetails) {
            this.loginDetails = loginDetails[0];
            db.userDao().Insert(loginDetails[0]);
            // Log.d("SHASHWATSHASHWATSHASHWA", db.userDao().getAll().get(0).getUsername());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            loginDetailsList.add(loginDetails);
            mPasswordAdapter.notifyDataSetChanged();
        }
    }

    private static class GetDetails extends AsyncTask<Void, Void, List<LoginDetails>> {
        @Override
        protected List<LoginDetails> doInBackground(Void... voids) {
            return db.userDao().getAll();
        }
    }

    private static class DeleteUser extends AsyncTask<LoginDetails, Void, Void> {
        LoginDetails loginDetails;
        @Override
        protected Void doInBackground(LoginDetails... loginDetails) {
            this.loginDetails = loginDetails[0];
            db.userDao().delete(loginDetails[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            loginDetailsList.remove(loginDetails);
            mPasswordAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_item_show_table){
            startActivity(new Intent(PasswordActivity.this, TableActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
