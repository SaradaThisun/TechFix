package com.techfix.app.utils;

import com.techfix.app.models.Branch;
import com.techfix.app.models.DispatchRequest;
import com.techfix.app.models.HistoryItem;
import com.techfix.app.models.PartTransferLog;
import com.techfix.app.models.PromoBanner;
import com.techfix.app.models.RepairService;
import com.techfix.app.models.RepairTicket;
import com.techfix.app.models.SparePart;
import com.techfix.app.models.TechnicianJob;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MockData {

    // ─── Promo Banners ────────────────────────────────────────────────────────

    public static List<PromoBanner> getPromoBanners() {
        List<PromoBanner> banners = new ArrayList<>();
        banners.add(new PromoBanner("promo-1", "24-Hour Express Repair in Galle!",
                "Free diagnostic & genuine OLED display replacements",
                "Limited Offer", "GALLE24", "15% OFF", "Galle Branch",
                "#0066FF", "#00F0FF"));
        banners.add(new PromoBanner("promo-2", "Laptop Logic Board Diagnostics",
                "Colombo Duplication Rd Lab equipped with thermal imaging",
                "Express Lab", "COLMAC10", "Rs. 2,500 OFF", "Colombo Branch",
                "#7000FF", "#0066FF"));
        banners.add(new PromoBanner("promo-3", "90-Day Nationwide Warranty",
                "All repairs backed by TechFix certified parts guarantee",
                "Guaranteed", "WARRANTY90", "Free 90D", "All Branches",
                "#00F0FF", "#00B4D8"));
        return banners;
    }

    // ─── Branches ─────────────────────────────────────────────────────────────

    public static List<Branch> getBranches() {
        List<Branch> branches = new ArrayList<>();
        branches.add(new Branch("colombo-main", "Colombo Branch", "Colombo HQ",
                "No. 112, Kirula Road, Colombo 05",
                "Colombo", "+94 11 259 8870", "+94 77 123 4567",
                "Mon – Sat: 8:30 AM – 7:30 PM", "Sunday: 9:00 AM – 3:00 PM",
                4.9, 384, 8, "High (98%)",
                6.8845, 79.8756,
                Arrays.asList("iPhone Screen Lab", "MacBook Micro-soldering", "PlayStation HDMI Repair", "Express Battery"),
                "https://maps.google.com/?q=Kirula+Road+Colombo+05"));
        branches.add(new Branch("galle-fort", "Galle Branch", "Galle Hub",
                "No. 45, Wackwella Road, Galle",
                "Galle", "+94 91 224 5590", "+94 71 889 9120",
                "Mon – Sat: 9:00 AM – 7:00 PM", "Sunday: 9:30 AM – 2:30 PM",
                4.8, 219, 5, "Optimal (94%)",
                6.0367, 80.2170,
                Arrays.asList("Samsung SuperAMOLED", "Water Damage Recovery", "Drone & Camera Gimbal", "iPad Digitizer"),
                "https://maps.google.com/?q=Wackwella+Road+Galle"));
        return branches;
    }

    // ─── Repair Services ──────────────────────────────────────────────────────

    public static List<RepairService> getRepairServices() {
        List<RepairService> services = new ArrayList<>();
        services.add(new RepairService("srv-1", "iPhone OLED Display Replacement",
                "Screen Replace", "mobile", 18500, "45 - 60 Mins", true, 90,
                "Factory-grade OLED panel with TrueTone restoration and Oleophobic coating.",
                Collections.singletonList("https://images.unsplash.com/photo-1592899677977-9c10ca588bbd?w=400")));
        services.add(new RepairService("srv-2", "Samsung Galaxy AMOLED Screen Fix",
                "Screen Replace", "mobile", 22000, "1 - 2 Hours", true, 90,
                "Original Dynamic AMOLED 120Hz display with ultrasonic fingerprint calibration.",
                Collections.singletonList("https://images.unsplash.com/photo-1610945415295-d9bbf067e59c?w=400")));
        services.add(new RepairService("srv-3", "High-Capacity Battery Replacement",
                "Battery", "mobile", 7500, "30 Mins", true, 180,
                "OEM grade battery cell with 0 cycles and battery health 100% telemetry calibration.",
                Collections.singletonList("https://images.unsplash.com/photo-1588872657578-7efd1f1555ed?w=400")));
        services.add(new RepairService("srv-6", "USB-C / Lightning Port Replacement",
                "Audio/Port", "mobile", 6500, "45 Mins", false, 90,
                "Fast-charging enabled flex cable repair with microphone and antenna integration.",
                Collections.singletonList("https://images.unsplash.com/photo-1583863788434-e58a36330cf0?w=400")));
        services.add(new RepairService("srv-9", "Google Pixel Camera Glass Repair",
                "Mobile", "mobile", 4500, "30 Mins", false, 90,
                "Replacement of shattered rear camera lens covers using tempered sapphire glass.",
                Collections.singletonList("https://images.unsplash.com/photo-1601784551446-20c9e07cdbab?w=400")));
        services.add(new RepairService("srv-10", "Phone Water Damage Recovery",
                "Board Level", "mobile", 8500, "24 Hours", false, 30,
                "Ultrasonic cleaning and logic board moisture displacement for non-booting devices.",
                Collections.singletonList("https://images.unsplash.com/photo-1542362567-b052cb1341f1?w=400")));
        services.add(new RepairService("srv-11", "Speaker & Microphone Cleaning",
                "Audio/Port", "mobile", 2500, "15 Mins", true, 30,
                "Deep sonic cleaning of mesh grills to restore original audio clarity and volume.",
                Collections.singletonList("https://images.unsplash.com/photo-1589003077984-894e133d98f7?w=400")));
        services.add(new RepairService("srv-4", "MacBook Logic Board Micro-Soldering",
                "Board Level", "computer", 34500, "24 - 48 Hours", true, 90,
                "BGA chip reballing, shorted capacitor replacement, and liquid corrosion ultrasonic cleaning.",
                Collections.singletonList("https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=400")));
        services.add(new RepairService("srv-7", "iPad Digitizer & Touch Glass Fix",
                "Screen Replace", "tablet", 16500, "2 - 3 Hours", false, 90,
                "Laser-cut laminated digitizer glass replacement maintaining Apple Pencil sensitivity.",
                Collections.singletonList("https://images.unsplash.com/photo-1544244015-0df4b3ffc6b0?w=400")));
        services.add(new RepairService("srv-8", "Laptop SSD & RAM Upgrade Combo",
                "Computers", "computer", 12500, "2 Hours", true, 365,
                "NVMe M.2 1TB/2TB high-speed solid state drive with OS migration & DDR4/DDR5 RAM expansion.",
                Collections.singletonList("https://images.unsplash.com/photo-1550745165-9bc0b252726f?w=400")));
        return services;
    }

    // ─── Active Repair Ticket ─────────────────────────────────────────────────

    public static RepairTicket getActiveTicket(String userId) {
        RepairTicket ticket = new RepairTicket();
        ticket.setId("#TF-8942");
        ticket.setDeviceModel("iPhone 15 Pro Max (Titanium Blue)");
        ticket.setDeviceType("phone");
        ticket.setCategory("Screen & Battery Overhaul");
        ticket.setIssue("Cracked front ceramic shield glass & 76% depleted battery health degradation");
        ticket.setStatus("Repair in Progress");
        ticket.setCurrentStepIndex(2);
        ticket.setProgressPercent(68);
        ticket.setBranch("Colombo Branch");
        ticket.setTechnicianName("Kasun Weerasinghe");
        ticket.setTechnicianRole("Lead Master Technician (Level 3 Apple Certified)");
        ticket.setTechnicianPhone("+94 77 445 1192");
        ticket.setTechnicianAvatar("https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150");
        ticket.setTechnicianRating(4.95f);
        ticket.setEstimatedCompletion("Today at 4:30 PM");
        ticket.setCreatedAt("2026-08-21 09:15 AM");
        ticket.setServiceFeeLKR(5500);
        ticket.setPartsFeeLKR(26000);
        ticket.setTaxDiscountLKR(-2000);
        ticket.setTotalCostLKR(29500);
        ticket.setPaid(false);
        ticket.setUserId(userId);
        ticket.setDevicePhoto("https://images.unsplash.com/photo-1592899677977-9c10ca588bbd?w=200");

        List<RepairTicket.TimelineStep> steps = new ArrayList<>();
        steps.add(new RepairTicket.TimelineStep(1, "Request Received",
                "Device checked in at Colombo Branch. Diagnostic intake barcode #TF-8942 scanned.",
                "Today, 09:15 AM", true, false));
        steps.add(new RepairTicket.TimelineStep(2, "Assigned to Technician",
                "Assigned to Master Tech Kasun W. Logic board diagnostics and safety checks passed.",
                "Today, 10:30 AM", true, false));
        steps.add(new RepairTicket.TimelineStep(3, "Repair in Progress",
                "OLED assembly installed with TrueTone programming. High-capacity battery installation underway.",
                "Today, 01:15 PM", false, true));
        steps.add(new RepairTicket.TimelineStep(4, "Ready for Pickup / Payment",
                "Quality assurance test (water seal pressure check, 40-point hardware telemetry verification).",
                "Est. 04:30 PM", false, false));
        ticket.setTimelineSteps(steps);

        List<RepairTicket.StatusLogEntry> logs = new ArrayList<>();
        logs.add(new RepairTicket.StatusLogEntry("log-1", "Device Checked In & Disassembled",
                "Clean room bench #04. Display bracket removed without frame denting.",
                "09:30 AM", "Kasun W.", "info"));
        logs.add(new RepairTicket.StatusLogEntry("log-2", "Hardware Micro-Inspection",
                "Microscope inspection confirmed zero liquid exposure on internal moisture indicators.",
                "11:00 AM", "Kasun W.", "diagnostic"));
        logs.add(new RepairTicket.StatusLogEntry("log-3", "OLED Panel Installation",
                "Factory-sealed OLED panel fitted. TrueTone calibration programmed via diagnostic cable.",
                "01:15 PM", "Kasun W.", "progress"));
        ticket.setStatusLogs(logs);

        return ticket;
    }

    // ─── History ──────────────────────────────────────────────────────────────

    public static List<HistoryItem> getHistoryItems(String userId) {
        List<HistoryItem> items = new ArrayList<>();
        items.add(new HistoryItem("hist-1", "#TF-7821", "MacBook Pro 14\" M3",
                "laptop", "2026-07-14", "Logic Board Micro-Soldering + Thermal Repaste",
                "Colombo Branch", 38500, "Completed", "2026-10-14", "INV-2026-7821", userId));
        items.add(new HistoryItem("hist-2", "#TF-7644", "Samsung Galaxy S24 Ultra",
                "phone", "2026-06-02", "AMOLED Display Replacement + Battery",
                "Galle Branch", 29500, "Completed", "2026-09-02", "INV-2026-7644", userId));
        items.add(new HistoryItem("hist-3", "#TF-7390", "iPad Pro 12.9\" (6th Gen)",
                "tablet", "2026-04-18", "Digitizer & Touch Glass Replacement",
                "Colombo Branch", 17500, "Completed", "2026-07-18", "INV-2026-7390", userId));
        items.add(new HistoryItem("hist-4", "#TF-7102", "Dell XPS 15 (2024)",
                "laptop", "2026-03-05", "USB-C Port Replacement (Canceled by Customer)",
                "Colombo Branch", 0, "Canceled", null, "INV-2026-7102", userId));
        return items;
    }

    // ─── Spare Parts ──────────────────────────────────────────────────────────

    public static List<SparePart> getSpareParts() {
        List<SparePart> parts = new ArrayList<>();
        // Samsung
        parts.add(new SparePart("sp-sams24u-scr", "SAM-S24U-SCR", "Samsung S24 Ultra Dynamic AMOLED 2X",
                "Screen Assemblies", Collections.singletonList("Samsung Galaxy S24 Ultra"),
                3, 1, 2, 28500, true, 6,
                "https://images.unsplash.com/photo-1610945415295-d9bbf067e59c?w=200"));
        parts.add(new SparePart("sp-sams23-bat", "SAM-S23-BAT", "Samsung S23 Series 3900mAh Battery",
                "Batteries", Arrays.asList("Samsung Galaxy S23", "S23 FE"),
                10, 4, 3, 6500, true, 6,
                "https://images.unsplash.com/photo-1588872657578-7efd1f1555ed?w=200"));
        
        // Apple
        parts.add(new SparePart("sp-apl15p-scr", "APL-IP15P-SCR", "iPhone 15 Pro Ceramic Shield OLED",
                "Screen Assemblies", Collections.singletonList("iPhone 15 Pro"),
                4, 2, 2, 32000, true, 3,
                "https://images.unsplash.com/photo-1592899677977-9c10ca588bbd?w=200"));
        parts.add(new SparePart("sp-apl14-bat", "APL-IP14-BAT", "iPhone 14 High-Capacity Battery",
                "Batteries", Collections.singletonList("iPhone 14"),
                8, 5, 3, 7500, true, 12,
                "https://images.unsplash.com/photo-1588872657578-7efd1f1555ed?w=200"));
        
        // Xiaomi / Oppo / Vivo
        parts.add(new SparePart("sp-xia-n13-scr", "XIA-N13-SCR", "Xiaomi Redmi Note 13 Pro Display",
                "Screen Assemblies", Collections.singletonList("Redmi Note 13 Pro"),
                6, 3, 2, 14500, false, 3,
                "https://images.unsplash.com/photo-1512054192342-d98e8d65f0ce?w=200"));
        parts.add(new SparePart("sp-uni-usbc", "UNI-USBC-FLEX", "Premium USB-C Charging Port Flex",
                "Charging Ports", Arrays.asList("Xiaomi", "Oppo", "Vivo", "Realme"),
                20, 12, 5, 2500, false, 3,
                "https://images.unsplash.com/photo-1583863788434-e58a36330cf0?w=200"));
        
        // Laptops
        parts.add(new SparePart("sp-apl-m2-scr", "APL-M2-SCR", "MacBook Air M2 Retina Display Panel",
                "Screen Assemblies", Collections.singletonList("MacBook Air M2"),
                2, 0, 1, 45000, true, 6,
                "https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=200"));
        parts.add(new SparePart("sp-del-xps-kb", "DEL-XPS-KB", "Dell XPS 15 Backlit Keyboard UK Layout",
                "Logic Boards", Collections.singletonList("Dell XPS 15"),
                3, 2, 1, 8500, true, 3,
                "https://images.unsplash.com/photo-1587829741301-dc798b83aca3?w=200"));
        
        // Maintenance
        parts.add(new SparePart("sp-uni-therm", "UNI-THERM-G", "Thermal Grizzly Kryonaut Paste (1g)",
                "Logic Boards", Arrays.asList("All Laptops", "PCs"),
                15, 8, 5, 1800, true, 0,
                "https://images.unsplash.com/photo-1518770660439-4636190af475?w=200"));

        // Camera Modules
        parts.add(new SparePart("sp-apl15-cam", "APL-IP15-CAM", "iPhone 15 Pro Main Camera Module",
                "Camera Modules", Collections.singletonList("iPhone 15 Pro"),
                2, 1, 1, 15500, true, 6,
                "https://images.unsplash.com/photo-1516035069371-29a1b244cc32?w=200"));
        parts.add(new SparePart("sp-sam-s24-cam", "SAM-S24U-CAM", "Samsung S24 Ultra 200MP Lens",
                "Camera Modules", Collections.singletonList("Samsung S24 Ultra"),
                3, 0, 1, 24000, true, 6,
                "https://images.unsplash.com/photo-1516035069371-29a1b244cc32?w=200"));

        // Additional Logic Boards & SSDs
        parts.add(new SparePart("sp-ssd-1tb", "CRU-NVME-1TB", "Crucial P3 1TB NVMe M.2 SSD",
                "Logic Boards", Arrays.asList("Laptops", "Desktops"),
                12, 6, 4, 18500, true, 36,
                "https://images.unsplash.com/photo-1591488320449-011701bb6704?w=200"));
        parts.add(new SparePart("sp-ram-16gb", "COR-DDR4-16G", "Corsair Vengeance 16GB DDR4 RAM",
                "Logic Boards", Arrays.asList("Laptops", "Desktops"),
                10, 5, 3, 9500, true, 36,
                "https://images.unsplash.com/photo-1591488320449-011701bb6704?w=200"));

        // Accessories & Screen Protection
        parts.add(new SparePart("sp-uni-9h", "UNI-9H-SCR", "9H Tempered Glass Screen Protector",
                "Screen Assemblies", Collections.singletonList("All Smartphones"),
                50, 30, 10, 850, false, 0,
                "https://images.unsplash.com/photo-1595941069915-4ebc5197c14a?w=400"));
        parts.add(new SparePart("sp-uni-usbc-cbl", "UNI-USBC-CBL", "Premium Braided USB-C Cable (2m)",
                "Charging Ports", Collections.singletonList("All USB-C Devices"),
                40, 25, 10, 1200, false, 6,
                "https://images.unsplash.com/photo-1583863788434-e58a36330cf0?w=200"));

        // Logic Boards
        parts.add(new SparePart("sp-apl-m1-lb", "APL-M1-LB", "MacBook Air M1 Logic Board (8GB/256GB)",
                "Logic Boards", Collections.singletonList("MacBook Air M1"),
                1, 0, 1, 85000, true, 6,
                "https://images.unsplash.com/photo-1518770660439-4636190af475?w=200"));

        return parts;
    }

    // ─── Dispatch Requests ────────────────────────────────────────────────────

    public static List<DispatchRequest> getDispatchRequests() {
        List<DispatchRequest> requests = new ArrayList<>();

        DispatchRequest r1 = new DispatchRequest();
        r1.setId("REQ-9101"); r1.setCustomerName("Amara Perera");
        r1.setCustomerPhone("+94 77 312 8845"); r1.setCustomerLocation("Nugegoda, Colombo");
        r1.setDeviceType("phone"); r1.setDeviceModel("iPhone 14 Pro");
        r1.setIssueSummary("Shattered screen & unresponsive Face ID module");
        r1.setAutoMatchBranch("Colombo Branch"); r1.setAutoMatchDistanceKm(4.2);
        r1.setPartInStock(true); r1.setTechniciansAvailable(3);
        r1.setUrgency("Urgent"); r1.setSubmittedTime("08:45 AM");
        r1.setStatus("Pending Dispatch");
        requests.add(r1);

        DispatchRequest r2 = new DispatchRequest();
        r2.setId("REQ-9102"); r2.setCustomerName("Priya Rajapaksa");
        r2.setCustomerPhone("+94 71 667 2291"); r2.setCustomerLocation("Galle Fort, Galle");
        r2.setDeviceType("laptop"); r2.setDeviceModel("MacBook Air M2");
        r2.setIssueSummary("No display output after liquid spill on keyboard");
        r2.setAutoMatchBranch("Galle Branch"); r2.setAutoMatchDistanceKm(1.8);
        r2.setPartInStock(false); r2.setTechniciansAvailable(2);
        r2.setUrgency("Urgent"); r2.setSubmittedTime("09:10 AM");
        r2.setStatus("Pending Dispatch");
        requests.add(r2);

        DispatchRequest r3 = new DispatchRequest();
        r3.setId("REQ-9103"); r3.setCustomerName("Lasith Fernando");
        r3.setCustomerPhone("+94 76 448 5512"); r3.setCustomerLocation("Dehiwala, Colombo");
        r3.setDeviceType("tablet"); r3.setDeviceModel("Samsung Galaxy Tab S9");
        r3.setIssueSummary("Battery swelling — back cover partially lifted");
        r3.setAutoMatchBranch("Colombo Branch"); r3.setAutoMatchDistanceKm(6.1);
        r3.setPartInStock(true); r3.setTechniciansAvailable(5);
        r3.setUrgency("Standard"); r3.setSubmittedTime("09:55 AM");
        r3.setStatus("Pending Dispatch");
        requests.add(r3);

        DispatchRequest r4 = new DispatchRequest();
        r4.setId("REQ-9104"); r4.setCustomerName("Nadeesha Silva");
        r4.setCustomerPhone("+94 70 991 3374"); r4.setCustomerLocation("Matara Town");
        r4.setDeviceType("phone"); r4.setDeviceModel("Google Pixel 8 Pro");
        r4.setIssueSummary("Charging port not recognizing cable — no charge");
        r4.setAutoMatchBranch("Galle Branch"); r4.setAutoMatchDistanceKm(38.5);
        r4.setPartInStock(true); r4.setTechniciansAvailable(4);
        r4.setUrgency("Standard"); r4.setSubmittedTime("10:20 AM");
        r4.setStatus("Pending Dispatch");
        requests.add(r4);

        return requests;
    }

    // ─── Technician Jobs ──────────────────────────────────────────────────────

    public static List<TechnicianJob> getTechnicianJobs() {
        List<TechnicianJob> jobs = new ArrayList<>();

        TechnicianJob j1 = new TechnicianJob();
        j1.setId("JOB-8942"); j1.setTicketNumber("#TF-8942");
        j1.setDeviceModel("iPhone 15 Pro Max"); j1.setDeviceType("phone");
        j1.setCustomerName("Tharindu Erandana"); j1.setCustomerPhone("+94 71 889 9120");
        j1.setCustomerNotes("Please preserve all data. Device has sentimental photos.");
        j1.setPriority("Urgent"); j1.setCurrentStage("In Repair");
        j1.setTargetCompletion("Today 04:30 PM");
        j1.setBranch("Colombo Branch"); j1.setBenchNumber("Bench #04");
        j1.setBeforeImages(Collections.singletonList(
                "https://images.unsplash.com/photo-1592899677977-9c10ca588bbd?w=300"));
        j1.setAfterImages(new ArrayList<>());

        List<TechnicianJob.InternalNote> notes1 = new ArrayList<>();
        notes1.add(new TechnicianJob.InternalNote("note-1", "09:30 AM",
                "Device disassembled cleanly. No water damage marks found on internal indicators.",
                "Kasun W."));
        j1.setInternalNotes(notes1);

        List<TechnicianJob.UsedPart> parts1 = new ArrayList<>();
        parts1.add(new TechnicianJob.UsedPart("OEM-IP15P-SCR", "iPhone 15 Pro Max OLED Assembly", 1, 21000));
        parts1.add(new TechnicianJob.UsedPart("OEM-IP15-BAT", "iPhone 15 Series 4422mAh Battery", 1, 5500));
        j1.setUsedParts(parts1);
        jobs.add(j1);

        TechnicianJob j2 = new TechnicianJob();
        j2.setId("JOB-8890"); j2.setTicketNumber("#TF-8890");
        j2.setDeviceModel("MacBook Pro 16\" M3 Max"); j2.setDeviceType("laptop");
        j2.setCustomerName("Roshan Kumara"); j2.setCustomerPhone("+94 77 221 0093");
        j2.setCustomerNotes("Liquid spill. Was working until yesterday.");
        j2.setPriority("Urgent"); j2.setCurrentStage("Diagnostic");
        j2.setTargetCompletion("Tomorrow 12:00 PM");
        j2.setBranch("Colombo Branch"); j2.setBenchNumber("Bench #01");
        j2.setBeforeImages(Collections.singletonList(
                "https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=300"));
        j2.setAfterImages(new ArrayList<>());
        j2.setInternalNotes(new ArrayList<>());
        j2.setUsedParts(new ArrayList<>());
        jobs.add(j2);

        return jobs;
    }

    // ─── Transfer Logs ────────────────────────────────────────────────────────

    public static List<PartTransferLog> getTransferLogs() {
        List<PartTransferLog> logs = new ArrayList<>();
        logs.add(new PartTransferLog("trf-1", "OEM-MBP14-FAN",
                "MacBook Pro 14\" M3 Dual Fan Kit",
                "Galle Branch", "Colombo Branch", 2,
                "Urgent job requirement — Colombo stock critically low",
                "Today, 08:15 AM", "In Transit"));
        logs.add(new PartTransferLog("trf-2", "OEM-SGS24U-SCR",
                "Samsung S24 Ultra Dynamic AMOLED",
                "Colombo Branch", "Galle Branch", 1,
                "Stock balancing — Galle had incoming S24 repair request",
                "Yesterday, 04:30 PM", "Delivered"));
        return logs;
    }
}
