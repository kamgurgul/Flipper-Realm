package com.kgurgul.flipper.realm.android

import io.realm.RealmConfiguration

/**
 * Inject all [RealmConfiguration] used inside the app
 */
interface RealmDatabaseProvider {

    fun getRealmConfigurations(): List<RealmConfiguration>
}