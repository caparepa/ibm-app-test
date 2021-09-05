# International Business Men.

This is a simple app made as a technical test for Bemobile, using transaction and currency exchange rate data obtained from [http://quiet-stone-2094.herokuapp.com/](http://quiet-stone-2094.herokuapp.com/) as data source.

## Premise

* "You work for GNB (Goliath National Bank), and your manager, Barney Stinson, has asked
  you to dexsign and implement a mobile application to help the firm executives who are
  always flying around the globe. Your executives need a list of every product GNB trades with,
  and the total sum of sales of those products in different currencies."
* For this task, web service is to be used to obtain data. "This web service can provide its results both in
  XML or in JSON format. You are free to choose the one you feel more comfortable with (you
  only need to use one of the formats)."
* The endpoints to be used are:
    * [http://quiet-stone-2094.herokuapp.com/rates.xml](http://quiet-stone-2094.herokuapp.com/rates.xml)​ or [http://quiet-stone-2094.herokuapp.com/rates.json](http://quiet-stone-2094.herokuapp.com/rates.json) for exchange rates.
    * [http://quiet-stone-2094.herokuapp.com/transactions.xml​](http://quiet-stone-2094.herokuapp.com/transactions.xml​) or
      [http://quiet-stone-2094.herokuapp.com/transactions.json](http://quiet-stone-2094.herokuapp.com/transactions.json) for transactions.

## Requirements

1. Your application should download this information upon starting and give the user the
   choice of which product they want to see. When the user selects a product, the application
   must show each of the transactions related to that product and a sum of all the transactions
   transformed into EUR.
2. Do not block the UI while downloading the data from the network. Do not download
   the data from the network before showing the UI.
3. You must use Kotlin and any third party libraries, as you would do with any project.
4. <u>**The use of design patterns, DI, well structured code and project
   architecture will be valued (do it as you would with a project with a larger
   scope).**</u>
5. Remember that some conversion rates are missing, and they should be derived using
   the information provided.
6. Your application should download this information upon starting and give the user the
   choice of which product they want to see. When the user selects a product, the application
   must show each of the transactions related to that product and a sum of all the transactions
   transformed into EUR.

## Additional Information

1. We are dealing with money. Try not to use floating point numbers.
* After each conversion, the result should be rounded to two decimal places, using
  Bankers’ Rounding ​ ( ​ http://en.wikipedia.org/wiki/Rounding#Round_half_to_even​ )
* The right way to specify the request format in HTTP is not using a file extension, but
  using an “Accept” header. Instead of using the extension try to use the “Accept”
  header with the right MIME type.
* Show us if you know about unit testing.

## Assumptions

For this project, some assumptions were made:

1. Since there is no UI/UX specified verbatim or provided as an attachment, requirement #1, "when the user selects a product, the application
   must show each of the transactions related to that product and a sum of all the transactions (...)" was interpreted as "show a view with a list of products, and when one product is selected, navigate to another view that will show a list with its related transactions and the total sum of those sales (...)". This assumption was made in order to elaborate on the usage of Android's Navigation Components.
2. "Do not block the UI while downloading the data from the network. Do not download the data from the network before showing the UI." was interpreted verbatim, adapted to the final application UI by showing a simple visual element that is to disappear when the required data is downloaded.
3. Point #5 of "Requirements" was assumed to be restricted by point #1 and point #6, whereas the missing exchange rates to be derived are to be those used in order to transform the transaction amount data (and its subsequent sum of) to EUR.

## Development Information

### Project architecture and stack

This application was developed using a MVVM design pattern for Android applications, following  the [recommended guidelines by Google](https://developer.android.com/jetpack/guide), and using Kotlin as programming language. The libraries used were the following:

* [Koin](https://insert-koin.io/docs/quickstart/android): dependency injection library.
* [Android-SpinKit](https://github.com/ybq/Android-SpinKit):for the loader/progress bar.
* [Retrofit2](https://square.github.io/retrofit/): for networking/API calls.
* [OkHttp](https://square.github.io/okhttp/): used for request interceptors.
* [Gson](https://github.com/google/gson): for data serialization (JSON to POJO or Kotlin data classes) when using Retrofit.
* [Itertools](https://gist.github.com/Tandrial/a63ccae712f08a56c2ca9a57b5e3be74): a nice-to-have Kotlin port of Python's itertools (iteration building blocks) for efficient manipulation of arrays and/or collections.

The build.gradle was refactored from Groovy to Kotlin DSL for consistency and future integration with the Kotlin codebase (in case there's a need to manage dependency versions programmatically).

### Package structure

The package structure is the following, explained in a short, concise way:

* **data**: contains everything related to data definition (Models)
    * **model**: contains the object definition for the data obtained from the API
* **di**: contains modules for dependency injection and the modules declaration.
    * modules: contains a series of class modules used in the app
* **network**: contains classes regarding API requests
    * **api**: contains the Retrofit API client  and endpoint declaration
    * **interceptor**: contains interfaces and implementations of the different interceptors used
* **repository**: contains interfaces and implementations of the different repositories used to obtain the from the web service and transform it.
* **ui**: contains all packages related to user interfaces:
    * **activity**: contains all the activities used in the application
    * **adapter**: contains packages for a base dynamic adapter and the subsequent implementation of different adapters used throughout the application
        * **base**: contains the base DynamicAdapter classes, interfaces and extensions
        * **holder**: contains the implementations of the different ViewHolder used in the app
        * **item.model**: contains the different implementations of ItemModel to use with the DynamicAdapter
        * **type.factory**: contains the implementation of the different TypeFactory used by the DynamicAdaptor
    * **dialog**: contains custom dialog implementations
    * **fragment**: contains the fragments
    * **viewmodel**: contains a BaseViewModel and the different viewmodels used, all extending from it.
* **utils**: utility package with classes and extensions that might serve differen purposes.
    * **library**: contains functions, files and or classes that might fall into the "library" classification. E.g.: Itertools.

## Comments

Some considerations were taken into account, given a series of observations made before and during the development.

1. Considering that there are two formats to obtain the data, XML and JSON, the latter was chosen, given it's easier to work with the selectec stack chosen for the app's development.
2. The data sets provided by the endpoints showed to contain inconsistent data regarding exchange rate's values and transaction's currecy sign value. For example, the data from the "rates" endpoint would contain a set of currency signs (e.g. AUD, CAD, EUR), but the data from  the "transactions" endpoint might not containt a corresponding set of currency signs (e.g. AUD, USD, EUR). In this case, the course of action was to show a dialog to the user explaining this case, and when closed, show the total sum of those transactions that could be converted, and the complete transactions list, showing all the transactions.
3. Considering the amount of time available for the development following the established requirements, and considering the random nature of key data provided by the endpoints (e.g. EUR to USD rate would be 1.13 in one call, and then would be 0.87 in another), the use of an onboard database was forgone. Also, the calculation of the missing exchange rates for the provided currency set was limited to the "X to EUR" pairing, as stated in point #3 of "Assumptions".
4. Given the time frame available for the app development, only basic unit testing, focused on the exchange rate calculation, were made.


### Guides and Reference
* [Converting your Android Gradle scripts to Kotlin](https://proandroiddev.com/converting-your-android-gradle-scripts-to-kotlin-1172f1069880): simple and complete explanation on migrating from Goovy to Kotlin DSL.