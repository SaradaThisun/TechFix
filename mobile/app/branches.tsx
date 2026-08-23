import { useState } from 'react';
import { router } from 'expo-router';

import {
  StyleSheet,
  Text,
  View,
  Pressable,
  ScrollView,
  TextInput,
  Alert,
} from 'react-native';

export default function BranchesScreen() {

  const [search, setSearch] = useState('');

    const branches = [
    {
        id: '1',
        name: 'Colombo Branch',
        address: '123 Galle Road, Colombo',
        phone: '011 234 5678',
        hours: '9:00 AM - 6:00 PM',
        latitude: 6.9271,
        longitude: 79.8612,
    },

    {
        id: '2',
        name: 'Galle Branch',
        address: '45 Main Street, Galle',
        phone: '091 234 5678',
        hours: '9:00 AM - 6:00 PM',
        latitude: 6.0329,
        longitude: 80.2168,
    },
    ];

  const filteredBranches = branches.filter((branch) =>
    branch.name
      .toLowerCase()
      .includes(search.toLowerCase())
  );

  return (
    <ScrollView
      style={styles.container}
      contentContainerStyle={styles.content}
    >

      {/* HEADER */}

      <Text style={styles.title}>
        Find Branch
      </Text>

      <Text style={styles.subtitle}>
        Find a TechFix branch near you
      </Text>


      {/* SEARCH */}

      <TextInput
        style={styles.searchInput}
        placeholder="🔍 Search branch..."
        value={search}
        onChangeText={setSearch}
      />


      {/* BRANCHES */}

      {filteredBranches.length === 0 ? (

        <View style={styles.emptyBox}>
          <Text style={styles.emptyText}>
            No branches found.
          </Text>
        </View>

      ) : (

        filteredBranches.map((branch) => (

          <View
            key={branch.id}
            style={styles.card}
          >

            <Text style={styles.branchName}>
              📍 {branch.name}
            </Text>


            <Text style={styles.label}>
              Address
            </Text>

            <Text style={styles.value}>
              {branch.address}
            </Text>


            <Text style={styles.label}>
              Phone
            </Text>

            <Text style={styles.value}>
              📞 {branch.phone}
            </Text>


            <Text style={styles.label}>
              Opening Hours
            </Text>

            <Text style={styles.value}>
              🕐 {branch.hours}
            </Text>


            <Pressable
            style={styles.locationButton}
            onPress={() =>
                router.push({
                pathname: '/branch-map',
                params: {
                    name: branch.name,
                    address: branch.address,
                    latitude: branch.latitude.toString(),
                    longitude: branch.longitude.toString(),
                },
                })
            }
            >
            <Text style={styles.locationButtonText}>
                📍 VIEW LOCATION
            </Text>
            </Pressable>

          </View>

        ))

      )}

    </ScrollView>
  );
}


const styles = StyleSheet.create({

  container: {
    flex: 1,
    backgroundColor: '#F8FAFC',
  },

  content: {
    padding: 20,
    paddingTop: 30,
    paddingBottom: 40,
  },

  title: {
    fontSize: 30,
    fontWeight: 'bold',
  },

  subtitle: {
    fontSize: 15,
    color: '#64748B',
    marginTop: 8,
    marginBottom: 20,
  },

  searchInput: {
    backgroundColor: '#FFFFFF',
    borderWidth: 1,
    borderColor: '#CBD5E1',
    borderRadius: 12,
    padding: 15,
    fontSize: 16,
    marginBottom: 20,
  },

  card: {
    backgroundColor: '#FFFFFF',
    borderRadius: 15,
    padding: 20,
    marginBottom: 15,
    borderWidth: 1,
    borderColor: '#E2E8F0',
  },

  branchName: {
    fontSize: 21,
    fontWeight: 'bold',
  },

  label: {
    fontSize: 13,
    color: '#64748B',
    marginTop: 15,
  },

  value: {
    fontSize: 16,
    fontWeight: '500',
    marginTop: 4,
  },

  locationButton: {
    backgroundColor: '#0A7EA4',
    padding: 15,
    borderRadius: 10,
    marginTop: 20,
  },

  locationButtonText: {
    color: '#FFFFFF',
    textAlign: 'center',
    fontWeight: 'bold',
  },

  emptyBox: {
    backgroundColor: '#FFFFFF',
    padding: 30,
    borderRadius: 15,
    alignItems: 'center',
  },

  emptyText: {
    fontSize: 16,
    color: '#64748B',
  },

});