# POJO to JSON Changelog

## [2.1.1]
### Compatible
- Compatible with Idea 2024.3

## [2.1.0]
### Fixed
- Fix https://github.com/organics2016/pojo2json/issues/40
### Changed
- Update gradle-intellij-plugin to 2.0.1
- IDEA minimum supported version 2023.3

## [2.0.6]
### Compatible
- Compatible with Idea 2024.1

## [2.0.5]
### Add
- Add custom date format configuration
- Add custom number configuration
### Changed
- Changed Default Configuration

## [2.0.4]
### Fixed
- Fix bugs caused by some collection types.
### Remove
- Build for idea 233 Remove commons.lang3

## [2.0.3]
### Added
- [Support JSON Keys Format Configuration](https://github.com/organics2016/pojo2json#json-keys-format-configuration)
### Fixed
- Fix listNonGeneric SOF
### Changed
- IDEA minimum supported version 2022.3
- Change recursion level to 200

## [2.0.2]
### Added
- [Integrated SpEL expression](https://github.com/organics2016/pojo2json#configure-spel-expression)
- [Add new interactions](https://github.com/organics2016/pojo2json/issues/25)
### Fixed
- Fix NPE problem caused when generics are not defined https://github.com/organics2016/pojo2json/issues/28
- Fix plugin.xml Exception

## [2.0.1]
### Added
- [Integrated SpEL expression](https://github.com/organics2016/pojo2json#configure-spel-expression)
- [Add new interactions](https://github.com/organics2016/pojo2json/issues/25)
### Fixed
- Fix NPE problem caused when generics are not defined https://github.com/organics2016/pojo2json/issues/28

## [1.2.6]
### Fixed
- Fix stack overflow errors

## [1.2.5]
### Changed
- IDEA minimum supported version 2020.3
- Update String type random value

## [1.2.4]
### Added
- Support conversion
  - Inner Class
  - Global Variable
  - Local Variable
  - Constructor Parameter
  - Method Parameter
### Changed
- Update org.jetbrains.intellij 1.7.0
- IDEA minimum supported version 2021.3

## [1.2.2]
### Changed
- Optimize menu operation.

## [1.2.1]
### Added
- Add batch operation.
- Add project view list operation.
### Security
- Controller refactor
### Remove
- Remove window tool

## [1.2.0]
### Changed
- Optimize Enum random values
### Security
- Controller refactor

## [1.1.8]
### Changed
- Only the class where the cursor is located is parsed. If the cursor is not in any class, the first class in the file is parsed.

## [1.1.7]
### Fixed
- When @JsonProperty value is no defined fallback to the field name.

## [1.1.6]
### Added
- Support Java14 Records
### Changed
- Remove supporting scala
- Remove supporting Kotlin Object-Declaration
### Security
- Remove invalid depends

## [1.1.5]
### Added
- Support java.util.UUID

## [1.1.4]
### Added
- Support Javadoc tags JsonIgnore
### Changed
- Not serialize static members,This includes Companion Object in kotlin.
  Static members are associated with the class, not the instances.

## [1.1.3]
### Fixed
- Fixed where the Kotlin object cannot be parsed correctly.
### Changed
- Only show the menu in supporting files.
- No need to move the cursor to the class scope, now the cursor can be anywhere in the file.  
- Load optional plugin dependencies according to different environments.

## [1.1.2]
### Added
- Support java.time.YearMonth
- Support Kotlin (beta)
- Support Scala (beta)

## [1.1.1]
### Fixed
- Partial collection parsing error<br>
  Now,think of (ArrayList,LinkedList,Set,HashSet,LinkedHashSet) as collection.
### Added
- Support FakeZonedDateTime
- Add Java Platform test case

## [1.1.0]
### Security
- Update Gradle build process
### Added
- Support @JsonIgnore
- Support @JsonIgnoreProperties
- Support @JsonIgnoreType

## [1.0.11]
### Added
- Support Jackson and Fastjson annotations.

## [1.0.10]
### Added
- Add a new option to the menu.
- The resulting JSON will have some random values.

## [1.0.9]
### Security
- Compatible with IDEA version 2020.3 and later no longer supports previous versions.

## [1.0.8]
### Fixed
- Fix incompatible dependencies.

## [1.0.7]
### Changed
- All floating-point types retain two decimal places.

## [1.0.6]
### Fixed
- Fix loop nesting caused stack overflow.

## [1.0.5]
### Security
- Fix idea 192.* compatibility problems.

## [1.0.4]
### Changed
- Support enum constant.

## [1.0.3]
### Changed
- Think of enum as a string.
- Special time type processing optimization.

## [1.0.2]
### Fixed
- Fix Parsing enum times wrong and support Java8 time type.

## [1.0.1]
### Fixed
- Fix dependence bug.

## [1.0.0]
### Added
- First version.