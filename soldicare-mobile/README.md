# Soldicare NFC Camera App

An Android camera application that integrates NFC technology for automated photo tagging and Firebase cloud storage. Designed for efficient data collection workflows where photos need to be associated with specific identifiers stored on NFC tags.

## 📋 Overview

Soldicare streamlines the process of capturing and organizing photos by utilizing NFC tags as automatic identifiers. Users simply scan an NFC tag to capture its stored ID, then take photos that are automatically uploaded to Firebase Storage with comprehensive metadata including the NFC identifier.

## ✨ Key Features

- **NFC Tag Integration**: Seamless reading of NDEF-formatted NFC tags
- **Automated Photo Tagging**: Photos are automatically tagged with scanned NFC IDs
- **Firebase Cloud Storage**: Secure upload and storage of images with metadata
- **Real-time Status Feedback**: Visual indicators for NFC scanning and upload progress
- **Comprehensive Metadata**: Each photo includes capture date/time, device info, and custom identifiers
- **Error Handling**: Robust error handling with user-friendly feedback
- **Modern Camera Interface**: Built with CameraX for optimal performance and compatibility

## 🛠️ Technical Stack

- **Language**: Java
- **Platform**: Android (API 26+)
- **Camera**: CameraX
- **NFC**: Android NFC API with foreground dispatch
- **Cloud Storage**: Firebase Storage
- **Architecture**: Activity-based with lifecycle-aware components

## 📱 System Requirements

- Android 8.0 (API level 26) or higher
- NFC-enabled device
- Camera permission
- Internet connectivity for Firebase uploads
- Minimum 50MB free storage space

## 🚀 Installation

### Prerequisites

1. Android Studio Arctic Fox or later
2. Firebase project setup
3. NFC Tools app (for programming NFC tags)

### Setup Instructions

# Install & Use in 3 Steps

Clone & Open in Android Studio
bashgit clone https://dorroi@bitbucket.org/itay_chabra/soldicare-android-app.git

## Open Android Studio
File → Open → Select the cloned soldicare-android-app folder
Run the app (Shift + F10) or click the green play button
Grant camera permissions when prompted


## Prepare NFC Tags

Use NFC Tools app to write soldier IDs to NFC tags
Format: Write soldier serial numbers as text (e.g., 7654321)


## Use the App

📱 Open Soldicare app
🏷️ Scan soldier's NFC tag → See "NFC ID: [number]"
📷 Tap camera button → Photo uploads automatically
✅ See "Photo uploaded" confirmation

1. **Clone the Repository**
   ```bash
   git clone https://dorroi@bitbucket.org/itay_chabra/soldicare-android-app.git
   cd soldicare-nfc-camera
   ```

2. **Firebase Configuration**
    - Create a new Firebase project at [Firebase Console](https://console.firebase.google.com)
    - Enable Firebase Storage
    - Download `google-services.json` and place it in the `app/` directory
    - Update Firebase Storage rules as needed

3. **Configure Application**
    - Update `SoldierID` in `MainActivity.java` (line 485):
      ```java
      .setCustomMetadata("SoldierID", "YOUR_ID_HERE")
      ```
    - Verify package name matches your Firebase project configuration

4. **Build and Install**
   ```bash
   ./gradlew assembleDebug
   adb install app/build/outputs/apk/debug/app-debug.apk
   ```

## 📖 Usage Guide

### Initial Setup

1. **Prepare NFC Tags**
    - Use NFC Tools app to write text records to NFC tags
    - Recommended format: `ITEM001`, `PATIENT123`, `ASSET456`
    - Ensure tags are NDEF-formatted with UTF-8 text records

2. **App Configuration**
    - Grant camera permissions when prompted
    - Ensure NFC is enabled in device settings
    - Verify internet connectivity for uploads


### Operation Workflow

1. **Launch Application**
    - App displays camera preview with NFC status indicator
    - Status shows "Ready to scan NFC tag"

2. **Scan NFC Tag**
    - Hold NFC tag against device back (near camera area)
    - Wait for confirmation toast and status update
    - Status changes to "NFC ID: [scanned_text]"

3. **Capture Photo**
    - Press capture button (only enabled after NFC scan)
    - Flash effect indicates photo capture
    - Upload progress spinner appears during Firebase upload

4. **Completion**
    - Success toast confirms upload with NFC ID
    - App ready for next scan/capture cycle

### Metadata Structure

Each uploaded photo includes the following metadata:

| Field | Description | Example |
|-------|-------------|---------|
| `UserID` | NFC tag text content | `PATIENT123` |
| `SoldierID` | Operator identifier | `8468132` |
| `captureDate` | Photo capture date | `2025-06-11` |
| `captureTime` | Photo capture time | `14:30:25` |
| `captureDateTime` | Combined timestamp | `2025-06-11 14:30:25` |
| `deviceModel` | Device model | `SM-G998B` |

## 🔧 Configuration Options

### Customizing Metadata

Modify the metadata in `uploadToFirebase()` method:

```java
StorageMetadata metadata = new StorageMetadata.Builder()
    .setContentType("image/jpeg")
    .setCustomMetadata("UserID", scannedNfcId)
    .setCustomMetadata("SoldierID", "YOUR_ID")
    .setCustomMetadata("customField", "your_value")
    .build();
```

### NFC Configuration

Adjust NFC settings in `initializeNFC()` method:

```java
// Modify tech lists for specific NFC tag types
techLists = new String[][]{
    new String[]{Ndef.class.getName()},
    new String[]{NfcA.class.getName()}
};
```

## 📊 Retrieving Data

### Firebase Console

1. Navigate to Firebase Storage in your project console
2. Browse to the `images/` folder
3. Select any image to view metadata in the properties panel

### Programmatic Access

Use Firebase Admin SDK or client SDK to query metadata:

```javascript
// Example: Firebase Admin SDK (Node.js)
const file = bucket.file('images/photo_1234567890.jpg');
const [metadata] = await file.getMetadata();
console.log(metadata.metadata.UserID); // NFC tag content
```

## 🐛 Troubleshooting

### Common Issues

| Issue | Cause | Solution |
|-------|-------|----------|
| NFC not scanning | NFC disabled or app not in foreground | Enable NFC in settings, ensure app is active |
| Camera not starting | Missing permissions | Grant camera permission in app settings |
| Upload failures | No internet or Firebase misconfiguration | Check connectivity and Firebase setup |
| Can't read NFC tag | Incompatible tag format | Use NFC Tools to format as NDEF text record |

### Debug Logging

Enable detailed logging with ADB:

```bash
adb logcat | grep -E "(SoldicareCam|NFC|Firebase)"
```

### NFC Testing

Test NFC functionality independently:
1. Use NFC Tools app to verify tag content
2. Test with other NFC-enabled apps
3. Try different tag positions on device

## 🔒 Security Considerations

- **Permissions**: App requests only necessary permissions (Camera, NFC)
- **Data Privacy**: Photos and metadata stored in your private Firebase project
- **Network Security**: All uploads use HTTPS encryption
- **Local Storage**: Temporary files deleted after successful upload

## 🏗️ Architecture

### Core Components

```
MainActivity
├── NFC System (Tag detection and reading)
├── Camera System (CameraX integration)
├── Firebase Integration (Upload and metadata)
└── UI Management (Status updates and user feedback)
```

### Data Flow

```
NFC Tag → Read Content → Store ID → Capture Photo → 
Add Metadata → Upload to Firebase → Confirm Success
```

**Built with ❤️ for efficient data collection workflows**