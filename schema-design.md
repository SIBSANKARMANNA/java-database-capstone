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
