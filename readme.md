[![Kotlin 1.0.4](https://img.shields.io/badge/Kotlin-1.0.4-blue.svg)](http://kotlinlang.org)
# DACR
json形式の列定義ファイルを読み込み、指定した行数のCSVデータを生成します。  

## Description
負荷試験で大量のダミーデータをDBにinsertしたかったため、このツールを作成しました。  
ダミーデータを生成したいテーブルの全カラム情報をjson形式で定義します。  
そのjsonファイルと生成行数を指定すると、CSVファイル（カンマ区切り）を生成します。  

## Requirement
* Kotlin 1.0.4
* gson 2.7  

> Gson is released under the Apache 2.0 license.
  License: [Apache License Version 2.0](/licenses/ApacheLicense2.0)

## Usage
jarにするかまたはmainを直接実行します。引数は3つ指定してください。  
```
java -jar dacr.jar [json file path] [output file path] [number of row to be created]
```

## Example
* input json file
```
[
  {"name": "name1", "primaryKey": true, "dataType": "char",
    "size": 5, "format": "zeroPadding", "autoIncrement": false,
    "fillMaxSize": false, "valueType": "variable",
    "value": "", "hasMultiByte": false, "encloseChar": ""
  },
  {
    "name": "name2", "primaryKey": false, "dataType": "char",
      "size": 10, "format": "", "autoIncrement": false,
      "fillMaxSize": false, "valueType": "fixing",
      "value": "A01,A02,B03,B04,C05", "hasMultiByte": false, "encloseChar": ""
  },
  {
    "name": "name3", "primaryKey": false, "dataType": "char",
      "size": 5, "format": "", "autoIncrement": false,
      "fillMaxSize": false, "valueType": "variable",
      "value": "", "hasMultiByte": false, "encloseChar": "SingleQuotation"
  }
]
```
* execute  
java -jar dacr.jar /var/tmp/sample.json /var/tmp/result.csv 5

* result
```
0000B,A01,'4'
00002,A02,'C'
00004,B03,'A'
00001,B04,'D'
0000E,C05,'1'
```

## Definition file description
1. name : カラム名
2. primaryKey : 主キーであればtrue、そうでなければfalse
3. dataType : 次のいずれかを指定[char varchar number date datetime timestamp]
4. size : データ長
5. format : カラムのフォーマット
6. valueType : 固定の値を出力する場合はfixing、値を可変に生成する場合はvariable
7. value : 固定の値を出力する場合はその値、プログラムで生成する場合は空
8. autoIncrement : 自動連番を出力する場合はtrue、しない場合はfalse
9. fillMaxSize : データ長の限界まで値を生成する場合はtrue、しない場合はfalse
10. hasMultiByte : 生成する値をマルチバイト文字（日本語）にする場合はtrue、しない場合はfalse
11. encloseChar : 文字をシングルクォーテーションで囲う場合はSingleQuotation、ダブルの場合はDoubleQuotation

## name
The following parameter defined: any value
DataType: all  
To specify the column name. not use in the program.

## primaryKey
The following parameter defined: true/false  
DataType: all  
*(attention! If this parameter is true, the value does not duplicate.  
  To increase the generation number of trials, remove the duplication.  
  This fact, there is a risk of an infinite loop.  
  Therefore, if the generation number of trials has become to a certain number will throw an IllegalStateException.)*
* primaryKey=false value="variable" value=""  
```
A13 <-
BA1
 :
A13 <- duplicate first line
```
* primaryKey=**true** value="variable" value=""
```
A11  <-
C14  <-
B23  <- not duplicate
A15  <-
 :   <-
```

## dataType
The following parameter defined: char/varchar/number/date/datetime/timestamp  
DataType: all  
*(attention! This parameter is case insensitive)*  
* dataType="char" or dataType="varchar"
```
A03
BE5
BC3
 :
```
* dataType="number"
```
3819
7958
4116
 :
```
* dataType="date" format="YYYY/MM/dd"
```
2016/11/21
2013/02/17
    :
```
* dataType="datetime" format="YYYY/MM/dd hh:mm:ss"
```
2014/06/04 21:37:45
2001/12/16 16:24:38
          :
```
* dataType="timestamp" format="YYYY/MM/dd hh:mm:ss.SSS"
```
2006/06/04 02:29:28.504
2004/02/16 04:42:45.061
          :
```

## size
The following parameter defined: any number  
DataType: char/varchar/number  
* dataType="char" size=6 valueType="variable"  
*(attention! The length of the creating value is size/3)*
```
A1 <- create to 6/3=2 byte character
B5
4A
 :
```
* dataType="char" size=12 valueType="variable"
```
BC1C <- create to 12/3=4 byte character
A2C5
ED43
 :
```

## format
The following parameter defined: DateFormat(YYYY/MM/dd, hh:mm:ss ...etc) zeroPadding  
DataType: all  
*(attention! "zeroPadding" parameter is case insensitive)*
* dataType="date" format="YYYY-MM-dd"
```
2005-06-25
2001-09-12
    :
```
* dataType="char" format="zeroPadding" size=6 valueType="variable"
```
0000A1
0000B5
00004A
   :
```
* dataType="number" autoIncrement=true format="zeroPadding" size=4
```
0001
0002
0003
 :
```

## valueType
The following parameter defined: fixing/variable  
DataType: all  
*(attention! This parameter is case insensitive)*
* dataType="char" valueType="fixing" value="hoge"
```
hoge
hoge
hoge
 :
```
* dataType="char" valueType="variable" value="hoge"
```
A1 <- ignore "value" key. to create a random value
B5
4A
 :
```

## value
The following parameter defined: single value/plurality of values  
DataType: all  
* dataType="char" valueType="fixing" value="hoge"
```
hoge
hoge
hoge
 :
```
* dataType="char" valueType="fixing" value="A01,B02,C03,D04"
```
A01 <- in case of "fixing", output in order
B02 <-
C03 <-
D04 <-
A01 <-
 :
```
* dataType="char" valueType="variable" value="A01,B02,C03,D04"
```
C03  <-
D04  <-
C03  <- in case of "variable", output in random
A01  <-
B02  <-
 :
```
* dataType="date" valueType="variable" value="2016/09/23,2016/09/24,2016/09/25"  
```
2016/09/24  <-
2016/09/25  <-
2016/09/23  <- in case of "variable", output in random
2016/09/23  <-
    :
```
* dataType="date" valueType="variable" value="now"  
*(attention! "now" parameter is case insensitive)*
```
2016/09/30 <- current date
2016/09/30
    :
```

## autoIncrement
The following parameter defined: true/false  
DataType: char/varchar/number  
* dataType="char" autoIncrement="true"
```
1
2
3
:
```
* dataType="number" autoIncrement="true" value="10055"
```
10055 <- start from the specified number in the value
10056
10057
  :
```

## fillMaxSize
The following parameter defined: true/false  
DataType: char/varchar  
* dataType="char" size=6 valueType="variable" fillMaxSize=false
```
A1 <- create to 6/3=2 byte character
B5
4A
 :
```
* dataType="char" size=6 valueType="variable" fillMaxSize=**true**
```
3D3A2 <- fill up to a maximum of size
C25CB
CBEBC
  :
```
## hasMultiByte
The following parameter defined: true/false  
DataType: char/varchar  
* dataType="char" hasMultiByte=false
```
ABC
B2C
35A
 :
```
* dataType="char" hasMultiByte=**true**
```
あいあ
えいあ
うあお
  :
```

## encloseChar
The following parameter defined: SingleQuotation DoubleQuotation  
DataType: char/varchar  
*(attention! This parameter is case insensitive)*
* dataType="char" encloseChar="SingleQuotation"
```
'ABC'
'B2C'
'35A'
 :
```
* dataType="char" encloseChar="DoubleQuotation"
```
"ABC"
"B2C"
"35A"
 :
```

# License  
* [The MIT License(MIT)](/licenses/LICENSE)
