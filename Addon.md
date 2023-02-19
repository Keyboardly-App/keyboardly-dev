# About

Add On is smart way to make your android keyboard more powerful.<br>
This repository is source code just used for development & testing purpose.

All live add on are listed on [marketplace](https://keyboardly.app/addons-marketplace/).

# Table of contents
- [About](#about)
- [Table of contents](#table-of-contents)
- [Development](#development)
    * [Tools](#tools)
    * [Create Module](#create-module)
    * [Setup Dependency](#setup-dependency)
    * [Setup Base Class](#setup-base-class)
    * [Create The Feature](#create-feature-by-keyboard-action-view)
    * [Resource File Rules](#resource-file-rules)
    * [Styling](#styling)
    * [Load Add On](#load-add-on)
    * [Add On Submenu](#add-on-submenu)
    * [App's addon Menu Configuration](#apps-addon-menu-configuration)
    * [Proguard rules](#proguard-rules)
    * [Testing](#testing)
      + [Indicator of success](#indicator-success-launched-of-add-on)

# Development

See [this module](/addon/sample) for full sample add on.

## Tools

Requires tools & config for development:
<table>
    <tr>
        <td>Name</td>
        <td>Version</td>
    </tr>
    <tr>
        <td>Android Studio</td>
        <td>2021.3.1 (min) / latest version</td>
    </tr>
    <tr>
        <td>Gradle</td>
        <td>7.4</td>
    </tr>
    <tr>
        <td>Android Gradle Plugin</td>
        <td>7.3.1</td>
    </tr>
    <tr>
        <td>Kotlin plugin</td>
        <td>1.7.20</td>
    </tr>
    <tr>
        <td>compileSdk</td>
        <td>33</td>
    </tr>
    <tr>
        <td>minSdkVersion</td>
        <td>21</td>
    </tr>
    <tr>
        <td>targetSdkVersion</td>
        <td>33</td>
    </tr>
</table>

## Create Module

To create an add on, start by create a dynamic feature module:

> File > New > New Module > Choose **Dynamic Feature** > next

On this dialog fill title and chose **on-demand only**

<p align="center">
    <img src="image/addon-create-dialog.png" >
</p>

## Setup Dependency

After creating a dynamic feature module, update gradle dependencies:

```groovy
plugins {
    id 'com.android.dynamic-feature'
    id 'org.jetbrains.kotlin.android'
    id 'kotlin-kapt'
}

android{
    ...

    kapt {
        generateStubs = true
    }

    buildFeatures {
        viewBinding true
    }
    ...
}

dependencies {
    implementation project(":libraries:style")
    implementation project(":libraries:actionview")

    implementation 'androidx.databinding:viewbinding:7.4.1'

    kapt "com.google.dagger:dagger-compiler:$dagger_version"
    implementation "com.google.dagger:dagger:$dagger_version"
    implementation "com.google.dagger:dagger-android-support:$dagger_version"
}
```

## Setup Base Class
After setup dependencies, We need to create some kotlin class with requirements:
1. A default class
    - inherits `KeyboardActionView`
    - located in the root module
    - see example : [SampleDefaultView](/addon/sample/src/main/java/app/keyboardly/sample/SampleDefaultView.kt).
> An add on can configured with empty submenus and with a default view, or with some submenus without default view.
> If an add on not contain a default view or submenus, the add on will doesn't work.
 
2. DynamicDagger class
    - contain some component class, interface and module
    - should fit with the default class to make it work
    - see example : [DynamicDagger](/addon/sample/src/main/java/app/keyboardly/sample/di/DynamicDagger.kt).

3. DynamicFeatureImpl.kt
    - should with name `DynamicFeatureImpl`
    - located in the root module
    - should inherit `DynamicFeature`
    - have a constructor with default class that inherits `KeyboardActionView` 
    - full code see [DynamicFeatureImpl](/addon/sample/src/main/java/app/keyboardly/sample/DynamicFeatureImpl.kt).
note:
> On DynamicFeatureImpl class, there is 2 override methods:
<br> - `getView()`  : will be used for return view.
<br> - `getSubMenus()`  : for return submenus to show on keyboard navigation.<br>

4. Start build your own feature by `KeyboardActionView` class. 

## Create Feature by Keyboard Action View
After setup all the base class, start develop the feature.
1. Create a new kotlin class with suffix name `ActionView`, for example `ProfileActionView`
2. Inherit `KeyboardActionView` and make default constructor with `KeyboardActionDependency` 
```kotlin

import app.keyboardly.lib.KeyboardActionDependency
import app.keyboardly.lib.KeyboardActionView


class ProfileActionView(
    dependency: KeyboardActionDependency
) : KeyboardActionView(dependency) {

}
```
3. Implement the function member `onCreate`
```kotlin
   override fun onCreate() {

   }
```
4. Create xml layout like common layout for fragment or activity. Please take a look the [resource file rule](#resource-file-rules) and [styling](#styling).<br>
And here is some note for layout xml of action view:<br>
   - must contain back button for navigation back to add on / keyboard navigation, because back press (on physic device) will detected from launcher system and hide the keyboard itself.
   - the parent layout height should `MATCH_PARENT` with minimum height total about 250 dp, if not the view will look hanging & not fulfill the keyboard 
 
6. Define the view binding and initiate the `viewLayout` variable
```kotlin
    override fun onCreate() {
        val binding = ProfileLayoutBinding.inflate(getLayoutInflater())
        viewLayout = binding.root 
    }
```
6. Give some logic as needed like back navigation.
```kotlin
    binding.apply {
        back.setOnClickListener {
            dependency.viewAddOnNavigation()
        }
    }
```
7. Done. Next step see [testing](#testing)


## Resource File Rules
To prevent risk of conflict when compiling/merging with main source code, there is some rules for naming of resource file.
- Resource file here mean only file under resource folder `drawable` and `layout`.
- The file name should start with `add on name id`. 
- For example an add on with id `sample`, all the file should be like : `sample_login_activity.xml`, `sample_icon_addon.png`.

## Styling
To make your add on fit with keyboard theme, there is two way:
1. Use default theme on library/style.
- check the attribution member on this file [attrs.xml](/libraries/style/src/main/res/values/attrs.xml)
- sample :
```xml
      <EditText
          android:id="@+id/name"
          style="?textActionKeyboardStyle"
          android:layout_width="match_parent"
          android:layout_height="wrap_content"
          android:layout_margin="10dp"
          android:focusable="false"
          android:hint="Name"
          android:inputType="text" />
```
2. Make your own theme and validate through KeyboardDependency. On keyboard dependency there is method
- `isDarkMode()` : for validate the keyboard theme is dark or bright
- `isBorderMode()` : for validate the keyboard button style, with border or not
- sample :
```kotlin

  val style = if(dependency.isDarkMode()) {
      if (dependency.isBorderMode()){
          R.style.YourStyle_Dark_Border
      } else {
          R.style.YourStyle_Dark
      }
  } else {
      if (dependency.isBorderMode()){
          R.style.YourStyle_Border
      } else {
          R.style.YourStyle
      }
  }
  val mThemeContext = ContextThemeWrapper(context, style)
  val inflater = LayoutInflater.from(mThemeContext)
  viewLayout = inflater.inflate(R.layout.home_layout, null)

```

## Add On Submenu

This submenu is list of [NavigationMenuModel](/libraries/actionview/src/main/java/app/keyboardly/lib/navigation/NavigationMenuModel.kt),
if you decide to create an add-on without a submenu it can be an empty list (not null).

The list will be called on `DynamicFeatureImpl` class through [override method](/addon/sample/src/main/java/app/keyboardly/sample/DynamicFeatureImpl.kt#L57-59).

## Load Add On

On production version, after the user installs an **Add On**, an icon will appear automatically on the keyboard's navigation menu.
If the user clicks the icon, the keyboard will do the validation :

1. if an **Add On** contain a list submenu (not empty), the sub menu will appear on top of the keyboard.
2. if not contained a list submenu, the keyboard will call `getView()` method.
<br>
<img src="image/keyboard-submenu-addon-menu.webm" width="250"/>
<br>

For development purpose, we will skip the download process. Let's say the add on downloaded and ready to use.
To make it ready to use on keyboard, add the menu to [this navigation list](app/src/main/java/app/keyboardly/dev/keyboard/keypad/keyboardaction/KeyboardNavigation.kt#L204-228).
This below data should match with the add on when creating dynamic feature module.
> featurePackageId = "app.keyboardly.sample"
> featureNameId = "sample"
> nameString = "Sample"
<br>
<img src="image/submenu-addon-dev.webm" width="250"/>
<br>

## App's Addon Menu Configuration
To make app's add on menu, follow this way:

1. include a dynamic navigation graph of the add-on to the default navigation graph. for sample:

```xml
<include-dynamic
    android:id="@+id/sample_default_nav"
    app:moduleName="sample"
    app:graphResName="sample_default_nav"
    app:graphPackage="${applicationId}.sample"/>
```

2. set the root navigation id of add-on same with id on include-dynamic.

```xml
<?xml version="1.0" encoding="utf-8"?>
<navigation xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto" 
    android:id="@id/sample_default_nav"
    app:startDestination="@id/default_nav">
    <!--the id above without plus (@id/...) -->

    <!-- startDestination fragment -->
    <fragment
        android:id="@+id/default_nav"
        android:name="app.keyboardly.sample.app.SampleFragment"
        android:label="Sample Fragment"
        tools:layout="@layout/fragment_sample" />
</navigation>
```

3. insert the add on data to [listAddOn](/app/src/main/java/app/keyboardly/dev/ui/addon/AddOnViewModel.kt#L15-34). Make sure the data match with the add on module configuration.
4. save the id of included dynamic navigation graph to [listNavigation](/app/src/main/java/app/keyboardly/dev/ui/addon/AddOnFragment.kt#L69-77) on AddOnFragment.

done.


## Proguard rules
On the main source code app, the proguard / minify enabled.
- Make sure the add-on has consumer-rules.pro to prevent error / failed load of the add-on.
- Be patient to keep some important
```proguard
# keep class data
-keep class app.keyboardly.sample.** { *; }
-keep class app.keyboardly.sample.di.** { *; }

-dontwarn com.google.errorprone.annotations.**

# keep the resource / raw file
-keep class *.R
-keep class dagger.* { *; }

-keepclasseswithmembers class **.R$* {
    public static <fields>;
}

```
- Don't forget to keep the model data class if exist.

see full sample [consumer-rules.pro](/addon/sample/consumer-rules.pro).


## Testing

1. Don't forget insert data `add on` to the [list navigation](app/src/main/java/app/keyboardly/dev/keyboard/keypad/keyboardaction/KeyboardNavigation.kt#L204-228) as mentioned on [this](#load-add-on)
2. And for navigation app's add on (if exist) in [this list](/app/src/main/java/app/keyboardly/dev/ui/addon/AddOnViewModel.kt#L15-34) and [list navigation id](/app/src/main/java/app/keyboardly/dev/ui/addon/AddOnFragment.kt#L69-77) 
3. Open `Run > Edit Configuration..` and make sure the dynamic module checked on `installation-option` section :

<p align="center">
    <img src="image/install-option.png" >
</p>

3. Run the code  / `Shift+F10`
4. When the app launches, open the add-on menu (app/keyboard) and do install the add-on.

### Indicator success launched of add-on:

- On the navigation keyboard, if add on clicked `->` success shows submenu/view from add on. And the `add on` works functionally.
- On the App menu, when opening the add-on menu and then clicking the `add on`, it should load the fragment from the add-on (if exist).
