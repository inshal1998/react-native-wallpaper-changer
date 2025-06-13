import { NativeModules } from 'react-native';

const { WallpaperModule } = NativeModules;

export const setWallpaper = (imagePath: string): Promise<string> => {
  return WallpaperModule.setWallpaper(imagePath);
};
