package com.example.hiringmanagersimulator.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.example.hiringmanagersimulator.rules.Rules;

public class ApplicantGenerator {

    private static final String[] FIRST_NAMES = {
            "Ahmad", "Sarah", "Marcus", "Priya", "James", "Lin", "Omar", "Elena",
            "David", "Fatima", "Ryan", "Mei", "Jordan", "Aisha", "Nathan", "Chloe",
            "Kevin", "Nurul", "Andre", "Siti", "Daniel", "Rina", "Hassan", "Claire"
    };

    private static final String[] LAST_NAMES = {
            "Hassan", "Chen", "Williams", "Patel", "Johnson", "Wong", "Al-Rashid",
            "Petrov", "Kim", "Rodriguez", "Okonkwo", "Tanaka", "Mueller", "Nair",
            "Ibrahim", "Lim", "Santos", "Krishnan", "Park", "Abdullah"
    };

    private static final String[] CITIES = {
            "Selangor, MY", "Kuala Lumpur, MY", "Penang, MY",
            "Johor, MY", "Perak, MY", "Negeri Sembilan, MY",
            "Melaka, MY", "Kedah, MY", "Pahang, MY", "Sabah, MY"
    };

    private static final String[] LEGIT_COMPANIES = {
            "Axiata Group", "Petronas Digital", "Grab Holdings", "Sea Limited",
            "TechNova Solutions", "InfraCore Sdn Bhd", "DataBridge Consulting",
            "ClearPath Technologies", "Quantum Analytics", "NexGen Systems",
            "Maybank Technology", "CIMB Digital", "Telekom Malaysia", "AirAsia X",
            "Shopee Malaysia", "Lazada Group", "Maxis Communications", "Celcom Axiata"
    };



    private static final String[][] ROLES_BY_DEPT = {
            {"Engineering",    "Software Engineer", "Backend Engineer", "DevOps Engineer", "Full Stack Engineer"},
            {"Data Science",   "Data Scientist", "ML Engineer", "Data Analyst", "Analytics Engineer"},
            {"Infrastructure", "Systems Administrator", "Network Engineer", "Cloud Architect", "Site Reliability Engineer"},
            {"Marketing",      "Marketing Manager", "Brand Strategist", "Growth Manager", "Digital Marketing Lead"},
            {"Sales",          "Account Executive", "Sales Manager", "Business Developer", "Enterprise Sales Lead"},
    };

    private static final String[] GRADES = {
            "JUNIOR I", "JUNIOR II", "MID I", "MID II", "SENIOR I", "SENIOR II", "PRINCIPAL"
    };
    private static final String[] SENIOR_GRADES = {"SENIOR I", "SENIOR II", "PRINCIPAL"};

    private static final String[] UNIVERSITIES = {
            "Universiti Malaya",                // 0  Kuala Lumpur
            "Universiti Teknologi Malaysia",     // 1  Johor
            "Universiti Putra Malaysia",         // 2  Selangor
            "Universiti Kebangsaan Malaysia",    // 3  Selangor
            "Universiti Sains Malaysia",         // 4  Penang
            "Universiti Teknologi MARA",         // 5  Selangor
            "Universiti Utara Malaysia",         // 6  Kedah
            "Multimedia University",             // 7  Selangor
            "Asia Pacific University",           // 8  Kuala Lumpur
            "Taylor's University",              // 9  Selangor
            "Sunway University",                 // 10 Selangor
            "INTI International University"      // 11 Negeri Sembilan
    };

    private static final String[] UNI_LOCATIONS = {
            "Kuala Lumpur, MY",     // Universiti Malaya
            "Johor, MY",            // Universiti Teknologi Malaysia
            "Selangor, MY",         // Universiti Putra Malaysia
            "Selangor, MY",         // Universiti Kebangsaan Malaysia
            "Penang, MY",           // Universiti Sains Malaysia
            "Selangor, MY",         // Universiti Teknologi MARA
            "Kedah, MY",            // Universiti Utara Malaysia
            "Selangor, MY",         // Multimedia University
            "Kuala Lumpur, MY",     // Asia Pacific University
            "Selangor, MY",         // Taylor's University
            "Selangor, MY",         // Sunway University
            "Negeri Sembilan, MY"   // INTI International University
    };

