package com.example.hiringmanagersimulator;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import com.example.hiringmanagersimulator.model.Applicant;
import com.example.hiringmanagersimulator.model.ApplicantGenerator;
import com.example.hiringmanagersimulator.model.DayConfig;
import com.example.hiringmanagersimulator.model.GameSession;
import com.example.hiringmanagersimulator.model.JobEntry;
import com.example.hiringmanagersimulator.rules.Rule;
import com.example.hiringmanagersimulator.rules.Rules;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    // ─── Panels ────────────────────────────────────────────────────────────────

    private View toolsPanel;
    private View rulesPanel;
    private boolean toolsPanelOpen = false;
    private boolean rulesPanelOpen = false;

    private static final int PANEL_CONTENT_DP  = 192;
    private static final int ANIM_DURATION_MS  = 220;
    private static final int FEEDBACK_DURATION = 1200;

    private float panelPx;

    // ─── Game state ────────────────────────────────────────────────────────────

    private GameSession session;
    private int currentDay    = 1;
    private int approvedCount = 0;
    private int rejectedCount = 0;
    private boolean decidingLocked = false;

    private final Handler handler = new Handler(Looper.getMainLooper());

    // ─── Views ─────────────────────────────────────────────────────────────────

    private TextView tvDayNumber, tvBonus;
    private TextView tvApplicantLabel;
    private ScrollView resumeScroll;
    private TextView tvName, tvRole, tvContact;
    private LinearLayout containerJobs;
    private TextView tvUniversity, tvUniversityLocation, tvDegree, tvGradYear;
    private TextView tvTechSkills, tvCertifications, tvGrade, tvLanguages;
    private View briefingOverlay;
    private TextView tvBriefingDay, tvBriefingRules;

    private TextView tvStats;
    private TextView tvRulesDay, tvRulesList, tvMistakesLeft;
    private View feedbackOverlay;
    private TextView tvFeedback;
    private View endOverlay;
    private TextView tvEndTitle, tvEndBody;
    private AppCompatButton btnEndAction;
    private AppCompatButton btnApprove, btnReject;

    // ─── Lifecycle ─────────────────────────────────────────────────────────────

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        setContentView(R.layout.activity_main);
        bindViews();
        setupPanels();
        startDay(currentDay);
    }

    // ─── View binding ──────────────────────────────────────────────────────────

    private void bindViews() {
        float density = getResources().getDisplayMetrics().density;
        panelPx = PANEL_CONTENT_DP * density;

        toolsPanel              = findViewById(R.id.left_panel);
        rulesPanel              = findViewById(R.id.right_panel);
        tvDayNumber             = findViewById(R.id.tv_day_number);
        tvBonus                 = findViewById(R.id.tv_bonus);
        tvApplicantLabel        = findViewById(R.id.tv_applicant_label);
        resumeScroll            = findViewById(R.id.resume_scroll);
        tvName                  = findViewById(R.id.tv_applicant_name);
        tvRole                  = findViewById(R.id.tv_applicant_role);
        tvContact               = findViewById(R.id.tv_applicant_contact);
        containerJobs           = findViewById(R.id.container_jobs);
        tvUniversity            = findViewById(R.id.tv_university);
        tvUniversityLocation    = findViewById(R.id.tv_university_location);
        tvDegree                = findViewById(R.id.tv_degree);
        tvGradYear              = findViewById(R.id.tv_grad_year);
        tvTechSkills            = findViewById(R.id.tv_tech_skills);
        tvCertifications        = findViewById(R.id.tv_certifications);
        tvGrade                 = findViewById(R.id.tv_grade);
        tvLanguages             = findViewById(R.id.tv_languages);
        briefingOverlay         = findViewById(R.id.briefing_overlay);
        tvBriefingDay           = findViewById(R.id.tv_briefing_day);
        tvBriefingRules         = findViewById(R.id.tv_briefing_rules);
        tvStats                 = findViewById(R.id.tv_stats);
        tvRulesDay              = findViewById(R.id.tv_rules_day);
        tvRulesList             = findViewById(R.id.tv_rules_list);
        tvMistakesLeft          = findViewById(R.id.tv_mistakes_left);
        feedbackOverlay         = findViewById(R.id.feedback_overlay);
        tvFeedback              = findViewById(R.id.tv_feedback);
        endOverlay              = findViewById(R.id.end_overlay);
        tvEndTitle              = findViewById(R.id.tv_end_title);
        tvEndBody               = findViewById(R.id.tv_end_body);
        btnEndAction            = findViewById(R.id.btn_end_action);
        btnApprove              = findViewById(R.id.btn_approve);
        btnReject               = findViewById(R.id.btn_reject);
    }

    // ─── Panel setup ───────────────────────────────────────────────────────────

    private void setupPanels() {
        toolsPanel.setTranslationX(panelPx);
        rulesPanel.setTranslationX(panelPx);

        findViewById(R.id.left_tab_handle).setOnClickListener(v -> {
            if (toolsPanelOpen) { slideOut(toolsPanel); }
            else {
                slideIn(toolsPanel);
                if (rulesPanelOpen) { slideOut(rulesPanel); rulesPanelOpen = false; }
            }
            toolsPanelOpen = !toolsPanelOpen;
        });

        findViewById(R.id.right_tab_handle).setOnClickListener(v -> {
            if (rulesPanelOpen) { slideOut(rulesPanel); }
            else {
                slideIn(rulesPanel);
                if (toolsPanelOpen) { slideOut(toolsPanel); toolsPanelOpen = false; }
            }
            rulesPanelOpen = !rulesPanelOpen;
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (toolsPanelOpen || rulesPanelOpen) {
                float panelLeftEdge = getWindow().getDecorView().getWidth() - panelPx;
                if (event.getX() < panelLeftEdge) {
                    if (toolsPanelOpen) { slideOut(toolsPanel); toolsPanelOpen = false; }
                    if (rulesPanelOpen) { slideOut(rulesPanel); rulesPanelOpen = false; }
                    return true;
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    // ─── Day management ────────────────────────────────────────────────────────

    private void startDay(int day) {
        currentDay     = day;
        approvedCount  = 0;
        rejectedCount  = 0;
        decidingLocked = false;

        DayConfig config      = DayConfig.forDay(day);
        List<Applicant> queue = new ApplicantGenerator().generateQueue(config);
        session = new GameSession(config, queue);

        tvDayNumber.setText("Day " + day);
        tvBonus.setText("RM 0.00");

        tvRulesDay.setText("OBJECTIVES - DAY " + day);
        tvRulesList.setText(buildRulesText(config));

        updateMistakesDisplay();
        updateStats();

        btnApprove.setOnClickListener(v -> onDecision(true));
        btnReject.setOnClickListener(v -> onDecision(false));
        btnEndAction.setOnClickListener(v -> {
            endOverlay.setVisibility(View.GONE);
            startDay(currentDay + 1);
        });

        showBriefing(config);
    }

    // ─── Decision handling ─────────────────────────────────────────────────────

    private void onDecision(boolean approved) {
        if (decidingLocked) return;
        decidingLocked = true;
        if (approved) approvedCount++; else rejectedCount++;
        showFeedback(session.recordDecision(approved));
    }

    private void showFeedback(GameSession.DecisionOutcome outcome) {
        tvFeedback.setText(outcome.getFeedbackLine());
        tvFeedback.setTextColor(outcome.correct
                ? ContextCompat.getColor(this, android.R.color.holo_green_light)
                : ContextCompat.getColor(this, android.R.color.holo_red_light));
        feedbackOverlay.setVisibility(View.VISIBLE);

        updateStats();
        updateMistakesDisplay();
        tvBonus.setText(String.format(Locale.US, "RM %.2f", session.calculateDailyBonus()));

        handler.postDelayed(() -> {
            feedbackOverlay.setVisibility(View.GONE);
            decidingLocked = false;
            if      (session.isGameOver())    showGameOver();
            else if (session.isDayComplete()) showDayComplete();
            else                              showCurrentApplicant();
        }, FEEDBACK_DURATION);
    }

    // ─── Rule text builders ────────────────────────────────────────────────────

    /**
     * Builds the text shown in the Rules side panel.
     * For UniversityRule and SkillRule, appends the accepted values indented below.
     */
    private String buildRulesText(DayConfig config) {
        StringBuilder sb = new StringBuilder();
        for (Rule r : config.getRules()) {
            sb.append(">  ").append(r.getLabel().toUpperCase()).append("\n");
            if (r instanceof Rules.UniversityRule) {
                for (String u : ((Rules.UniversityRule) r).accepted) {
                    sb.append("     - ").append(u).append("\n");
                }
            } else if (r instanceof Rules.SkillRule) {
                sb.append("     ").append(String.join(" / ", ((Rules.SkillRule) r).required)).append("\n");
            }
            sb.append("\n");
        }
        return sb.toString().trim();
    }

    /**
     * Builds the text shown in the post-it briefing overlay.
     * Same structure but uses checkbox format.
     */
    private String buildBriefingText(DayConfig config) {
        StringBuilder sb = new StringBuilder();
        for (Rule r : config.getRules()) {
            sb.append("[ ]  ").append(r.getLabel()).append("\n");
            if (r instanceof Rules.UniversityRule) {
                for (String u : ((Rules.UniversityRule) r).accepted) {
                    sb.append("       • ").append(u).append("\n");
                }
            } else if (r instanceof Rules.SkillRule) {
                sb.append("       ").append(String.join("  /  ", ((Rules.SkillRule) r).required)).append("\n");
            }
            sb.append("\n");
        }
        return sb.toString().trim();
    }

        // ─── Day briefing ──────────────────────────────────────────────────────────

    private void showBriefing(DayConfig config) {
        tvBriefingDay.setText("DAY " + config.dayNumber + " BRIEFING");

        tvBriefingRules.setText(buildBriefingText(config));

        briefingOverlay.setVisibility(View.VISIBLE);

        findViewById(R.id.btn_briefing_dismiss).setOnClickListener(v -> {
            briefingOverlay.setVisibility(View.GONE);
            showCurrentApplicant();
        });
    }

    // ─── Resume rendering ──────────────────────────────────────────────────────

    private void showCurrentApplicant() {
        Applicant a = session.currentApplicant();
        if (a == null) return;

        resumeScroll.scrollTo(0, 0);
        tvApplicantLabel.setText(session.getApplicantLabel());

        // Header
        tvName.setText(a.getFullName().toUpperCase());
        tvRole.setText(a.targetRole);
        tvContact.setText(a.city + "   •   " + a.email + "   •   " + a.phone);

        // Jobs — rebuild dynamically
        containerJobs.removeAllViews();
        for (JobEntry job : a.jobHistory) {
            addJobView(job);
        }

        // Education
        tvUniversity.setText(a.university.toUpperCase());
        tvUniversityLocation.setText(a.universityLocation);
        tvDegree.setText(a.degree);
        tvGradYear.setText(a.graduationYear);

        // Additional information
        tvTechSkills.setText("Technical Skills:   " + a.technicalSkills);
        tvCertifications.setText("Certifications:       " + a.certifications);
        tvGrade.setText("Seniority Grade:    " + a.displayedGrade);
        tvLanguages.setText("Languages:            " + a.languages);
    }


    private void addJobView(JobEntry job) {
        // Company + location row
        LinearLayout row1 = hRow();
        row1.addView(boldText(job.company.toUpperCase(), 10, 1f));
        row1.addView(boldText(job.location, 10, 0f));
        containerJobs.addView(row1);

        // Title + date row
        LinearLayout row2 = hRow();
        row2.addView(italicText(job.title, 10, true, 1f));
        row2.addView(italicText(job.dateRange(), 10, false, 0f));
        containerJobs.addView(row2);

        // Bullets — use MATCH_PARENT width, not weight
        for (String bullet : job.bullets) {
            TextView tv = new TextView(this);
            tv.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            tv.setText("•  " + bullet);
            tv.setTextSize(11);
            tv.setTextColor(ContextCompat.getColor(this, R.color.paper_text));
            tv.setPadding(dp(4), 0, 0, dp(2));
            tv.setLineSpacing(dp(1), 1f);
            containerJobs.addView(tv);
        }

        // Spacer
        View spacer = new View(this);
        spacer.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, dp(10)));
        containerJobs.addView(spacer);
    }

    // ─── End screens ───────────────────────────────────────────────────────────

    private void showDayComplete() {
        tvEndTitle.setText("DAY " + currentDay + " COMPLETE");
        tvEndBody.setText(
                "CORRECT  : " + session.getCorrectCount() + "\n" +
                "MISTAKES : " + session.getMistakeCount() + "\n\n" +
                "BONUS    : " + String.format(Locale.US, "RM %.2f", session.calculateDailyBonus()));
        btnEndAction.setText("NEXT DAY");
        endOverlay.setVisibility(View.VISIBLE);
    }

    private void showGameOver() {
        tvEndTitle.setText("YOU'RE FIRED");
        tvEndBody.setText(
                "Too many violations approved.\n\n" +
                "CORRECT       : " + session.getCorrectCount() + "\n" +
                "MISTAKES      : " + session.getMistakeCount() + "\n" +
                "DAYS SURVIVED : " + currentDay);
        btnEndAction.setText("TRY AGAIN");
        btnEndAction.setOnClickListener(v -> {
            endOverlay.setVisibility(View.GONE);
            startDay(1);
        });
        endOverlay.setVisibility(View.VISIBLE);
    }

    // ─── Stats helpers ─────────────────────────────────────────────────────────

    private void updateStats() {
        int pending = session.getTotalApplicants() - session.getCurrentIndex();
        tvStats.setText(
                "APPROVED :  " + String.format("%02d", approvedCount) + "\n" +
                "REJECTED :  " + String.format("%02d", rejectedCount) + "\n" +
                "MISTAKES :  " + String.format("%02d", session.getMistakeCount()) + "\n" +
                "PENDING  :  " + String.format("%02d", pending));
    }

    private void updateMistakesDisplay() {
        int left  = session.getMistakesLeft();
        int total = session.config.mistakeQuota;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < total; i++) {
            if (i > 0) sb.append("  ");
            sb.append(i < left ? "O" : "X");
        }
        tvMistakesLeft.setText(sb.toString());
    }

    // ─── Panel animation ───────────────────────────────────────────────────────

    private void slideIn(View p) { p.animate().translationX(0).setDuration(ANIM_DURATION_MS).start(); }
    private void slideOut(View p) { p.animate().translationX(panelPx).setDuration(ANIM_DURATION_MS).start(); }

    // ─── View factory helpers ──────────────────────────────────────────────────

    private LinearLayout hRow() {
        LinearLayout r = new LinearLayout(this);
        r.setOrientation(LinearLayout.HORIZONTAL);
        r.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        return r;
    }

    /** Text with BOLD style. weight>0 → width=0 (for use in horizontal rows). */
    private TextView boldText(String text, int sp, float weight) {
        TextView tv = new TextView(this);
        tv.setLayoutParams(new LinearLayout.LayoutParams(
                weight > 0 ? 0 : LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, weight));
        tv.setText(text);
        tv.setTextSize(sp);
        tv.setTextColor(ContextCompat.getColor(this, R.color.paper_text));
        tv.setTypeface(null, android.graphics.Typeface.BOLD);
        return tv;
    }

    /** Italic (optionally bold) text for horizontal rows. */
    private TextView italicText(String text, int sp, boolean bold, float weight) {
        TextView tv = boldText(text, sp, weight);
        tv.setTypeface(null, bold
                ? android.graphics.Typeface.BOLD_ITALIC
                : android.graphics.Typeface.ITALIC);
        return tv;
    }

    private int dp(int dp) {
        return Math.round(dp * getResources().getDisplayMetrics().density);
    }
}
