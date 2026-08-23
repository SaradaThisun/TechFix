require('dotenv').config();
const express = require('express');
const cors = require('cors');
const pool = require('./config/db');
const authRoutes = require('./routes/authRoutes');
const branchRoutes = require('./routes/branchRoutes');
const catalogRoutes = require('./routes/catalogRoutes');
const repairRoutes = require('./routes/repairRoutes');

const app = express();
app.use(cors());
app.use(express.json());

const path = require('path');

app.get('/', (req, res) => res.json({ message: 'TechFix API is running' }));
app.use('/api/auth', authRoutes);
app.use('/api/branches', branchRoutes);
app.use('/api', catalogRoutes);
app.use('/api/repair-requests', repairRoutes);

app.use('/uploads', express.static(path.join(__dirname, 'uploads')));

app.get('/api/test-db', async (req, res) => {
    try {
        const [rows] = await pool.query('SELECT COUNT(*) AS branch_count FROM branches');
        res.json({ success: true, branches_in_db: rows[0].branch_count });
    } catch (err) {
        res.status(500).json({ success: false, error: err.message });
    }
});

const PORT = process.env.PORT || 3000;
app.listen(PORT, () => console.log(`TechFix API running on http://localhost:${PORT}`));