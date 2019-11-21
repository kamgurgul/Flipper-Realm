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

package com.kgurgul.flipper.realm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kgurgul.flipper.realm.model.Test1
import com.kgurgul.flipper.realm.model.Test2
import io.realm.Realm
import io.realm.RealmList
import io.realm.kotlin.createObject

class MainActivity : AppCompatActivity() {

    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        realm = Realm.getDefaultInstance()
        realm.executeTransaction { realm ->
            realm.deleteAll()
        }

        realm.executeTransaction { realm ->
            for (i in 0 until 200) {
                val test1 = realm.createObject<Test1>(i)
                test1.nameTest = "Name test"
                test1.intTest = 10 + i
                test1.booleanTest = i % 2 == 0
                test1.floatTest = 11.11f + i
                test1.doubleTest = 12.12 + i
            }

            val test2 = realm.createObject<Test2>()
            test2.colorName = "Color name"
            test2.colorValue = 20

            val test3 = realm.createObject<Test2>()
            test3.colorName = "Color name 2"
            test3.colorValue = 30
            test3.listTest = RealmList(realm.createObject(1000))
            test3.listStringTest = RealmList("Abc", "Cdf")
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
}
