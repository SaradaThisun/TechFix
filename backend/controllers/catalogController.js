const pool = require('../config/db');

exports.getCategories = async (req, res) => {
    try {
        const [rows] = await pool.query('SELECT * FROM device_categories ORDER BY name');
        res.json(rows);
    } catch (err) {
        console.error(err);
        res.status(500).json({ error: 'Failed to fetch categories' });
    }
};

// GET /api/services?category_id=1&search=screen
exports.getServices = async (req, res) => {
    try {
        const { category_id, search } = req.query;
        let query = `SELECT rs.*, dc.name AS category_name
                     FROM repair_services rs
                     JOIN device_categories dc ON dc.id = rs.device_category_id
                     WHERE 1=1`;
        const params = [];

        if (category_id) {
            query += ' AND rs.device_category_id = ?';
            params.push(category_id);
        }
        if (search) {
            query += ' AND rs.name LIKE ?';
            params.push(`%${search}%`);
        }
        query += ' ORDER BY rs.name';

        const [rows] = await pool.query(query, params);
        res.json(rows);
    } catch (err) {
        console.error(err);
        res.status(500).json({ error: 'Failed to fetch services' });
    }
};

exports.getServiceById = async (req, res) => {
    try {
        const [rows] = await pool.query(
            `SELECT rs.*, dc.name AS category_name
             FROM repair_services rs
             JOIN device_categories dc ON dc.id = rs.device_category_id
             WHERE rs.id = ?`,
            [req.params.id]
        );
        if (rows.length === 0) return res.status(404).json({ error: 'Service not found' });
        res.json(rows[0]);
    } catch (err) {
        console.error(err);
        res.status(500).json({ error: 'Failed to fetch service' });
    }
};