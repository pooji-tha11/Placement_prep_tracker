# 🎓 Placement Preparation Tracker (PrepSphere)

A comprehensive, full-stack web application designed to help students and job seekers track their placement journey. PrepSphere brings all your preparation metrics—from DSA problem-solving and project building to job applications and focus timers—under one unified dashboard.

## ✨ Features

*   **📊 Dashboard:** Get a bird's-eye view of your readiness score, total applications, and recent activities.
*   **💻 DSA Tracker:** Log and track data structures and algorithms problems by difficulty and topic.
*   **🚀 Projects & STAR Method:** Manage your portfolio projects and automatically structure your talking points using the built-in STAR (Situation, Task, Action, Result) interview wizard.
*   **💼 Job Applications:** Keep tabs on companies, roles, and interview statuses (Applied, Assessment, Interview, Selected, Rejected).
*   **⏱️ Focus Timer:** Built-in Pomodoro-style timer to track deep work and study sessions.
*   **🎯 Goals & Achievements:** Set short/long term goals and log certifications, hackathons, and awards.
*   **📄 Resume Management:** Track different versions of your resume tailored for specific roles.

## 🛠️ Technology Stack

*   **Backend:** Java, Spring Boot 3, Maven
*   **Database:** MySQL (JDBC implementation)
*   **Frontend:** React 19, Vite, Tailwind CSS
*   **Icons:** React Icons (Feather Icons)

---

## 🚀 Getting Started

Follow these instructions to set up the project locally on your machine.

### Prerequisites

Ensure you have the following installed on your system:
*   [Java Development Kit (JDK) 17+](https://www.oracle.com/java/technologies/downloads/) (Project uses Java 26)
*   [Maven](https://maven.apache.org/download.cgi)
*   [Node.js (v18+) and npm](https://nodejs.org/)
*   [MySQL Server](https://dev.mysql.com/downloads/mysql/)

### 1. Database Setup

1. Start your MySQL Server.
2. Open your MySQL CLI or a GUI tool (like MySQL Workbench) and create the database:
   ```sql
   CREATE DATABASE placement_tracker;
   ```
3. Open the `src/main/java/com/placementtracker/database/DatabaseConfig.java` file in the project.
4. Update the `PASSWORD` variable with your local MySQL root password:
   ```java
   public static final String PASSWORD = "YOUR_MYSQL_PASSWORD";
   ```
*(Note: If you have a database initialization script / schema.sql, run it now to create the required tables).*

### 2. Backend Setup (Spring Boot)

1. Open a terminal and navigate to the root directory of the project.
2. Run the Spring Boot application using Maven:
   ```bash
   mvn spring-boot:run
   ```
3. The backend API server will start running on `http://localhost:8080`.

### 3. Frontend Setup (React + Vite)

1. Open a **new** terminal window.
2. Navigate to the frontend directory:
   ```bash
   cd prepsphere-frontend
   ```
3. Install the required Node dependencies:
   ```bash
   npm install
   ```
4. Start the Vite development server:
   ```bash
   npm run dev
   ```
5. The frontend will start running on `http://localhost:5173`.

---

## 💻 Usage

Once both servers are running:
1. Open your web browser.
2. Navigate to [http://localhost:5173](http://localhost:5173).
3. Start tracking your placement preparation!

## 📁 Project Structure

```text
PlacementPreparationTracker/
├── src/main/java/com/placementtracker/   # Spring Boot Backend Code
│   ├── achievement/                      # Hackathons, certifications, and awards
│   ├── app/                              # Application entry point & controllers
│   ├── application/                      # Job applications tracking
│   ├── common/                           # Shared utilities, base models, exceptions
│   ├── database/                         # JDBC configuration & connections
│   ├── dsa/                              # Data Structures & Algorithms progress
│   ├── goal/                             # Short and long-term placement goals
│   ├── project/                          # Projects and STAR method integration
│   ├── reports/                          # Dashboard statistics and analytics
│   ├── resume/                           # Resume version management
│   ├── study/                            # Study sessions tracking
│   └── timer/                            # Focus Pomodoro timer logic
├── prepsphere-frontend/                  # React Frontend Code
│   ├── src/
│   │   ├── api/                          # Axios configuration
│   │   ├── components/                   # Reusable UI components (Sidebar, Layout)
│   │   └── pages/                        # Main page views (Dashboard, DSA, Projects)
│   ├── tailwind.config.js                # Tailwind theme customization
│   └── package.json                      # Frontend dependencies
├── pom.xml                               # Maven backend dependencies
└── README.md                             # Project documentation
