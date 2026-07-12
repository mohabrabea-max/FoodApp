package com.example.applicationhome.data.data.local.db

import android.content.Context
import androidx.room.Room
import com.example.applicationhome.data.data.local.dao.CartDao
import com.example.applicationhome.data.data.local.dao.FavoriteDao
import com.example.applicationhome.data.data.local.dao.OrdersDao
import com.example.applicationhome.data.data.local.dao.UsersDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): UsersDatabase{
        return Room.databaseBuilder(
            context,
            UsersDatabase::class.java,
            "food_app_database"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideUserDao(database: UsersDatabase): UsersDao{
        return database.userDao
    }

    @Provides
    @Singleton
    fun provideCartDao(database: UsersDatabase): CartDao {
        return database.cartDao
    }

    @Provides
    @Singleton
    fun provideFavoriteDao(database: UsersDatabase): FavoriteDao {
        return database.favoriteDao
    }

    @Provides
    @Singleton
    fun provideOrdersDao(database: UsersDatabase): OrdersDao {
        return database.ordersDao
    }
}


//        val MIGRATION_1_2 = object : Migration(1, 2){   // الجزء دا عشان لو هنضيف عمود جديد في الجدول ميمسحش الداتا القديمة
//            override fun migrate(db: SupportSQLiteDatabase) {
//                db.execSQL("ALTER TABLE users ADD COLUMN address2 TEXT NOT NULL DEFAULT ''")
//            }
//        }