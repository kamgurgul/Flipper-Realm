package com.kgurgul.flipper.realm

import android.app.Application
import com.facebook.flipper.android.AndroidFlipperClient
import com.facebook.flipper.android.utils.FlipperUtils
import com.facebook.flipper.plugins.databases.DatabasesFlipperPlugin
import com.facebook.flipper.plugins.inspector.DescriptorMapping
import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin
import com.facebook.soloader.SoLoader
import com.kgurgul.flipper.RealmDatabaseDriver
import com.kgurgul.flipper.RealmDatabaseProvider
import io.realm.Realm
import io.realm.RealmConfiguration

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        Realm.init(this)
        val realmConfiguration = RealmConfiguration.Builder()
            .name("testRealm")
            .schemaVersion(1)
            .deleteRealmIfMigrationNeeded()
            .build()
        Realm.setDefaultConfiguration(realmConfiguration)

        SoLoader.init(this, false)
        if (BuildConfig.DEBUG && FlipperUtils.shouldEnableFlipper(this)) {
            val client = AndroidFlipperClient.getInstance(this)
            client.addPlugin(InspectorFlipperPlugin(this, DescriptorMapping.withDefaults()))
            client.addPlugin(
                DatabasesFlipperPlugin(
                    RealmDatabaseDriver(
                        this,
                        object : RealmDatabaseProvider {
                            override fun getRealmConfigurations(): List<RealmConfiguration> {
                                return listOf(realmConfiguration)
                            }
                        })
                )
            )
            client.start()
        }
    }
}