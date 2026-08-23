package com.example.techtrack.models;

import java.util.List;

public class InventorySparePart {
    private String id;
    private String partId;
    private String name;
    private String category;           // Screen Assemblies, Batteries, Charging Ports, Logic Boards, Camera Modules
    private List<String> compatibleDevices;
    private int colomboStock;
    private int galleStock;
    private int minThreshold;
    private double unitCostLKR;
    private boolean isOem;
    private int warrantyMonths;

    public InventorySparePart() {
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

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

    public double getUnitCostLKR() { return unitCostLKR; }
    public void setUnitCostLKR(double unitCostLKR) { this.unitCostLKR = unitCostLKR; }

    public boolean isOem() { return isOem; }
    public void setOem(boolean oem) { isOem = oem; }

    public int getWarrantyMonths() { return warrantyMonths; }
    public void setWarrantyMonths(int warrantyMonths) { this.warrantyMonths = warrantyMonths; }
}