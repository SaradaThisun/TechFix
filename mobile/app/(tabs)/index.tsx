import { StyleSheet, Text, View, Pressable } from 'react-native';
import { router } from 'expo-router';

export default function HomeScreen() {
  return (
    <View style={styles.container}>
      <Text style={styles.logo}>🔧 TECHFIX</Text>

      <Text style={styles.title}>
        Computer & Phone Repair
      </Text>

      <Text style={styles.subtitle}>
        Reliable repair services at your fingertips
      </Text>

      <Text style={styles.question}>
        What do you need?
      </Text>

      <Pressable
  style={styles.button}
  onPress={() => router.push('/services')}
>
  <Text style={styles.buttonText}>
    🔧 Repair Service
  </Text>
</Pressable>

      <Pressable style={styles.button}>
        <Text style={styles.buttonText}>
          📋 My Repairs
        </Text>
      </Pressable>

      <Pressable
        style={styles.button}
        onPress={() => router.push('/branches')}
      >
        <Text style={styles.buttonText}>
          📍 Find Branch
        </Text>
      </Pressable>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 24,
    justifyContent: 'center',
  },

  logo: {
    fontSize: 32,
    fontWeight: 'bold',
    textAlign: 'center',
    marginBottom: 10,
  },

  title: {
    fontSize: 22,
    fontWeight: '600',
    textAlign: 'center',
  },

  subtitle: {
    fontSize: 15,
    textAlign: 'center',
    marginTop: 8,
    marginBottom: 40,
  },

  question: {
    fontSize: 18,
    fontWeight: '600',
    marginBottom: 15,
  },

  button: {
    padding: 18,
    borderRadius: 12,
    marginBottom: 15,
    backgroundColor: '#0A7EA4',
  },

  buttonText: {
    color: '#FFFFFF',
    fontSize: 17,
    fontWeight: '600',
    textAlign: 'center',
  },
});