package com.example.international.business.men.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.international.business.men.data.db.dao.ProductDao
import com.example.international.business.men.data.db.entity.ProductEntity

@Database(
    entities = [ProductEntity::class],
    version = 1,
    exportSchema = false
)
abstract class InternationalBusinessMenDatabase: RoomDatabase() {

    abstract fun getProductDao(): ProductDao

    companion object {

        @Volatile
        private var instance: InternationalBusinessMenDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance
            ?: synchronized(LOCK) {
                instance
                    ?: buildDatabase(
                        context
                    ).also {
                        instance = it
                    }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                InternationalBusinessMenDatabase::class.java, "newsfeedtest.db"
            ).fallbackToDestructiveMigration().build()
    }
}