    private static final String[] DEGREES = {
            "Bachelor of Computer Science",
            "Bachelor of Engineering (Electrical & Electronic)",
            "Bachelor of Information Technology",
            "Master of Business Administration",
            "Bachelor of Science in Data Analytics",
            "Bachelor of Engineering (Software)",
            "Master of Science in Computer Science"
    };

    private static final String[] TECH_SKILL_SETS = {
            "Python, R, TensorFlow, Apache Spark, SQL",
            "Java, Kotlin, Spring Boot, Docker, Kubernetes",
            "AWS, GCP, Terraform, Ansible, Linux",
            "Python, SQL, Power BI, Tableau, Excel",
            "React, Node.js, TypeScript, MongoDB, Redis",
            "C++, Python, MATLAB, Embedded Systems, RTOS",
            "Salesforce, SQL, HubSpot, Marketo, Google Analytics"
    };

    private static final String[] CERT_SETS = {
            "AWS Certified Solutions Architect (2022), PMP (2021)",
            "Google Cloud Professional (2023), Scrum Master (2020)",
            "PRINCE2 Practitioner (2022), ITIL Foundation (2021)",
            "CFA Level I (2022), Bloomberg Market Concepts (2021)",
            "Certified Data Scientist — Microsoft (2023), Power BI Certified (2022)",
            "Cisco CCNA (2021), CompTIA Security+ (2022)",
            "PMI-ACP (2022), SAFe Agilist (2021)"
    };

    private static final String[] LANGUAGE_SETS = {
            "English (Fluent), Malay (Native), Mandarin (Conversational)",
            "English (Fluent), Bahasa Indonesia (Native), Javanese (Native)",
            "English (Fluent), Thai (Native), Mandarin (Basic)",
            "English (Fluent), Tagalog (Native), Cebuano (Native)",
            "English (Fluent), Malay (Fluent), Tamil (Native)",
            "English (Fluent), Vietnamese (Native), French (Basic)"
    };

    // Bullet templates per category
    private static final String[][] BULLET_TEMPLATES = {
        {   // Engineering / Infrastructure
            "Led migration of monolithic architecture to microservices, reducing deployment time by %d%%",
            "Designed and maintained CI/CD pipelines handling %d+ releases per month with zero downtime",
            "Built %s platform serving %dM+ daily active users across %d regions",
            "Reduced infrastructure costs by %d%% through rightsizing and reserved instance optimization",
            "Mentored team of %d junior engineers; established code review standards adopted org-wide",
            "Implemented automated testing suite achieving %d%% code coverage across %d microservices",
            "Led on-call rotation for %d+ services; reduced incident MTTR by %d%% in first quarter",
            "Collaborated with product and design to deliver %d features across %d sprints"
        },
        {   // Data / Analytics
            "Built ML pipeline improving recommendation accuracy by %d%%, driving %dM additional revenue",
            "Designed data warehouse consolidating %d+ data sources; reduced reporting time by %d%%",
            "Delivered %d+ dashboards in Power BI and Tableau used by %d business units",
            "Led A/B testing framework evaluating %d+ experiments per quarter with statistical rigor",
            "Trained and deployed %d machine learning models to production serving %dK+ predictions daily",
            "Reduced data pipeline failures by %d%% through automated monitoring and alerting",
            "Partnered with %d cross-functional teams to define KPIs and OKRs for quarterly planning"
        },
        {   // Management / Strategy
            "Led cross-functional team of %d across %d departments to deliver $%dM project on schedule",
            "Spearheaded adoption of %s across organization; onboarded %d+ users in %d months",
            "Managed vendor relationships with %d partners, negotiating contracts worth $%dM annually",
            "Drove %d%% increase in team productivity by restructuring sprint planning and retrospectives",
            "Presented quarterly business reviews to C-suite; recommendations adopted in %d of %d quarters",
            "Reduced operational overhead by %d%% by consolidating %d redundant processes",
            "Coached and promoted %d team members to senior roles within %d-year tenure"
        }
    };

