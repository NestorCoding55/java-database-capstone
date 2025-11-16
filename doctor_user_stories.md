Exercise 4 – Doctor User Stories
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
