const express = require('express');
const router = express.Router();
const repairController = require('../controllers/repairController');
const authenticate = require('../middleware/auth');
const upload = require('../middleware/upload');

router.use(authenticate); // every route below requires login

router.post('/', upload.single('device_image'), repairController.createRepairRequest);
router.get('/', repairController.getMyRepairRequests);
router.get('/:id', repairController.getRepairRequestById);

module.exports = router;