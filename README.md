Android Realm driver for [Flipper](https://github.com/facebook/flipper). Driver was 
tested with versions 5.4.+ and 6.+ of [Realm](https://github.com/realm/realm-java).

Download
========
* Top level gradle:
```kotlin
allprojects {
    repositories {
        ...
        jcenter()
    }
}
```
* Dependency:
```kotlin
implementation "com.kgurgul.flipper:flipper-realm-android:1.0.0"
```
* Instantiate and add the plugin in FlipperClient. All your 
RealmConfigurations should be passed to RealmDatabaseProvider:
```kotlin
client.addPlugin(
    DatabasesFlipperPlugin(
        RealmDatabaseDriver(
            this,
            object : RealmDatabaseProvider {
                override fun getRealmConfigurations(): List<RealmConfiguration> {
                    return listOf(yourRealmConfiguration)
            }
        })
    )
)
```

Usage
=====
Open Flipper app and select Database plugin

<img src="info/flipper.png" width="512" />

Features
========
* Displaying data from Realm database
* Displaying database structure

Currently it is not possible to sort data by columns or modify database from Flipper.

License
-------
    Copyright 2019 KG Soft

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.