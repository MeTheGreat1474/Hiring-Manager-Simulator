package com.example.hiringmanagersimulator.rules;

import com.example.hiringmanagersimulator.model.Applicant;
import com.example.hiringmanagersimulator.model.ViolationType;

/**
 * A single hiring rule.
 *
 * Each Rule knows:
 *   - its ViolationType (what it catches)
 *   - a display label shown to the player in the Rules panel
 *   - how to evaluate an Applicant
 *
 * evaluate() returns ViolationType.NONE if the applicant passes this rule,
 * or the specific ViolationType if they fail it.
 */
public interface Rule {

    /** Short description shown in the Rules panel, e.g. "Min. 5 years experience". */
    String getLabel();

    /**
     * Returns ViolationType.NONE if the applicant passes,
     * or the caught ViolationType if they fail.
     */
    ViolationType evaluate(Applicant applicant);
}
