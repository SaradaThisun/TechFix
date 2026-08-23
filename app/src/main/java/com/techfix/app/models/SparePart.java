package com.techfix.app.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "spare_parts")
public class SparePart {

    @PrimaryKey
    @NonNull
    private String id = "";

    private String partId;
    private String name;
    private String category;            // Screen Assemblies | Batteries | Charging Ports | Logic Boards | Camera Modules
    private List<String> compatibleDevices;
    private int colomboStock;
    private int galleStock;
    private int minThreshold;
    private long unitCostLKR;
    private boolean isOem;
    private int warrantyMonths;
    private String imageUrl;

    // Required empty constructor for Firebase
    public SparePart() {}

    public SparePart(@NonNull String id, String partId, String name, String category,
                     List<String> compatibleDevices, int colomboStock, int galleStock,
                     int minThreshold, long unitCostLKR, boolean isOem, int warrantyMonths) {
        this.id = id;
        this.partId = partId;
        this.name = name;
        this.category = category;
        this.compatibleDevices = compatibleDevices;
        this.colomboStock = colomboStock;
        this.galleStock = galleStock;
        this.minThreshold = minThreshold;
        this.unitCostLKR = unitCostLKR;
        this.isOem = isOem;
        this.warrantyMonths = warrantyMonths;
    }

    public SparePart(@NonNull String id, String partId, String name, String category,
                     List<String> compatibleDevices, int colomboStock, int galleStock,
                     int minThreshold, long unitCostLKR, boolean isOem, int warrantyMonths,
                     String imageUrl) {
        this(id, partId, name, category, compatibleDevices, colomboStock, galleStock,
             minThreshold, unitCostLKR, isOem, warrantyMonths);
        this.imageUrl = imageUrl;
    }

    @NonNull
    public String getId() { return id; }
    public void setId(@NonNull String id) { this.id = id; }

    public String getPartId() { return partId; }
    public void setPartId(String partId) { this.partId = partId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public List<String> getCompatibleDevices() { return compatibleDevices; }
    public void setCompatibleDevices(List<String> compatibleDevices) { this.compatibleDevices = compatibleDevices; }

    public int getColomboStock() { return colomboStock; }
    public void setColomboStock(int colomboStock) { this.colomboStock = colomboStock; }

    public int getGalleStock() { return galleStock; }
    public void setGalleStock(int galleStock) { this.galleStock = galleStock; }

    public int getMinThreshold() { return minThreshold; }
    public void setMinThreshold(int minThreshold) { this.minThreshold = minThreshold; }

    public long getUnitCostLKR() { return unitCostLKR; }
    public void setUnitCostLKR(long unitCostLKR) { this.unitCostLKR = unitCostLKR; }

    public boolean isOem() { return isOem; }
    public void setOem(boolean oem) { isOem = oem; }

    public int getWarrantyMonths() { return warrantyMonths; }
    public void setWarrantyMonths(int warrantyMonths) { this.warrantyMonths = warrantyMonths; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    // Helper: get stock for a given branch name
    public int getStockForBranch(String branch) {
        return branch.contains("Colombo") ? colomboStock : galleStock;
    }

    // Helper: stock status
    public String getStockStatus(String branch) {
        int stock = getStockForBranch(branch);
        if (stock == 0) return "OUT";
        if (stock <= minThreshold) return "LOW";
        return "OK";
    }
}
