// package com.project.back_end.mongo.models;


// import jakarta.persistence.Entity;
// import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.GenerationType;
// import jakarta.persistence.Id;
// import jakarta.validation.constraints.NotNull;
// import jakarta.validation.constraints.Size;

// @Entity
// @Document(collection = "prescriptions")
// public class Prescription {

//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long id;

//     @NotNull(message = "Patient name is required")
//     @Size(min = 3, max = 100)
//     private String patientName;

//     @NotNull(message = "Appointment ID is required")
//     private Long appointmentId;

//     @NotNull(message = "Medication name is required")
//     @Size(min = 3, max = 100)
//     private String medication;

//     @NotNull(message = "Dosage is required")
//     @Size(min = 3, max = 20)
//     private String dosage;

//     @Size(max = 200)
//     private String doctorNotes;

//     /* -------- Getters & Setters -------- */

//     public Long getId() {
//         return id;
//     }

//     public void setId(Long id) {
//         this.id = id;
//     }

//     public String getPatientName() {
//         return patientName;
//     }

//     public void setPatientName(String patientName) {
//         this.patientName = patientName;
//     }

//     public Long getAppointmentId() {
//         return appointmentId;
//     }

//     public void setAppointmentId(Long appointmentId) {
//         this.appointmentId = appointmentId;
//     }

//     public String getMedication() {
//         return medication;
//     }

//     public void setMedication(String medication) {
//         this.medication = medication;
//     }

//     public String getDosage() {
//         return dosage;
//     }

//     public void setDosage(String dosage) {
//         this.dosage = dosage;
//     }

//     public String getDoctorNotes() {
//         return doctorNotes;
//     }

//     public void setDoctorNotes(String doctorNotes) {
//         this.doctorNotes = doctorNotes;
//     }
// }



package com.project.back_end.mongo.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Document(collection = "prescriptions")
public class Prescription {

    @Id
    private String id;   // Mongo IDs are String / ObjectId

    @NotNull(message = "Patient name is required")
    @Size(min = 3, max = 100)
    private String patientName;

    @NotNull(message = "Appointment ID is required")
    private Long appointmentId;

    @NotNull(message = "Medication name is required")
    @Size(min = 3, max = 100)
    private String medication;

    @NotNull(message = "Dosage is required")
    @Size(min = 3, max = 20)
    private String dosage;

    @Size(max = 200)
    private String doctorNotes;

    // Getters & Setters
}
