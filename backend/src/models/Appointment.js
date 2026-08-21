const mongoose = require('mongoose');

const appointmentSchema = new mongoose.Schema(
  {
    appointmentNumber: {
      type: String,
      required: true,
      unique: true,
    },

    deviceType: {
      type: String,
      required: true,
    },

    deviceModel: {
      type: String,
      required: true,
    },

    repairService: {
      type: String,
      required: true,
    },

    branch: {
      type: String,
      required: true,
    },

    date: {
      type: String,
      required: true,
    },

    time: {
      type: String,
      required: true,
    },

    problem: {
      type: String,
      required: true,
    },

    deviceImage: {
      type: String,
      default: '',
    },

    status: {
      type: String,
      default: 'REQUESTED',
    },
  },

  {
    timestamps: true,
  }
);

module.exports = mongoose.model(
  'Appointment',
  appointmentSchema
);