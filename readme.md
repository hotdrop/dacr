# プラクティス
まだ未完成のツールです。

## 開発環境
* Kotlin 1.0.4
* Gradle 2.1.3

## Gradleの依存関係
```
dependencies {  
    compile fileTree(include: ['*.jar'], dir: 'libs')  
    compile 'com.github.salomonbrys.kotson:kotson:2.3.0'  
    compile "org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version"  
    testCompile 'junit:junit:4.12'
}
```
