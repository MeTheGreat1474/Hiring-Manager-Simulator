package com.example.hiringmanagersimulator.model;

import com.example.hiringmanagersimulator.rules.RuleResult;
import com.example.hiringmanagersimulator.rules.RulesEngine;

import java.util.List;

/**
 * Holds all mutable state for the current game day.
 *
 * Responsibilities:
 *   - Owns the applicant queue and tracks the current position
 *   - Records each decision and whether it was correct
 *   - Tracks mistake count, score, and bonus
 *   - Determines game-over and day-complete conditions
 *
 * Usage pattern (called from your Activity/ViewModel):
 *
 *   DayConfig config    = DayConfig.forDay(currentDay);
 *   List<Applicant> q   = new ApplicantGenerator().generateQueue(config);
 *   GameSession session  = new GameSession(config, q);
 *
 *   // When player taps APPROVE or REJECT:
 *   DecisionOutcome out  = session.recordDecision(true); // true = approved
 *   if (session.isGameOver())   → show fired screen
 *   if (session.isDayComplete()) → show end-of-day summary
 */
public class GameSession {

    // ─── Config ────────────────────────────────────────────────────────────────

    public final DayConfig config;
    private final RulesEngine rulesEngine;

    // ─── Queue state ───────────────────────────────────────────────────────────

    private final List<Applicant> queue;
    private int currentIndex = 0;

    // ─── Score tracking ────────────────────────────────────────────────────────

    private int mistakeCount  = 0;
    private int correctCount  = 0;

    /** Base pay per correct decision (RM). */
    private static final float PAY_PER_CORRECT = 50f;
    /** Penalty per mistake (docked from bonus). */
    private static final float PENALTY_PER_MISTAKE = 30f;

    // ─── Constructor ───────────────────────────────────────────────────────────

    public GameSession(DayConfig config, List<Applicant> queue) {
        this.config      = config;
        this.queue       = queue;
        this.rulesEngine = new RulesEngine(config.getRules());
    }

    // ─── Queue access ──────────────────────────────────────────────────────────

    /** The applicant currently shown to the player. Null if queue is exhausted. */
    public Applicant currentApplicant() {
        if (currentIndex >= queue.size()) return null;
        return queue.get(currentIndex);
    }

    public int getCurrentIndex()  { return currentIndex; }
    public int getTotalApplicants() { return queue.size(); }

    /** 1-based display string, e.g. "Applicant #3 of 10". */
    public String getApplicantLabel() {
        return "Applicant #" + String.format("%02d", currentIndex + 1)
                + " of " + String.format("%02d", queue.size());
    }

    // ─── Decision recording ────────────────────────────────────────────────────

    /**
     * Records the player's decision for the current applicant.
     *
     * @param playerApproved true if the player tapped APPROVE, false for REJECT
     * @return DecisionOutcome describing correctness and the violated rule (if any)
     */
    public DecisionOutcome recordDecision(boolean playerApproved) {
        Applicant applicant = currentApplicant();
        if (applicant == null) {
            throw new IllegalStateException("No current applicant to decide on.");
        }

        RuleResult truth = rulesEngine.evaluate(applicant);
        boolean correct  = (playerApproved == truth.shouldApprove);

        if (correct) {
            correctCount++;
        } else {
            mistakeCount++;
        }

        currentIndex++;

        return new DecisionOutcome(
                correct,
                playerApproved,
                truth.shouldApprove,
                truth.violation,
                truth.failedRuleLabel
        );
    }

    // ─── State queries ─────────────────────────────────────────────────────────

    /** True if the player has exceeded the allowed mistake quota. */
    public boolean isGameOver() {
        return mistakeCount > config.mistakeQuota;
    }

    /** True if all applicants have been reviewed (and game is not over). */
    public boolean isDayComplete() {
        return !isGameOver() && currentIndex >= queue.size();
    }

    public int getMistakeCount()  { return mistakeCount; }
    public int getCorrectCount()  { return correctCount; }
    public int getMistakesLeft()  { return Math.max(0, config.mistakeQuota - mistakeCount); }

    // ─── Bonus calculation ─────────────────────────────────────────────────────

    /** Total bonus earned at end of day (cannot go below zero). */
    public float calculateDailyBonus() {
        float earned  = correctCount * PAY_PER_CORRECT;
        float penalty = mistakeCount * PENALTY_PER_MISTAKE;
        return Math.max(0f, earned - penalty);
    }

    // ─── Inner: outcome of a single decision ───────────────────────────────────

    public static class DecisionOutcome {
        /** Whether the player's decision matched ground truth. */
        public final boolean correct;
        public final boolean playerApproved;
        public final boolean shouldHaveApproved;
        public final com.example.hiringmanagersimulator.model.ViolationType violation;
        /** Label of the rule the player missed, or null if correct. */
        public final String missedRuleLabel;

        DecisionOutcome(boolean correct, boolean playerApproved, boolean shouldHaveApproved,
                        com.example.hiringmanagersimulator.model.ViolationType violation,
                        String missedRuleLabel) {
            this.correct            = correct;
            this.playerApproved     = playerApproved;
            this.shouldHaveApproved = shouldHaveApproved;
            this.violation          = violation;
            this.missedRuleLabel    = missedRuleLabel;
        }

        /**
         * Human-readable feedback line shown briefly after each decision.
         * e.g. "✓ Correct" or "✗ Violation — Work permit must not be expired"
         */
        public String getFeedbackLine() {
            if (correct) return "✓  Correct";
            if (playerApproved) {
                return "✗  Should have rejected — " + (missedRuleLabel != null ? missedRuleLabel : "rule violation");
            } else {
                return "✗  Should have approved — applicant meets all requirements";
            }
        }
    }
}
