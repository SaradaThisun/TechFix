import { useState } from 'react';
import { router } from 'expo-router';
import DateTimePicker from '@react-native-community/datetimepicker';
import * as ImagePicker from 'expo-image-picker';

import {
  StyleSheet,
  Text,
  View,
  TextInput,
  Pressable,
  ScrollView,
  Alert,
  Modal,
  Image,
} from 'react-native';

import API from '../services/api';

export default function AppointmentScreen() {

  const [deviceType, setDeviceType] = useState('');
  const [deviceModel, setDeviceModel] = useState('');

  const [deviceImage, setDeviceImage] = useState<string | null>(null);

  const [repairService, setRepairService] = useState('');
  const [branch, setBranch] = useState('');
  const [date, setDate] = useState('');
  const [time, setTime] = useState('');

  const [selectedDate, setSelectedDate] = useState(new Date());
  const [showDatePicker, setShowDatePicker] = useState(false);

  const [selectedTime, setSelectedTime] = useState(new Date());
  const [showTimePicker, setShowTimePicker] = useState(false);

  const [problem, setProblem] = useState('');

  const [deviceTypeModal, setDeviceTypeModal] = useState(false);
  const [repairServiceModal, setRepairServiceModal] = useState(false);
  const [branchModal, setBranchModal] = useState(false);


  // IMAGE PICKER

  const pickDeviceImage = async () => {
    const permission =
      await ImagePicker.requestMediaLibraryPermissionsAsync();

    if (!permission.granted) {
      Alert.alert(
        'Permission Required',
        'Please allow photo access to add a device image.'
      );

      return;
    }

    const result =
      await ImagePicker.launchImageLibraryAsync({
        mediaTypes: ['images'],
        allowsEditing: true,
        aspect: [4, 3],
        quality: 0.8,
      });

    if (!result.canceled) {
      setDeviceImage(result.assets[0].uri);
    }
  };


  // SUBMIT APPOINTMENT

const handleSubmit = async () => {

  if (
    !deviceType ||
    !deviceModel ||
    !repairService ||
    !branch ||
    !date ||
    !time ||
    !problem
  ) {
    Alert.alert(
      'Missing Information',
      'Please fill in all fields before booking.'
    );

    return;
  }


  try {

    const response = await API.post(
      '/appointments',
      {
        deviceType,
        deviceModel,
        repairService,
        branch,
        date,
        time,
        problem,
        deviceImage: deviceImage || '',
      }
    );


    console.log(
      'Appointment response:',
      response.data
    );


    Alert.alert(
      'Appointment Submitted',
      `Appointment ${response.data.appointment.appointmentNumber} was created successfully.`
    );


  } catch (error) {

    console.error(
      'Appointment error:',
      error
    );


    Alert.alert(
      'Booking Failed',
      'Could not connect to the server. Please try again.'
    );

  }

};

  return (
    <ScrollView
      style={styles.container}
      contentContainerStyle={styles.content}
    >

      <Text style={styles.title}>
        Book Repair Appointment
      </Text>

      <Text style={styles.subtitle}>
        Enter your device and repair details
      </Text>


      {/* DEVICE TYPE */}

      <Text style={styles.label}>
        Device Type
      </Text>

      <Pressable
        style={styles.input}
        onPress={() => setDeviceTypeModal(true)}
      >
        <Text
          style={
            deviceType
              ? styles.selectedText
              : styles.placeholderText
          }
        >
          {deviceType || 'Select device type'}
        </Text>
      </Pressable>


      {/* DEVICE TYPE MODAL */}

      <Modal
        visible={deviceTypeModal}
        transparent={true}
        animationType="slide"
        onRequestClose={() => setDeviceTypeModal(false)}
      >

        <View style={styles.modalOverlay}>

          <View style={styles.modalBox}>

            <Text style={styles.modalTitle}>
              Select Device Type
            </Text>


            <Pressable
              style={styles.option}
              onPress={() => {
                setDeviceType('Laptop');
                setDeviceTypeModal(false);
              }}
            >
              <Text style={styles.optionText}>
                💻 Laptop
              </Text>
            </Pressable>


            <Pressable
              style={styles.option}
              onPress={() => {
                setDeviceType('Desktop');
                setDeviceTypeModal(false);
              }}
            >
              <Text style={styles.optionText}>
                🖥️ Desktop
              </Text>
            </Pressable>


            <Pressable
              style={styles.option}
              onPress={() => {
                setDeviceType('Mobile Phone');
                setDeviceTypeModal(false);
              }}
            >
              <Text style={styles.optionText}>
                📱 Mobile Phone
              </Text>
            </Pressable>


            <Pressable
              style={styles.option}
              onPress={() => {
                setDeviceType('Tablet');
                setDeviceTypeModal(false);
              }}
            >
              <Text style={styles.optionText}>
                📱 Tablet
              </Text>
            </Pressable>


            <Pressable
              style={styles.cancelButton}
              onPress={() => setDeviceTypeModal(false)}
            >
              <Text style={styles.cancelText}>
                Cancel
              </Text>
            </Pressable>

          </View>

        </View>

      </Modal>


      {/* DEVICE MODEL */}

      <Text style={styles.label}>
        Device Model
      </Text>

      <TextInput
        style={styles.input}
        placeholder="Example: Dell Inspiron 15"
        value={deviceModel}
        onChangeText={setDeviceModel}
      />


{/* REPAIR SERVICE */}

<Text style={styles.label}>
  Repair Service
</Text>

<Pressable
  style={styles.input}
  onPress={() => setRepairServiceModal(true)}
>
  <Text
    style={
      repairService
        ? styles.selectedText
        : styles.placeholderText
    }
  >
    {repairService || 'Select repair service'}
  </Text>
</Pressable>


{/* REPAIR SERVICE MODAL */}

<Modal
  visible={repairServiceModal}
  transparent={true}
  animationType="slide"
  onRequestClose={() => setRepairServiceModal(false)}
>
  <View style={styles.modalOverlay}>

    <View style={styles.modalBox}>

      <Text style={styles.modalTitle}>
        Select Repair Service
      </Text>

      <Pressable
        style={styles.option}
        onPress={() => {
          setRepairService('Screen Replacement');
          setRepairServiceModal(false);
        }}
      >
        <Text style={styles.optionText}>
          🖥️ Screen Replacement
        </Text>
      </Pressable>

      <Pressable
        style={styles.option}
        onPress={() => {
          setRepairService('Keyboard Replacement');
          setRepairServiceModal(false);
        }}
      >
        <Text style={styles.optionText}>
          ⌨️ Keyboard Replacement
        </Text>
      </Pressable>

      <Pressable
        style={styles.option}
        onPress={() => {
          setRepairService('SSD Upgrade');
          setRepairServiceModal(false);
        }}
      >
        <Text style={styles.optionText}>
          💾 SSD Upgrade
        </Text>
      </Pressable>

      <Pressable
        style={styles.option}
        onPress={() => {
          setRepairService('Battery Replacement');
          setRepairServiceModal(false);
        }}
      >
        <Text style={styles.optionText}>
          🔋 Battery Replacement
        </Text>
      </Pressable>

      <Pressable
        style={styles.option}
        onPress={() => {
          setRepairService('General Repair');
          setRepairServiceModal(false);
        }}
      >
        <Text style={styles.optionText}>
          🔧 General Repair
        </Text>
      </Pressable>

      <Pressable
        style={styles.cancelButton}
        onPress={() => setRepairServiceModal(false)}
      >
        <Text style={styles.cancelText}>
          Cancel
        </Text>
      </Pressable>

    </View>

  </View>
</Modal>


{/* BRANCH */}

<Text style={styles.label}>
  Preferred Branch
</Text>

<Pressable
  style={styles.input}
  onPress={() => setBranchModal(true)}
>
  <Text
    style={
      branch
        ? styles.selectedText
        : styles.placeholderText
    }
  >
    {branch || 'Select preferred branch'}
  </Text>
</Pressable>

{/* BRANCH MODAL */}

<Modal
  visible={branchModal}
  transparent={true}
  animationType="slide"
  onRequestClose={() => setBranchModal(false)}
>
  <View style={styles.modalOverlay}>

    <View style={styles.modalBox}>

      <Text style={styles.modalTitle}>
        Select Preferred Branch
      </Text>

      <Pressable
        style={styles.option}
        onPress={() => {
          setBranch('Colombo Branch');
          setBranchModal(false);
        }}
      >
        <Text style={styles.optionText}>
          📍 Colombo Branch
        </Text>
      </Pressable>

      <Pressable
        style={styles.option}
        onPress={() => {
          setBranch('Galle Branch');
          setBranchModal(false);
        }}
      >
        <Text style={styles.optionText}>
          📍 Galle Branch
        </Text>
      </Pressable>

      <Pressable
        style={styles.cancelButton}
        onPress={() => setBranchModal(false)}
      >
        <Text style={styles.cancelText}>
          Cancel
        </Text>
      </Pressable>

    </View>

  </View>
</Modal>


{/* DATE */}

<Text style={styles.label}>
  Preferred Date
</Text>

<Pressable
  style={styles.input}
  onPress={() => setShowDatePicker(true)}
>
  <Text
    style={
      date
        ? styles.selectedText
        : styles.placeholderText
    }
  >
    {date || 'Select preferred date'}
  </Text>
</Pressable>

{showDatePicker && (
  <DateTimePicker
    value={selectedDate}
    mode="date"
    display="default"
    minimumDate={new Date()}
    onChange={(event, selected) => {

      setShowDatePicker(false);

      if (selected) {

        setSelectedDate(selected);

        const day = String(
          selected.getDate()
        ).padStart(2, '0');

        const month = String(
          selected.getMonth() + 1
        ).padStart(2, '0');

        const year = selected.getFullYear();

        setDate(
          `${day}/${month}/${year}`
        );
      }
    }}
  />
)}


{/* TIME */}

<Text style={styles.label}>
  Preferred Time
</Text>

<Pressable
  style={styles.input}
  onPress={() => setShowTimePicker(true)}
>
  <Text
    style={
      time
        ? styles.selectedText
        : styles.placeholderText
    }
  >
    {time || 'Select preferred time'}
  </Text>
</Pressable>

{showTimePicker && (
  <DateTimePicker
    value={selectedTime}
    mode="time"
    display="default"
    onChange={(event, selected) => {

      setShowTimePicker(false);

      if (selected) {

        setSelectedTime(selected);

        let hours = selected.getHours();
        const minutes = String(
          selected.getMinutes()
        ).padStart(2, '0');

        const ampm = hours >= 12 ? 'PM' : 'AM';

        hours = hours % 12;

        if (hours === 0) {
          hours = 12;
        }

        setTime(
          `${hours}:${minutes} ${ampm}`
        );
      }
    }}
  />
)}


      {/* PROBLEM */}

      <Text style={styles.label}>
        Problem Description
      </Text>

      <TextInput
        style={[
          styles.input,
          styles.descriptionInput,
        ]}
        placeholder="Describe the problem with your device"
        value={problem}
        onChangeText={setProblem}
        multiline
      />


      {/* IMAGE */}

        <Pressable
        style={styles.imageButton}
        onPress={pickDeviceImage}
        >
        <Text style={styles.imageButtonText}>
            📷 Add Device Image
        </Text>
        </Pressable>

        {deviceImage && (
        <Image
            source={{ uri: deviceImage }}
            style={styles.imagePreview}
        />
        )}


      {/* SUBMIT */}

      <Pressable
        style={styles.submitButton}
        onPress={handleSubmit}
      >
        <Text style={styles.submitButtonText}>
          BOOK APPOINTMENT
        </Text>
      </Pressable>

    </ScrollView>
  );
}


