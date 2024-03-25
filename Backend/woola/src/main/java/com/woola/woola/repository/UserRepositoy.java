public interface UserRepositoy {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
}
