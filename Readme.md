# 🎨 RN WallPaper changer
A simple React Native app that allows users to change their device wallpaper with a single tap. The app uses the `react-native-wallpaper-manager` library to set wallpapers from local images or remote URLs.


## ✨ Features
- Change wallpaper with a single tap
- Support for both local images .
- Support for remote image URLs
- Simple and intuitive user interface


## ⚙️ Installation
```
 npm install react-native-wallpaper-manager
```

## 🚀 Usage
1. Open the app on your device.
2. Tap the "Change Wallpaper" button to set a new wallpaper.
3. The app will change the wallpaper to a random image from the provided list of local images.

# 🧩 Sample Code

```javascript
import React from 'react';
import { View, Button, Platform, PermissionsAndroid, Alert } from 'react-native';
import { launchImageLibrary } from 'react-native-image-picker';
import {setWallpaper} from 'react-native-wallpaper-changer';

const WallpaperChanger = () => {
  const requestStoragePermission = async (): Promise<boolean> => {
    if (Platform.OS !== 'android') return true;

    let permission;
    if (Platform.Version >= 33) {
      permission = PermissionsAndroid.PERMISSIONS.READ_MEDIA_IMAGES;
    } else {
      permission = PermissionsAndroid.PERMISSIONS.READ_EXTERNAL_STORAGE;
    }

    const granted = await PermissionsAndroid.request(permission, {
      title: 'Storage Permission',
      message: 'This app needs access to your gallery to set wallpaper.',
      buttonNeutral: 'Ask Me Later',
      buttonNegative: 'Cancel',
      buttonPositive: 'OK',
    });

    return granted === PermissionsAndroid.RESULTS.GRANTED;
  };

  const pickAndSetWallpaper = async () => {
    const hasPermission = await requestStoragePermission();

    if (!hasPermission) {
      console.warn('Permission not granted');
      Alert.alert('Permission Required', 'Storage permission is needed to access images.');
      return;
    }

    launchImageLibrary({ mediaType: 'photo' }, async (response) => {
      if (response.didCancel) {
        console.log('User cancelled image picker');
        return;
      }

      if (response.errorCode) {
        console.log('Image Picker Error:', response.errorMessage);
        return;
      }

      const image = response.assets?.[0];
      if (!image?.uri) {
        console.warn('No image selected');
        return;
      }

      try {
        const result = await setWallpaper(image.uri);
        console.log(result);
        Alert.alert('Success', 'Wallpaper successfully set!');
      } catch (err: any) {
        console.error('Error setting wallpaper:', err);
        Alert.alert('Error', err?.message || 'Failed to set wallpaper');
      }
    });
  };

  return (
    <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center' }}>
      <Button title="Set Wallpaper from Gallery" onPress={pickAndSetWallpaper} />
    </View>
  );
};

export default WallpaperChanger;
```