const express = require('express');
const router = express.Router();
const catalogController = require('../controllers/catalogController');

router.get('/categories', catalogController.getCategories);
router.get('/services', catalogController.getServices);
router.get('/services/:id', catalogController.getServiceById);

module.exports = router;