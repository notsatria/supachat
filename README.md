# Supachat 💬

Supachat is a real-time chat application built with **Jetpack Compose** for Android and powered by **Supabase** as the backend. It showcases modern Android development best practices including **Realtime messaging**, **User Authentication**, and **Push Notifications**.

## ✨ Features

- 🔐 User Authentication using Supabase Auth (email/password)
- 💬 Real-time messaging using Supabase Realtime
- 📬 Push Notifications with Firebase Cloud Messaging (FCM)
- 💉 Dependency Injection with Dagger Hilt
- ⚡ REST & Edge Function integration via Ktor client
- 🧱 Modern UI with Jetpack Compose
- ☁️ Local data persistence using DataStore

---

## 🛠️ Tech Stack

### 📱 Client (Android)
- **Jetpack Compose** – Declarative UI toolkit
- **Dagger Hilt** – Dependency Injection framework
- **Supabase Kotlin Client** – Supabase integration for Auth, Realtime, and Database
- **Firebase Cloud Messaging (FCM)** – Push notification service
- **Ktor Client** – HTTP client for API and Supabase Edge Functions
- **DataStore** – Local storage solution
- **Timber** – Logging library

### 🔙 Backend (Supabase)
- **Supabase Auth** – User login and registration
- **PostgreSQL Database** – Stores users, conversations, and messages
- **Supabase Realtime** – WebSocket-based subscription for live messages
- **Supabase Edge Functions** – Custom backend logic (optional, for future extensions)

---

## 🚀 Getting Started

### 📋 Prerequisites

- Android Studio Hedgehog or newer
- A Supabase project configured
- A Firebase project with FCM enabled

### 🔑 Configuration

1. Create a `local.properties` or `.env` file:
```properties
BASE_URL=your-supabase-url
API_KEY=your-supabase-anon-key
```

2. Add your `google-services.json` file into the `app/` directory.

3. Sync your project and run it on an emulator or physical device.

---

## 📈 Project Structure (Brief)

```bash
📁 app
 ┣ 📂 component 
 ┣ 📂 data         
 ┣ 📂 di        
 ┣ 📂 navigation
 ┣ 📂 services          
 ┣ 📂 ui
 ┣ 📂 utils
 ┣ 📄 MainActivity.kt
 ┣ 📄 App.kt
 ┣ 📄 SessionViewModel.kt
 ┣ 📄 SupachatApp.kt
```

---

## 🔄 Work In Progress

- [ ] Group Chat support
- [ ] Voice messages
- [ ] Online/Offline presence
- [ ] Media attachments via Supabase Storage
- [ ] Reply message
- [ ] Unset message

---

## 📸 Screenshots
### Authentication
| *Register* | *Login* |
|-|-|
| <img src="https://github.com/user-attachments/assets/52c8a46d-2634-4767-b1c7-be038edc0e19" width="300px"/> | <img src="https://github.com/user-attachments/assets/522526df-234e-474b-a84c-0922bc47659e" width="300px"/> |
| *OTP Verification* | *OTP Verification (Input Failed)* |
| <img src="https://github.com/user-attachments/assets/432698fb-46c0-46c4-a7c0-72e8eadd5ffb" width="300px"/> | <img src="https://github.com/user-attachments/assets/b6ab8079-dfd9-41a7-b15c-f0632fe92c7d" width="300px"/> |

### Chat
| *Chat List* | *Room Chat* |
|-|-|
| <img src="https://github.com/user-attachments/assets/5bec82df-581c-4bfa-89c7-3040b83dfec8" width="300px"/> | <img src="https://github.com/user-attachments/assets/c6bfb64d-1c5b-4ddf-a2c2-32cb96021957" width="300px"/> |
| *Chat Notification (Real Time)* | *Demo Video* |
| <img src="https://github.com/user-attachments/assets/9f0a0fae-0b32-4f2b-b430-e166166d75b6" width="300px"/> | <img src="https://github.com/user-attachments/assets/022349ae-8297-49fd-9dbc-810283ffeabb" width="300px"/> |

---

## 🤝 Contribution

Feel free to open issues or pull requests to improve this project. Contributions are always welcome!

---

## 👤 Author

Created by [Notsatria]  
📧 Email: notsatria.dev@gmail.com  
🌐 [LinkedIn](https://www.linkedin.com/in/damarsatria)
