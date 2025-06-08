# PogodynkaApp - Dokumentacja Projektu

## 📱 Informacje Podstawowe
- **Nazwa aplikacji**: PogodynkaApp
- **Platforma**: Android (Java)
- **Wersja SDK**: Min 24, Target 34
- **Package**: com.example.pogodynkaapp

## 🎯 Opis Funkcjonalności

### Główne Funkcje
1. **Autentykacja użytkowników**
   - Logowanie/rejestracja email + hasło (Firebase Auth)
   - Logowanie przez Google Sign-In
   - Automatyczne zapamiętywanie sesji

2. **Wyszukiwanie pogody**
   - Wyszukiwanie po nazwie miasta
   - Lokalizacja GPS z automatycznym pobieraniem pogody
   - Wyświetlanie szczegółowych danych pogodowych

3. **Zarządzanie ulubionymi**
   - Dodawanie miast do ulubionych
   - Lista ulubionych z możliwością usuwania
   - Szybki dostęp do pogody z ulubionych

4. **Historia wyszukiwań**
   - Automatyczne zapisywanie historii
   - Chronologiczna lista z datami i temperaturami
   - Możliwość czyszczenia całej historii

## 🏗️ Architektura Aplikacji

### Struktura Pakietów
```
com.example.pogodynkaapp/
├── activities/          # Główne aktywności
├── adapters/           # Adaptery RecyclerView
├── api/               # Interfejsy API i modele odpowiedzi
├── models/            # Modele danych
└── services/          # Usługi (WeatherService)
```

### Główne Komponenty

#### Activities
- **LoginActivity**: Ekran logowania z Firebase Auth i Google Sign-In
- **MainActivity**: Główny ekran z wyszukiwaniem pogody i nawigacją
- **FavoritesActivity**: Lista ulubionych miast
- **HistoryActivity**: Historia wyszukiwań

#### Models
- **Weather**: Model danych pogodowych
- **FavoriteCity**: Model miasta w ulubionych
- **HistoryEntry**: Model wpisu w historii

#### Services
- **WeatherService**: Obsługa API OpenWeatherMap z Retrofit

## 🔧 Technologie i Biblioteki

### Backend/Cloud
- **Firebase Authentication**: Autentykacja użytkowników
- **Firebase Realtime Database**: Przechowywanie ulubionych i historii
- **Google Sign-In**: Alternatywne logowanie

### API
- **OpenWeatherMap API**: Dane pogodowe
- **Retrofit 2**: HTTP client dla API calls
- **Gson**: Serializacja/deserializacja JSON

### UI/UX
- **Material Design 3**: Nowoczesny design system
- **RecyclerView**: Listy ulubionych i historii
- **Navigation Drawer**: Boczne menu nawigacyjne
- **ConstraintLayout**: Responsywne layouty

### Lokalizacja
- **Google Play Services Location**: GPS i lokalizacja
- **FusedLocationProviderClient**: Optymalne pobieranie lokalizacji

## 📊 Baza Danych (Firebase)

### Struktura danych:
```
pogodynkaapp-f3a47/
├── favorites/
│   └── {userId}/
│       └── {pushId}/
│           ├── cityName: String
│           ├── country: String
│           └── timestamp: Long
└── history/
    └── {userId}/
        └── {pushId}/
            ├── cityName: String
            ├── country: String
            ├── temperature: Double
            ├── description: String
            └── timestamp: Long
```

## 🎨 Design i UX

### Material Design 3 Features
- **Dynamiczne kolory**: Niebiesko-pomarańczowa paleta
- **Gradient backgrounds**: Zmiana tła w zależności od pogody
- **Emoji icons**: Intuicyjne ikony pogodowe (☀️🌧️☁️❄️⛈️🌫️)
- **Rounded corners**: 24dp zaokrąglenia dla kart
- **Elevation**: 12dp cienie dla głębi

### Responsywność
- Adaptacyjne layouty dla różnych rozmiarów ekranów
- Proper spacing i typography
- Accessibility support

## 🔐 Bezpieczeństwo

### Konfiguracja
- **Network Security Config**: Bezpieczne połączenia HTTPS
- **Firebase Rules**: Autoryzacja dostępu do danych
- **API Key**: Zabezpieczony klucz OpenWeatherMap

### Uprawnienia
- `ACCESS_FINE_LOCATION`: Lokalizacja GPS
- `INTERNET`: Połączenia sieciowe

## 🚀 Instalacja i Uruchomienie

### Wymagania
- Android Studio Arctic Fox+
- Android SDK 24+
- Konto Firebase (skonfigurowane)
- Klucz API OpenWeatherMap

### Kroki instalacji
1. Sklonować repozytorium
2. Otworzyć w Android Studio
3. Sync Gradle files
4. Uruchomić na emulatorze lub urządzeniu

### Konfiguracja Firebase
- Project ID: `pogodynkaapp-f3a47`
- Database URL: `https://pogodynkaapp-f3a47-default-rtdb.europe-west1.firebasedatabase.app/`
- Plik `google-services.json` już skonfigurowany

## 📱 Testowanie

### Funkcjonalności do przetestowania
1. **Logowanie**: Email/hasło i Google Sign-In
2. **Wyszukiwanie**: Nazwa miasta i lokalizacja GPS
3. **Ulubione**: Dodawanie, wyświetlanie, usuwanie
4. **Historia**: Automatyczne zapisywanie, czyszczenie
5. **Nawigacja**: Drawer menu, powroty między ekranami

### Przykładowe miasta do testowania
- Warszawa, Kraków, Gdańsk
- London, Paris, New York
- Tokyo, Sydney, Berlin

## 🎓 Aspekty Edukacyjne

### Demonstrowane umiejętności
- **Android Development**: Activities, Fragments, RecyclerView
- **Firebase Integration**: Auth, Realtime Database
- **REST API**: Retrofit, JSON parsing
- **Material Design**: Modern UI/UX principles
- **Location Services**: GPS, permissions
- **Data Persistence**: Cloud storage
- **Authentication**: Multiple providers
- **Error Handling**: Network, user input validation

### Wzorce projektowe
- **MVP/MVVM**: Separation of concerns
- **Adapter Pattern**: RecyclerView adapters
- **Observer Pattern**: Firebase listeners
- **Singleton**: Service classes

## 📈 Możliwości Rozwoju

### Przyszłe funkcje
- Prognozy 5-dniowe
- Notyfikacje pogodowe
- Widget na ekran główny
- Tryb offline z cache
- Personalizacja interfejsu
- Eksport danych

---

**Autor**: Bartosz Tałanda
**Data**: 08.06.2025
**Wersja**: 1.0  
**Status**: Gotowe do oceny ✅ 
