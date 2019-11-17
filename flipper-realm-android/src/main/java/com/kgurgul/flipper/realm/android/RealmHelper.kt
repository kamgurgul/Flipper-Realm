package com.kgurgul.flipper.realm.android

import io.realm.RealmConfiguration
import io.realm.internal.OsSharedRealm

object RealmHelper {

    fun getSharedRealm(realmConfiguration: RealmConfiguration): OsSharedRealm {
        return OsSharedRealm.getInstance(realmConfiguration)
    }

    fun getTableNames(realmConfiguration: RealmConfiguration): List<String> {
        return getSharedRealm(realmConfiguration).use { sharedRealm ->
            val tableNames = mutableListOf<String>()
            for (i in 0 until sharedRealm.size()) {
                tableNames.add(sharedRealm.getTableName(i.toInt()))
            }
            tableNames
        }
    }

    fun getTableColumns(
        realmConfiguration: RealmConfiguration,
        tableName: String
    ): List<RealmColumnInfo> {
        return getSharedRealm(realmConfiguration).use { sharedRealm ->
            val columnNames = mutableListOf<RealmColumnInfo>()
            val table = sharedRealm.getTable(tableName)
            for (i in 0 until table.columnCount) {
                columnNames.add(
                    RealmColumnInfo(
                        table.getColumnName(i),
                        table.getColumnType(i).name,
                        table.isColumnNullable(i)
                    )
                )
            }
            columnNames
        }
    }

    data class RealmColumnInfo(
        val name: String,
        val type: String,
        val isNullable: Boolean
    )
}