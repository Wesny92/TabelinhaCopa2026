package com.exemplo.copa2026.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.exemplo.copa2026.data.local.dao.GrupoDao
import com.exemplo.copa2026.data.local.dao.PartidaDao
import com.exemplo.copa2026.data.local.dao.PartidaMataMataDao
import com.exemplo.copa2026.data.local.dao.TimeDao
import com.exemplo.copa2026.data.model.Grupo
import com.exemplo.copa2026.data.model.Partida
import com.exemplo.copa2026.data.model.PartidaMataMata
import com.exemplo.copa2026.data.model.Time
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Database(
    entities = [
        Grupo::class,
        Time::class,
        Partida::class,
        PartidaMataMata::class
    ],
    version = 9,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun grupoDao(): GrupoDao
    abstract fun timeDao(): TimeDao
    abstract fun partidaDao(): PartidaDao
    abstract fun partidaMataMataDao(): PartidaMataMataDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "copa2026_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }

        fun populateIfNeeded(context: Context) {
            val db = getInstance(context)
            // Verifica se os grupos ja existem
            CoroutineScope(Dispatchers.IO).launch {
                val count = db.grupoDao().getAllGrupos().first().size
                if (count == 0) {
                    PrePopulateData.populate(db)
                }
            }
        }
    }
}
