package com.kgurgul.flipper.realm.android

import io.realm.RealmConfiguration
import io.realm.RealmFieldType
import io.realm.internal.OsSharedRealm
import io.realm.internal.Row
import java.text.SimpleDateFormat
import java.util.*

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

    fun getRows(realmConfiguration: RealmConfiguration, tableName: String): List<List<Any>> {
        return getSharedRealm(realmConfiguration).use { sharedRealm ->
            val valueList = mutableListOf<List<Any>>()
            val table = sharedRealm.getTable(tableName)
            for (i in 0 until table.size()) {
                val rawCheckedRow = table.getCheckedRow(i)
                val rowValues = mutableListOf<Any>()
                for (j in 0 until rawCheckedRow.columnCount) {
                    rowValues.add(getRowData(rawCheckedRow, j))
                }
                valueList.add(rowValues)
            }
            valueList
        }
    }

    private fun getRowData(row: Row, index: Long): Any {
        return when (row.getColumnType(index)) {
            RealmFieldType.INTEGER -> {
                if (row.isNull(index)) {
                    NULL
                } else {
                    row.getLong(index).toString()
                }
            }
            RealmFieldType.BOOLEAN -> {
                if (row.isNull(index)) {
                    NULL
                } else {
                    row.getBoolean(index)
                }
            }
            RealmFieldType.STRING -> {
                if (row.isNull(index)) {
                    NULL
                } else {
                    row.getString(index)
                }
            }
            RealmFieldType.BINARY -> {
                if (row.isNull(index)) {
                    NULL
                } else {
                    row.getBinaryByteArray(index).toString()
                }
            }
            RealmFieldType.DATE -> {
                if (row.isNull(index)) {
                    NULL
                } else {
                    formatDate(row.getDate(index))
                }
            }
            RealmFieldType.FLOAT -> {
                if (row.isNull(index)) {
                    NULL
                } else {
                    when (val aFloat = row.getFloat(index)) {
                        Float.NaN -> "NaN"
                        Float.POSITIVE_INFINITY -> "Infinity"
                        Float.NEGATIVE_INFINITY -> "-Infinity"
                        else -> aFloat.toString()
                    }
                }
            }
            RealmFieldType.DOUBLE -> {
                if (row.isNull(index)) {
                    NULL
                } else {
                    when (val aDouble = row.getDouble(index)) {
                        Double.NaN -> "NaN"
                        Double.POSITIVE_INFINITY -> "Infinity"
                        Double.NEGATIVE_INFINITY -> "-Infinity"
                        else -> aDouble.toString()
                    }
                }
            }
            RealmFieldType.OBJECT -> {
                if (row.isNullLink(index)) {
                    NULL
                } else {
                    row.getLink(index).toString()
                }
            }
            RealmFieldType.LIST -> {
                "[FLIPPER_WARNING] - lists will be added later]"
            }
            else -> "[FLIPPER_UNKNOWN_VALUE]"
        }
    }

    private fun formatDate(date: Date): String {
        val sdf = SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.LONG, SimpleDateFormat.LONG)
        return "${sdf.format(date)} (${date.time})"
    }

    data class RealmColumnInfo(
        val name: String,
        val type: String,
        val isNullable: Boolean
    )

    private const val NULL = "[null]"
}