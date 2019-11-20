package com.kgurgul.flipper.realm.model

import io.realm.RealmList
import io.realm.RealmObject

open class Test2 : RealmObject() {
    var colorName: String? = null
    var colorValue: Int? = null
    var test1: Test1? = null
    var listTest: RealmList<Test1>? = null
    var listStringTest: RealmList<String>? = null
}