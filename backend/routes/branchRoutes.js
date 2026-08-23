const express = require('express');
const router = express.Router();
const branchController = require('../controllers/branchController');

// IMPORTANT: /nearby must come BEFORE /:id, otherwise Express thinks "nearby" is an :id value
router.get('/nearby', branchController.getNearbyBranches);
router.get('/', branchController.getAllBranches);
router.get('/:id', branchController.getBranchById);

module.exports = router;