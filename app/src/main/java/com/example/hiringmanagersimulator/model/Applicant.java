package com.example.hiringmanagersimulator.model;

import java.util.List;

/**
 * Represents a single job applicant shown to the player.
 *
 * Fields are split into two groups:
 *   - Display fields: rendered on the resume UI
 *   - Ground truth fields: used by RulesEngine to determine the correct decision
 *
 * The player sees only display fields. Ground truth fields may or may not match
 * what's displayed — that mismatch IS the violation (e.g. displayedYearsExp
 * looks valid but actualYearsExp is under the threshold).
 */
public class Applicant {

    // ─── Identity ──────────────────────────────────────────────────────────────

    public final String firstName;
    public final String lastName;
    public final String email;
    public final String phone;
    public final String city;

    // ─── Current / target role ─────────────────────────────────────────────────

    public final String targetRole;       // e.g. "Software Engineer"
    public final String targetDepartment; // e.g. "Engineering", "Marketing"

    // ─── Work history ──────────────────────────────────────────────────────────

    /** What is printed on the resume. May be inflated in a forgery violation. */
    public final float displayedYearsExp;

    /** Actual years of experience used by the rules engine. */
    public final float actualYearsExp;

    public final List<JobEntry> jobHistory;

    // ─── Seniority ─────────────────────────────────────────────────────────────

    /**
     * Grade shown on the resume. e.g. "SENIOR I", "JUNIOR II".
     * May be forged — see actualGrade.
     */
    public final String displayedGrade;
    public final String actualGrade;

    // ─── Work permit ───────────────────────────────────────────────────────────

    public final boolean hasWorkPermit;
    /** True if the permit is past its expiry date (a violation even if hasWorkPermit=true). */
    public final boolean workPermitExpired;

    // ─── Background / integrity ────────────────────────────────────────────────

    public final boolean hasPriorOffense;
    /** True if the employer listed is on the current day's blacklist. */
    public final boolean fromBlacklistedCompany;

    // ─── Education ─────────────────────────────────────────────────────────────

    public final String university;
    public final String universityLocation;
    public final String degree;
    public final String graduationYear;

    // ─── Skills & additional info ──────────────────────────────────────────────

    public final List<String> skills;
    public final String technicalSkills;   // e.g. "Python, SQL, AWS"
    public final String certifications;    // e.g. "PMP (2022), AWS Certified"
    public final String languages;         // e.g. "English (fluent), Malay (native)"

    // ─── Violation summary (set by ApplicantGenerator) ─────────────────────────

    /**
     * The single violation injected into this applicant, or NONE if clean.
     * RulesEngine uses this to confirm ground truth — it doesn't re-derive
     * the violation from fields, avoiding subtle float comparison bugs.
     */
    public final ViolationType violation;

    // ─── Constructor ───────────────────────────────────────────────────────────

    private Applicant(Builder b) {
        this.firstName            = b.firstName;
        this.lastName             = b.lastName;
        this.email                = b.email;
        this.phone                = b.phone;
        this.city                 = b.city;
        this.targetRole           = b.targetRole;
        this.targetDepartment     = b.targetDepartment;
        this.displayedYearsExp    = b.displayedYearsExp;
        this.actualYearsExp       = b.actualYearsExp;
        this.jobHistory           = b.jobHistory;
        this.displayedGrade       = b.displayedGrade;
        this.actualGrade          = b.actualGrade;
        this.hasWorkPermit        = b.hasWorkPermit;
        this.workPermitExpired    = b.workPermitExpired;
        this.hasPriorOffense      = b.hasPriorOffense;
        this.fromBlacklistedCompany = b.fromBlacklistedCompany;
        this.university           = b.university;
        this.universityLocation   = b.universityLocation;
        this.degree               = b.degree;
        this.graduationYear       = b.graduationYear;
        this.skills               = b.skills;
        this.technicalSkills      = b.technicalSkills;
        this.certifications       = b.certifications;
        this.languages            = b.languages;
        this.violation            = b.violation;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    // ─── Builder ───────────────────────────────────────────────────────────────

    public static class Builder {
        private String firstName, lastName, email, phone, city;
        private String targetRole, targetDepartment;
        private float displayedYearsExp, actualYearsExp;
        private List<JobEntry> jobHistory;
        private String displayedGrade, actualGrade;
        private boolean hasWorkPermit, workPermitExpired;
        private boolean hasPriorOffense, fromBlacklistedCompany;
        private String university, universityLocation, degree, graduationYear;
        private List<String> skills;
        private String technicalSkills, certifications, languages;
        private ViolationType violation = ViolationType.NONE;

        public Builder name(String first, String last)            { firstName = first; lastName = last; return this; }
        public Builder email(String v)                            { email = v; return this; }
        public Builder phone(String v)                            { phone = v; return this; }
        public Builder city(String v)                             { city = v; return this; }
        public Builder role(String role, String dept)             { targetRole = role; targetDepartment = dept; return this; }
        public Builder experience(float displayed, float actual)  { displayedYearsExp = displayed; actualYearsExp = actual; return this; }
        public Builder jobHistory(List<JobEntry> v)               { jobHistory = v; return this; }
        public Builder grade(String displayed, String actual)     { displayedGrade = displayed; actualGrade = actual; return this; }
        public Builder workPermit(boolean has, boolean expired)   { hasWorkPermit = has; workPermitExpired = expired; return this; }
        public Builder priorOffense(boolean v)                    { hasPriorOffense = v; return this; }
        public Builder blacklistedCompany(boolean v)              { fromBlacklistedCompany = v; return this; }
        public Builder education(String uni, String loc, String deg, String yr){ university = uni; universityLocation = loc; degree = deg; graduationYear = yr; return this; }
        public Builder skills(List<String> v)                     { skills = v; return this; }
        public Builder technicalSkills(String v)                  { technicalSkills = v; return this; }
        public Builder certifications(String v)                   { certifications = v; return this; }
        public Builder languages(String v)                        { languages = v; return this; }
        public Builder violation(ViolationType v)                 { violation = v; return this; }

        public Applicant build() { return new Applicant(this); }
    }
}
