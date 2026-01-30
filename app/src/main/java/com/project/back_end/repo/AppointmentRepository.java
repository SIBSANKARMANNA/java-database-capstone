public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query("""
        SELECT a FROM Appointment a
        LEFT JOIN FETCH a.doctor
        LEFT JOIN FETCH a.patient
        WHERE a.doctor.id = :doctorId
        AND a.appointmentTime BETWEEN :start AND :end
    """)
    List<Appointment> findByDoctorIdAndAppointmentTimeBetween(
            Long doctorId, LocalDateTime start, LocalDateTime end);

    @Query("""
        SELECT a FROM Appointment a
        LEFT JOIN FETCH a.doctor
        LEFT JOIN FETCH a.patient
        WHERE a.doctor.id = :doctorId
        AND LOWER(a.patient.name) LIKE LOWER(CONCAT('%', :patientName, '%'))
        AND a.appointmentTime BETWEEN :start AND :end
    """)
    List<Appointment> findByDoctorIdAndPatient_NameContainingIgnoreCaseAndAppointmentTimeBetween(
            Long doctorId, String patientName, LocalDateTime start, LocalDateTime end);

    @Modifying
    @Transactional
    void deleteAllByDoctorId(Long doctorId);

    List<Appointment> findByPatientId(Long patientId);

    List<Appointment> findByPatient_IdAndStatusOrderByAppointmentTimeAsc(Long patientId, int status);

    @Query("""
        SELECT a FROM Appointment a
        WHERE LOWER(a.doctor.name) LIKE LOWER(CONCAT('%', :doctorName, '%'))
        AND a.patient.id = :patientId
    """)
    List<Appointment> filterByDoctorNameAndPatientId(String doctorName, Long patientId);

    @Query("""
        SELECT a FROM Appointment a
        WHERE LOWER(a.doctor.name) LIKE LOWER(CONCAT('%', :doctorName, '%'))
        AND a.patient.id = :patientId
        AND a.status = :status
    """)
    List<Appointment> filterByDoctorNameAndPatientIdAndStatus(
            String doctorName, Long patientId, int status);
}
