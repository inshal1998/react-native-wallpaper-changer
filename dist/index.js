import { NativeModules } from 'react-native';
const { WallpaperModule } = NativeModules;
export const setWallpaper = (imagePath) => {
    return WallpaperModule.setWallpaper(imagePath);
};
