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

package com.lgurgul.flipper.realm.kotlin

import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

internal object RealmHelper {

    private fun <T> useRealm(
        realmConfiguration: RealmConfiguration,
        block: (realm: Realm) -> T
    ): T {
        val realm = Realm.open(realmConfiguration)
        return try {
            block(realm)
        } finally {
            realm.close()
        }
    }

    fun getTableNames(realmConfiguration: RealmConfiguration): List<String> {
        return useRealm(realmConfiguration) { realm ->
            realm.schema()
                .classes
                .map { it.name }
        }
    }

    /*fun getTableColumns(
        realmConfiguration: RealmConfiguration,
        tableName: String
    ): List<RealmColumnInfo> {
        return getSharedRealm(realmConfiguration)
            .use { sharedRealm ->
                val columnNames = mutableListOf<RealmColumnInfo>()
                val table = sharedRealm.getTable(tableName)
                for (columnName in table.columnNames) {
                    val columnKey = table.getColumnKey(columnName)
                    columnNames.add(
                        RealmColumnInfo(
                            columnName,
                            table.getColumnType(columnKey).name,
                            table.isColumnNullable(columnKey)
                        )
                    )
                }
                columnNames
            }
    }

    private fun getTableColumnFieldType(
        sharedRealm: OsSharedRealm,
        tableName: String,
        columnName: String
    ): RealmFieldType {
        val table = sharedRealm.getTable(tableName)
        return table.getColumnType(table.getColumnKey(columnName))
    }

    private val sortableColumnFieldType = listOf(
        RealmFieldType.BOOLEAN,
        RealmFieldType.INTEGER,
        RealmFieldType.FLOAT,
        RealmFieldType.DOUBLE,
        RealmFieldType.STRING,
        RealmFieldType.DATE,
    )

    fun getRows(
        realmConfiguration: RealmConfiguration,
        tableName: String,
        order: String?,
        reverse: Boolean,
        start: Int,
        count: Int
    ): List<List<Any>> {
        return getSharedRealm(realmConfiguration)
            .use { sharedRealm ->
                val valueList = mutableListOf<List<Any>>()
                val table = sharedRealm.getTable(tableName)
                val osResults = OsResults.createFromQuery(
                    sharedRealm,
                    table.where()
                        .apply {
                            order
                                ?.takeIf {
                                    getTableColumnFieldType(
                                        sharedRealm,
                                        tableName,
                                        it
                                    ) in sortableColumnFieldType
                                }
                                ?.let {
                                    val sortOrder = if (reverse) Sort.DESCENDING else Sort.ASCENDING
                                    sort(
                                        null,
                                        arrayOf(it),
                                        arrayOf(sortOrder)
                                    )
                                }
                        }
                )
                for (i in start until osResults.size()) {
                    val uncheckedRow = osResults.getUncheckedRow(i.toInt())
                    val rowValues = mutableListOf<Any>()
                    for (columnName in uncheckedRow.columnNames) {
                        rowValues.add(getRowData(uncheckedRow, columnName))
                    }
                    valueList.add(rowValues)
                    if (valueList.size == count) {
                        break
                    }
                }
                valueList
            }
    }

    fun getRowsCount(realmConfiguration: RealmConfiguration, tableName: String): Long {
        return getSharedRealm(realmConfiguration)
            .use { sharedRealm ->
                OsResults
                    .createFromQuery(sharedRealm, sharedRealm.getTable(tableName).where())
                    .size()
            }
    }

    private fun getRowData(row: Row, columnName: String): Any {
        val columnKey = row.getColumnKey(columnName)
        return when (row.getColumnType(columnKey)) {
            RealmFieldType.INTEGER -> {
                if (row.isNull(columnKey)) {
                    NULL
                } else {
                    row.getLong(columnKey).toString()
                }
            }
            RealmFieldType.BOOLEAN -> {
                if (row.isNull(columnKey)) {
                    NULL
                } else {
                    row.getBoolean(columnKey)
                }
            }
            RealmFieldType.STRING -> {
                if (row.isNull(columnKey)) {
                    NULL
                } else {
                    row.getString(columnKey)
                }
            }
            RealmFieldType.BINARY -> {
                if (row.isNull(columnKey)) {
                    NULL
                } else {
                    row.getBinaryByteArray(columnKey).toString()
                }
            }
            RealmFieldType.DATE -> {
                if (row.isNull(columnKey)) {
                    NULL
                } else {
                    formatDate(row.getDate(columnKey))
                }
            }
            RealmFieldType.FLOAT -> {
                if (row.isNull(columnKey)) {
                    NULL
                } else {
                    row.getFloat(columnKey).toString()
                }
            }
            RealmFieldType.DOUBLE -> {
                if (row.isNull(columnKey)) {
                    NULL
                } else {
                    row.getDouble(columnKey).toString()
                }
            }
            RealmFieldType.OBJECT -> {
                if (row.isNullLink(columnKey)) {
                    NULL
                } else {
                    row.getLink(columnKey).toString()
                }
            }
            RealmFieldType.LIST -> {
                formatList(row.getModelList(columnKey))
            }
            RealmFieldType.INTEGER_LIST,
            RealmFieldType.FLOAT_LIST,
            RealmFieldType.DOUBLE_LIST,
            RealmFieldType.BOOLEAN_LIST,
            RealmFieldType.BINARY_LIST,
            RealmFieldType.DATE_LIST,
            RealmFieldType.STRING_LIST -> {
                if (row.isNullLink(columnKey)) {
                    NULL
                } else {
                    val columnType = row.getColumnType(columnKey)
                    formatValueList(row.getValueList(columnKey, columnType), columnType)
                }
            }
            else -> "[FLIPPER_UNKNOWN_VALUE]"
        }
    }

    private fun formatDate(date: Date): String {
        val sdf = SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.LONG, SimpleDateFormat.LONG)
        return "${sdf.format(date)} (${date.time})"
    }

    private fun formatList(osList: OsList): String {
        val sb = StringBuilder(osList.targetTable?.name ?: "")
        val size = osList.size()
        sb.append("{")
        for (i in 0 until size) {
            sb.append(osList.getUncheckedRow(i).objectKey)
            sb.append(',')
        }
        if (size > 0) {
            sb.setLength(sb.length - 1)
        }
        sb.append("}")
        return sb.toString()
    }

    private fun formatValueList(osList: OsList, columnType: RealmFieldType): String {
        val sb = StringBuilder(columnType.name)
        val size = osList.size()
        sb.append("{")
        for (i in 0 until size) {
            sb.append(osList.getValue(i))
            sb.append(',')
        }
        if (size > 0) {
            sb.setLength(sb.length - 1)
        }
        sb.append("}")
        return sb.toString()
    }*/

    data class RealmColumnInfo(
        val name: String,
        val type: String,
        val isNullable: Boolean
    )

    private const val NULL = "[null]"
}