/*
 * Copyright 2019 KG Soft
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kgurgul.flipper

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
        return realmDatabaseProvider.getRealmConfigurations().map {
            RealmDatabaseDescriptor(
                it
            )
        }
    }

    override fun getTableNames(databaseDescriptor: RealmDatabaseDescriptor): List<String> {
        return RealmHelper.getTableNames(
            databaseDescriptor.realmConfiguration
        )
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
        val structureColumns = listOf("Column name", "Column type", "Nullable")
        val structureValue = mutableListOf<List<Any>>()
        RealmHelper.getTableColumns(
            databaseDescriptor.realmConfiguration,
            table
        ).forEach {
            structureValue.add(listOf(it.name, it.type, it.isNullable))
        }
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
        val columns = RealmHelper.getTableColumns(
            databaseDescriptor.realmConfiguration,
            table
        )
            .map { it.name }
        val values = RealmHelper.getRows(
            databaseDescriptor.realmConfiguration,
            table,
            start,
            count
        )
        return DatabaseGetTableDataResponse(
            columns,
            values,
            start,
            values.size,
            RealmHelper.getRowsCount(
                databaseDescriptor.realmConfiguration,
                table
            )
        )
    }

    override fun executeSQL(
        databaseDescriptor: RealmDatabaseDescriptor,
        query: String
    ): DatabaseExecuteSqlResponse? {
        return null
    }

    class RealmDatabaseDescriptor(
        val realmConfiguration: RealmConfiguration
    ) : DatabaseDescriptor {

        override fun name(): String = File(realmConfiguration.path).name
    }
}