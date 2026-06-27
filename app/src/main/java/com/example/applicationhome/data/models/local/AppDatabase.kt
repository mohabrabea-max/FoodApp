package com.example.applicationhome.data.models.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [UserClass::class, CartClass::class, CartItemsClass::class],
    version = 14,
    exportSchema = false
)
abstract class UsersDatabase : RoomDatabase(){
    abstract val userDao : UsersDao
    abstract val cartDao : CartDao
    companion object {
        @Volatile  // بتخلي التغيير اللي بيحصل على المتغير daoInstance نفسه في الرام يسمع فوراً في كل الـ Threads
        private var INSTANCE : UsersDatabase? = null

//        val MIGRATION_1_2 = object : Migration(1, 2){   // الجزء دا عشان لو هنضيف عمود جديد في الجدول ميمسحش الداتا القديمة
//            override fun migrate(db: SupportSQLiteDatabase) {
//                db.execSQL("ALTER TABLE users ADD COLUMN address2 TEXT NOT NULL DEFAULT ''")
//            }
//        }

        //  الفانكشن دي هي اللي بتخلي مكتبة Room تدي لdao قيمة
        private fun buildDatabase(context : Context): UsersDatabase =
            Room.databaseBuilder(context.applicationContext, UsersDatabase::class.java, "food_app_database").fallbackToDestructiveMigration().build()

        fun getDaoInstance(context: Context) : UsersDatabase{
            return INSTANCE ?: synchronized(this) {
                var instance = INSTANCE
                if(instance == null){
                    instance = buildDatabase(context)
                    INSTANCE = instance
                }
                instance
            }
        }
    }
}