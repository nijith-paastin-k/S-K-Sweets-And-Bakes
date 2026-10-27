# Known Limitations and Missing Dependencies — S K Sweets & Bakes

## 1. External Configuration Dependencies

### 1.1 Firebase Credentials (`google-services.json`)

- **Status**: A project configuration file is present; live provider setup must be completed in
  Firebase Console.
- **Details**: Firebase initialization uses the Android app's `google-services.json`; there is no
  hard-coded or fake Firebase project fallback. Google Authentication is wired through Credential
  Manager and Firebase Auth.
- **Remaining provider setup**: Facebook sign-in is implemented with the native Login SDK, but the
  ignored local `local.properties` must contain `FACEBOOK_APP_ID` and `FACEBOOK_CLIENT_TOKEN`, and
  the Meta/Firebase provider settings must be complete. LinkedIn uses Firebase OIDC, but LinkedIn's
  discovered issuer and documented ID-token issuer differ; verify live Firebase sign-in before
  production. X is disabled until X Developer access is restored. Instagram requires a server-side
  OAuth exchange and validation.

### 1.2 Payment Gateway Merchant Account

- **Status**: Production merchant credentials (API Key ID and Key Secret) pending merchant sign-up
  with Razorpay / Cashfree.
- **Mitigation**: A complete sandbox integration contract and mock test provider have been
  implemented to test full payment checkout, HMAC verification, and refund flows.

### 1.3 SMS Gateway / Real Phone OTP

- **Status**: Live SMS carrier dispatch requires an active Firebase billing plan (Blaze) for quota
  above free tier.
- **Mitigation**: Development test numbers (e.g. `+91 9876543210` with OTP `123456`) are configured
  in the Auth service to facilitate automated and manual testing without consuming SMS quotas.

---

## 2. Product Limitations & Future Enhancements

- **Production Manufacturing Accounting**: If the sweet shop manufactures raw items on-site from raw
  milk, dry fruits, and sugar, an ingredient consumption recipe tracker can be added to dynamically
  decrement raw materials upon sweet batch production. The current implementation utilizes
  Weighted-Average Cost (WAC) based on purchase invoices.
- **Hardware Weighing Scale Integration**: Direct USB/Bluetooth serial weighing scale integration
  can be plugged into the POS screen in a future iteration. The current POS screen provides rapid
  one-tap preset weight buttons and custom decimal input.
