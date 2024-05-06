# BuddyMovies Android App

BuddyMovies is an Android application built with Java and Android Studio, designed to serve as a comprehensive movie library. The app allows users to browse movies based on genre or release dates, leveraging advanced programming concepts and architectural patterns for a seamless user experience.

## Features

- **Genre and Release Date Filtering**: Users can explore movies based on genres or release dates, enhancing discoverability.
- **Multithreading**: Implemented multithreading to ensure smooth performance and responsiveness throughout the app.
- **View Modeling**: Utilized MVVM architecture to separate concerns and improve code maintainability.
- **Cache Mechanism**: Employed caching mechanisms for efficient data storage and retrieval, optimizing app performance.
- **Preloading Data**: Implemented preloading of data to enhance user experience and reduce loading times.
- **Integration with External APIs**: Integrated third-party APIs seamlessly into the app to fetch movie data.
- **Modularized and Clean Code**: Structured codebase using clean and modularized design principles for readability and maintainability.
- **Design Patterns**: Implemented design patterns such as Singleton and Observer to improve code structure and scalability.

## Directory Structure

```
java/
  com.example.buddymovies/
    Adaptors/
      ContainerAdaptor.java
      MovieAdaptor.java
    Models/
      ContainerModel.java
      MovieModel.java
    Utils/
      MovieNetworkUtils.java
DashboardActivity.java
DashboardViewModel.java
LoginActivity.java
MovieDetailsActivity.java
MovieDetailsViewModel.java
```

## Technologies Used

- **Glide**: Image loading and caching library for smooth image rendering.
- **Serializable**: Interface used for object serialization.
- **URL, org.json**: Used for handling network requests and parsing JSON data.
- **HttpURLConnection**: Java API for performing HTTP requests.
- **Concurrent Framework**: Utilized CompletableFuture, ExecutorService, and Executors for multithreading.
- **Stream API**: Leveraged Stream API for efficient data manipulation.

## Getting Started

To get started with BuddyMovies, follow these steps:

1. Clone the repository: `git clone https://github.com/your-username/BuddyMovies.git`
2. Open the project in Android Studio.
3. Build and run the app on an Android device or emulator.

## Contributing

Contributions are welcome! Feel free to fork the repository and submit pull requests to contribute new features, enhancements, or bug fixes.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgements

- Special thanks to [name] for their invaluable contributions.
- Hat tip to [name] for inspiration and guidance.
- Movie data provided by [API Provider].
