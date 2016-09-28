[![Kotlin 1.0.4](https://img.shields.io/badge/Kotlin-1.0.4-blue.svg)](http://kotlinlang.org)
# DACR
json形式の列定義ファイルを読み込んで、指定した行数のCSVデータを生成します。  
列によって固定値を出力したり、プログラムでランダムな値を自動生成することができます。

## 詳細
ダミーデータを生成したいテーブルの全カラム情報をjson形式で定義します。  
定義された情報に従って、カンマ区切りのCSVファイルを生成します。
負荷試験等で大量のダミーデータをDBのテーブルにinsertすることを目的に作成ました。  

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
## 使い方
jarにするかまたはmainを直接実行します。引数は3つ指定してください。  
```
java -jar dacr.jar [json file path] [output csv file path] [create record number]
(create record number: 生成するレコード数)
```

以下、3列5行のダミーデータを作成する例を示します。
1. 入力ファイル
```
[
  {"name": "name1", "primaryKey": true, "dataType": "char",
    "size": 5, "format": "zeroPadding", "autoIncrement": false,
    "fillMaxSize": false, "valueType": "variable",
    "value": "", "hasMultiByte": false
  },
  {
    "name": "name2", "primaryKey": false, "dataType": "char",
      "size": 10, "format": "", "autoIncrement": false,
      "fillMaxSize": false, "valueType": "fixing",
      "value": "A01,A02,B03,B04,C05", "hasMultiByte": false
  },
  {
    "name": "name3", "primaryKey": false, "dataType": "char",
      "size": 5, "format": "", "autoIncrement": false,
      "fillMaxSize": false, "valueType": "variable",
      "value": "", "hasMultiByte": false
  }
]
```
2. 実行
java -jar dacr.jar /var/tmp/sample.json /var/tmp 5

3. 結果
```
0000B,A01,4
00002,A02,C
00004,B03,A
00001,B04,D
0000E,C05,1
```

## 入力ファイルの各定義値について（概要）
1. name : カラム名。特に使用しません。
2. primaryKey : 主キーであればtrue、そうでなければfalse
3. dataType : 次のいずれかを指定[char varchar number date datetime timestamp]
4. size : カラムのサイズを指定
5. format : カラムのフォーマットを指定
6. autoIncrement : 自動的に連番をふる場合はtrue、ふらない場合はfalse
7. fillMaxSize : サイズの限界まで値を生成する場合はtrue、しない場合はfalse
8. valueType : 値のタイプを指定。すべての行を固定値にする場合はfixing、可変値を生成したいならvariable
9. value : 値を指定。固定値を入れる場合はその値、プログラムで生成する場合は空を指定。
10. hasMultiByte : 生成する値をマルチバイト文字（日本語）にする場合はtrue、しない場合はfalse

## 入力ファイルの各定義値について（詳細）

### name
指定値：任意の文字列  
カラム名を設定します。この定義値は今のところ使用していません。

### primaryKey
指定値：true/false
このカラムがプライマリキーに指定されている場合はtrueを指定します。
以下の条件を満たした場合、この値がtrueに指定されていると重複しない値を生成します。
1. valueType="variable"の場合（fixingの場合はprimariKeyの指定を完全無視します）
  * この条件がある理由は、固定値がある場合は使用者の意図がある可能性を考慮したためです。  
  例えば、PrimaryKeyを01,02,03の順で生成したい場合はその指定（valueType="Fixing" value="01,02,03")をするのみで、プライマリキーの判定は邪魔なだけなので除外しました。
  従って、上記の指定をして出力行数を4以上にした場合、出力されたCSVファイルのprimaryKeyは必ず重複しますのでinsert時に一意制約違反となります。
2. 1つのjson定義ファイルの中で、primaryKey="true"かつautoIncrement="true"が1つもない・・
