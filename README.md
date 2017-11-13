![PianoView](./img/piano.jpg)

# PianoView

A custom view on Android,which can help you easily to create a piano on Android.

## Features
- Beautiful UI
- Good flexibility,which can use in different devices and layout.
- Two interface,which can help user to use this view better.
- Mutil-Touch.
i
## Gradle Dependency

Add it in your root `build.gradle` at the end of repositories:

```gradle
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```
Add the dependency:

```gradle
dependencies {
        compile 'com.github.ParadiseHell:PianoView:1.0.4'
}
```

## How to use

In the `XML` layout:

```xml
<com.chengtao.pianoview.view.PianoView
    android:id="@+id/pv"
    android:layout_width="match_parent"
    android:layout_height="match_parent"/>
```

For more reference,plaese see the [sample](./sample).

## License

    Copyright 2016 ChengTao

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
