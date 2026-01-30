public interface AdminRepository extends JpaRepository<Admin, Long> {
    Admin findByUsername(String username);
}
