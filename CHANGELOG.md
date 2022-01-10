# POJO to JSON Changelog

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