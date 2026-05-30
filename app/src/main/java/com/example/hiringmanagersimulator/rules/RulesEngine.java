package com.example.hiringmanagersimulator.rules;

import com.example.hiringmanagersimulator.model.Applicant;
import com.example.hiringmanagersimulator.model.ViolationType;

import java.util.List;

/**
 * Evaluates an applicant against the active rules for the current day.
 *
 * Rules are checked in order. The first failure short-circuits — this keeps
 * feedback to the player simple (one reason per rejection) and matches the
 * single-violation guarantee on Applicant.
 *
 * Usage:
 *   RulesEngine engine = new RulesEngine(dayConfig.getRules());
 *   RuleResult result  = engine.evaluate(applicant);
 *   boolean correct    = (playerApproved == result.shouldApprove);
 */
public class RulesEngine {

    private final List<Rule> rules;

    public RulesEngine(List<Rule> rules) {
        this.rules = rules;
    }

    /**
     * Runs all active rules against the applicant.
     * Returns on the first failure; returns pass if all rules clear.
     */
    public RuleResult evaluate(Applicant applicant) {
        for (Rule rule : rules) {
            ViolationType violation = rule.evaluate(applicant);
            if (violation != ViolationType.NONE) {
                return RuleResult.fail(violation, rule.getLabel());
            }
        }
        return RuleResult.pass();
    }

    public List<Rule> getRules() {
        return rules;
    }
}
