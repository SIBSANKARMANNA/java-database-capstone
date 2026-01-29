# Smart Clinic Database Schema Design

## MySQL Database Design
Structured and relational data such as users, appointments, and administrative records are stored in MySQL. 
These entities require strong consistency, validation, and clear relationships, making a relational database 
the best choice.

### Table: admins
- id: BIGINT, Primary Key, Auto Increment
- username: VARCHAR(50), Not Null, Unique
- password: VARCHAR(255), Not Null

**Notes:**
- Admin credentials are stored securely.
- Passwords will be hashed at the application level.

### Table: patients
- id: BIGINT, Primary Key, Auto Increment
- name: VARCHAR(100), Not Null
- email: VARCHAR(100), Not Null, Unique
- password: VARCHAR(255), Not Null
- phone: VARCHAR(10), Not Null
- address: VARCHAR(255), Not Null

**Notes:**
- Email must be unique per patient.
- Patient history is retained even if appointments are deleted.

### Table: doctors
- id: BIGINT, Primary Key, Auto Increment
- name: VARCHAR(100), Not Null
- specialty: VARCHAR(50), Not Null
- email: VARCHAR(100), Not Null, Unique
- password: VARCHAR(255), Not Null
- phone: VARCHAR(10), Not Null

**Notes:**
- Doctors authenticate like users.
- Availability slots are managed separately at the application level.


### Table: appointments
- id: BIGINT, Primary Key, Auto Increment
- doctor_id: BIGINT, Foreign Key → doctors(id)
- patient_id: BIGINT, Foreign Key → patients(id)
- appointment_time: DATETIME, Not Null
- status: INT, Not Null  
  (0 = Scheduled, 1 = Completed, 2 = Cancelled)

**Notes:**
- A doctor cannot have overlapping appointments.
- Appointments are retained for history and reporting.
- Deleting a patient or doctor should not automatically delete appointments.

### Table: clinic_locations
- id: BIGINT, Primary Key, Auto Increment
- name: VARCHAR(100), Not Null
- address: VARCHAR(255), Not Null

**Notes:**
- Supports multi-branch clinics in future.


## MongoDB Collection Design

MongoDB is used to store flexible and semi-structured data that may evolve over time.
Prescription records include nested information such as doctor notes and medication details,
which makes a document-based database a better fit than relational tables.

### Collection: prescriptions
{
  "_id": "ObjectId('64abc123456')",
  "patientName": "John Smith",
  "appointmentId": 51,
  "medication": "Paracetamol",
  "dosage": "500mg",
  "doctorNotes": "Take 1 tablet every 6 hours.",
  "issuedAt": "2026-01-15T10:30:00",
  "tags": ["fever", "pain"],
  "pharmacy": {
    "name": "City Health Pharmacy",
    "location": "Downtown"
  }
}

**Design Notes:**
- Only the appointmentId is stored instead of embedding full patient or doctor objects.
- This avoids data duplication and keeps MongoDB documents lightweight.
- Nested objects like pharmacy and tags allow schema flexibility.
- New fields (e.g., refillCount, attachments) can be added without schema migration.

**Schema Evolution Considerations:**
- MongoDB allows adding new fields without impacting existing documents.
- Prescription structure may evolve to include file attachments or chat logs.
- Optional fields ensure backward compatibility.

