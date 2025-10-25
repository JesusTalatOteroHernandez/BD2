package mx.edu.utng.jtoh.bd2.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import mx.edu.utng.jtoh.bd2.data.local.dao.PostDao
import mx.edu.utng.jtoh.bd2.data.local.entity.Post

@Database(entities = [Post::class], version = 1)
abstract class AppDataBase: RoomDatabase() {
    abstract fun postDao(): PostDao

    companion object{
        @Volatile
        private var instance: AppDataBase? = null
        fun getInstance(context: Context): AppDataBase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java,
                    "app_database"
                ).build()
            }
            return instance!!
        }
    }
}