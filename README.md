# ACNHCatalog (Animal Crossing: New Horizons Catalog)

Welcome to ACNHCatalog, an Android application dedicated to exploring and managing villagers from the popular game Animal Crossing: New Horizons. This project utilizes modern Android development techniques and libraries to provide a seamless user experience.


<img src="https://github.com/coffmancorrim/ACNHVillagerCatalog/assets/104328482/9f7540c8-6b06-4808-bb57-7c54264da01c" width="200px">

## Technologies Used

- **Kotlin:** The primary programming language used for developing the Android application.
- **MVVM Architecture:** Follows the Model-View-ViewModel architectural pattern for separation of concerns and easier testing.
- **View Binding:** Utilized for accessing views in the XML layout files.
- **Manual Dependency Injection:** Dependency injection for managing dependencies and improving testability.
- **Fragments:** Modularize the user interface and navigation within the app.
- **ViewModels:** Store and manage UI-related data in a lifecycle-conscious manner.
- **Glide:** Efficiently load and display images retrieved from the API.
- **Room Database:** Store and manage persistent data locally, including villager information and user lists.
- **Retrofit:** Communicate with the API to fetch data about Animal Crossing villagers.
- **SearchView:** Implement a search functionality to filter villagers based on their names, species, or birth months.

## Features

- **Home Screen:** Central hub for navigating through different sections of the app.
<img src="https://github.com/coffmancorrim/ACNHVillagerCatalog/assets/104328482/b513aec0-fb3b-4032-bf3a-6885c0ea0b75" width="200px">

- **Discover Screen:** Browse through a comprehensive list of all available villagers.
- **Favorites Screen:** View and manage your favorite villagers. Easily add or remove villagers from your favorites list.
<img src="https://github.com/coffmancorrim/ACNHVillagerCatalog/assets/104328482/381fdd2a-de1f-439c-a7f9-1d2858898e96" width="200px">

- **Custom Lists Screen:** Create and manage custom lists to categorize villagers according to your preferences.
<img src="https://github.com/coffmancorrim/ACNHVillagerCatalog/assets/104328482/d76807cd-15f6-446f-81a4-42f5d9781457" width="200px">

- **Villager Detail Screen:** Explore detailed information about each villager, including their name, species, and birth month.
<img src="https://github.com/coffmancorrim/ACNHVillagerCatalog/assets/104328482/1e52822c-f5a3-47f3-8923-12d7b5f6fb33" width="200px">

- **Search Bar:** Filter the villagers by name, species, or birth months.
<img src="https://github.com/coffmancorrim/ACNHVillagerCatalog/assets/104328482/c456f5b8-dd00-4b6c-817c-8a9d633e74f6" width="200px">

## Installation
To run the ACNHCatalog app locally, follow these steps:

1. Clone this repository to your local machine.
2. Open the project in Android Studio.
3. Build and run the app on an Android emulator or physical device.

**Note** - To run the app and retreive data from the api you will have to request access and get a api key. You can switch to retrieve data locally from a json file (to simulate a api call) by changing the mockResponse variable to true, in the AnimalCrossingRepositoryImpl class (located in the data package).
