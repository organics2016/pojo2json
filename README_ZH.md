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

[spel-url]: https://docs.spring.io/spring-framework/reference/core/expressions.html

### [English](./README.md) | [中文](./README_ZH.md) | [日本語](./README_JP.md)

<!-- Plugin description -->

# POJO to JSON

将 POJO 转换为 JSON 的简单插件

- 支持Java中您能想到的任何类型
- 支持 Java14 Records [JEP-359](https://openjdk.java.net/jeps/359)
- 部分支持 Jackson 和 Fastjson 注解
- 支持转换
    - Inner Class
    - Global Variable
    - Local Variable
    - Constructor Parameter
    - Method Parameter

## Support JVM platform languages

- Java - 完全支持
- Kotlin - 完全支持

## Usage

- 请注意，光标的位置会影响结果！
- <kbd>打开class文件</kbd> > <kbd>移动光标到 Class/Variable/Parameter</kbd> > <kbd>右键单击</kbd> > <kbd>Copy
  JSON</kbd> > <kbd>JSON 结果将复制到剪贴板</kbd>
  ![Image text](https://raw.githubusercontent.com/organics2016/pojo2json/master/screenshot/file_single.gif)

- <kbd>打开class文件</kbd> > <kbd>移动光标到 Class/Variable/Parameter</kbd> > <kbd>Alt + Insert</kbd> > <kbd>Copy
  JSON</kbd> > <kbd>JSON 结果将复制到剪贴板</kbd>
  ![Image text](https://raw.githubusercontent.com/organics2016/pojo2json/master/screenshot/file_generate.gif)

- <kbd>项目视图选择一个class文件</kbd> > <kbd>右键单击</kbd> > <kbd>Copy JSON</kbd> > <kbd>JSON 结果将复制到剪贴板</kbd>
  ![Image text](https://raw.githubusercontent.com/organics2016/pojo2json/master/screenshot/list_single.gif)

- <kbd>项目视图选择多个class文件</kbd> > <kbd>右键单击</kbd> > <kbd>Copy JSON</kbd> > <kbd>JSON 结果将生成到 Scratches
  文件夹中</kbd>
  ![Image text](https://raw.githubusercontent.com/organics2016/pojo2json/master/screenshot/batch.gif)

## Installation

- **IDEA中安装:**
    - <kbd>Preferences(Settings)</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>搜索<b>"POJO to
      JSON"</b></kbd> > <kbd>Install</kbd>

- **手动安装:**
    - [plugin] -> <kbd>Preferences(Settings)</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from
      disk...</kbd> -> Select the plug-in package and install（No need to unzip）

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

- @JsonProperty and @JSONField

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

- @JsonIgnore or Javadoc tags JsonIgnore

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

- @JsonIgnoreProperties or Javadoc tags JsonIgnoreProperties

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


- @JsonIgnoreType

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

## Configure SpEL expression

### [SpEL expression][spel-url] 能做什么?

- 配置有意义的测试值。
- 字段级别控制转换结果。
- 自定义您想要转换的任何类型。

- <kbd>IntelliJ IDEA</kbd> > <kbd>File</kbd> > <kbd>Settings</kbd> > <kbd>Tools</kbd> > <kbd>POJO to JSON</kbd>
  ![Image text](https://raw.githubusercontent.com/organics2016/pojo2json/master/screenshot/config_spel.gif)

### Configuration

#### Default Configuration

- 这是默认配置。一般情况下不需要修改。

    ```properties
    com.fasterxml.jackson.databind.JsonNode=#{#object.getValue()}
    com.fasterxml.jackson.databind.node.ArrayNode=#{#array.getValue()}
    com.fasterxml.jackson.databind.node.ObjectNode=#{#object.getValue()}
    java.lang.Boolean=#{#boolean.getValue()}
    java.lang.CharSequence=#{#field.getName() + '_' + #shortuuid.getValue()}
    java.lang.Character=#{'c'}
    java.lang.Double=#{#decimal.getValue()}
    java.lang.Float=#{#decimal.getValue()}
    java.lang.Number=#{#integer.getValue()}
    java.math.BigDecimal=#{#decimal.getValue()}
    java.time.LocalDate=#{#localdate.getValue()}
    java.time.LocalDateTime=#{#localdatetime.getValue()}
    java.time.LocalTime=#{#localtime.getValue()}
    java.time.YearMonth=#{#yearmonth.getValue()}
    java.time.ZonedDateTime=#{#zoneddatetime.getValue()}
    java.time.temporal.Temporal=#{#temporal.getValue()}
    java.util.Date=#{#localdatetime.getValue()}
    java.util.UUID=#{#uuid.getValue()}
    ```

#### Random Value Configuration

- 如果你想问之前的随机值JSON去哪儿了？使用如下配置即可达到之前的效果。

    ```properties
    com.fasterxml.jackson.databind.JsonNode=#{#object.getValue()}
    com.fasterxml.jackson.databind.node.ArrayNode=#{#array.getValue()}
    com.fasterxml.jackson.databind.node.ObjectNode=#{#object.getValue()}
    java.lang.Boolean=#{#boolean.getRandomValue()}
    java.lang.CharSequence=#{#field.getName() + '_' + #shortuuid.getValue()}
    java.lang.Character=#{'c'}
    java.lang.Double=#{#decimal.getRandomValue()}
    java.lang.Float=#{#decimal.getRandomValue()}
    java.lang.Number=#{#integer.getRandomValue()}
    java.math.BigDecimal=#{#decimal.getRandomValue()}
    java.time.LocalDate=#{#localdate.getRandomValue()}
    java.time.LocalDateTime=#{#localdatetime.getRandomValue()}
    java.time.LocalTime=#{#localtime.getRandomValue()}
    java.time.YearMonth=#{#yearmonth.getRandomValue()}
    java.time.ZonedDateTime=#{#zoneddatetime.getRandomValue()}
    java.time.temporal.Temporal=#{#temporal.getRandomValue()}
    java.util.Date=#{#localdatetime.getRandomValue()}
    java.util.UUID=#{#uuid.getValue()}
    ```

#### Configuration Details

- 完全符合 [SpEL expression][spel-url] 标准.
- 仅当需要计算或引用时才需要使用表达式. 表达式必须以`#{`开头，以`}`结尾

  ```properties
  # result {"test":"ABCD"}
  com.example.TestClass=ABCD
  ```

  ```properties
  # result {"test":"ABCD_4_732f65b6b9cf"}
  com.example.TestClass=ABCD#{"_" + 2+2 + "_" + #shortuuid.getValue()}
  ```

- 插件有一些内置Value的快捷引用.请注意只有部分引用支持`getRandomValue()`

| Ref              | Expression                     | Result Eg                            | Support getRandomValue() |
|:-----------------|:-------------------------------|:-------------------------------------|:------------------------:|
| `#boolean`       | `#{#boolean.getValue())`       | false                                |                          |
| `#array`         | `#{#array.getValue())`         | []                                   |            N             |
| `#object`        | `#{#object.getValue())`        | {}                                   |            N             |
| `#decimal`       | `#{#decimal.getValue())`       | 0.00                                 |                          |
| `#integer`       | `#{#integer.getValue())`       | 0                                    |                          |
| `#localdatetime` | `#{#localdatetime.getValue())` | 2023-09-14 15:04:52                  |                          |
| `#localdate`     | `#{#localdate.getValue())`     | 2023-09-14                           |                          |
| `#localtime`     | `#{#localtime.getValue())`     | 15:04:52                             |                          |
| `#yearmonth`     | `#{#yearmonth.getValue())`     | 2023-09                              |                          |
| `#temporal`      | `#{#temporal.getValue())`      | 1694675092600                        |                          |
| `#zoneddatetime` | `#{#zoneddatetime.getValue())` | 2023-09-14T15:04:52.601+08:00        |                          |
| `#uuid`          | `#{#uuid.getValue())`          | 679e70fa-d24b-4726-ab87-2de620333f20 |            N             |
| `#shortuuid`     | `#{#shortuuid.getValue())`     | 732f65b6b9cf                         |            N             |

- 自定义 String 类型

  ```properties
  java.lang.CharSequence=#{#field.getName() + '_' + #shortuuid.getValue()}
  ```

  or

  ```properties
  java.lang.String=#{#field.getName() + '_' + #shortuuid.getValue()}
  ```

  `#field`
  引用比较特殊，可以简单理解为Class中的每一个字段.实际上是 [PsiVariable](https://github.com/JetBrains/intellij-community/blob/master/java/java-psi-api/src/com/intellij/psi/PsiVariable.java)
  实例对象.


- 你会发现这个配置有继承关系。所以你可以定义父类的表达式，让子类也可以收益。

  ```properties
  com.example.ParentClass=#{#this.getName() + '_' + #shortuuid.getValue()}
  ```

  ```java
  class SubClass extends ParentClass {
  }
  
  public class Test {
      private SubClass subClass;
  }
  ```

  json result:

  ```json
  {
    "subClass": "subClass_4c672dc197e3"
  }
  ```

- 一些特殊情况, SpEL expressions 无法适配.
    - 您无法在 Java 基础包之外创建自定义类，因为插件无法反射自定义类的实例。

      You can
        ```properties
        java.lang.Number=#{new java.math.BigDecimal(6)}
        ```
      You can't
        ```properties
        java.lang.Number=#{new com.example.BigDecimal(6)}
        ```

    - 测试发现第一种情况在Kotlin中会出现问题。

      Eg
      ```properties
      java.lang.Number=#{#integer.getRandomValue}
      ```
      推荐这样做
      ```properties
      java.lang.Number=#{#integer.getRandomValue()}
      ```

    - 当配置不正确导致插件无法正常运行时，可以清空配置后保存，插件将初始化默认配置。

<!-- Plugin description end -->

## Contributors

想法和部分实现来自
[![](https://avatars.githubusercontent.com/u/12984934?s=28)linsage](https://github.com/linsage)