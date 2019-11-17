package com.kgurgul.flipper.realm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kgurgul.flipper.realm.model.Test1
import com.kgurgul.flipper.realm.model.Test2
import io.realm.Realm
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
            val test1 = realm.createObject<Test1>(0)
            test1.nameTest = "Name test"
            test1.intTest = 10
            test1.booleanTest = true
            test1.floatTest = 11.11f
            test1.doubleTest = 12.12

            val test2 = realm.createObject<Test2>()
            test2.colorName = "Color name"
            test2.colorValue = 20

            val test3 = realm.createObject<Test2>()
            test3.colorName = "Color name 2"
            test3.colorValue = 30
            test3.test1 = test1
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
}
