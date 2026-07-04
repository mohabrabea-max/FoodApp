package com.example.applicationhome.data.models.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.applicationhome.data.models.local.dao.CartDao
import com.example.applicationhome.data.models.local.dao.FavoriteDao
import com.example.applicationhome.data.models.local.dao.UsersDao
import com.example.applicationhome.data.models.local.entity.CartClass
import com.example.applicationhome.data.models.local.entity.CartItemsClass
import com.example.applicationhome.data.models.local.entity.FavoriteFoodDatabase
import com.example.applicationhome.data.models.local.entity.FavoriteRestaurantDatabase
import com.example.applicationhome.data.models.local.entity.FavoriteSnacksDatabase
import com.example.applicationhome.data.models.local.entity.UserClass

@Database(
    entities = [UserClass::class, CartClass::class, CartItemsClass::class, FavoriteFoodDatabase::class, FavoriteSnacksDatabase::class, FavoriteRestaurantDatabase::class],
    version = 22,
    exportSchema = false
)

@TypeConverters(FavoriteConverters::class)

abstract class UsersDatabase : RoomDatabase(){
    abstract val userDao : UsersDao
    abstract val cartDao : CartDao
    abstract val favoriteDao : FavoriteDao
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