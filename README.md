# 📱 Pokédex Android App

A modern Android implementation of the classic Pokédex from Pokémon, allowing trainers to browse, search, and catch 'em all!

## ✨ Features

- 🔍 Search functionality to quickly find your favorite Pokémon
- 📋 Complete listing of all original 151 Pokémon from the Kanto region
- 📊 Detailed information for each Pokémon including:
  - Type information
  - Pokémon number
  - Description from the game
  - Official sprite artwork
- ⭐ "Catch" your favorite Pokémon and build your collection
- 💾 Persistent storage to remember which Pokémon you've caught

## 🛠️ Technical Implementation

- Built with native Android in Java
- RecyclerView for efficient scrolling through Pokémon list
- Volley library for API requests
- Uses the public [PokéAPI](https://pokeapi.co/) for comprehensive Pokémon data
- Implements AsyncTask for image loading
- SharedPreferences for saving caught Pokémon between sessions
- Search functionality with real-time filtering

## 📲 Usage

1. Launch the app to see a list of all 151 original Pokémon
2. Use the search bar to filter Pokémon by name
3. Tap on any Pokémon to view detailed information
4. Press the "Catch" button to add a Pokémon to your collection
5. Press "Release" to remove it from your collection

## 🧩 Project Structure

- `MainActivity.java` - Entry point with RecyclerView and search implementation
- `PokedexAdapter.java` - Adapter for the Pokémon list with filtering capability
- `Pokemon.java` - Model class for Pokémon data
- `PokemonActivity.java` - Detailed view for individual Pokémon information

## 🔄 Future Improvements

- Add support for additional Pokémon generations
- Implement more detailed statistics and evolution chains
- Create a dedicated view for caught Pokémon
- Add sound effects from the games

---

Happy Pokémon hunting! 🎮
