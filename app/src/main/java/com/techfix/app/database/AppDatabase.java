package com.techfix.app.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.techfix.app.models.HistoryItem;
import com.techfix.app.models.RepairTicket;
import com.techfix.app.models.SparePart;
import com.techfix.app.models.User;

@Database(
    entities = {User.class, RepairTicket.class, HistoryItem.class, SparePart.class},
    version = 3,
    exportSchema = false
)
@TypeConverters(Converters.class)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    public abstract UserDao userDao();
    public abstract RepairTicketDao repairTicketDao();
    public abstract HistoryDao historyDao();
    public abstract SparePartDao sparePartDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "techfix_db"
                    )
                    .fallbackToDestructiveMigration()
                    .build();
                }
            }
        }
        return INSTANCE;
    }
}
