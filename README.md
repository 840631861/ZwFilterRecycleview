### ZwFilterRecycleview
### 使用
**Step1：添加依赖**

Gradle
```xml
allprojects {
  repositories {
    ...
    maven { url 'https://www.jitpack.io' }
  }
}
```
```xml
dependencies {
        implementation 'com.github.840631861:ZwFilterRecycleview:v0.1.6'
}
```
Maven
```xml
<repositories>
  <repository>
      <id>jitpack.io</id>
      <url>https://www.jitpack.io</url>
  </repository>
</repositories>
```
```xml
<dependency>
    <groupId>com.github.840631861</groupId>
    <artifactId>ZwFilterRecycleview</artifactId>
    <version>v0.1.6</version>
</dependency>
```
### 附
minSdkVersion 19  targetSdkVersion 27
