const express = require('express');

const {
  createAppointment,
  getAppointments,
} = require('../controllers/appointmentController');

const router = express.Router();


// POST /api/appointments
router.post('/', createAppointment);


// GET /api/appointments
router.get('/', getAppointments);


module.exports = router;