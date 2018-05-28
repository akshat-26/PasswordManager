package monkey.d.luffy.passwordmanager.db.helperdb;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import monkey.d.luffy.passwordmanager.db.dao.LoginDetailsDao;
import monkey.d.luffy.passwordmanager.db.model.LoginDetails;

@Database(entities = {LoginDetails.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract LoginDetailsDao userDao();
}
