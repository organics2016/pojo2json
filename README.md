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

A simple plugin for converting POJO to JSON in IntelliJ IDEA

- Support any type you can think of in Java.
- Support Java17 and last
- Support Java14 Records [JEP-359](https://openjdk.java.net/jeps/359)
- Partial support Jackson and Fastjson annotations.
- Support conversion
    - Inner Class
    - Global Variable
    - Local Variable
    - Constructor Parameter
    - Method Parameter

## Support JVM platform languages

- Java - full support
- Kotlin - full support

## Usage

- Note that the position of the cursor can affect the result!
- <kbd>Open class file</kbd> > <kbd>Move cursor to Class/Variable/Parameter</kbd> > <kbd>Right click</kbd> > <kbd>Copy
  JSON</kbd> > <kbd>JSON result will copy to clipboard</kbd>
  ![Image text](https://raw.githubusercontent.com/organics2016/pojo2json/master/screenshot/file_single.gif)

- <kbd>Open class file</kbd> > <kbd>Move cursor to Class/Variable/Parameter</kbd> > <kbd>Alt + Insert</kbd> > <kbd>Copy
  JSON</kbd> > <kbd>JSON result will copy to clipboard</kbd>
  ![Image text](https://raw.githubusercontent.com/organics2016/pojo2json/master/screenshot/file_generate.gif)

- <kbd>Project view select a class file</kbd> > <kbd>Right click</kbd> > <kbd>Copy JSON</kbd> > <kbd>JSON result will
  copy to clipboard</kbd>
  ![Image text](https://raw.githubusercontent.com/organics2016/pojo2json/master/screenshot/list_single.gif)

- <kbd>Project view select multiple files</kbd> > <kbd>Right click</kbd> > <kbd>Copy JSON</kbd> > <kbd>JSON result will
  generate to files in the Scratches folder</kbd>
  ![Image text](https://raw.githubusercontent.com/organics2016/pojo2json/master/screenshot/batch.gif)

## Installation

- **Install in IDEA:**
    - <kbd>Preferences(Settings)</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search<b>"POJO to
      JSON"</b></kbd> > <kbd>Install</kbd>

- **Manual Install:**
    - [plugin] -> <kbd>Preferences(Settings)</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from
      disk...</kbd> -> Select the plugin package and install（No need to unzip）

## Q&A

- Why always report errors when use it?

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
                "......_501":{}
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

### What can [SpEL expression][spel-url] do?

- Configure meaningful test values.
- Field-level control of convert results.
- Customize any type you want to convert.

- <kbd>IntelliJ IDEA</kbd> > <kbd>File</kbd> > <kbd>Settings</kbd> > <kbd>Tools</kbd> > <kbd>POJO to JSON</kbd>
  ![Image text](https://raw.githubusercontent.com/organics2016/pojo2json/master/screenshot/config_spel.gif)

### Configuration

#### Default Configuration

- This is a default configuration. Normally no modification is required.

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

- If you want to ask where did the previous random value JSON? Use the following configuration to achieve the previous
  effect.

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

- Full compliance with [SpEL expression][spel-url] standards.
- Expressions need to be used only when operations or references are required. The expression must start with `#{` and
  end with `}`

  ```properties
  # result {"test":"ABCD"}
  com.example.TestClass=ABCD
  ```

  ```properties
  # result {"test":"ABCD_4_732f65b6b9cf"}
  com.example.TestClass=ABCD#{"_" + 2+2 + "_" + #shortuuid.getValue()}
  ```

- The plugin has some built-in Value shortcut references.Note that only some of these references
  support `getRandomValue()`

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

- Custom String type

  ```properties
  java.lang.CharSequence=#{#field.getName() + '_' + #shortuuid.getValue()}
  ```

  or

  ```properties
  java.lang.String=#{#field.getName() + '_' + #shortuuid.getValue()}
  ```

  The `#field` reference is quite special and can be simply understood as each field in the Class.It actually points to
  a [PsiVariable](https://github.com/JetBrains/intellij-community/blob/master/java/java-psi-api/src/com/intellij/psi/PsiVariable.java)
  instance.


- You will find that this configuration has an extends relationship.So you can define the expression of the parent
  class,
  and the sub-class will also benefit.

  ```properties
  com.example.ParentClass=#{#field.getName() + '_' + #shortuuid.getValue()}
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

- Some special cases, SpEL expressions cannot be adapted.
    - You cannot create custom classes outside of Java Base package because plugins cannot reflect instances of custom
      classes.

      You can
        ```properties
        java.lang.Number=#{new java.math.BigDecimal(6)}
        ```
      You can't
        ```properties
        java.lang.Number=#{new com.example.BigDecimal(6)}
        ```

    - The test found that the first situation will cause problems in Kotlin.

      Eg
      ```properties
      java.lang.Number=#{#integer.getRandomValue}
      ```
      But recommended this
      ```properties
      java.lang.Number=#{#integer.getRandomValue()}
      ```

    - When incorrect configuration causes the plugin to fail to run properly, you can clear the configuration and save
      it, and the plugin will initialize the default configuration.

<!-- Plugin description end -->

## Contributors

Ideas and partial realization from
[![](https://avatars.githubusercontent.com/u/12984934?s=28)linsage](https://github.com/linsage)