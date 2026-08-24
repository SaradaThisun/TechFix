import { useEffect, useState } from 'react';

import {
  StyleSheet,
  Text,
  View,
  Pressable,
  ScrollView,
  ActivityIndicator,
  Alert,
} from 'react-native';

import { router } from 'expo-router';

import API from '../services/api';


export default function MyRepairsScreen() {

  const [repairs, setRepairs] = useState<any[]>([]);

  const [loading, setLoading] = useState(true);


  useEffect(() => {

    loadRepairs();

  }, []);


  const loadRepairs = async () => {

    try {

      console.log('Loading appointments...');

      const response = await API.get('/appointments');

      console.log(
        'Appointments:',
        response.data
      );

      setRepairs(
        response.data.appointments
      );

    } catch (error) {

      console.error(
        'My Repairs Error:',
        error
      );

      Alert.alert(
        'Error',
        'Unable to load repairs.'
      );

    } finally {

      setLoading(false);

    }

  };


  return (

    <ScrollView
      style={styles.container}
      contentContainerStyle={styles.content}
    >

      <Text style={styles.title}>
        My Repairs
      </Text>

      <Text style={styles.subtitle}>
        View and track your repair appointments
      </Text>


      {/* LOADING */}

      {loading && (

        <View style={styles.center}>

          <ActivityIndicator
            size="large"
          />

          <Text style={styles.loadingText}>
            Loading repairs...
          </Text>

        </View>

      )}


      {/* EMPTY */}

      {!loading && repairs.length === 0 && (

        <View style={styles.emptyBox}>

          <Text style={styles.emptyIcon}>
            🔧
          </Text>

          <Text style={styles.emptyTitle}>
            No Repairs Yet
          </Text>

          <Text style={styles.emptyText}>
            Your repair appointments will
            appear here.
          </Text>

          <Pressable
            style={styles.bookButton}
            onPress={() =>
              router.push('/appointment')
            }
          >

            <Text style={styles.bookButtonText}>
              BOOK A REPAIR
            </Text>

          </Pressable>

        </View>

      )}


      {/* REPAIR LIST */}

      {!loading &&
        repairs.map((repair) => (

          <Pressable
            key={repair.appointmentNumber}
            style={styles.card}

            onPress={() =>
              router.push({
                pathname: '/repair-details',

                params: {
                  id: repair.appointmentNumber,

                  device:
                    repair.deviceModel,

                  service:
                    repair.repairService,

                  branch:
                    repair.branch,

                  date:
                    repair.date,

                  time:
                    repair.time,

                  status:
                    repair.status,
                },
              })
            }
          >

            {/* TOP ROW */}

            <View style={styles.topRow}>

              <Text style={styles.appointmentNumber}>
                #{repair.appointmentNumber}
              </Text>

              <View style={styles.statusBox}>

                <Text style={styles.statusText}>
                  {repair.status}
                </Text>

              </View>

            </View>


            {/* DEVICE */}

            <Text style={styles.device}>
              {repair.deviceModel}
            </Text>


            {/* DEVICE TYPE */}

            <Text style={styles.deviceType}>
              {repair.deviceType}
            </Text>


            {/* SERVICE */}

            <Text style={styles.service}>
              🔧 {repair.repairService}
            </Text>


            {/* BRANCH */}

            <Text style={styles.info}>
              📍 {repair.branch}
            </Text>


            {/* DATE */}

            <Text style={styles.info}>
              📅 {repair.date}
            </Text>


            {/* TIME */}

            <Text style={styles.info}>
              🕐 {repair.time}
            </Text>


            {/* DETAILS */}

            <Text style={styles.details}>
              Tap to view details →
            </Text>

          </Pressable>

        ))}

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
    paddingBottom: 50,
  },

  title: {
    fontSize: 30,
    fontWeight: 'bold',
    color: '#111827',
  },

  subtitle: {
    fontSize: 15,
    color: '#64748B',
    marginTop: 8,
    marginBottom: 25,
  },

  center: {
    alignItems: 'center',
    justifyContent: 'center',
    padding: 50,
  },

  loadingText: {
    marginTop: 15,
    fontSize: 16,
    color: '#64748B',
  },

  emptyBox: {
    backgroundColor: '#FFFFFF',
    borderRadius: 16,
    padding: 30,
    alignItems: 'center',
    borderWidth: 1,
    borderColor: '#E2E8F0',
  },

  emptyIcon: {
    fontSize: 45,
    marginBottom: 15,
  },

  emptyTitle: {
    fontSize: 21,
    fontWeight: 'bold',
  },

  emptyText: {
    fontSize: 15,
    color: '#64748B',
    textAlign: 'center',
    marginTop: 10,
    lineHeight: 22,
  },

  bookButton: {
    backgroundColor: '#0A7EA4',
    paddingHorizontal: 25,
    paddingVertical: 14,
    borderRadius: 10,
    marginTop: 20,
  },

  bookButtonText: {
    color: '#FFFFFF',
    fontSize: 14,
    fontWeight: 'bold',
  },

  card: {
    backgroundColor: '#FFFFFF',
    borderRadius: 16,
    padding: 20,
    marginBottom: 15,
    borderWidth: 1,
    borderColor: '#E2E8F0',
  },

  topRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
  },

  appointmentNumber: {
    fontSize: 17,
    fontWeight: 'bold',
    color: '#111827',
  },

  statusBox: {
    backgroundColor: '#FEF3C7',
    paddingHorizontal: 10,
    paddingVertical: 6,
    borderRadius: 20,
  },

  statusText: {
    fontSize: 11,
    fontWeight: 'bold',
    color: '#92400E',
  },

  device: {
    fontSize: 21,
    fontWeight: 'bold',
    marginTop: 18,
    color: '#111827',
  },

  deviceType: {
    fontSize: 14,
    color: '#64748B',
    marginTop: 4,
  },

  service: {
    fontSize: 16,
    color: '#334155',
    marginTop: 12,
    marginBottom: 15,
  },

  info: {
    fontSize: 14,
    color: '#475569',
    marginTop: 7,
  },

  details: {
    color: '#0A7EA4',
    fontWeight: 'bold',
    textAlign: 'right',
    marginTop: 20,
  },

});