    private final Random rng;

    public ApplicantGenerator(long seed) { this.rng = new Random(seed); }
    public ApplicantGenerator()          { this.rng = new Random(); }

    public List<Applicant> generateQueue(DayConfig config) {
        int total     = config.applicantCount;
        int violators = Math.max(1, (int) Math.round(total * 0.4));
        int clean     = total - violators;

        List<ViolationType> activeViolations = getActiveViolations(config);
        List<Applicant> queue = new ArrayList<>();

        for (int i = 0; i < violators; i++) {
            ViolationType v = activeViolations.get(rng.nextInt(activeViolations.size()));
            queue.add(buildApplicant(config, v));
        }
        for (int i = 0; i < clean; i++) {
            queue.add(buildApplicant(config, ViolationType.NONE));
        }

        Collections.shuffle(queue, rng);
        return queue;
    }

    private List<ViolationType> getActiveViolations(DayConfig config) {
        List<ViolationType> active = new ArrayList<>();
        for (com.example.hiringmanagersimulator.rules.Rule r : config.getRules()) {
            String label = r.getLabel().toLowerCase();
            if (label.contains("experience")) {
                active.add(ViolationType.INSUFFICIENT_EXPERIENCE);
                // Forgeries only from Day 4+ — magnifier not yet available earlier
                if (config.dayNumber >= 4) active.add(ViolationType.FALSIFIED_EXPERIENCE);
            }
            if (r instanceof Rules.BlacklistRule) active.add(ViolationType.BLACKLISTED_COMPANY);
            if (r instanceof Rules.GradeRule) {
                active.add(ViolationType.WRONG_GRADE);
                active.add(ViolationType.FALSIFIED_GRADE);
            }
            // Typed rules — use instanceof so constraints are readable by the generator
            if (r instanceof Rules.UniversityRule) active.add(ViolationType.WRONG_UNIVERSITY);
            if (r instanceof Rules.SkillRule)      active.add(ViolationType.MISSING_REQUIRED_SKILL);
        }
        List<ViolationType> deduped = new ArrayList<>();
        for (ViolationType v : active) if (!deduped.contains(v)) deduped.add(v);
        return deduped;
    }

