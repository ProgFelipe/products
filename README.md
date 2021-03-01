# Merca
## _Search app using mercadolibre Api_

![Alt Text](https://raw.githubusercontent.com/ProgFelipe/products/master/demo.gif)

## Features

- CustomSearchView with suggestions debounce API ("/autosuggest") call
- RecyclerView with search results - API ("/search")
- DetailView
- Avility to rotate screen

## Tech

Architecture and Tech aspects:

- [Main Architecture] - Clean architecture with two project modules App and Data.
Data: contains UseCases, Repository, Domain and service dto models.
App: contains the presentation layer, like viewModels, customViews.
In this layer you will see a BaseViewModel which cover useCase calls with success and error callbacks and a common status handler.

- [Unit Test] - Cover the UseCases, Repositories, ViewModels and Domain.


## Libraries

Dillinger is currently extended with the following plugins.
Instructions on how to use them in your own application are linked below.

| Libraries |
| ------ |
| Navigation component |
| Livedata |
| com.squareup.retrofit2 |
| RxJava2 |
| DI-Hilt |
| Mockito |
| com.nhaarman.mockitokotlin2 |
| Glide |
| Moshi |

## License

MIT

**Free Software**