const styles = StyleSheet.create({

  container: {
    flex: 1,
    backgroundColor: '#FFFFFF',
  },

  content: {
    padding: 20,
    paddingTop: 30,
    paddingBottom: 40,
  },

  title: {
    fontSize: 28,
    fontWeight: 'bold',
  },

  subtitle: {
    fontSize: 15,
    marginTop: 8,
    marginBottom: 25,
  },

  label: {
    fontSize: 16,
    fontWeight: '600',
    marginBottom: 8,
    marginTop: 12,
  },

  input: {
    borderWidth: 1,
    borderColor: '#CBD5E1',
    borderRadius: 10,
    padding: 14,
    fontSize: 16,
    backgroundColor: '#FFFFFF',
  },

  placeholderText: {
    color: '#64748B',
    fontSize: 16,
  },

  selectedText: {
    color: '#111827',
    fontSize: 16,
  },

  descriptionInput: {
    height: 120,
    textAlignVertical: 'top',
  },

  imageButton: {
    padding: 15,
    borderRadius: 10,
    marginTop: 20,
    backgroundColor: '#E2E8F0',
  },

  imageButtonText: {
    textAlign: 'center',
    fontSize: 16,
    fontWeight: '600',
  },

  imagePreview: {
  width: '100%',
  height: 220,
  borderRadius: 12,
  marginTop: 15,
  },

  submitButton: {
    padding: 17,
    borderRadius: 10,
    marginTop: 15,
    backgroundColor: '#0A7EA4',
  },

  submitButtonText: {
    color: '#FFFFFF',
    textAlign: 'center',
    fontWeight: 'bold',
    fontSize: 16,
  },

  modalOverlay: {
    flex: 1,
    backgroundColor: 'rgba(0,0,0,0.5)',
    justifyContent: 'center',
    padding: 20,
  },

  modalBox: {
    backgroundColor: '#FFFFFF',
    borderRadius: 15,
    padding: 20,
  },

  modalTitle: {
    fontSize: 22,
    fontWeight: 'bold',
    marginBottom: 15,
  },

  option: {
    padding: 16,
    borderBottomWidth: 1,
    borderBottomColor: '#E2E8F0',
  },

  optionText: {
    fontSize: 17,
  },

  cancelButton: {
    marginTop: 15,
    padding: 15,
    borderRadius: 10,
    backgroundColor: '#E2E8F0',
  },

  cancelText: {
    textAlign: 'center',
    fontWeight: 'bold',
    fontSize: 16,
  },

});