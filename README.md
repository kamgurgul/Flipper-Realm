[![Download](https://img.shields.io/maven-central/v/com.kgurgul.flipper/flipper-realm-android/2.1.0)](https://search.maven.org/artifact/com.kgurgul.flipper/flipper-realm-android/2.1.0/pom)

Android Realm driver for [Flipper](https://github.com/facebook/flipper).

Because of breaking changes between [Realm](https://github.com/realm/realm-java) versions driver is split into two versions:
* **2.+** for **Realm 7.+** and **Realm 10.+** (from 2.1.0 available on mavenCentral)
* **1.+** for **Realm 5.+** and **Realm 6.+** (legacy version still available on jcenter)

Download
========
* Configure [Flipper](https://fbflipper.com/docs/getting-started.html)
* Top level gradle:
```kotlin
allprojects {
    repositories {
        ...
        mavenCentral()
    }
}
```
* Dependency:

```kotlin
implementation "com.kgurgul.flipper:flipper-realm-android:2.1.0"
```
* Instantiate and add plugin to the FlipperClient. All your 
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
Open Flipper app and enable Database plugin

<img src="info/flipper.png" width="512" />

Features
========
* Displaying data from Realm database
* Sort data by columns, for the types `RealmFieldType.BOOLEAN`, `RealmFieldType.INTEGER`, `RealmFieldType.FLOAT`, `RealmFieldType.DOUBLE`, `RealmFieldType.STRING`, `RealmFieldType.DATE`.
* Displaying database structure

Currently it is not possible to modify database from Flipper.

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
