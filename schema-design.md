## MySQL Database Design
MySQL Database Design

Below is a clear, practical schema for the Smart Clinic Management System. It starts with the required core tables (patients, doctors, appointments, admin) and adds several supporting tables (clinic_locations, doctor_availability, prescriptions, payments). Each table includes columns, types, primary/foreign keys, common constraints, and short justification notes. I also include implementation notes about soft deletes, appointment-overlap handling, and where to enforce rules in application vs DB.

Assumptions / global choices

Storage engine: InnoDB (supports FK and transactions).

Timestamps use DATETIME for portability; TIMESTAMP may be used if timezone behavior is desired.

Prefer soft deletes (is_deleted / deleted_at) for audit/history retention rather than hard cascade deletes.

Appointment overlap prevention is primarily enforced in application logic; MySQL cannot easily express non-overlap constraints across rows. Optionally use a doctor_availability / appointment_slot table to more strictly control bookable slots.

Table: patients

id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT

first_name VARCHAR(100) NOT NULL

last_name VARCHAR(100) NOT NULL

email VARCHAR(255) NOT NULL UNIQUE

phone VARCHAR(30) NULL

date_of_birth DATE NULL

gender ENUM('M','F','O') NULL

created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP

updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP

is_deleted TINYINT(1) NOT NULL DEFAULT 0 -- soft-delete flag
Notes: email should be validated in application. Soft-delete preserves appointment history and audits.

Table: doctors

id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT

first_name VARCHAR(100) NOT NULL

last_name VARCHAR(100) NOT NULL

email VARCHAR(255) NOT NULL UNIQUE

phone VARCHAR(30) NULL

specialization VARCHAR(150) NULL

bio TEXT NULL

is_active TINYINT(1) NOT NULL DEFAULT 1

created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP

updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP

is_deleted TINYINT(1) NOT NULL DEFAULT 0
Notes: Doctors can be deactivated rather than deleted to keep historical refs. Unique email constraint to identify accounts.

Table: admin_users

id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT

username VARCHAR(100) NOT NULL UNIQUE

email VARCHAR(255) NOT NULL UNIQUE

password_hash VARCHAR(255) NOT NULL

full_name VARCHAR(200) NULL

role ENUM('SUPER_ADMIN','ADMIN','ANALYST') NOT NULL DEFAULT 'ADMIN'

created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP

last_login_at DATETIME NULL

is_active TINYINT(1) NOT NULL DEFAULT 1
Notes: Store only hashed passwords. Admins manage doctors, reports, and can run stored procedures.

Table: clinic_locations

id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT

name VARCHAR(150) NOT NULL

address VARCHAR(300) NULL

phone VARCHAR(30) NULL

timezone VARCHAR(50) NULL

created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
Notes: Useful if clinic has multiple branches; appointments can reference location.

Table: appointments

id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT

doctor_id INT UNSIGNED NOT NULL, FOREIGN KEY → doctors(id)

patient_id INT UNSIGNED NOT NULL, FOREIGN KEY → patients(id)

location_id INT UNSIGNED NULL, FOREIGN KEY → clinic_locations(id)

start_time DATETIME NOT NULL

end_time DATETIME NOT NULL

duration_minutes SMALLINT NOT NULL DEFAULT 60 -- redundancy for quick queries

status ENUM('SCHEDULED','COMPLETED','CANCELLED','NO_SHOW') NOT NULL DEFAULT 'SCHEDULED'

created_by_admin_id INT UNSIGNED NULL, FOREIGN KEY → admin_users(id) -- optional

notes TEXT NULL

created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP

updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP

is_deleted TINYINT(1) NOT NULL DEFAULT 0
Indexes: (doctor_id, start_time), (patient_id, start_time)
Notes / Constraints:

Application should enforce end_time > start_time.

Preventing overlapping appointments for the same doctor must be done in application logic OR by booking only from predefined doctor_availability slots. MySQL cannot declaratively prevent arbitrary interval overlaps without complex procedures/triggers.

Table: doctor_availability

id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT

doctor_id INT UNSIGNED NOT NULL, FK → doctors(id)

available_from DATETIME NOT NULL

available_to DATETIME NOT NULL

recurrence VARCHAR(100) NULL -- e.g., RRULE or simple text for recurring rules

is_block TINYINT(1) NOT NULL DEFAULT 0 -- if true, this is an unavailability block (vacation)

created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
Notes: Use this table to present bookable slots. When creating appointments, check these records to ensure booking only in available ranges. When doctor marks unavailability, insert a block record.

