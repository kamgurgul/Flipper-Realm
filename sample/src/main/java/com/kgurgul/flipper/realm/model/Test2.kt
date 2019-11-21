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