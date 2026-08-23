package com.techfix.app.models;

public class PromoBanner {

    private String id;
    private String title;
    private String subtitle;
    private String badge;
    private String code;
    private String discount;
    private String branch;
    private String colorStart;
    private String colorEnd;

    public PromoBanner() {}

    public PromoBanner(String id, String title, String subtitle, String badge,
                       String code, String discount, String branch,
                       String colorStart, String colorEnd) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.badge = badge;
        this.code = code;
        this.discount = discount;
        this.branch = branch;
        this.colorStart = colorStart;
        this.colorEnd = colorEnd;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getSubtitle() { return subtitle; }
    public void setSubtitle(String subtitle) { this.subtitle = subtitle; }

    public String getBadge() { return badge; }
    public void setBadge(String badge) { this.badge = badge; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getDiscount() { return discount; }
    public void setDiscount(String discount) { this.discount = discount; }

    public String getBranch() { return branch; }
    public void setBranch(String branch) { this.branch = branch; }

    public String getColorStart() { return colorStart; }
    public void setColorStart(String colorStart) { this.colorStart = colorStart; }

    public String getColorEnd() { return colorEnd; }
    public void setColorEnd(String colorEnd) { this.colorEnd = colorEnd; }
}