Table: prescriptions

id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT

appointment_id INT UNSIGNED NULL, FK → appointments(id) -- tie to appointment if applicable

doctor_id INT UNSIGNED NOT NULL, FK → doctors(id)

patient_id INT UNSIGNED NOT NULL, FK → patients(id)

content TEXT NOT NULL -- medication, dosage, instructions

created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP

valid_until DATE NULL
Notes: Tying prescriptions to appointments is common (so you know context), but allow appointment_id NULL if created outside an appointment.

Table: payments

id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT

appointment_id INT UNSIGNED NOT NULL, FK → appointments(id)

patient_id INT UNSIGNED NOT NULL, FK → patients(id)

amount_cents INT UNSIGNED NOT NULL -- store as integer cents to avoid floating issues

currency CHAR(3) NOT NULL DEFAULT 'USD'

method ENUM('CREDIT_CARD','CASH','INSURANCE','OTHER') NOT NULL

status ENUM('PENDING','PAID','FAILED','REFUNDED') NOT NULL DEFAULT 'PENDING'

created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
Notes: One-to-one or one-to-many depending on refunds or partial payments. Payment info (card tokens) should not be stored unless PCI compliant.

Table: appointment_notes (optional, for audit/history)

id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT

appointment_id INT UNSIGNED NOT NULL, FK → appointments(id)

author_type ENUM('DOCTOR','PATIENT','ADMIN') NOT NULL

author_id INT UNSIGNED NULL -- could reference doctors/patients/admins depending on author_type

note TEXT NOT NULL

created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP

Referential actions / cascade policy recommendations

Soft delete recommended: Use is_deleted and deleted_at for patients and doctors. This preserves appointments, prescriptions, payments for auditing and reporting.

Foreign keys: Typically ON UPDATE CASCADE. For ON DELETE, prefer RESTRICT or NO ACTION and implement logical delete in app. If hard delete is required for GDPR reasons, then ON DELETE SET NULL for some FKs (e.g., appointments.patient_id → SET NULL) or ON DELETE CASCADE if you intentionally want to remove appointments. Choose carefully.

Prescription retention: Keep prescriptions even if appointment is soft-deleted (so tie via appointment_id but do not cascade delete).

Enforcing no overlapping appointments (practical approaches)

Application-level checks: Before creating an appointment, check for existing appointments for the doctor where intervals overlap:
SELECT 1 FROM appointments WHERE doctor_id = ? AND is_deleted = 0 AND NOT (end_time <= :start_time OR start_time >= :end_time)
Run within a transaction and lock relevant rows to avoid race conditions.

Slot-based booking: Create doctor_availability and generate explicit appointment_slot records. Appointments can only be created by reserving a slot (enforced with a unique constraint on slot → appointment). This makes DB-level uniqueness possible.

DB-level locking: Use SELECT ... FOR UPDATE on doctor rows or a dedicated lock row to serialize booking operations.

Additional considerations / choices

Indexes: Add indexes on frequently queried columns: (doctor_id, start_time), (patient_id, start_time), (status), (created_at) for reports.

Audit & logging: Consider audit_logs table to track changes to sensitive records (who changed what and when).

Data validation: Validate emails and phone formats in application layer; add CHECK constraints where appropriate (MySQL 8+ supports CHECK).

Timezones: Store times in UTC (DATETIME or TIMESTAMP with conversion) and display in user's timezone. Save doctor's timezone in clinic_locations or doctors if needed.

Retention & GDPR: Implement policies (archival) for removing PII when required.

Example CREATE TABLE snippet (appointments)
CREATE TABLE appointments (
  id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  doctor_id INT UNSIGNED NOT NULL,
  patient_id INT UNSIGNED NOT NULL,
  location_id INT UNSIGNED NULL,
  start_time DATETIME NOT NULL,
  end_time DATETIME NOT NULL,
  duration_minutes SMALLINT NOT NULL DEFAULT 60,
  status ENUM('SCHEDULED','COMPLETED','CANCELLED','NO_SHOW') NOT NULL DEFAULT 'SCHEDULED',
  created_by_admin_id INT UNSIGNED NULL,
  notes TEXT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  is_deleted TINYINT(1) NOT NULL DEFAULT 0,
  INDEX idx_doctor_start (doctor_id, start_time),
  INDEX idx_patient_start (patient_id, start_time),
  CONSTRAINT fk_appointments_doctor FOREIGN KEY (doctor_id) REFERENCES doctors(id),
  CONSTRAINT fk_appointments_patient FOREIGN KEY (patient_id) REFERENCES patients(id)
) ENGINE=InnoDB;