    private Applicant buildApplicant(DayConfig config, ViolationType violation) {
        String firstName   = pick(FIRST_NAMES);
        String lastName    = pick(LAST_NAMES);
        String[] deptRole  = pickDeptRole();
        String dept = deptRole[0], role = deptRole[1];

        // Experience
        float minExp = getMinExp(config);
        float actualExp, displayedExp;
        if (violation == ViolationType.INSUFFICIENT_EXPERIENCE) {
            actualExp    = randFloat(0.5f, Math.max(0.6f, minExp - 0.5f));
            displayedExp = actualExp;
        } else if (violation == ViolationType.FALSIFIED_EXPERIENCE) {
            actualExp    = randFloat(0.5f, Math.max(0.6f, minExp - 0.5f));
            displayedExp = randFloat(minExp, minExp + 3);
        } else {
            actualExp    = randFloat(minExp, minExp + 8);
            displayedExp = actualExp;
        }

        // Grade
        boolean gradeRequired = config.dayNumber >= 3;
        String actualGrade, displayedGrade;
        if (violation == ViolationType.WRONG_GRADE) {
            actualGrade    = pick(new String[]{"JUNIOR I", "JUNIOR II", "MID I", "MID II"});
            displayedGrade = actualGrade;
        } else if (violation == ViolationType.FALSIFIED_GRADE) {
            actualGrade    = pick(new String[]{"JUNIOR I", "JUNIOR II", "MID I", "MID II"});
            displayedGrade = pick(SENIOR_GRADES);
        } else {
            actualGrade    = gradeRequired ? pick(SENIOR_GRADES) : pick(GRADES);
            displayedGrade = actualGrade;
        }

        boolean hasPermit  = violation != ViolationType.NO_WORK_PERMIT;
        boolean expired    = violation == ViolationType.EXPIRED_WORK_PERMIT;
        boolean offense    = violation == ViolationType.PRIOR_OFFENSE;
        boolean blacklisted = violation == ViolationType.BLACKLISTED_COMPANY;

        // Bullet category based on dept
        int bulletCat = dept.equals("Engineering") || dept.equals("Infrastructure") ? 0
                      : dept.equals("Data Science") ? 1 : 2;

        List<JobEntry> jobs = buildJobHistory(config, actualExp, displayedExp, blacklisted, bulletCat);

        // Education — pick university based on violation
        String uni, uniLoc;
        if (violation == ViolationType.WRONG_UNIVERSITY) {
            // Pick a university NOT in the accepted list
            List<String> accepted = getAcceptedUniversities(config);
            List<String> rejected = new ArrayList<>();
            for (int i = 0; i < UNIVERSITIES.length; i++) {
                if (!accepted.contains(UNIVERSITIES[i])) {
                    rejected.add(i + ":" + UNIVERSITIES[i]);
                }
            }
            if (rejected.isEmpty()) {
                // Fallback: all unis accepted, just pick any
                int idx = rng.nextInt(UNIVERSITIES.length);
                uni = UNIVERSITIES[idx]; uniLoc = UNI_LOCATIONS[idx];
            } else {
                String picked = rejected.get(rng.nextInt(rejected.size()));
                int idx = Integer.parseInt(picked.split(":")[0]);
                uni = UNIVERSITIES[idx]; uniLoc = UNI_LOCATIONS[idx];
            }
        } else {
            // Pick a university from the accepted list (or any on early days)
            List<String> accepted = getAcceptedUniversities(config);
            if (accepted.isEmpty()) {
                int idx = rng.nextInt(UNIVERSITIES.length);
                uni = UNIVERSITIES[idx]; uniLoc = UNI_LOCATIONS[idx];
            } else {
                String pickedUni = accepted.get(rng.nextInt(accepted.size()));
                int idx = Arrays.asList(UNIVERSITIES).indexOf(pickedUni);
                if (idx < 0) idx = 0;
                uni = UNIVERSITIES[idx]; uniLoc = UNI_LOCATIONS[idx];
            }
        }
        String deg   = pick(DEGREES);
        int gradYear = 2025 - (int) actualExp - rng.nextInt(3) - 18;

        // Contact
        String email = firstName.toLowerCase() + "." + lastName.toLowerCase()
                + "@" + pick(new String[]{"gmail.com", "yahoo.com", "outlook.com"});
        String phone = "+60 1" + rng.nextInt(9) + "-" + digits(3) + " " + digits(4);

        // Additional info — skill set must match or violate required skills
        List<String> requiredSkills = getRequiredSkills(config);
        String techSkills;
        if (violation == ViolationType.MISSING_REQUIRED_SKILL && !requiredSkills.isEmpty()) {
            // Pick a tech skill set that contains NONE of the required skills
            techSkills = pickSkillSetExcluding(requiredSkills);
        } else if (!requiredSkills.isEmpty()) {
            // Must include at least one required skill — pick a set that does
            techSkills = pickSkillSetIncluding(requiredSkills);
        } else {
            techSkills = pick(TECH_SKILL_SETS);
        }
        String certs = pick(CERT_SETS);
        String langs = pick(LANGUAGE_SETS);

        // Discrete skill tags aligned with techSkills
        List<String> skillTags = extractSkillTags(techSkills);

        return new Applicant.Builder()
                .name(firstName, lastName)
                .email(email)
                .phone(phone)
                .city(pick(CITIES))
                .role(role, dept)
                .experience(displayedExp, actualExp)
                .jobHistory(jobs)
                .grade(displayedGrade, actualGrade)
                .workPermit(hasPermit, expired)
                .priorOffense(offense)
                .blacklistedCompany(blacklisted)
                .education(uni, uniLoc, deg, String.valueOf(Math.max(1995, gradYear)))
                .skills(skillTags)
                .technicalSkills(techSkills)
                .certifications(certs)
                .languages(langs)
                .violation(violation)
                .build();
    }

