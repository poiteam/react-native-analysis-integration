/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow strict-local
 */

import React from 'react';
import type {Node} from 'react';
import {SafeAreaView, StatusBar} from 'react-native';

import Permissions from './Permissions';

const App: () => Node = () => {
  return (
    <SafeAreaView>
      <StatusBar />
      <Permissions />
    </SafeAreaView>
  );
};

export default App;
