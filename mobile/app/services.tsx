import { StyleSheet, Text, View, Pressable } from 'react-native';
import { router } from 'expo-router';

export default function ServicesScreen() {
  return (
    <View style={styles.container}>

      <Text style={styles.title}>
        Repair Services
      </Text>

      <Text style={styles.subtitle}>
        Choose a service for your device
      </Text>

      <Pressable
        style={styles.card}
        onPress={() => router.push('/computer-repair')}
      >
        <Text style={styles.cardTitle}>
          💻 Computer Repair
        </Text>

        <Text style={styles.cardText}>
          Laptop and desktop repair services
        </Text>
      </Pressable>

      <View style={styles.card}>
        <Text style={styles.cardTitle}>
          📱 Mobile Phone Repair
        </Text>

        <Text style={styles.cardText}>
          Smartphone repair and maintenance
        </Text>
      </View>

    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 24,
    paddingTop: 70,
  },

  title: {
    fontSize: 30,
    fontWeight: 'bold',
  },

  subtitle: {
    fontSize: 16,
    marginTop: 8,
    marginBottom: 30,
  },

  card: {
    padding: 20,
    borderRadius: 15,
    marginBottom: 15,
    backgroundColor: '#0A7EA4',
  },

  cardTitle: {
    fontSize: 19,
    fontWeight: 'bold',
    color: '#FFFFFF',
  },

  cardText: {
    fontSize: 14,
    color: '#FFFFFF',
    marginTop: 8,
  },
});