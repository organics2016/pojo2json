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

### [中文](./README_ZH.md) | [English](./README.md)

<!-- Plugin description -->

# POJO to JSON

将 POJO 转换为 JSON 的简单插件

- 支持 BigDecimal 和其他的 Numeric
- 支持 Java8 时间类型
- 支持 枚举
- 部分支持 Jackson 和 Fastjson 注解
- 支持 Java14 Records [JEP-359](https://openjdk.java.net/jeps/359)

## Support JVM platform languages

- Java - 完全支持
- Kotlin - 大概, 完全支持

## Usage

- <kbd>打开class文件</kbd> > <kbd>右键单击</kbd> > <kbd>Copy/Paste Special</kbd> > <kbd>Copy JSON</kbd> > <kbd>JSON 结果将复制到剪贴板</kbd>
![Image text](https://raw.githubusercontent.com/organics2016/pojo2json/master/screenshot/file_single.gif)

- <kbd>项目视图选择一个class文件</kbd> > <kbd>右键单击</kbd> > <kbd>Copy JSON</kbd> > <kbd>JSON 结果将复制到剪贴板</kbd>
![Image text](https://raw.githubusercontent.com/organics2016/pojo2json/master/screenshot/list_single.gif)

- <kbd>项目视图选择多个class文件</kbd> > <kbd>右键单击</kbd> > <kbd>Copy JSON</kbd> > <kbd>JSON 结果将生成到 Scratches 文件夹中</kbd>
![Image text](https://raw.githubusercontent.com/organics2016/pojo2json/master/screenshot/batch.gif)

## Installation

- **IDEA中安装:**
    - <kbd>Preferences(Settings)</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>搜索<b>"POJO to JSON"</b></kbd> > <kbd>Install</kbd>

- **手动安装:**
    - [plugin] -> <kbd>Preferences(Settings)</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd> -> Select the plug-in package and install（No need to unzip）
    
## Q&A

- 为什么使用的时候总是报错？
```
This class reference level exceeds maximum limit or has nested references!
```
当程序抛出此警告时，只有两种可能性。

1. 这个 class 或父级 class 存在嵌套引用

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

2. 当前class的引用层级 > 500 

eg:
```java
public class A {
    private B _0;
    public class B {
        private C _1;
        public class C {
            private D _2;
            public class D {
                // _3 ..... _501..
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
        "......_501":{}
      }
    }
  }
}
```
也许这两种情况都会发生在实体上，但这个实体不适合JSON。<br>
您可以尝试使用 Jackson 序列化您的 POJO 看看会发生什么。<br>
如果没有任何异常, 可以将您的POJO作为例子向此repo的Issues提交BUG :)

- 但是如何解决这个问题呢？

您可以尝试以下方法。

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
或者当您没有导入 jackson 库时
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
或者当您没有导入 jackson 库时
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

您在使用过程中可能会遇到此问题。
```
This class reference level exceeds maximum limit or has nested references!
```
上述方法可以很好地解决嵌套引用问题。

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

想法和部分实现来自
[![](https://avatars.githubusercontent.com/u/12984934?s=28)linsage](https://github.com/linsage)