
# Hire Me Music

Hire Me Music is a sample project that showcases Spotify integration on Android.

This project uses Android development best practices under a MVVM architecture with Co-routines.

(If you think this app is cool, hire me! 😀 )

### Architecture

This project follows the MVVM architecture making use of most of Android Jetpack components and co-routines, to make a clean, easy to understand, and maintainable reactive structure.

##### Layers

In this architecture, the `Fragments` act as the **View layer**, while we use subclasses of the `ViewModel` from Google's architecture components as the **ViewModel layer**.

To deal with data sets, the `Repository` pattern is applied, which controls possible different data sources and maps the entities into domain objects. For now, the only data source is the network.

The app requires Spotify authentication and the user session is stored locally. The token is short lived and as soon as it expires, the app invalidates it from the persistence. 

The API returns the deferred results to the Repository layer, which gets called by the ViewModel and relayed to the View in the form of `ViewStates`. 

The UI renders the ViewStates in a reactive approach, using `LiveData`.

##### Navigation

There is a single activity in the project, called `MainActivity`. 

All the content is delegated to fragments which are configured using the Navigation Library to link between the different screens.

##### Dependency Injection

To keep things testable and maintainable, the project uses Dagger 2 as the dependency injection framework.

This allows the caller to use the callee without having to know how to construct them, separating responsibilities and leaving the code cleaner.

##### Databinding

Databinding is used to make the code simpler.

`Epoxy` makes it easy to use databinding in the lists, while custom adapters do the rest of the work.

### Getting Started

This project uses the Gradle build system. To build this project, use the `gradlew build` command or use "Import Project" in Android Studio.

There are two Gradle tasks for testing the project:

- `connectedAndroidTest` - for running Espresso on a connected device
- `test` - for running unit tests

##### Coding Standards

- All public methods should be documented

- Good methods are small methods

- Meaningful method names only


### Continuous Integration

This project is continuously build and checked for errors using Bitrise.

### Libraries

##### Foundation

- AppCompat
- KTX
- Coroutines
- Navigation
- Lifecycle

##### Reactive

- Live Data

##### Dependency Injection

- Dagger 2

##### UI

- Constraint Layout
- Glide
- Lottie
- Databinding

##### Networking

- Retrofit
- Moshi
- OkHttp

##### Third Party SDKs

- Spotify 

##### Testing

- JUnit
- Mockito
- Mockito-Kotlin
- Espresso
- Hamcrest
- DexOpener

##### Debugging

- Timber

