package com.example.hiringmanagersimulator.model;

/**
 * A single entry in an applicant's work history, rendered as one job block on the resume.
 */
public class JobEntry {

    public final String company;
    public final String title;
    public final String location;
    /** e.g. "2018" */
    public final String startYear;
    /** e.g. "2022" or "Present" */
    public final String endYear;
    public final java.util.List<String> bullets;

    public JobEntry(String company, String title, String location,
                    String startYear, String endYear,
                    java.util.List<String> bullets) {
        this.company   = company;
        this.title     = title;
        this.location  = location;
        this.startYear = startYear;
        this.endYear   = endYear;
        this.bullets   = bullets;
    }

    /** Formatted date range shown on the resume, e.g. "2018 – 2022". */
    public String dateRange() {
        return startYear + " – " + endYear;
    }
}
