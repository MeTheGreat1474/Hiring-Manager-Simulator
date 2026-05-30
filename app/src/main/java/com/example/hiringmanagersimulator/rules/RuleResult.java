package com.example.hiringmanagersimulator.rules;

import com.example.hiringmanagersimulator.model.ViolationType;

/**
 * The outcome of running RulesEngine against a single applicant.
 */
public class RuleResult {

    /** True = applicant should be APPROVED. False = applicant should be REJECTED. */
    public final boolean shouldApprove;

    /** The violation found, or ViolationType.NONE if clean. */
    public final ViolationType violation;

    /**
     * The label of the rule that failed, for showing in the end-of-round
     * feedback ("You missed: Expired work permit").
     * Null when shouldApprove is true.
     */
    public final String failedRuleLabel;

    private RuleResult(boolean shouldApprove, ViolationType violation, String failedRuleLabel) {
        this.shouldApprove   = shouldApprove;
        this.violation       = violation;
        this.failedRuleLabel = failedRuleLabel;
    }

    public static RuleResult pass() {
        return new RuleResult(true, ViolationType.NONE, null);
    }

    public static RuleResult fail(ViolationType violation, String ruleLabel) {
        return new RuleResult(false, violation, ruleLabel);
    }
}
