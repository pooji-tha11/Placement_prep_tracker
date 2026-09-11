-- ================================
-- resumes
-- ================================
CREATE TABLE resumes (
    id VARCHAR(50) PRIMARY KEY,
    label VARCHAR(255) NOT NULL,
    version VARCHAR(50) NOT NULL,
    filename VARCHAR(255) NOT NULL,
    date_added DATE NOT NULL,
    created_at DATETIME NOT NULL
);

-- ================================
-- applications  (real foreign key: resume_id -> resumes.id)
-- ================================
CREATE TABLE applications (
    id VARCHAR(50) PRIMARY KEY,
    company VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL,
    date_applied DATE NOT NULL,
    status VARCHAR(30) NOT NULL,
    job_link VARCHAR(500),
    job_description TEXT,
    required_skills VARCHAR(1000),   -- semicolon-joined, same convention as your CSV export
    notes TEXT,
    resume_id VARCHAR(50) NOT NULL,
    created_at DATETIME NOT NULL,
    FOREIGN KEY (resume_id) REFERENCES resumes(id)
);

-- ================================
-- projects  (STAR fields inline — a project has at most one STAR form)
-- ================================
CREATE TABLE projects (
    id VARCHAR(50) PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    domain VARCHAR(255) NOT NULL,
    tech_stack VARCHAR(500) NOT NULL,   -- comma-joined
    repo_link VARCHAR(500) NOT NULL,
    status VARCHAR(30) NOT NULL,
    star_situation TEXT,
    star_task TEXT,
    star_action TEXT,
    star_result TEXT,
    created_at DATETIME NOT NULL
);

-- ================================
-- problems (DSA)
-- ================================
CREATE TABLE problems (
    id VARCHAR(50) PRIMARY KEY,
    platform VARCHAR(255) NOT NULL,
    dsa_tag VARCHAR(255) NOT NULL,
    difficulty VARCHAR(20) NOT NULL,
    confidence_level INT NOT NULL,
    solved_date DATE NOT NULL,
    created_at DATETIME NOT NULL
);

-- ================================
-- study_sessions
-- ================================
CREATE TABLE study_sessions (
    id VARCHAR(50) PRIMARY KEY,
    session_date DATE NOT NULL,
    duration_minutes INT NOT NULL,
    topic VARCHAR(255) NOT NULL,
    created_at DATETIME NOT NULL
);

-- ================================
-- achievements  (single-table inheritance: type discriminator + nullable
-- per-type columns, since sealed interfaces have no direct SQL equivalent)
-- ================================
CREATE TABLE achievements (
    id VARCHAR(50) PRIMARY KEY,
    achievement_type VARCHAR(20) NOT NULL,   -- 'HACKATHON' | 'CERTIFICATION' | 'AWARD'
    name VARCHAR(255) NOT NULL,              -- Hackathon.name / Certification.name / CompetitionAward.competitionName
    organizer VARCHAR(255),                  -- Hackathon only
    result VARCHAR(255),                     -- Hackathon only
    issuing_org VARCHAR(255),                -- Certification only
    rank_label VARCHAR(50),                  -- CompetitionAward only ("rank" is a reserved SQL word, hence rank_label)
    achievement_date DATE NOT NULL,
    created_at DATETIME NOT NULL
);

-- ================================
-- goals  (target_type mirrors the same discriminator idea as achievements)
-- ================================
CREATE TABLE goals (
    id VARCHAR(50) PRIMARY KEY,
    description VARCHAR(500) NOT NULL,
    target_count INT NOT NULL,
    current_count INT NOT NULL DEFAULT 0,
    deadline DATE NOT NULL
);

-- ================================
-- focus_sessions
-- ================================
CREATE TABLE focus_sessions (
    id VARCHAR(50) PRIMARY KEY,
    topic VARCHAR(255) NOT NULL,
    duration_minutes INT NOT NULL,
    created_at DATETIME NOT NULL
);