# 🌤️ PogodynkaApp

Nowoczesna aplikacja pogodowa na Android z autentykacją Firebase, lokalizacją GPS i eleganckim interfejsem Material Design 3.

## ✨ Funkcje

- 🔐 **Autentykacja**: Firebase Auth + Google Sign-In
- 🌍 **Wyszukiwanie pogody**: Nazwa miasta lub lokalizacja GPS  
- ⭐ **Ulubione miasta**: Zapisywanie i zarządzanie
- 📜 **Historia wyszukiwań**: Automatyczne zapisywanie z datami
- 🎨 **Material Design 3**: Nowoczesny, responsywny interfejs
- 🌈 **Dynamiczne tła**: Zmiana kolorów w zależności od pogody

## 🛠️ Technologie

- **Android**: Java, SDK 24+
- **Backend**: Firebase Auth, Realtime Database
- **API**: OpenWeatherMap, Retrofit 2
- **UI**: Material Design 3, RecyclerView, Navigation Drawer
- **Lokalizacja**: Google Play Services Location

## 🚀 Instalacja

1. Sklonuj repozytorium
2. Otwórz w Android Studio
3. Sync Gradle files
4. Uruchom na urządzeniu/emulatorze

## 📱 Zrzuty ekranu

### Główne funkcje:
- Ekran logowania z Google Sign-In
- Wyszukiwanie pogody z emoji ikonami
- Lista ulubionych miast
- Historia wyszukiwań z datami

## 🔧 Konfiguracja

### Firebase
- Project ID: `pogodynkaapp-f3a47`
- Plik `google-services.json` już skonfigurowany

### API
- OpenWeatherMap API key już zintegrowany

## 📊 Architektura

```
app/src/main/java/com/example/pogodynkaapp/
├── activities/     # LoginActivity, MainActivity, FavoritesActivity, HistoryActivity
├── adapters/      # FavoriteCitiesAdapter, HistoryAdapter
├── api/          # WeatherApi, WeatherResponse
├── models/       # Weather, FavoriteCity, HistoryEntry
└── services/     # WeatherService
```

## 🎯 Demonstrowane umiejętności

- Android Activities i RecyclerView
- Firebase Authentication i Realtime Database
- REST API z Retrofit
- Material Design 3 implementation
- GPS i Location Services
- Error handling i user input validation

## 📄 Licencja

Projekt edukacyjny - Android Development

---

**Status**: ✅ Gotowe do oceny  
**Build**: Successful  
**Tests**: Functional 