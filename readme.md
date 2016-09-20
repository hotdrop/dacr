# プラクティス
まだ設計途中のツールなので中身は何もありません。

## 開発環境
* Kotlin 1.0.3

## Gradleの依存関係
```
dependencies {  
    compile fileTree(include: ['*.jar'], dir: 'libs')  
    compile 'com.github.salomonbrys.kotson:kotson:2.3.0'  
    compile "org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version"  
}
```
