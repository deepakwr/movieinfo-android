# movieinfo-android

Application Name : Movie Info

Built on Android Studio 3.5
- Minimum SDK - 19 - (KitKat)
- Target SDK - 28 - (Pie)
- Language - Kotlin
- Third Party Libraries Used - Glide : https://github.com/bumptech/glide

Demo:

<img src="example/movieinfo.gif" width="220px">

Features :

- Uses http://www.omdbapi.com/ API for receiving list of content and displays it on a RecyclerView.
- ViewPager is used for content page wise since the api results provides item list of 10 elements.
- A basic layout animation has been applied once the adapter is loaded of the RecyclerView.
- The page number is added on the top for ease of the user.
- The api key text field on the top can be modified and if valid it will be used for the search results.


ToDo :

- Tutorial animation to be added on the first launch or usage (SharedPreferences).
- Find more efficient loading mechanism of Images and MetaData and keeping in buffer when necessary.
- CardView to be used for each row in RecyclerView.
- Grid layout manager to be tested for large devices.
- Move hardcoded values to resources.

Issues To Fix :

- On minimizing and resuming the fragment does not resume previous state  
