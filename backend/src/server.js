const express = require('express');
const cors = require('cors');
const mongoose = require('mongoose');
require('dotenv').config();

const appointmentRoutes = require('./routes/appointmentRoutes');

const app = express();

const PORT = process.env.PORT || 5000;


// Middleware

app.use(cors());

app.use(express.json());


// Test Route

app.get('/', (req, res) => {
  res.json({
    message: 'TechFix Backend is running!',
  });
});


// Appointment Routes

app.use(
  '/api/appointments',
  appointmentRoutes
);


// MongoDB Connection

mongoose
  .connect(process.env.MONGO_URI)
  .then(() => {

    console.log('MongoDB Connected');

    app.listen(PORT, () => {

      console.log(
        `TechFix Backend running on port ${PORT}`
      );

    });

  })
  .catch((error) => {

    console.error(
      'MongoDB connection failed:',
      error.message
    );

  });