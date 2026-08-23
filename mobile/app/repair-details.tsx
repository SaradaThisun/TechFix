import { router, useLocalSearchParams } from 'expo-router';

import {
  StyleSheet,
  Text,
  View,
  Pressable,
  ScrollView,
} from 'react-native';

export default function RepairDetailsScreen() {

  const {
    id,
    device,
    service,
    branch,
    date,
    time,
    status,
  } = useLocalSearchParams();

  return (
    <ScrollView
      style={styles.container}
      contentContainerStyle={styles.content}
    >

      {/* HEADER */}

      <Text style={styles.title}>
        Repair Details
      </Text>

      <Text style={styles.subtitle}>
        Track your device repair progress
      </Text>


      {/* APPOINTMENT */}

      <View style={styles.card}>

        <View style={styles.headerRow}>

          <Text style={styles.appointmentId}>
            #{id}
          </Text>

          <View style={styles.statusBox}>
            <Text style={styles.statusText}>
              🟡 {status}
            </Text>
          </View>

        </View>

      </View>


      {/* DEVICE */}

      <View style={styles.card}>

        <Text style={styles.cardTitle}>
          Device Information
        </Text>

        <Text style={styles.label}>
          Device
        </Text>

        <Text style={styles.value}>
          {device}
        </Text>


        <Text style={styles.label}>
          Repair Service
        </Text>

        <Text style={styles.value}>
          🔧 {service}
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

      </View>


      {/* TRACKING */}

      <View style={styles.card}>

        <Text style={styles.cardTitle}>
          Repair Progress
        </Text>


        {/* STEP 1 */}

        <View style={styles.timelineRow}>

          <View style={styles.timelineLeft}>

            <View style={styles.completedCircle}>
              <Text style={styles.circleText}>
                ✓
              </Text>
            </View>

            <View style={styles.line} />

          </View>

          <View style={styles.timelineContent}>

            <Text style={styles.stepTitle}>
              Request Submitted
            </Text>

            <Text style={styles.stepText}>
              Your repair request has been received.
            </Text>

          </View>

        </View>


        {/* STEP 2 */}

        <View style={styles.timelineRow}>

          <View style={styles.timelineLeft}>

            <View style={styles.completedCircle}>
              <Text style={styles.circleText}>
                ✓
              </Text>
            </View>

            <View style={styles.line} />

          </View>

          <View style={styles.timelineContent}>

            <Text style={styles.stepTitle}>
              Appointment Confirmed
            </Text>

            <Text style={styles.stepText}>
              Your appointment has been confirmed.
            </Text>

          </View>

        </View>


        {/* STEP 3 */}

        <View style={styles.timelineRow}>

          <View style={styles.timelineLeft}>

            <View style={styles.currentCircle}>
              <Text style={styles.circleText}>
                3
              </Text>
            </View>

            <View style={styles.line} />

          </View>

          <View style={styles.timelineContent}>

            <Text style={styles.currentTitle}>
              Device Received
            </Text>

            <Text style={styles.stepText}>
              Waiting for the device to be received at the branch.
            </Text>

          </View>

        </View>


        {/* STEP 4 */}

        <View style={styles.timelineRow}>

          <View style={styles.timelineLeft}>

            <View style={styles.pendingCircle}>
              <Text style={styles.pendingText}>
                4
              </Text>
            </View>

            <View style={styles.line} />

          </View>

          <View style={styles.timelineContent}>

            <Text style={styles.pendingTitle}>
              Under Repair
            </Text>

            <Text style={styles.stepText}>
              Technician will diagnose and repair the device.
            </Text>

          </View>

        </View>


        {/* STEP 5 */}

        <View style={styles.timelineRow}>

          <View style={styles.timelineLeft}>

            <View style={styles.pendingCircle}>
              <Text style={styles.pendingText}>
                5
              </Text>
            </View>

          </View>

          <View style={styles.timelineContent}>

            <Text style={styles.pendingTitle}>
              Ready for Collection
            </Text>

            <Text style={styles.stepText}>
              You will be notified when your device is ready.
            </Text>

          </View>

        </View>

      </View>


      {/* BACK BUTTON */}

      <Pressable
        style={styles.button}
        onPress={() => router.back()}
      >
        <Text style={styles.buttonText}>
          BACK TO MY REPAIRS
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

  card: {
    backgroundColor: '#FFFFFF',
    borderRadius: 15,
    padding: 20,
    marginBottom: 15,
    borderWidth: 1,
    borderColor: '#E2E8F0',
  },

  headerRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
  },

  appointmentId: {
    fontSize: 24,
    fontWeight: 'bold',
  },

  statusBox: {
    backgroundColor: '#FEF3C7',
    paddingHorizontal: 12,
    paddingVertical: 7,
    borderRadius: 20,
  },

  statusText: {
    fontSize: 12,
    fontWeight: 'bold',
    color: '#92400E',
  },

  cardTitle: {
    fontSize: 20,
    fontWeight: 'bold',
    marginBottom: 10,
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

  timelineRow: {
    flexDirection: 'row',
    minHeight: 85,
  },

  timelineLeft: {
    width: 40,
    alignItems: 'center',
  },

  completedCircle: {
    width: 30,
    height: 30,
    borderRadius: 15,
    backgroundColor: '#16A34A',
    justifyContent: 'center',
    alignItems: 'center',
  },

  currentCircle: {
    width: 30,
    height: 30,
    borderRadius: 15,
    backgroundColor: '#0A7EA4',
    justifyContent: 'center',
    alignItems: 'center',
  },

  pendingCircle: {
    width: 30,
    height: 30,
    borderRadius: 15,
    backgroundColor: '#E2E8F0',
    justifyContent: 'center',
    alignItems: 'center',
  },

  circleText: {
    color: '#FFFFFF',
    fontWeight: 'bold',
  },

  pendingText: {
    color: '#64748B',
    fontWeight: 'bold',
  },

  line: {
    width: 2,
    flex: 1,
    backgroundColor: '#CBD5E1',
    marginVertical: 4,
  },

  timelineContent: {
    flex: 1,
    paddingLeft: 10,
    paddingBottom: 15,
  },

  stepTitle: {
    fontSize: 16,
    fontWeight: 'bold',
  },

  currentTitle: {
    fontSize: 16,
    fontWeight: 'bold',
  },

  pendingTitle: {
    fontSize: 16,
    fontWeight: '600',
    color: '#64748B',
  },

  stepText: {
    fontSize: 13,
    color: '#64748B',
    marginTop: 4,
    lineHeight: 19,
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
    fontWeight: 'bold',
    fontSize: 16,
  },

});