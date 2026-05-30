package com.example.hiringmanagersimulator.model;

public enum ViolationType {

    NONE,

    /** actualYearsExp is below the day's required minimum. */
    INSUFFICIENT_EXPERIENCE,

    /** hasWorkPermit is false. */
    NO_WORK_PERMIT,

    /** hasWorkPermit is true but workPermitExpired is true. */
    EXPIRED_WORK_PERMIT,

    /** actualGrade does not meet the day's required seniority level. */
    WRONG_GRADE,

    /** hasPriorOffense is true. */
    PRIOR_OFFENSE,

    /** fromBlacklistedCompany is true. */
    BLACKLISTED_COMPANY,

    /** Resume inflates experience — displayed >= min but actual is below. */
    FALSIFIED_EXPERIENCE,

    /** Resume shows senior grade but actual grade is junior. */
    FALSIFIED_GRADE,

    /** Applicant's university is not in the day's accepted list. */
    WRONG_UNIVERSITY,

    /** Applicant has none of the day's required technical skills. */
    MISSING_REQUIRED_SKILL,
}
