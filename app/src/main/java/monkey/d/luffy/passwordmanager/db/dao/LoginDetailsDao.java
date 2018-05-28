package monkey.d.luffy.passwordmanager.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import monkey.d.luffy.passwordmanager.db.model.LoginDetails;

/**
 * Created by shasha on 28/5/18.
 */

@Dao
public interface LoginDetailsDao {
    @Query("SELECT * from LoginDetails")
    List<LoginDetails> getAll();

    @Insert
    void Insert(LoginDetails loginDetails);

    @Delete
    void delete(LoginDetails loginDetails);
}
