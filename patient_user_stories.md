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
