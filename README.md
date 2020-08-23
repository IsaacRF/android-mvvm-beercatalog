# Android Beer Catalog (MVVM App)

**Author**: [IsaacRF239](https://isaacrf.com/about)

**Minimum SDK**: API 20

**Target SDK**: API 29

Android MVVM app built in modern architecture, that shows a beer catalog and allows user to interact with it changing beer availability.

![beercatalog_tablet_demo](https://user-images.githubusercontent.com/2803925/90969086-a3d4b200-e4f4-11ea-8597-3663dc08fb7a.gif)
![beercatalog_phone_demo](https://user-images.githubusercontent.com/2803925/90969087-a46d4880-e4f4-11ea-9cde-a14af8307d2e.gif)

## Main Features

### Adaptable layout
App is displayed in two pane mode on tablets, and master-detail navigation in phones, making the most of screen space. Navigation is made via Android's [Navigation Component](https://developer.android.com/guide/navigation/navigation-getting-started) using [NavGraph](https://developer.android.com/guide/navigation/navigation-getting-started#create-nav-graph) alongside two fragments (one for master and another one for detail). Fragments allow to easily configure layout distribution and navigation depending on screen size, and NavGraph allows to configure navigation transitions and required info in one place.

![navgraph](https://user-images.githubusercontent.com/2803925/90969152-a4217d00-e4f5-11ea-860d-b0a447bef2ee.png)

### MVVM Architecture
This project uses [MVVM](https://developer.android.com/jetpack/docs/guide) (Model - View - Viewmodel) architecture, via new [Jetpack ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) feature.

![image](https://user-images.githubusercontent.com/2803925/87967886-d3d90180-cabf-11ea-86fc-47e19eb460e7.png)

### Development Patterns
This project implements "By feature + layout" structure, [Separation of Concerns](https://en.wikipedia.org/wiki/Separation_of_concerns) pattern and [SOLID](https://en.wikipedia.org/wiki/SOLID) and Clean principles.

#### By feature + layout structure
Project architecture combines the "By feature" structure, consisting on separating all files concerning to a specific feature (for example, an app screen / section) on its own package, plus the "By layout" classic structure, separating all files serving a similar purpose on its own sub-package.

By feature structure complies with the Separation of Concerns and encapsulation patterns, also making the app highly scalable, modular and way easier to manipulate, as deleting or adding features impact only app base layer and refactor is minimum to non-existent.

![folder-structure](https://user-images.githubusercontent.com/2803925/90969290-2494ad80-e4f7-11ea-853e-df2c64148643.png)

Testing project replicates the same feature structure to ease test running separation

![folder-structure-test](https://user-images.githubusercontent.com/2803925/90969291-2494ad80-e4f7-11ea-8f6a-cbb168085a7f.png)

#### LiveData / Observable Pattern
Data is handled via [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) / Observable Pattern instead of RxJava, as it's better performant and includes a series of benefits as, for example, avoiding manual app lifecycle management. Information retrieved from network is also stored on Room persistent DB to allow product availability manipulation and data cache.

***BeerListViewModel***
```Kotlin
val beerList: LiveData<NetworkResource<List<Beer>>> = beerListRepository.getBeers()
```

***BeerListFragment***
```Kotlin
//Observe live data changes and update UI accordingly
beerListViewModel.beerList.observe(this) {
    when(it.status) {
        Status.LOADING -> {}
        Status.SUCCESS -> {}
        Status.ERROR -> {}
    }
}
```

#### Dependency Injection
Project implements Dependency Injection (SOLI**D**) to isolate modules, avoid inter-dependencies and make testing easier

Dependency Injection is handled via [Hilt](https://developer.android.com/training/dependency-injection/hilt-android), a library that uses Dagger under the hood easing its implementation via @ annotations, and is developed and recommended to use by Google.

ViewModel is retrieved from activity to share it between master and detail fragments, allowing detail to modify data and reflect it on master list. The Module located on di package is in charge of defining implementations for everything that is injected in the app.

***GithubNdApp (App main class)***
```Kotlin
@HiltAndroidApp
class AndroidBaseApp : Application() {}
```

***MainActivity***
```Kotlin
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {}
```

***BeerListFragment***
```Kotlin
class BeerListFragment : Fragment() {
    private val beerListViewModel: BeerListViewModel by activityViewModels()
}
```

***BeerListViewModel***
```Kotlin
class BeerListViewModel @ViewModelInject constructor (
    private val beerListRepository: BeerListRepository,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {}
```

### API Calls
API Calls are handled via [retrofit](https://square.github.io/retrofit/), declaring calls via an interface, and automatically deserialized by [Gson](https://github.com/google/gson) into model objects.

***BeerListService***
```Kotlin
@GET("beers")
fun getBeers(
    @Query("page") page: Int,
    @Query("per_page") perPage: Int
): Call<List<Beer>>
```

### Image rendering and caching
Beer images are rendered and cached using [EpicBitmapRenderer](https://github.com/IsaacRF/EpicBitmapRenderer), a Java Android library I also developed.

![EpicBitmapRenderer Icon](http://isaacrf.com/libs/epicbitmaprenderer/images/EpicBitmapRenderer-Icon.png)

### Testing
All business logic, services and database interactions are tested 

Every test is isolated, and all API calls are mocked using [Mockito](https://site.mockito.org/) and okhttp [Mockwebserver](https://github.com/square/okhttp/tree/master/mockwebserver) to avoid test results to depend on external sources, becoming unrealiable and possibly leading to unexpected results.

Database is tested via a instrumentation test because best testing practices require a real device (or emulator) to test database interactions using Room in-memory database builder.
