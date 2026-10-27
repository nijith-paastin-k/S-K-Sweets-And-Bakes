# Setup and Deployment Guide — S K Sweets & Bakes

## 1. Prerequisites

- Android Studio Ladybug / Meerkat or later with Android SDK 37 (API 37).
- Java Development Kit: OpenJDK 17.
- Node.js (v20 or v22) & npm.
- Firebase CLI (`npm install -g firebase-tools`).

---

## 2. Firebase Project Setup

1. **Create Firebase Project**:
    - Go to [Firebase Console](https://console.firebase.google.com/) and create a new project named
      `sk-sweets-and-bakes`.
2. **Register Android Application**:
    - Package name: `com.skylink.sksweetsandbakes`
    - App nickname: `S K Sweets And Bakes`
    - Add SHA-1 and SHA-256 fingerprints from your debug and release keystores:
      ```bash
      ./gradlew signingReport
      ```
    - Download the generated `google-services.json` and place it in the `app/` directory:
      ```bash
      cp path/to/google-services.json app/
      ```
3. **Enable Firebase Services**:
    - **Authentication**: Enable the Google provider. Add the Android app's SHA-1 and SHA-256
      fingerprints for Google sign-in. Phone OTP is currently demo-only and is not wired to Firebase
      Phone Auth.
    - To enable Facebook, create a Facebook Login app, configure its App ID, App Secret, and
      Firebase OAuth redirect URI in Firebase Authentication and the Meta Developer Portal, and
      enable the `email` and `public_profile` permissions. Add `FACEBOOK_APP_ID` and
      `FACEBOOK_CLIENT_TOKEN` to the ignored `local.properties`; do not commit that file. Android
      uses the Facebook Login SDK and passes its access token to Firebase Authentication.
    - X sign-in is currently disabled in the app while X Developer access is blocked.
    - LinkedIn uses the Firebase Identity Platform OIDC provider ID `oidc.linkedin`; configure its
      issuer, client ID/secret, and callback URL. Instagram does not provide a Firebase social
      provider; it needs a server-side OAuth integration that validates the provider response before
      issuing an app session.
    - **Cloud Firestore**: Create database in production mode.
    - **Cloud Storage**: Create default storage bucket.
    - **Firebase Cloud Messaging (FCM)**: Enable for notifications.

Google sign-in uses Credential Manager to show Google's account picker and obtain a Google ID token;
Firebase Authentication establishes the app session and supplies the signed-in profile. Facebook
sign-in launches the Facebook Login SDK consent/login flow for `public_profile` and `email`, then
exchanges the returned access token with Firebase Authentication. Both use provider-verified
identities rather than locally enumerated device accounts. LinkedIn uses Firebase Identity Platform
OIDC provider `oidc.linkedin`; validate LinkedIn issuer compatibility with Firebase during live
testing before production release. The client persists only Firebase-authenticated UIDs and
profiles; an email address alone is never accepted as proof of sign-in. Instagram requires a
server-side OAuth integration and must not create a local demo session.

---

## 3. Deploying Firebase Rules & Cloud Functions

1. **Login to Firebase CLI**:
   ```bash
   firebase login
   ```
2. **Link Project**:
   ```bash
   firebase use --add
   ```
3. **Deploy Security Rules**:
   ```bash
   firebase deploy --only firestore:rules,storage
   ```
4. **Deploy Cloud Functions**:
   ```bash
   cd functions
   npm install
   npm run build
   firebase deploy --only functions
   ```

---

## 4. Local Emulator Suite (Optional for Offline Testing)

Start the Firebase Emulator Suite for local testing without incurring cloud costs:

```bash
firebase emulators:start --only firestore,auth,storage,functions
```

The Android application includes an emulator toggle in `core/firebase/FirebaseModule.kt` to connect
to local IP `10.0.2.2` (Android Emulator) or localhost.

---

## 5. Building the Android Application

- **Debug Build**:
  ```bash
  ./gradlew assembleDebug
  ```
- **Run Unit Tests**:
  ```bash
  ./gradlew test
  ```
- **Release APK / Bundle**:
  ```bash
  ./gradlew assembleRelease
  # or
  ./gradlew bundleRelease
  ```
