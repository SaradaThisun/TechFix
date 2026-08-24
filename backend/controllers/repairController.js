const pool = require('../config/db');

// Haversine distance in km between two lat/lng points
function distanceKm(lat1, lng1, lat2, lng2) {
    const R = 6371;
    const dLat = (lat2 - lat1) * Math.PI / 180;
    const dLng = (lng2 - lng1) * Math.PI / 180;
    const a =
        Math.sin(dLat / 2) * Math.sin(dLat / 2) +
        Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) *
        Math.sin(dLng / 2) * Math.sin(dLng / 2);
    const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    return R * c;
}

// Finds the nearest branch to (lat, lng) that has:
//  - an available technician specializing in the given device category
//  - at least 1 spare part in stock for that device category
// Falls back to the plain nearest branch if no branch fully qualifies.
async function findBestBranch(lat, lng, deviceCategoryId) {
    const [branches] = await pool.query('SELECT * FROM branches');

    const scored = branches.map(b => ({
        ...b,
        distance_km: distanceKm(lat, lng, b.latitude, b.longitude)
    })).sort((a, b) => a.distance_km - b.distance_km);

    for (const branch of scored) {
        const [techs] = await pool.query(
            `SELECT * FROM technicians
             WHERE branch_id = ? AND specialization = (SELECT name FROM device_categories WHERE id = ?)
             AND is_available = TRUE LIMIT 1`,
            [branch.id, deviceCategoryId]
        );
        const [parts] = await pool.query(
            `SELECT * FROM spare_parts
             WHERE branch_id = ? AND device_category_id = ? AND quantity > 0 LIMIT 1`,
            [branch.id, deviceCategoryId]
        );

        if (techs.length > 0 && parts.length > 0) {
            return { branch, technician: techs[0] };
        }
    }

    return { branch: scored[0], technician: null };
}

// POST /api/repair-requests
exports.createRepairRequest = async (req, res) => {
    try {
        const {
            repair_service_id, device_category_id, device_model,
            issue_description, customer_lat, customer_lng, requested_date
        } = req.body;

        if (!repair_service_id || !device_category_id || !customer_lat || !customer_lng || !requested_date) {
            return res.status(400).json({ error: 'Missing required fields' });
        }

        const { branch, technician } = await findBestBranch(
            parseFloat(customer_lat), parseFloat(customer_lng), device_category_id
        );

        const device_image_url = req.file ? `/uploads/${req.file.filename}` : null;

        const [result] = await pool.query(
            `INSERT INTO repair_requests
             (user_id, branch_id, technician_id, repair_service_id, device_category_id,
              device_model, issue_description, device_image_url, customer_lat, customer_lng,
              status, requested_date)
             VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)`,
            [
                req.userId, branch.id, technician ? technician.id : null,
                repair_service_id, device_category_id, device_model || null,
                issue_description || null, device_image_url, customer_lat, customer_lng,
                technician ? 'ASSIGNED' : 'PENDING', requested_date
            ]
        );

        await pool.query(
            'INSERT INTO repair_status_history (repair_request_id, status, notes) VALUES (?, ?, ?)',
            [result.insertId, technician ? 'ASSIGNED' : 'PENDING',
             technician ? `Assigned to ${branch.name}, technician ${technician.name}` : `Assigned to ${branch.name}, awaiting technician`]
        );

        res.status(201).json({
            id: result.insertId,
            branch,
            technician,
            status: technician ? 'ASSIGNED' : 'PENDING'
        });
    } catch (err) {
        console.error(err);
        res.status(500).json({ error: 'Failed to create repair request' });
    }
};

// GET /api/repair-requests  (current user's repair history)
exports.getMyRepairRequests = async (req, res) => {
    try {
        const [rows] = await pool.query(
            `SELECT rr.*, b.name AS branch_name, rs.name AS service_name, rs.price,
                    dc.name AS category_name, t.name AS technician_name
             FROM repair_requests rr
             JOIN branches b ON b.id = rr.branch_id
             JOIN repair_services rs ON rs.id = rr.repair_service_id
             JOIN device_categories dc ON dc.id = rr.device_category_id
             LEFT JOIN technicians t ON t.id = rr.technician_id
             WHERE rr.user_id = ?
             ORDER BY rr.created_at DESC`,
            [req.userId]
        );
        res.json(rows);
    } catch (err) {
        console.error(err);
        res.status(500).json({ error: 'Failed to fetch repair requests' });
    }
};

// GET /api/repair-requests/:id
exports.getRepairRequestById = async (req, res) => {
    try {
        const [rows] = await pool.query(
            `SELECT rr.*, b.name AS branch_name, b.address AS branch_address,
                    rs.name AS service_name, rs.price, dc.name AS category_name,
                    t.name AS technician_name, t.phone AS technician_phone
             FROM repair_requests rr
             JOIN branches b ON b.id = rr.branch_id
             JOIN repair_services rs ON rs.id = rr.repair_service_id
             JOIN device_categories dc ON dc.id = rr.device_category_id
             LEFT JOIN technicians t ON t.id = rr.technician_id
             WHERE rr.id = ? AND rr.user_id = ?`,
            [req.params.id, req.userId]
        );
        if (rows.length === 0) return res.status(404).json({ error: 'Repair request not found' });

        const [history] = await pool.query(
            'SELECT * FROM repair_status_history WHERE repair_request_id = ? ORDER BY updated_at ASC',
            [req.params.id]
        );

        res.json({ ...rows[0], history });
    } catch (err) {
        console.error(err);
        res.status(500).json({ error: 'Failed to fetch repair request' });
    }
};