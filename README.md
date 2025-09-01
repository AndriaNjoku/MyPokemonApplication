# 📱 MyPokemonApplication

A simple Pokémon browser built with **Kotlin** and **Jetpack Compose**,

---

## ✨ Features

- **Pokémon List**
  - Fetches Pokémon from the [PokeAPI](https://pokeapi.co/api/v2/pokemon).
  - Displays Pokémon names in a scrollable list.
  - Pagination controls: previous/next arrows and current page indicator.

- **Pokémon Details**
  - Shows when a Pokémon is tapped:
    - Name  
    - Image (from `sprites.other["official-artwork"].front_default` where available)  
    - Height (converted to meters)

- **Error Handling**
  - Displays a retry error state if API requests fail.

---

## 🏗️ Architecture

- **Clean MVVM** with three layers:
  - **Data** → Retrofit API + `PokemonGateway` (repository implementation)
  - **Domain** → `PokemonRepository` interface + models (`PokemonLite`, `PokemonFull`)
  - **Presentation** → `ListPokemonViewModel` + Jetpack Compose UI

- **State management** → Kotlin `StateFlow`
- **Dependency Injection** → Hilt
- **Networking** → Retrofit + Gson + OkHttp
- **Coroutines** → suspend functions + structured concurrency
- **Image Loading** → Coil-Compose

---

## ⚙️ Tech Stack

- **Language**: Kotlin (target JVM 21)  
- **UI**: Jetpack Compose + Material 3  
- **DI**: Hilt  
- **Networking**: Retrofit, OkHttp, Gson  
- **Async**: Coroutines + Flow  
- **Images**: Coil Compose  

---

## ✅ Testing

- **Unit Tests** (JVM)
  - `ListPokemonViewModel` (state emissions, paging, details)
  - `PokemonMapper` (DTO → domain)
  - Repository error mapping

- **UI Tests** (Instrumentation)
  - Verifies Pokémon names render in the list
  - Verifies pagination buttons are clickable and update state

### Run Tests

```bash
# Unit tests
./gradlew testDebugUnitTest

# UI / Instrumentation tests (requires device/emulator)
./gradlew connectedAndroidTest
