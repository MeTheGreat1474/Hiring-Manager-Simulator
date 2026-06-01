package com.example.hiringmanagersimulator.model;

import com.example.hiringmanagersimulator.rules.Rule;
import com.example.hiringmanagersimulator.rules.Rules;

import java.util.Arrays;
import java.util.List;

public class DayConfig {

    public final int dayNumber;
    public final int applicantCount;
    public final int timeLimitSeconds;
    public final int mistakeQuota;
    private final List<Rule> rules;

    private DayConfig(int dayNumber, int applicantCount, int timeLimitSeconds,
                      int mistakeQuota, List<Rule> rules) {
        this.dayNumber        = dayNumber;
        this.applicantCount   = applicantCount;
        this.timeLimitSeconds = timeLimitSeconds;
        this.mistakeQuota     = mistakeQuota;
        this.rules            = rules;
    }

    public List<Rule> getRules() { return rules; }

    /**
     * Accepted universities used from Day 3 onward.
     * Must match names in ApplicantGenerator.UNIVERSITIES exactly.
     */
    public static final String[] ACCEPTED_UNIVERSITIES = {
            "Universiti Malaya",
            "Universiti Teknologi Malaysia",
            "Universiti Putra Malaysia",
            "Universiti Kebangsaan Malaysia",
            "Universiti Sains Malaysia"
    };

    /**
     * Required skills used from Day 4 onward.
     * At least ONE must appear in the applicant's technicalSkills or skills list.
     */
    public static final String[] REQUIRED_SKILLS = {
            "Python", "Java", "SQL", "AWS", "Kotlin",
            "Docker", "Kubernetes", "TensorFlow"
    };

    /**
     * Blacklisted companies used from Day 5 onward.
     * Applicants whose most recent employer matches one of these must be rejected.
     */
    public static final String[] BLACKLISTED_COMPANIES = {
            "RedFlag Corp", "Nexus Offshore Ltd", "ShadowTech Ventures",
            "Phantom Solutions", "DarkHorse Analytics"
    };

    /**
     * Required seniority grades used from Day 6 onward.
     * Applicants must have an actualGrade matching one of these.
     */
    public static final String[] REQUIRED_GRADES = {
            "SENIOR I", "SENIOR II", "PRINCIPAL"
    };

    /**
     * Day progression:
     *   Day 1 — min 3 yrs experience only, 3 mistakes, no timer
     *   Day 2 — min 5 yrs experience, 3 mistakes, no timer
     *   Day 3 — + target university, 2 mistakes, no timer
     *   Day 4 — + required technical skill, 2 mistakes, no timer
     *   Day 5 — + blacklist check, 2 mistakes, no timer
     *   Day 6 — + prior offenses, 2 mistakes, 5-min timer
     *   Day 7+ — all rules, 1 mistake, shrinking timer
     */
    public static DayConfig forDay(int day) {
        switch (day) {
            case 1:
                return new DayConfig(1, 1, 0, 3,
                        Arrays.asList(
                                Rules.minimumExperience(3)
                        ));

            case 2:
                return new DayConfig(2, 1, 0, 3,
                        Arrays.asList(
                                Rules.minimumExperience(5)
                        ));

            case 3:
                return new DayConfig(3, 1, 0, 2,
                        Arrays.asList(
                                Rules.minimumExperience(5),
                                Rules.targetUniversity(ACCEPTED_UNIVERSITIES)
                        ));

            case 4:
                return new DayConfig(4, 1, 0, 2,
                        Arrays.asList(
                                Rules.minimumExperience(5),
                                Rules.targetUniversity(ACCEPTED_UNIVERSITIES),
                                Rules.requireSkill(REQUIRED_SKILLS)
                        ));

            case 5:
                return new DayConfig(5, 1, 0, 2,
                        Arrays.asList(
                                Rules.minimumExperience(5),
                                Rules.targetUniversity(ACCEPTED_UNIVERSITIES),
                                Rules.requireSkill(REQUIRED_SKILLS),
                                Rules.noBlacklistedCompany(BLACKLISTED_COMPANIES)
                        ));

            case 6:
                return new DayConfig(6, 1, 300, 2,
                        Arrays.asList(
                                Rules.minimumExperience(5),
                                Rules.targetUniversity(ACCEPTED_UNIVERSITIES),
                                Rules.requireSkill(REQUIRED_SKILLS),
                                Rules.noBlacklistedCompany(BLACKLISTED_COMPANIES),
                                Rules.requireGrade(REQUIRED_GRADES)
                        ));

            default:
                // Day 7: 120s, decreases 15s per day, floor 15s
                return new DayConfig(day, 1,
                        Math.max(15, 120 - (day - 7) * 15), 1,
                        Arrays.asList(
                                Rules.minimumExperience(5),
                                Rules.targetUniversity(ACCEPTED_UNIVERSITIES),
                                Rules.requireSkill(REQUIRED_SKILLS),
                                Rules.noBlacklistedCompany(BLACKLISTED_COMPANIES),
                                Rules.requireGrade(REQUIRED_GRADES)
                        ));
        }
    }
}
