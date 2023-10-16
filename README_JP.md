[![][idea-img]][plugin]
[![][license-img]][github]
[![][release-img]][plugin]
[![][download-img]][plugin]

[idea-img]: https://img.shields.io/badge/IntelliJ%20IDEA%20Plugins-000000?logo=IntelliJ-idea&logoColor=white
[license-img]: https://img.shields.io/github/license/organics2016/pojo2json
[release-img]: https://img.shields.io/jetbrains/plugin/v/12066
[download-img]: https://img.shields.io/jetbrains/plugin/d/12066

[github]: https://github.com/organics2016/pojo2json
[plugin]: https://plugins.jetbrains.com/plugin/12066-pojo-to-json

### [English](./README.md) | [中文](./README_ZH.md) | [日本語](./README_JP.md)

<!-- Plugin description -->

# POJO to JSON

POJOをJSONに変換するシンプルなプラグイン

- BigDecimalと他のNumericぷったいをサポートします
- Java8 時間タイプをサポートします
- Enumをサポートします
- JacksonとFastjson 注釈は部分的なサポートします
- Java14 Recordsをサポートします [JEP-359](https://openjdk.java.net/jeps/359)
- Support conversion
  - Inner Class
  - Global Variable
  - Local Variable
  - Constructor Parameter
  - Method Parameter

## Support JVM platform languages

- Java - 完全にサポートします
- Kotlin - どうやら完全にサポートします

## Usage

- カーソルの位置が結果に影響を与える可能性があることに注意してください！
- <kbd>classファイルを開く</kbd> > <kbd>カーソルを Class/Variable/Parameter に移動します</kbd> > <kbd>右クリック</kbd> > <kbd>Copy/Paste Special</kbd> > <kbd>Copy JSON</kbd> > <kbd>JSONの結果がクリップボードにコピーされます</kbd>
![Image text](https://raw.githubusercontent.com/organics2016/pojo2json/master/screenshot/file_single.gif)

- <kbd>Project viewでclassファイルを選択</kbd> > <kbd>右クリック</kbd> > <kbd>Copy JSON</kbd> > <kbd>JSONの結果がクリップボードにコピーされます</kbd>
![Image text](https://raw.githubusercontent.com/organics2016/pojo2json/master/screenshot/list_single.gif)

- <kbd>Project viewで複数ファイルを選択</kbd> > <kbd>右クリック</kbd> > <kbd>Copy JSON</kbd> > <kbd>JSON result will generate to files in the Scratches folder</kbd>
![Image text](https://raw.githubusercontent.com/organics2016/pojo2json/master/screenshot/batch.gif)

## Installation

- **Install in IDEA:**
    - <kbd>Preferences(Settings)</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search<b>"POJO to JSON"</b></kbd> > <kbd>Install</kbd>

- **Manual Install:**
    - [plugin] -> <kbd>Preferences(Settings)</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd> -> Select the plug-in package and install（No need to unzip）
    
## Q&A

- 使用時に常にエラーを報告するのはなぜですか？
```
This class reference level exceeds maximum limit or has nested references!
```
When the program throws this warning there are only two possibilities.

1. This class or parent class has nested references

eg:
```java
public class A {
    private B b;

    public class B {
        private A a;
    }
}
```
```
{
 "b":{
  "a":{
   "b":{
     "a":{
        ......
      }
    }
   }
 }
}
```
or
```java
public class A {
    private A a;
}
```
```
{
 "a":{
  "a":{
   "a":{
     "a":{
        ......
      }
    }
   }
 }
}
```

2. This class reference level > 200 

eg:
```java
public class A {
    private B _0;
    public class B {
        private C _1;
        public class C {
            private D _2;
            public class D {
                // _3 ..... _201..
            }
        }
    }
}
```
```
{
  "_0": {
    "_1": {
      "_2": {
        "......_201":{}
      }
    }
  }
}
```
Perhaps both will happen for entity but this entity are not suitable for JSON.<br>
So you can try to serialize your POJO using Jackson to see what happens.<br>
If no exception, you can submit a bug to this repository issues with your target class :)

- But how to solve this problem?

You can try the following methods.

## Support Annotations and Javadoc

### @JsonProperty and @JSONField

```java
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;

public class User {

    @JsonProperty("name")
    private String username;
    
    @JSONField(name = "pass")
    private String password;
}
```
paste result:
```json
{
  "name": "",
  "pass": ""
}
```

### @JsonIgnore or Javadoc tags JsonIgnore

```java
import com.fasterxml.jackson.annotation.JsonIgnore;

public class User {
    
    @JsonIgnore
    private String username;
    private String password;
}
```
or when there is no jackson library
```java
public class JsonIgnoreDocTestPOJO {

  /**
   * @JsonIgnore
   */
  private String username;
  private String password;
}
```

paste result:
```json
{
  "password": ""
}
```

### @JsonIgnoreProperties or Javadoc tags JsonIgnoreProperties

```java
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

public class User {

    private String username;
    @JsonIgnoreProperties({"users", "aaa", "bbb"})
    private List<Role> roles;

    public class Role {

        private String roleName;
        private List<User> users;
    }
}
```
or when there is no jackson library
```java
import java.util.List;

public class User {

    private String username;
    /**
     * @JsonIgnoreProperties users, aaa, bbb
     */
    private List<Role> roles;

    public class Role {

        private String roleName;
        private List<User> users;
    }
}
```
paste result:
```json
{
  "username": "",
  "roles": [
    {
      "roleName": ""
    }
  ]
}
```

You may encounter this problem during use.
```
This class reference level exceeds maximum limit or has nested references!
```
The above method can solve the nested reference problem well.

### @JsonIgnoreType

```java
import com.fasterxml.jackson.annotation.JsonIgnoreType;

import java.util.List;

public class User {

    private String username;
    private List<Role> roles;

    @JsonIgnoreType
    public class Role {
        private String roleName;
        private List<User> users;
    }
}
```
paste result:
```json
{
  "username": "",
  "roles": []
}
```
<!-- Plugin description end -->

## Contributors

Ideas and partial realization from
[![](https://avatars.githubusercontent.com/u/12984934?s=28)linsage](https://github.com/linsage)