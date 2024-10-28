package org.ihateham.kotlinmultiplatform.preferences

import androidx.datastore.core.DataMigration
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.internal.synchronized
import okio.Path.Companion.toPath
import org.ihateham.kotlinmultiplatform.db.initPrefDataStore
import kotlin.concurrent.Volatile

object PrefDataStore {
    private var prefDataStore: DataStore<Preferences>? = null

    fun getDataStore(
    ): DataStore<Preferences> {
        return prefDataStore ?: initPrefDataStore().also { prefDataStore = it }
    }

    fun createDataStore(
        corruptionHandler: ReplaceFileCorruptionHandler<Preferences>?,
        scope: CoroutineScope,
        migrations: List<DataMigration<Preferences>>,
        path: () -> String
    ): DataStore<Preferences> {
        return PreferenceDataStoreFactory.createWithPath(
            corruptionHandler = corruptionHandler,
            scope = scope,
            migrations = migrations,
            produceFile = { path().toPath() },
        )
    }
}

class PrefDataStoreWithClass private constructor() {

    companion object {
        @Volatile
        private var prefDataStore: DataStore<Preferences>? = null

        fun getDataStore(
        ): DataStore<Preferences> {
            return prefDataStore ?: synchronized(this) {
                prefDataStore ?: initPrefDataStore().also { prefDataStore = it }
            }
        }

        private fun createDataStore(
            corruptionHandler: ReplaceFileCorruptionHandler<Preferences>?,
            scope: CoroutineScope,
            migrations: List<DataMigration<Preferences>>,
            path: () -> String
        ): DataStore<Preferences> {
            return PreferenceDataStoreFactory.createWithPath(
                corruptionHandler = corruptionHandler,
                scope = scope,
                migrations = migrations,
                produceFile = { path().toPath() }
            )
        }
    }
}
