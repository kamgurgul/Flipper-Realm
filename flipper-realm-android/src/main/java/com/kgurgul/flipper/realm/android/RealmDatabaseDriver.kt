package com.kgurgul.flipper.realm.android

import android.content.Context
import com.facebook.flipper.plugins.databases.DatabaseDescriptor
import com.facebook.flipper.plugins.databases.DatabaseDriver
import io.realm.Realm
import io.realm.RealmConfiguration
import java.io.File

/**
 * Flipper driver for [Realm] database
 */
class RealmDatabaseDriver(
    context: Context,
    private val realmDatabaseProvider: RealmDatabaseProvider
) : DatabaseDriver<RealmDatabaseDriver.RealmDatabaseDescriptor>(context) {

    override fun getDatabases(): List<RealmDatabaseDescriptor> {
        return realmDatabaseProvider.getRealmConfigurations().map { RealmDatabaseDescriptor(it) }
    }

    override fun getTableNames(databaseDescriptor: RealmDatabaseDescriptor): List<String> {
        return databaseDescriptor.realmConfiguration.realmObjectClasses.map { it.name }
    }

    override fun getTableInfo(
        databaseDescriptor: RealmDatabaseDescriptor,
        table: String
    ): DatabaseGetTableInfoResponse {
        return DatabaseGetTableInfoResponse(table)
    }

    override fun getTableStructure(
        databaseDescriptor: RealmDatabaseDescriptor,
        table: String
    ): DatabaseGetTableStructureResponse {
        val structureColumns = listOf("Field name", "Field type")
        val structureValue = mutableListOf<List<Any>>()
        getFields(table).forEach { structureValue.add(listOf(it.first, it.second)) }
        return DatabaseGetTableStructureResponse(
            structureColumns,
            structureValue,
            emptyList(),
            emptyList()
        )
    }

    override fun getTableData(
        databaseDescriptor: RealmDatabaseDescriptor,
        table: String,
        order: String?,
        reverse: Boolean,
        start: Int,
        count: Int
    ): DatabaseGetTableDataResponse? {
        return null
    }

    override fun executeSQL(
        databaseDescriptor: RealmDatabaseDescriptor,
        query: String
    ): DatabaseExecuteSqlResponse? {
        return null
    }

    /**
     *  Realm doesn't support inherited models now so declaredFields will represent all fields
     *  https://github.com/realm/realm-java/issues/761
     *
     *  @return [Pair] with field name and type
     */
    private fun getFields(table: String): List<Pair<String, String>> {
        return Class.forName(table).declaredFields.map { it.name to it.type.name }
    }

    class RealmDatabaseDescriptor(
        val realmConfiguration: RealmConfiguration
    ) : DatabaseDescriptor {

        override fun name(): String = File(realmConfiguration.path).name
    }
}