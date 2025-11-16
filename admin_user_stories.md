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
