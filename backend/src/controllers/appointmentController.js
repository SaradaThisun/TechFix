const Appointment = require('../models/Appointment');


const createAppointment = async (req, res) => {

  try {

    const appointment = new Appointment({
      appointmentNumber:
        `TF${Date.now().toString().slice(-6)}`,

      deviceType: req.body.deviceType,

      deviceModel: req.body.deviceModel,

      repairService: req.body.repairService,

      branch: req.body.branch,

      date: req.body.date,

      time: req.body.time,

      problem: req.body.problem,

      deviceImage: req.body.deviceImage || '',

      status: 'REQUESTED',
    });


    const savedAppointment =
      await appointment.save();


    res.status(201).json({

      message:
        'Appointment created successfully',

      appointment: savedAppointment,

    });

  } catch (error) {

    console.error(error);

    res.status(500).json({

      message:
        'Failed to create appointment',

      error: error.message,

    });

  }
};


const getAppointments = async (req, res) => {

  try {

    const appointments =
      await Appointment.find()
        .sort({ createdAt: -1 });


    res.json({

      message:
        'Appointments retrieved successfully',

      appointments,

    });

  } catch (error) {

    res.status(500).json({

      message:
        'Failed to retrieve appointments',

      error: error.message,

    });

  }
};


module.exports = {
  createAppointment,
  getAppointments,
};