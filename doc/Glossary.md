# Glossary
There are several vocabularies that you have to know to this development.

# Table Of Content
- [Add On](#add-on)
- [App's Add On Navigation](#apps-add-on-navigation)
- [Base Class](#base-class)
- [Default Add On Class](#default-class-add-on)
- [DynamicDagger Class](#dynamicdagger-class)
- [DynamicImpl Class](#dynamicfeatureimpl-class)
- [Dynamic Feature](#dynamic-feature)
- [EditorInfo](#editorinfo)
- [InputConnection](#inputconnection)
- [Interface](#interface)
    * [ChipGroupCallBack](#chipgroupcallback)
    * [InputPresenter](#inputpresenter)
    * [OnViewMessage](#onviewmessage)
    * [OnViewReady](#onviewready)
- [KeyboardActionView](#keyboard-action-view)
    * [Keyboard Action View Flow](#keyboard-action-view-flow)
- [KeyboardDependency](#keyboard-action-dependency)
- [Keyboard Navigation](#keyboard-navigation)
- [Request Input](#request-input)
    * [Default Request Input](#default-request-input)
    * [Long Text Request Input](#long-text-request-input)
    * [Chip Options](#chip-options)
    * [Date Picker](#date-picker)
    * [Top RecyclerView](#top-recyclerview)
    * [RecyclerView Options](#recyclerview-options)
- [Submenu Add On Navigation](#submenu-add-on-navigation)

## Add On
On this development, `Add On` mean a module package of dynamic feature that fit and work with `Keyboardly` keyboard ecosystem
with guideline that explained on this documentation.

## App's Add On Navigation
App's addon navigation is a way to access installed **add on** like the common app menu.
It can be accessed from add on the menu;

1. if it's installed & configured the navigation, the user will be redirected to a fragment of add on by the navigation graph
2. if not installed or not configured, a detail add-on will appear.


![](https://user-images.githubusercontent.com/13946213/231936933-f9d5cfea-1869-4260-9caa-57deebc704e0.gif)

It's should to be configured by [this](./Addon.md#apps-addon-navigation-configuration)

## Base Class
Base Class means several class that must exist in an add on. Member of base class is :
- Default Class Add On
- DynamicDagger Class
- DynamicFeatureImpl Class
<br>
All the class will explain on below.

## Default Class Add On
`Default Class Add On` mean a class that created as a gateway of Add On will loaded & works.
See this sample of default class : [SampleDefaultClass](/addon/sample/src/main/java/app/keyboardly/sample/SampleDefaultClass.kt)

## DynamicDagger Class
`DynamicDagger Class` mean a class that contain `@Component` & `@Module` dagger class to build default class.
See this sample of default class : [DynamicDagger](/addon/sample/src/main/java/app/keyboardly/sample/di/DynamicDagger.kt)

## DynamicFeatureImpl Class
`DynamicFeatureImpl` a class for implementation to connect the default class with DynamicDagger class
See this sample of default class : [DynamicFeatureImpl](/addon/sample/src/main/java/app/keyboardly/sample/DynamicFeatureImpl.kt)

## Dynamic Feature
Dynamic Feature is base of add on, to get more information see [feature delivery](https://developer.android.com/guide/playcore/feature-delivery)

## EditorInfo
An editorInfo describes several attributes of a text editing object that an input method is communicating with (typically an EditText), most importantly the type of text content it contains and the current cursor position.
See [detail](https://developer.android.com/reference/android/view/inputmethod/EditorInfo).

## Interface
There is several interfaces that used on this development.
### ChipGroupCallBack
Used for handle request chips options.
See [detail](/libraries/actionview/src/main/java/app/keyboardly/lib/helper/ChipGroupCallBack.kt).

### InputPresenter
Interface for handling when input mode activated.
See [detail](/libraries/actionview/src/main/java/app/keyboardly/lib/helper/InputPresenter.kt).

### OnViewMessage
For showing message on options area like default RecyclerView and ChipGroup options.
See [detail](/libraries/actionview/src/main/java/app/keyboardly/lib/helper/OnViewMessage.kt).

### OnViewReady
For showing default options with RecyclerView.
See [detail](/libraries/actionview/src/main/java/app/keyboardly/lib/helper/OnViewReady.kt).

## InputConnection
The InputConnection interface is the communication channel from an InputMethod back to the
application that is receiving its input. It is used to perform such things as reading text
around the cursor, committing text to the text box, and sending raw key events to the application.
See [detail](https://developer.android.com/reference/android/view/inputmethod/InputConnection).

InputConnection also possible to commit an image, if the input app support to receive it.
See [detail](https://developer.android.com/reference/android/view/inputmethod/InputConnection#commitContent(android.view.inputmethod.InputContentInfo,%20int,%20android.os.Bundle)).

## Keyboard Action View
Keyboard Action View is base class for view of add on that will show on keyboard.
The default parameter is `Keyboard Dependency`. This class is where the feature add on started and showed by user.
See detail [KeyboardActionView](/libraries/actionview/src/main/java/app/keyboardly/lib/KeyboardActionView.kt)
<p align="center">
    <img src="assets/actionview-sample.png" width="250" alt="sample action view">
</p>

### Keyboard Action View Flow
A keyboard action view have simple flow that should be understand. See diagram below:
<p align="center">
    <img src="assets/keyboard-action-view-flow.png" width="250">
</p>



## Keyboard Action Dependency
Keyboard Dependency is interface way to communicate with the main keyboard, it just used inside the keyboard.
See full source [KeyboardActionDependency](/libraries/actionview/src/main/java/app/keyboardly/lib/KeyboardActionDependency.kt)

This is detail of the function member:
<table>
    <tr>
        <td>Name</td>
        <td>Return</td>
        <td>Description</td>
    </tr>
    <tr>
        <td>commitText(text: String)</td>
        <td>-</td>
        <td>commit string data to editor outside keyboard.</td>
    </tr>
    <tr>
        <td>getContext()</td>
        <td>Context</td>
        <td>Get context of keyboard service / theme context.</td>
    </tr>
    <tr>
        <td>getEditTextInput()</td>
        <td>EditText?</td>
        <td>Get current editText while on input mode (inside keyboard)<br>
there is two type EditText, default and long,<br>
- default  : for short input and single line<br>
- long     : for long input and multiline
* return null if keyboard not in input mode.</td>
    </tr>
    <tr>
        <td>getCurrentInputConnection()</td>
        <td>InputConnection</td>
        <td>Get current InputConnection where the keyboard is active</td>
    </tr>
    <tr>
        <td>getCurrentEditorInfo()</td>
        <td>EditorInfo</td>
        <td>Get current active EditorInfo of cursor where the keyboard is active</td>
    </tr>
    <tr>
        <td>getKeyboardHeight()</td>
        <td>Int</td>
        <td>height of current keyboard</td>
    </tr>
    <tr>
        <td>isBorderMode()</td>
        <td>Boolean</td>
        <td>for check current keyboard border theme, is with border or not.</td>
    </tr>
    <tr>
        <td>isDarkMode()</td>
        <td>Boolean</td>
        <td>for check current keyboard theme, is dark mode or not.</td>
    </tr>
    <tr>
        <td>loadingOnInput(loading: Boolean)</td>
        <td>-</td>
        <td>loading view while on input mode.</td>
    </tr>
    <tr>
        <td>setTextWatcher(textWatcher: TextWatcher)</td>
        <td>-</td>
        <td>add text watcher to input edittext while on input mode.</td>
    </tr>
    <tr>
        <td>setActionView(view: KeyboardActionView)</td>
        <td>-</td>
        <td>set view on keyboard layout, like add on menu.</td>
    </tr>
    <tr>
        <td>setActionView(view: View?)</td>
        <td>-</td>
        <td>set view on keyboard layout, like add on menu.</td>
    </tr>
    <tr>
        <td>showChipOptions(...)
</td>
        <td>-</td>
        <td>show chip options from keyboard.</td>
    </tr>
    <tr>
        <td>showDatePicker(...)
</td>
        <td>-</td>
        <td>show date picker from keyboard</td>
    </tr>
    <tr>
        <td>showTitleAboveList(...)</td>
        <td>-</td>
        <td>show title and back button above default Recycler view, same position with keyboard navigation.</td>
    </tr>
    <tr>
        <td>loadingMain(loading: Boolean)</td>
        <td>-</td>
        <td>loading view on main keyboard.</td>
    </tr>
    <tr>
        <td>requestInput(...)</td>
        <td>-</td>
        <td>request input text from keyboard inside a KeyboardActionView</td>
    </tr>
    <tr>
        <td>showRecyclerViewOptions(...)</td>
        <td>-</td>
        <td>show recyclerview options</td>
    </tr>
    <tr>
        <td>showFloatingRecyclerView(...)</td>
        <td>-</td>
        <td>show recyclerview options. the position is above keyboard navigation, usually use when on input mode.</td>
    </tr>
    <tr>
        <td>showMessageView(...)</td>
        <td>-</td>
        <td>show message on main keyboard layout, it's relate with showRecyclerViewOptions for example 
if search on the list, then the result is not found the message can be show up here.</td>
    </tr>
    <tr>
        <td>setNavigationCallback(...)</td>
        <td>-</td>
        <td>set callback navigation when navigation keyboard change to be submenu add on.</td>
    </tr>
    <tr>
        <td>setNavigationMenu(...)</td>
        <td>-</td>
        <td> For set list menu to keyboard navigation. for example: when the add on installed & clicked, 
it should do login first before can access all the menu of add on.
</td>
    </tr>
    <tr>
        <td>viewAddOnNavigation()</td>
        <td>-</td>
        <td>view addon's default submenu navigation if exist.</td>
    </tr>
    <tr>
        <td>viewDefaultKeyboard()</td>
        <td>-</td>
        <td>reset view to default keyboard view</td>
    </tr>
    <tr>
        <td>viewKeyboardNavigation()</td>
        <td>-</td>
        <td>view default keyboard navigation</td>
    </tr>
    <tr>
        <td>viewLayoutAction()</td>
        <td>-</td>
        <td>move back to current view KeyboardActionView. Usually this function will called after call requestInput() function or something else</td>
    </tr>
    <tr>
        <td>showDialog()</td>
        <td>-</td>
        <td>function to show an alert dialog from keyboard</td>
    </tr>
    <tr>
        <td>hideTopView()</td>
        <td>-</td>
        <td>function to hide view above keyboard, like</td>
    </tr>
    <tr>
        <td>setTopActionView(view: KeyboardActionView)</td>
        <td>-</td>
        <td>set top view on keyboard layout, like add on menu.</td>
    </tr>
    <tr>
        <td>setTopActionView(view: View?)</td>
        <td>-</td>
        <td>set top view on keyboard layout, like add on menu.</td>
    </tr>
</table>

## Keyboard Navigation
Keyboard navigation is list menu that appear on top of keyboard.
The default navigation is available when user click the keyboard logo.
<p align="center">
    <img src="assets/keyboard-navigation.png" width="250">
</p>
If new add on installed, the logo will appear on this list keyboard navigation.
<p align="center">
    <img src="assets/addon-installed.png" width="250">
</p>

## Request Input
`Request Input` mean an activity inside `KeyboardActionView` to request input value like text and can be configured with options view like chip group, date picker, recyclerview, top recyclerview.

### Default Request Input
The default request input is for short text that fit on single line. <br>
Triggered via [KeyboardActionDependency](#keyboard-action-dependency) > `requestInput()`
<p align="center">
    <img src="assets/request-input-text.png" width="250" alt="sample request input text">
</p>
<br> See sample on [this](/addon/sample/src/main/java/app/keyboardly/sample/action/register/RegisterActionView.kt#L35-43)

### Long Text Request Input
The long request input is for long text but will appeared with scrollable 2 line inside keyboard. <br>
Triggered via [KeyboardActionDependency](#keyboard-action-dependency) > `requestInput()` with value `longInput` = `true`
<p align="center">
    <img src="assets/longtext-sample.png" width="250" alt="sample request input text">
</p>
<br> See sample on [this](/addon/sample/src/main/java/app/keyboardly/sample/action/register/RegisterActionView.kt#L45)

### Chip Options
Chip options is view `ChipGroup` that can be configured via [KeyboardActionDependency](#keyboard-action-dependency) > `showChipOptions()`
<p align="center">
    <img src="assets/chip-options.png" width="250" alt="sample chip options">
</p>
<br> See sample on [this](/addon/sample/src/main/java/app/keyboardly/sample/action/register/RegisterActionView.kt#L52-59)

### Date Picker
Just like `Chip Options`, a KeyboardActionView can request input date via [KeyboardActionDependency](#keyboard-action-dependency) > `showDatePicker()`
<p align="center">
    <img src="assets/datepicker-sample.png" width="250" alt="sample date picker">
</p>
<br> See sample on [this](/addon/sample/src/main/java/app/keyboardly/sample/action/register/RegisterActionView.kt#L49)

### Top RecyclerView
A recyclerview options that show on top keyboard navigation, usually used when on input mode for quick search.
<p align="center">
    <img src="assets/floating-recyclerview.png" width="250">
</p>
<br>
To use it, triggered it from [KeyboardDependency](#keyboard-action-dependency) > `showTopRecyclerView(...)` and combine it with `requestInput(...)` function.
<br> See sample on [this](/addon/sample/src/main/java/app/keyboardly/sample/action/campaign/CampaignActionView.kt#L109)

### RecyclerView Options
Recyclerview options is that appeared on keyboard area and can can be modify the layout manager as needed.
<p align="center">
    <img src="assets/recyclerview-options-title.png" width="250">
</p>
<br>
To use it, triggered it from [KeyboardDependency](#keyboard-action-dependency) > `showTopRecyclerView(...)` and can be configured with title by `showTitleAboveList()`
<br> See sample on [this](/addon/sample/src/main/java/app/keyboardly/sample/action/shopping/ShoppingActionView.kt#L61)

## Submenu Add On Navigation
This submenu is list of [NavigationMenuModel](/libraries/actionview/src/main/java/app/keyboardly/lib/navigation/NavigationMenuModel.kt),
if you decide to create an add-on without a submenu it can be an empty list (not null). <br>
If an add on contain submenu navigation and then if user click the add on logo, the default keyboard navigation will be replaced with the submenus.<br>
<br>
The list will be called on `DynamicFeatureImpl` class through [override method](/addon/sample/src/main/java/app/keyboardly/sample/DynamicFeatureImpl.kt#L57-59).
The add on should handle the submenu click event on default class.
<p align="center">
    <img src="assets/submenu-navigation.png" width="250">
</p>
<br>
On production version, after the user installs an **Add On**, an icon will appear automatically on the keyboard's navigation menu.
If the user clicks the icon, the keyboard will do the validation :

1. if an **Add On** contain a list submenu (not empty), the sub menu will appear on top of the keyboard.
2. if not contained a list submenu, the keyboard will call `getTopView()` method.
2. if topView return null, then keyboard will call `getView()` method.

![](https://user-images.githubusercontent.com/13946213/231961760-c8dceeb4-eb16-4b27-bc9f-b528d1a96856.gif)


On development version, the download process skipped. And the add on package is just included when compile process.
To make it ready to use on keyboard, add the menu to [this navigation list](../app/src/main/java/app/keyboardly/dev/keyboard/keypad/keyboardaction/KeyboardNavigation.kt#L204-228).
This below data should match with the add on when creating dynamic feature module.

![](https://user-images.githubusercontent.com/13946213/231962118-89293765-bd8f-48fc-82d6-22ec7b97ce27.gif)
