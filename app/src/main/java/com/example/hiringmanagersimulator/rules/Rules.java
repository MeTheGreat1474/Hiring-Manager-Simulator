package com.example.hiringmanagersimulator.rules;

import com.example.hiringmanagersimulator.model.Applicant;
import com.example.hiringmanagersimulator.model.ViolationType;

import java.util.Arrays;
import java.util.List;

public final class Rules {

    private Rules() {}

    // ─── Experience ────────────────────────────────────────────────────────────

    public static Rule minimumExperience(float minYears) {
        return new Rule() {
            @Override public String getLabel() {
                return "Min. " + formatYears(minYears) + " years experience";
            }
            @Override public ViolationType evaluate(Applicant a) {
                if (a.actualYearsExp < minYears) {
                    return (a.displayedYearsExp >= minYears)
                            ? ViolationType.FALSIFIED_EXPERIENCE
                            : ViolationType.INSUFFICIENT_EXPERIENCE;
                }
                return ViolationType.NONE;
            }
        };
    }

    // ─── Background ────────────────────────────────────────────────────────────

    public static Rule noPriorOffenses() {
        return new Rule() {
            @Override public String getLabel() { return "No prior offenses"; }
            @Override public ViolationType evaluate(Applicant a) {
                return a.hasPriorOffense ? ViolationType.PRIOR_OFFENSE : ViolationType.NONE;
            }
        };
    }

    public static BlacklistRule noBlacklistedCompany(String... companies) {
        return new BlacklistRule(companies);
    }

    public static class BlacklistRule implements Rule {
        public final List<String> companies;

        BlacklistRule(String[] companies) {
            this.companies = Arrays.asList(companies);
        }

        @Override
        public String getLabel() { return "No blacklisted company affiliation"; }

        @Override
        public ViolationType evaluate(Applicant a) {
            return a.fromBlacklistedCompany ? ViolationType.BLACKLISTED_COMPANY : ViolationType.NONE;
        }
    }

    // ─── University ────────────────────────────────────────────────────────────

    /**
     * Typed rule — generator can instanceof-check this to read acceptedUniversities
     * and generate an appropriate WRONG_UNIVERSITY violator.
     *
     * Pass the accepted university names exactly as they appear in ApplicantGenerator.UNIVERSITIES.
     */
    public static UniversityRule targetUniversity(String... acceptedUniversities) {
        return new UniversityRule(acceptedUniversities);
    }

    public static class UniversityRule implements Rule {
        public final List<String> accepted;

        UniversityRule(String[] accepted) {
            this.accepted = Arrays.asList(accepted);
        }

        @Override
        public String getLabel() {
            return "Degree from approved institution";
        }

        @Override
        public ViolationType evaluate(Applicant a) {
            for (String u : accepted) {
                if (u.equalsIgnoreCase(a.university)) return ViolationType.NONE;
            }
            return ViolationType.WRONG_UNIVERSITY;
        }
    }

    // ─── Technical skills ──────────────────────────────────────────────────────

    /**
     * Typed rule — generator can instanceof-check this to read requiredSkills
     * and generate an applicant missing all of them.
     *
     * The applicant passes if ANY one required skill appears in their technicalSkills
     * string or their skills list (case-insensitive).
     */
    public static SkillRule requireSkill(String... requiredSkills) {
        return new SkillRule(requiredSkills);
    }

    public static class SkillRule implements Rule {
        public final List<String> required;

        SkillRule(String[] required) {
            this.required = Arrays.asList(required);
        }

        @Override
        public String getLabel() {
            return "Must have one of the required skills";
        }

        @Override
        public ViolationType evaluate(Applicant a) {
            String techLower = a.technicalSkills == null ? "" : a.technicalSkills.toLowerCase();
            for (String req : required) {
                // Check technicalSkills string
                if (techLower.contains(req.toLowerCase())) return ViolationType.NONE;
                // Check discrete skills list
                if (a.skills != null) {
                    for (String s : a.skills) {
                        if (s.equalsIgnoreCase(req)) return ViolationType.NONE;
                    }
                }
            }
            return ViolationType.MISSING_REQUIRED_SKILL;
        }
    }

    // ─── Kept for backward compat (unused in current day configs) ──────────────

    public static Rule requireWorkPermit() {
        return new Rule() {
            @Override public String getLabel() { return "Valid work permit required"; }
            @Override public ViolationType evaluate(Applicant a) {
                return a.hasWorkPermit ? ViolationType.NONE : ViolationType.NO_WORK_PERMIT;
            }
        };
    }

    public static Rule noExpiredPermit() {
        return new Rule() {
            @Override public String getLabel() { return "Work permit must not be expired"; }
            @Override public ViolationType evaluate(Applicant a) {
                return a.workPermitExpired ? ViolationType.EXPIRED_WORK_PERMIT : ViolationType.NONE;
            }
        };
    }

    public static GradeRule requireGrade(String... acceptedGrades) {
        return new GradeRule(acceptedGrades);
    }

    public static class GradeRule implements Rule {
        public final List<String> accepted;

        GradeRule(String[] accepted) {
            this.accepted = Arrays.asList(accepted);
        }

        @Override
        public String getLabel() { return "Minimum seniority grade required:"; }

        @Override
        public ViolationType evaluate(Applicant a) {
            for (String g : accepted) {
                if (g.equalsIgnoreCase(a.actualGrade)) return ViolationType.NONE;
            }
            return a.displayedGrade.equalsIgnoreCase(a.actualGrade)
                    ? ViolationType.WRONG_GRADE
                    : ViolationType.FALSIFIED_GRADE;
        }
    }

    // ─── Helpers ───────────────────────────────────────────────────────────────

    private static String formatYears(float y) {
        return (y == Math.floor(y)) ? String.valueOf((int) y) : String.valueOf(y);
    }
}
