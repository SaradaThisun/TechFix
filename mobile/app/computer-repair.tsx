import { StyleSheet, Text, View, Pressable } from 'react-native';
import { router } from 'expo-router';

export default function ComputerRepairScreen() {
  return (
    <View style={styles.container}>

      <Text style={styles.title}>
        💻 Computer Repair
      </Text>

      <Text style={styles.subtitle}>
        Select a repair service for your computer
      </Text>

      <View style={styles.serviceCard}>
        <Text style={styles.serviceTitle}>
          🖥️ Screen Replacement
        </Text>

        <Text style={styles.description}>
          Replace damaged or broken laptop screens.
        </Text>

        <Text style={styles.price}>
          Starting from Rs. 15,000
        </Text>

        <Pressable
          style={styles.bookButton}
          onPress={() => router.push('/appointment')}
        >
          <Text style={styles.bookButtonText}>
            Book Repair
          </Text>
        </Pressable>
      </View>

      <View style={styles.serviceCard}>
        <Text style={styles.serviceTitle}>
          ⌨️ Keyboard Replacement
        </Text>

        <Text style={styles.description}>
          Replace damaged or faulty laptop keyboards.
        </Text>

        <Text style={styles.price}>
          Starting from Rs. 8,000
        </Text>

        <Pressable
          style={styles.bookButton}
          onPress={() => router.push('/appointment')}
        >
          <Text style={styles.bookButtonText}>
            Book Repair
          </Text>
        </Pressable>
      </View>

      <View style={styles.serviceCard}>
        <Text style={styles.serviceTitle}>
          💾 SSD Upgrade
        </Text>

        <Text style={styles.description}>
          Upgrade your computer storage with a new SSD.
        </Text>

        <Text style={styles.price}>
          Starting from Rs. 12,000
        </Text>

        <Pressable
          style={styles.bookButton}
          onPress={() => router.push('/appointment')}
        >
          <Text style={styles.bookButtonText}>
            Book Repair
          </Text>
        </Pressable>
      </View>

    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 20,
    paddingTop: 60,
  },

  title: {
    fontSize: 28,
    fontWeight: 'bold',
  },

  subtitle: {
    fontSize: 16,
    marginTop: 8,
    marginBottom: 25,
  },

  serviceCard: {
    padding: 18,
    borderRadius: 15,
    marginBottom: 15,
    backgroundColor: '#F1F5F9',
  },

  serviceTitle: {
    fontSize: 19,
    fontWeight: 'bold',
  },

  description: {
    fontSize: 14,
    marginTop: 8,
    lineHeight: 20,
  },

  price: {
    fontSize: 15,
    fontWeight: 'bold',
    marginTop: 10,
  },

  bookButton: {
    backgroundColor: '#0A7EA4',
    padding: 12,
    borderRadius: 10,
    marginTop: 15,
  },

  bookButtonText: {
    color: '#FFFFFF',
    textAlign: 'center',
    fontWeight: 'bold',
    fontSize: 15,
  },
});