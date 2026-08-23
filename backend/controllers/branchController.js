const pool = require('../config/db');

exports.getAllBranches = async (req, res) => {
    try {
        const [rows] = await pool.query('SELECT * FROM branches ORDER BY name');
        res.json(rows);
    } catch (err) {
        console.error(err);
        res.status(500).json({ error: 'Failed to fetch branches' });
    }
};

exports.getBranchById = async (req, res) => {
    try {
        const [rows] = await pool.query('SELECT * FROM branches WHERE id = ?', [req.params.id]);
        if (rows.length === 0) return res.status(404).json({ error: 'Branch not found' });
        res.json(rows[0]);
    } catch (err) {
        console.error(err);
        res.status(500).json({ error: 'Failed to fetch branch' });
    }
};

// Returns branches sorted by distance from a given lat/lng, using the Haversine formula
// GET /api/branches/nearby?lat=..&lng=..
exports.getNearbyBranches = async (req, res) => {
    try {
        const { lat, lng } = req.query;
        if (!lat || !lng) return res.status(400).json({ error: 'lat and lng query params are required' });

        const [rows] = await pool.query(
            `SELECT *,
                (6371 * acos(
                    cos(radians(?)) * cos(radians(latitude)) *
                    cos(radians(longitude) - radians(?)) +
                    sin(radians(?)) * sin(radians(latitude))
                )) AS distance_km
             FROM branches
             ORDER BY distance_km ASC`,
            [lat, lng, lat]
        );
        res.json(rows);
    } catch (err) {
        console.error(err);
        res.status(500).json({ error: 'Failed to fetch nearby branches' });
    }
};