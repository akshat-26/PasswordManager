package monkey.d.luffy.passwordmanager.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import monkey.d.luffy.passwordmanager.R;
import monkey.d.luffy.passwordmanager.db.model.LoginDetails;
import monkey.d.luffy.passwordmanager.util.EncryDercy;

public class PasswordAdapter extends RecyclerView.Adapter<PasswordAdapter.PasswordViewHolder> {
    List<LoginDetails> loginDetailsList;

    public PasswordAdapter(List<LoginDetails> loginDetailsList) {
        this.loginDetailsList = loginDetailsList;
    }

    class PasswordViewHolder extends RecyclerView.ViewHolder{
        TextView username;
        PasswordViewHolder(View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
        }
    }

    @NonNull
    @Override
    public PasswordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PasswordViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_passwords, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PasswordViewHolder holder, int position) {
        holder.username.setText(EncryDercy.decrypt(loginDetailsList.get(position).getUsername()));
    }

    @Override
    public int getItemCount() {
        return loginDetailsList.size();
    }
}
