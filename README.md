[![Maven Central](https://img.shields.io/maven-central/v/com.picsart/stateful.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.picsart%22%20AND%20a:%22stateful%22)
# Stateful
Stateful is a Kotlin library which makes Android application development faster and easier. It helps you delete all the boilerplate code for saving instance state and lets you forget about saving and restoring your fragment's/activity's state.

# Download
Gradle:
```groovy
implementation 'com.picsart:stateful:1.2.1'
```
# Using Stateful
The most activities look like the following example.
```kotlin
class MySuperCoolActivity : Activity() {
    private var importantNumber: Int = 3
    private var importantNullableNumber: Int? = null
    private var importantFlag: Boolean = false
    ///and many other super important properties


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            importantNumber = savedInstanceState.getInt("important property key")
            importantFlag = savedInstanceState.getBoolean("important flag key")
            if (savedInstanceState.containsKey("important nullable property key")) {
                importantNullableNumber = savedInstanceState.getInt("important nullable property key")            
            }
            //....
        }
        //some important logic...
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("important property key", importantNumber)
        outState.putBoolean("important flag key", importantFlag)
        importantNullableNumber?.let {
            outState.putInt("important nullable property key", it)
        }
        //....
    }

    //some more code..
}
```

What if I say that you can remove all boilerplate code for saving state with the help of stateful?

```kotlin
class MySuperCoolActivity : Activity(), Stateful by state() {
    private var importantNumber: Int by statefulProperty(3)
    private var importantNullableNumber: Int? by statefulNullableProperty<Int>(null)
    private var importantFlag: Boolean by statefulProperty(false)
    ///and many more super important properties


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        restore(savedInstanceState)
        //some important logic...
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        save(outState)
    }

    //some more code..
}
```

The default implementation is using property names as a key to put them in the bundle. If you want to implement your own logic for a key generation you can extend *Stateful* interface and use your own implementation.
