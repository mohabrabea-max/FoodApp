package com.example.applicationhome.data.models.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [UserClass::class],
    version = 1,
    exportSchema = false
)
abstract class UsersDatabase : RoomDatabase(){
    abstract val dao : UsersDao
    companion object {
        @Volatile
        private var daoInstance : UsersDao? = null

        private fun buildDatabase(context : Context): UsersDatabase =
            Room.databaseBuilder(context.applicationContext, UsersDatabase::class.java, "food_app_database").fallbackToDestructiveMigration().build()

        fun getDaoInstance(context: Context) : UsersDao{
            synchronized(this){   // دا بيخلي الفانكشن دي متتناداش مرتين في الكود
                if(daoInstance == null){
                    daoInstance = buildDatabase(context).dao
                }
                return daoInstance as UsersDao
            }
        }
    }
}