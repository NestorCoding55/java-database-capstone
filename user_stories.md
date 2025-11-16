# User Story Template

**Title:**
_As a [user role], I want [feature/goal], so that [reason]._

**Acceptance Criteria:**
1. [Criteria 1]
2. [Criteria 2]
3. [Criteria 3]

**Priority:** [High/Medium/Low]
**Story Points:** [Estimated Effort in Points]
**Notes:**
- [Additional information or edge cases]

## Admin User Stories
User Story 1 – Admin Login

Title:
As an admin, I want to log into the portal with my username and password, so that I can securely manage the platform.

Acceptance Criteria:

Admin must enter a valid username and password.

System validates credentials against the database.

Admin is redirected to the Admin Dashboard upon successful login.

Priority: High
Story Points: 3
Notes:

Display an error message for invalid credentials.

User Story 2 – Admin Logout

Title:
As an admin, I want to log out of the portal, so that I can protect system access when I'm not using it.

Acceptance Criteria:

Admin can click a logout button from any dashboard view.

Session is invalidated immediately.

Admin is redirected to the login page.

Priority: High
Story Points: 2
Notes:

Ensure no dashboard pages are accessible after logout.

User Story 3 – Add Doctor Profile

Title:
As an admin, I want to add doctors to the portal, so that they can access the system and manage their appointments.

Acceptance Criteria:

Admin can fill out a form to add doctor details (name, specialty, email, etc.).

System validates all required fields before submission.

Doctor record is saved to the MySQL database and appears in the doctor list.

Priority: High
Story Points: 5
Notes:

Optional: add default password generation logic.

User Story 4 – Delete Doctor Profile

Title:
As an admin, I want to delete a doctor's profile from the portal, so that inactive or incorrect accounts are removed.

Acceptance Criteria:

Admin can select a doctor from the list and click "Delete."

System shows a confirmation prompt.

Doctor record is removed from the MySQL database.

Priority: Medium
Story Points: 3
Notes:

Consider implementing soft-delete if needed for audit trails.

User Story 5 – Run Stored Procedure for Appointment Statistics

Title:
As an admin, I want to run a stored procedure in the MySQL CLI to get the number of appointments per month, so that I can track usage statistics.

Acceptance Criteria:

Admin can execute the stored procedure using the CLI.

Stored procedure returns appointment count grouped by month.

Output is readable and can be exported or recorded.

Priority: Medium
Story Points: 4
Notes:

Stored procedure example: CALL GetMonthlyAppointmentCount();

## Patient User Stories
User Story 1 – View Doctor List (No Login Required)

Title:
As a patient, I want to view a list of doctors without logging in, so that I can explore my options before registering.

Acceptance Criteria:

Patient can access the doctor list from the home page without authentication.

System displays doctor names, specialties, and availability.

Navigation to the sign-up page is available if the patient wants to book an appointment.

Priority: High
Story Points: 3
Notes:

No sensitive doctor data (email, phone) should be shown unless logged in.

User Story 2 – Patient Registration

Title:
As a patient, I want to sign up using my email and password, so that I can book appointments.

Acceptance Criteria:

Patient fills out a registration form (name, email, password).

System validates the data and checks that the email is unique.

Account is created and the patient is redirected to the login page.

Priority: High
Story Points: 5
Notes:

Password encryption must be applied.

Email format must be validated.

User Story 3 – Patient Login

Title:
As a patient, I want to log into the portal, so that I can manage my bookings.

Acceptance Criteria:

Patient enters valid credentials.

System authenticates and creates a secure session.

Patient is redirected to the Patient Dashboard.

Priority: High
Story Points: 3
Notes:

Show error message for invalid login attempts.

User Story 4 – Patient Logout

Title:
As a patient, I want to log out of the portal, so that I can secure my account.

Acceptance Criteria:

Patient can click a logout button from any page.

System terminates the session.

Patient is redirected to the login page.

Priority: Medium
Story Points: 2
Notes:

After logout, patient must not be able to access dashboard via back button.

User Story 5 – Book Appointment

Title:
As a patient, I want to log in and book an hour-long appointment, so that I can consult with a doctor.

Acceptance Criteria:

Patient selects a doctor and an available time slot.

System checks for scheduling conflicts.

Appointment is created in the database and confirmation is shown.

Priority: High
Story Points: 5
Notes:

Appointment duration defaults to 1 hour.

Prevent double-booking for both patients and doctors.

User Story 6 – View Upcoming Appointments

Title:
As a patient, I want to view my upcoming appointments, so that I can prepare accordingly.

Acceptance Criteria:

Patient can access an “Upcoming Appointments” page.

System displays doctor name, date, time, and appointment status.

Only appointments belonging to the logged-in patient appear.

Priority: Medium
Story Points: 3
Notes:

Optionally allow sorting by soonest date.

## Doctor User Stories
User Story 1 – Doctor Login

Title:
As a doctor, I want to log into the portal, so that I can manage my appointments.

Acceptance Criteria:

The system must validate my username and password.

I should be redirected to my doctor dashboard after successful login.

If credentials are incorrect, I should receive an error message.

Priority: High
Story Points: 3
Notes:

Standard authentication rules apply.

User Story 2 – Doctor Logout

Title:
As a doctor, I want to log out of the portal, so that my data remains protected.

Acceptance Criteria:

Logout should terminate the current session.

I should be redirected to the home page or login page after logout.

No protected routes should be accessible without logging in again.

Priority: High
Story Points: 2
Notes:

Session timeout rules may apply.

User Story 3 – View Appointment Calendar

Title:
As a doctor, I want to view my appointment calendar, so that I can stay organized.

Acceptance Criteria:

The calendar must show all confirmed appointments.

Each appointment should display date, time, and patient name.

The calendar must load for the correct doctor.

Priority: High
Story Points: 5
Notes:

Calendar may be weekly or monthly.

User Story 4 – Mark Unavailability

Title:
As a doctor, I want to mark my unavailability, so that patients can only book available time slots.

Acceptance Criteria:

I must be able to select dates or time ranges where I am unavailable.

The system must block patients from booking during unavailable times.

The system must update availability in real time.

Priority: Medium
Story Points: 5
Notes:

Should prevent overlapping with existing appointments.

User Story 5 – Update Profile

Title:
As a doctor, I want to update my profile with specialization and contact information, so that patients see accurate details.

Acceptance Criteria:

I must be able to edit my specialization, experience, contact info, and bio.

Changes must be saved to the database.

Updated details must appear on my public doctor profile.

Priority: Medium
Story Points: 3
Notes:

Form validation required.

User Story 6 – View Patient Details for Appointments

Title:
As a doctor, I want to view the patient details for my upcoming appointments, so that I can prepare in advance.

Acceptance Criteria:

Patient details page must show name, age, medical notes (if available), and appointment reason.

The details must be accessible only for my own appointments.

The system must load correct patient info securely.

Priority: High
Story Points: 4
Notes:

Ensure proper authorization or role-based access.
