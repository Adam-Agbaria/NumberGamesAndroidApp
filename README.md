# Numbers Game - Android Host App

The **Android Host App** allows game organizers to manage games, generate QR codes, and control game rounds.

## 📱 Features
- **Create a Game:** Generates a unique game ID.
- **Generate QR Code:** Players scan to join.
- **Start & End Rounds:** Control the game from your phone.
- **Seamless Backend Integration:** Connects to the Flask API.
- **Uses the Numbers Game Android Library:** Handles all API calls efficiently.

## 🔧 Setup & Installation
1. Clone the repository:
   git clone https://github.com/YourUsername/NumbersGame-HostApp.git
   cd NumbersGame-HostApp

2. Open in **Android Studio**.
3. Sync Gradle dependencies.
4. Build and run the app.

## 🔗 Dependencies
This app uses the **Numbers Game Android Library** for communicating with the backend. Ensure the library is added as a dependency in `build.gradle`:

   dependencies {
       implementation project(':numbers-game-library')
   }

## 📜 License
This project is licensed under the **MIT License**.
