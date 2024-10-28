package org.ihateham.kotlinmultiplatform.db

import android.content.Context
import androidx.datastore.core.DataMigration
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.CoroutineScope
import org.ihateham.kotlinmultiplatform.preferences.PrefDataStore
import java.io.File

actual fun initPrefDataStore(
    corruptionHandler: ReplaceFileCorruptionHandler<Preferences>?,
    coroutineScope: CoroutineScope,
    migrations: List<DataMigration<Preferences>>,
): DataStore<Preferences> {
    return PrefDataStore.createDataStore(
        corruptionHandler = corruptionHandler,
        scope = coroutineScope,
        migrations = migrations,
        path = { ("settings.preferences_pb") },
    )
}