## MongoDB Collection Design
Below is a suggested MongoDB collection that complements the MySQL schema: prescriptions in MongoDB holds flexible prescription metadata, free-form doctor notes, attachments, and audit/history entries. The document design uses references to relational IDs (to keep canonical data in MySQL) but allows embedded arrays and nested structures where it makes sense.

Collection: prescriptions
{
  "_id": { "$oid": "654f1a2b3c4d5e6f7a8b9c0d" },
  "patientId": 123,                      // reference to patients.id in MySQL
  "doctorId": 45,                        // reference to doctors.id in MySQL
  "appointmentId": 987,                  // optional link to appointments.id
  "issuedAt": "2025-11-10T14:30:00Z",
  "validUntil": "2026-05-10",
  "medications": [
    {
      "name": "Amoxicillin",
      "strength": "500 mg",
      "form": "capsule",
      "dose": "1 capsule",
      "frequency": "3 times a day",
      "durationDays": 7,
      "notes": "Take with food",
      "tags": ["antibiotic", "first-line"]
    },
    {
      "name": "Paracetamol",
      "strength": "500 mg",
      "form": "tablet",
      "dose": "1-2 tablets",
      "frequency": "every 6-8 hours as needed",
      "durationDays": 5,
      "notes": "Max 4g/day",
      "tags": ["analgesic"]
    }
  ],
  "doctorNotes": {
    "freeText": "Patient shows signs of sinusitis. Avoid NSAIDs due to GI history.",
    "structuredSymptoms": ["nasal congestion", "headache", "facial pain"],
    "impression": "Acute bacterial sinusitis suspected"
  },
  "attachments": [
    {
      "type": "pdf",
      "fileId": "presc_20251110_1234.pdf",   // stored in GridFS or external blob store
      "description": "Detailed instructions and patient leaflet",
      "uploadedAt": "2025-11-10T14:31:00Z"
    }
  ],
  "pharmacy": {
    "preferred": {
      "name": "Central Pharmacy",
      "address": "123 Clinic St.",
      "phone": "+56-9-1234-5678"
    },
    "alternatives": []
  },
  "audit": [
    { "action": "created", "by": "doctor:45", "at": "2025-11-10T14:30:00Z" },
    { "action": "updated", "by": "doctor:45", "at": "2025-11-10T14:40:00Z", "note": "dose adjustment" }
  ],
  "flags": {
    "private": false,
    "isElectronic": true,
    "requiresPriorAuth": false
  },
  "meta": {
    "source": "web-portal",
    "schemaVersion": 1
  }
}

Design rationale & considerations

Reference vs embed: keep patientId, doctorId, and appointmentId as references to the authoritative MySQL records (avoids PII duplication). Embed medication items and free-form doctor notes because they are tightly coupled with the prescription and benefit from being stored together for fast reads.

Attachments: store large binary attachments (scans, PDFs) in GridFS (MongoDB) or an external object store (S3) and keep only metadata (fileId, type, description) in the document.

Audit trail: use an embedded audit array to track changes; this is inexpensive and convenient for per-prescription history.

Tags & metadata: tags, flags, and meta.schemaVersion make the schema flexible and easier to evolve (versioning helps migrate or interpret old documents).

Schema evolution: MongoDB’s schemaless nature lets you add fields (e.g., insuranceCode) without breaking older documents. To catch bad data, consider using MongoDB schema validation rules that are permissive but enforce minimal constraints (types for key fields).

Indexing: create indexes on commonly queried fields: { patientId: 1 }, { doctorId: 1 }, { appointmentId: 1 }, and possibly { "medications.name": 1 } or { "issuedAt": -1 } for recent prescriptions.

TTL & retention: if any logs or ephemeral notes are added that should auto-expire (not prescriptions themselves), use TTL indexes on an expiry field.

Security & privacy: keep PII minimal; respect encryption and access controls. Use role-based controls to allow only authorized roles (doctor, patient, admin) to read/modify documents.

Queries & performance: embedding medications makes fetching a full prescription cheap (one read). If prescriptions grow very large, consider splitting large attachments or notes into a separate collection (e.g., prescription_notes) to keep the main doc small.

Offline / sync scenarios: store meta.source and meta.syncStatus if offline mobile clients will later sync prescription edits.