    private List<JobEntry> buildJobHistory(DayConfig config, float actualExp, float displayedExp,
                                           boolean blacklisted, int bulletCat) {
        List<JobEntry> jobs = new ArrayList<>();
        int currentYear = 2025;
        float remaining = displayedExp;

        // Job 1 — most recent
        String company1 = blacklisted ? pickBlacklisted(config) : pick(LEGIT_COMPANIES);
        int dur1   = Math.max(1, Math.round(remaining * 0.55f));
        int start1 = currentYear - dur1;
        jobs.add(new JobEntry(
                company1,
                pick(new String[]{"Senior Engineer", "Senior Analyst", "Senior Manager",
                        "Lead Consultant", "Principal Specialist"}),
                pick(new String[]{"Kuala Lumpur, MY", "Selangor, MY", "Penang, MY"}),
                String.valueOf(start1), "Present",
                makeBullets(bulletCat, 3)));
        remaining -= dur1;

        // Job 2
        if (remaining >= 1f) {
            int dur2   = Math.max(1, Math.round(remaining * 0.7f));
            int end2   = start1 - 1;
            int start2 = end2 - dur2 + 1;
            jobs.add(new JobEntry(
                    pick(LEGIT_COMPANIES),
                    pick(new String[]{"Engineer", "Analyst", "Specialist", "Consultant", "Associate"}),
                    pick(new String[]{"Selangor, MY", "Johor, MY", "Perak, MY"}),
                    String.valueOf(Math.max(2000, start2)), String.valueOf(end2),
                    makeBullets(bulletCat, 3)));
            remaining -= dur2;
        }

        // Job 3 (for applicants with long experience)
        if (remaining >= 1.5f) {
            int dur3   = Math.max(1, (int) remaining);
            int end3   = jobs.get(jobs.size() - 1).startYear.equals("Present")
                    ? 2020 : Integer.parseInt(jobs.get(jobs.size() - 1).startYear) - 1;
            int start3 = end3 - dur3 + 1;
            jobs.add(new JobEntry(
                    pick(LEGIT_COMPANIES),
                    pick(new String[]{"Junior Engineer", "Analyst", "Associate", "Coordinator"}),
                    pick(new String[]{"Negeri Sembilan, MY", "Melaka, MY", "Kedah, MY"}),
                    String.valueOf(Math.max(1998, start3)), String.valueOf(end3),
                    makeBullets(bulletCat, 2)));
        }

        return jobs;
    }

    private List<String> makeBullets(int category, int count) {
        String[] pool = BULLET_TEMPLATES[category];
        List<String> poolList = new ArrayList<>(Arrays.asList(pool));
        Collections.shuffle(poolList, rng);

        List<String> result = new ArrayList<>();
        for (int i = 0; i < Math.min(count, poolList.size()); i++) {
            result.add(fillTemplate(poolList.get(i)));
        }
        return result;
    }

    private String fillTemplate(String t) {
        String[] techs = {"Agile", "CI/CD", "Kubernetes", "Terraform", "the new platform",
                "Salesforce", "the ERP system", "Power BI", "our ML framework"};
        while (t.contains("%s"))  t = t.replaceFirst("%s", pick(techs));
        while (t.contains("%d")) {
            // Peek ahead: is this %d followed by %% (i.e. a percentage value)?
            boolean isPct = t.matches("(?s).*%d%%.*");
            boolean isMoney = t.contains("$");
            int val = isPct    ? rng.nextInt(35) + 10
                    : isMoney  ? rng.nextInt(10) + 1
                    :            rng.nextInt(12) + 2;
            t = t.replaceFirst("%d", String.valueOf(val));
        }
        // Resolve escaped percent signs left by the templates
        t = t.replace("%%", "%");
        return t;
    }

    private <T> T pick(T[] arr) { return arr[rng.nextInt(arr.length)]; }

    private List<String> pickFrom(String[] pool, int min, int max) {
        int count = min + rng.nextInt(max - min + 1);
        List<String> list = new ArrayList<>(Arrays.asList(pool));
        Collections.shuffle(list, rng);
        return list.subList(0, Math.min(count, list.size()));
    }

