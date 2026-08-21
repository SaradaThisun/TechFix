import { router, useLocalSearchParams } from 'expo-router';

import {
  StyleSheet,
  Text,
  View,
  Pressable,
} from 'react-native';

import MapView, { Marker } from 'react-native-maps';

export default function BranchMapScreen() {

  const {
    name,
    address,
    latitude,
    longitude,
  } = useLocalSearchParams();

  const lat = Number(latitude);
  const lng = Number(longitude);

  return (
    <View style={styles.container}>

      <View style={styles.header}>

        <Text style={styles.title}>
          {name}
        </Text>

        <Text style={styles.address}>
          📍 {address}
        </Text>

      </View>


      <MapView
        style={styles.map}
        initialRegion={{
          latitude: lat,
          longitude: lng,
          latitudeDelta: 0.05,
          longitudeDelta: 0.05,
        }}
      >

        <Marker
          coordinate={{
            latitude: lat,
            longitude: lng,
          }}
          title={String(name)}
          description={String(address)}
        />

      </MapView>


      <View style={styles.bottomCard}>

        <Text style={styles.branchName}>
          📍 {name}
        </Text>

        <Text style={styles.branchAddress}>
          {address}
        </Text>

        <Pressable
          style={styles.backButton}
          onPress={() => router.back()}
        >
          <Text style={styles.backButtonText}>
            BACK TO BRANCHES
          </Text>
        </Pressable>

      </View>

    </View>
  );
}


const styles = StyleSheet.create({

  container: {
    flex: 1,
    backgroundColor: '#FFFFFF',
  },

  header: {
    padding: 20,
    paddingTop: 50,
    backgroundColor: '#FFFFFF',
  },

  title: {
    fontSize: 25,
    fontWeight: 'bold',
  },

  address: {
    fontSize: 14,
    color: '#64748B',
    marginTop: 6,
  },

  map: {
    flex: 1,
  },

  bottomCard: {
    padding: 20,
    backgroundColor: '#FFFFFF',
    borderTopWidth: 1,
    borderTopColor: '#E2E8F0',
  },

  branchName: {
    fontSize: 19,
    fontWeight: 'bold',
  },

  branchAddress: {
    fontSize: 14,
    color: '#64748B',
    marginTop: 5,
  },

  backButton: {
    backgroundColor: '#0A7EA4',
    padding: 15,
    borderRadius: 10,
    marginTop: 15,
  },

  backButtonText: {
    color: '#FFFFFF',
    textAlign: 'center',
    fontWeight: 'bold',
  },

});