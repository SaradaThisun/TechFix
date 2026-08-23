import { useLocalSearchParams, router } from 'expo-router';

import {
  StyleSheet,
  Text,
  View,
  Pressable,
  ScrollView,
  Image,
} from 'react-native';

export default function ConfirmationScreen() {

  const {
    deviceType,
    deviceModel,
    repairService,
    branch,
    date,
    time,
    problem,
    deviceImage,
  } = useLocalSearchParams();

  return (
    <ScrollView
      style={styles.container}
      contentContainerStyle={styles.content}
    >

      {/* SUCCESS */}

      <View style={styles.successBox}>

        <Text style={styles.successIcon}>
          ✅
        </Text>

        <Text style={styles.successTitle}>
          Booking Confirmed!
        </Text>

        <Text style={styles.successText}>
          Your repair appointment has been submitted successfully.
        </Text>

      </View>


      {/* APPOINTMENT NUMBER */}

      <View style={styles.card}>

        <Text style={styles.cardTitle}>
          Appointment Details
        </Text>

        <Text style={styles.appointmentNumber}>
          #TF1001
        </Text>

        <View style={styles.statusBox}>

          <Text style={styles.statusLabel}>
            Status
          </Text>

          <Text style={styles.status}>
            🟡 REQUESTED
          </Text>

        </View>

      </View>


      {/* DEVICE INFORMATION */}

      <View style={styles.card}>

        <Text style={styles.cardTitle}>
          Device Information
        </Text>

        <Text style={styles.label}>
          Device Type
        </Text>

        <Text style={styles.value}>
          {deviceType}
        </Text>


        <Text style={styles.label}>
          Device Model
        </Text>

        <Text style={styles.value}>
          {deviceModel}
        </Text>


        <Text style={styles.label}>
          Repair Service
        </Text>

        <Text style={styles.value}>
          {repairService}
        </Text>

      </View>


      {/* APPOINTMENT INFORMATION */}

      <View style={styles.card}>

        <Text style={styles.cardTitle}>
          Appointment Information
        </Text>

        <Text style={styles.label}>
          Branch
        </Text>

        <Text style={styles.value}>
          📍 {branch}
        </Text>


        <Text style={styles.label}>
          Date
        </Text>

        <Text style={styles.value}>
          📅 {date}
        </Text>


        <Text style={styles.label}>
          Time
        </Text>

        <Text style={styles.value}>
          🕐 {time}
        </Text>


        <Text style={styles.label}>
          Problem
        </Text>

        <Text style={styles.value}>
          {problem}
        </Text>

      </View>


      {/* IMAGE */}

      {deviceImage && (
        <View style={styles.card}>

          <Text style={styles.cardTitle}>
            Device Image
          </Text>

          <Image
            source={{ uri: String(deviceImage) }}
            style={styles.deviceImage}
          />

        </View>
      )}


      {/* BUTTON */}

        <Pressable
        style={styles.button}
        onPress={() => router.replace('/my-repairs')}
        >
        <Text style={styles.buttonText}>
            VIEW MY REPAIRS
        </Text>
        </Pressable>


      <Pressable
        style={styles.secondaryButton}
        onPress={() => router.replace('/appointment')}
      >
        <Text style={styles.secondaryButtonText}>
          BOOK ANOTHER REPAIR
        </Text>
      </Pressable>

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

  successBox: {
    alignItems: 'center',
    marginBottom: 20,
  },

  successIcon: {
    fontSize: 55,
  },

  successTitle: {
    fontSize: 28,
    fontWeight: 'bold',
    marginTop: 10,
  },

  successText: {
    fontSize: 15,
    textAlign: 'center',
    marginTop: 8,
    color: '#64748B',
  },

  card: {
    backgroundColor: '#FFFFFF',
    borderRadius: 15,
    padding: 20,
    marginBottom: 15,
    borderWidth: 1,
    borderColor: '#E2E8F0',
  },

  cardTitle: {
    fontSize: 20,
    fontWeight: 'bold',
    marginBottom: 15,
  },

  appointmentNumber: {
    fontSize: 22,
    fontWeight: 'bold',
  },

  statusBox: {
    marginTop: 15,
    padding: 12,
    borderRadius: 10,
    backgroundColor: '#FEF3C7',
  },

  statusLabel: {
    fontSize: 13,
    color: '#92400E',
  },

  status: {
    fontSize: 16,
    fontWeight: 'bold',
    marginTop: 4,
  },

  label: {
    fontSize: 14,
    color: '#64748B',
    marginTop: 12,
  },

  value: {
    fontSize: 17,
    fontWeight: '600',
    marginTop: 4,
  },

  deviceImage: {
    width: '100%',
    height: 220,
    borderRadius: 12,
  },

  button: {
    backgroundColor: '#0A7EA4',
    padding: 17,
    borderRadius: 12,
    marginTop: 5,
  },

  buttonText: {
    color: '#FFFFFF',
    textAlign: 'center',
    fontSize: 16,
    fontWeight: 'bold',
  },

  secondaryButton: {
    padding: 17,
    borderRadius: 12,
    marginTop: 10,
    backgroundColor: '#E2E8F0',
  },

  secondaryButtonText: {
    textAlign: 'center',
    fontSize: 16,
    fontWeight: 'bold',
  },

});