import React from 'react';
import {PermissionsAndroid, Platform, Alert, View, Text} from 'react-native';

const Permissions = () => {
  // permission
  const blutoothConnect = PermissionsAndroid.PERMISSIONS.BLUETOOTH_CONNECT;
  const blutoothScan = PermissionsAndroid.PERMISSIONS.BLUETOOTH_SCAN;

  const backgroudLocation =
    PermissionsAndroid.PERMISSIONS.ACCESS_BACKGROUND_LOCATION;
  const locationFine = PermissionsAndroid.PERMISSIONS.ACCESS_FINE_LOCATION;
  const locationCoarse = PermissionsAndroid.PERMISSIONS.ACCESS_COARSE_LOCATION;

  const requestBluetoothPermission = async () => {
    try {
      const granted = await PermissionsAndroid.requestMultiple([
        blutoothScan,
        blutoothConnect,
      ]);

      if (
        granted[blutoothScan] &&
        granted[blutoothConnect] === PermissionsAndroid.RESULTS.GRANTED
      ) {
        console.log('You allowed to location permission');
      } else if (
        granted[blutoothScan] &&
        granted[blutoothConnect] === PermissionsAndroid.RESULTS.NEVER_ASK_AGAIN
      ) {
        Alert(
          'Please Go into Settings -> Applications -> APP_NAME -> Permissions and Allow permissions to continue',
        );
      }
    } catch (err) {
      console.warn(err);
    }
  };

  const requestLocationPermission = async () => {
    try {
      const granted = await PermissionsAndroid.requestMultiple([
        locationFine,
        locationCoarse,
      ]);

      if (
        granted[locationFine] &&
        granted[locationCoarse] === PermissionsAndroid.RESULTS.GRANTED
      ) {
        console.log('You allowed to location permission');
      } else if (
        granted[locationFine] &&
        granted[locationCoarse] === PermissionsAndroid.RESULTS.NEVER_ASK_AGAIN
      ) {
        Alert(
          'Please Go into Settings -> Applications -> APP_NAME -> Permissions and Allow permissions to continue',
        );
      }
    } catch (err) {
      console.warn(err);
    }
  };

  const requestBackgroudLocation = async () => {
    try {
      const granted = await PermissionsAndroid.request(backgroudLocation);
      if (granted === PermissionsAndroid.RESULTS.GRANTED) {
        console.log('requestBackgroudLocation');
      } else if (granted === PermissionsAndroid.RESULTS.NEVER_ASK_AGAIN) {
        Alert.alert('Info', `Settings -> Applications -> Poi -> Permissions`);
      } else {
        console.log('location permission denied');
      }
    } catch (err) {
      console.warn(err);
    }
  };

  React.useEffect(async () => {
    if (Platform.OS === 'android') {
      await requestBluetoothPermission();
      await requestLocationPermission();

      await PermissionsAndroid.check(backgroudLocation).then(response => {
        console.log(response);
        if (response === false) {
          requestBackgroudLocation();
        } else if (response === true) {
        }
      });

      
    }
  }, []);

  return (
    <View>
      <Text>Poilabs Permissions</Text>
    </View>
  );
};

export default Permissions;