    private String[] pickDeptRole() {
        String[] entry = ROLES_BY_DEPT[rng.nextInt(ROLES_BY_DEPT.length)];
        return new String[]{entry[0], entry[1 + rng.nextInt(entry.length - 1)]};
    }

    private float randFloat(float min, float max) {
        if (min >= max) return min;
        return min + rng.nextFloat() * (max - min);
    }

    private float getMinExp(DayConfig config) {
        for (com.example.hiringmanagersimulator.rules.Rule r : config.getRules()) {
            if (r.getLabel().startsWith("Min.")) {
                try { return Float.parseFloat(r.getLabel().split(" ")[1]); }
                catch (Exception ignored) {}
            }
        }
        return 3;
    }

    private String digits(int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) sb.append(rng.nextInt(10));
        return sb.toString();
    }


    /** Returns a random blacklisted company name from the day's BlacklistRule, or a fallback. */
    private String pickBlacklisted(DayConfig config) {
        for (com.example.hiringmanagersimulator.rules.Rule r : config.getRules()) {
            if (r instanceof Rules.BlacklistRule) {
                List<String> list = ((Rules.BlacklistRule) r).companies;
                if (!list.isEmpty()) return list.get(rng.nextInt(list.size()));
            }
        }
        return DayConfig.BLACKLISTED_COMPANIES[rng.nextInt(DayConfig.BLACKLISTED_COMPANIES.length)];
    }

    // ─── University helpers ────────────────────────────────────────────────────

    /** Returns the accepted university list from the day's UniversityRule, or empty. */
    private List<String> getAcceptedUniversities(DayConfig config) {
        for (com.example.hiringmanagersimulator.rules.Rule r : config.getRules()) {
            if (r instanceof Rules.UniversityRule) {
                return ((Rules.UniversityRule) r).accepted;
            }
        }
        return new ArrayList<>();
    }

    // ─── Skill helpers ─────────────────────────────────────────────────────────

    /** Returns the required skill list from the day's SkillRule, or empty. */
    private List<String> getRequiredSkills(DayConfig config) {
        for (com.example.hiringmanagersimulator.rules.Rule r : config.getRules()) {
            if (r instanceof Rules.SkillRule) {
                return ((Rules.SkillRule) r).required;
            }
        }
        return new ArrayList<>();
    }

    /**
     * Picks a TECH_SKILL_SET string that contains NONE of the required skills.
     * Falls back to the first non-matching set; if all match, generates a safe fallback.
     */
    private String pickSkillSetExcluding(List<String> required) {
        List<String> candidates = new ArrayList<>(Arrays.asList(TECH_SKILL_SETS));
        Collections.shuffle(candidates, rng);
        for (String set : candidates) {
            boolean hasRequired = false;
            for (String req : required) {
                if (set.toLowerCase().contains(req.toLowerCase())) {
                    hasRequired = true;
                    break;
                }
            }
            if (!hasRequired) return set;
        }
        // All sets contain a required skill — build a safe fallback with no required terms
        return "Microsoft Office, Google Workspace, Slack, Confluence, Notion";
    }

    /**
     * Picks a TECH_SKILL_SET string that contains AT LEAST ONE of the required skills.
     * Falls back to a string built from a required skill if no set matches.
     */
    private String pickSkillSetIncluding(List<String> required) {
        List<String> candidates = new ArrayList<>(Arrays.asList(TECH_SKILL_SETS));
        Collections.shuffle(candidates, rng);
        for (String set : candidates) {
            for (String req : required) {
                if (set.toLowerCase().contains(req.toLowerCase())) return set;
            }
        }
        // No set naturally includes a required skill — prepend one
        String req = required.get(rng.nextInt(required.size()));
        return req + ", Docker, Linux, Git, Bash";
    }

    /**
     * Extracts a List<String> of individual skill tokens from a comma-separated techSkills string.
     * Used to populate the Applicant's discrete skills list consistently with technicalSkills.
     */
    private List<String> extractSkillTags(String techSkills) {
        List<String> tags = new ArrayList<>();
        for (String s : techSkills.split(",")) {
            String trimmed = s.trim();
            if (!trimmed.isEmpty()) tags.add(trimmed);
        }
        return tags;
    }
}
