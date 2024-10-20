package app.hotel.apphotel.repository;

import app.hotel.apphotel.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.relational.core.sql.In;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
    Boolean existsByEmail(String email);

    User findByUsernameAndPassword(String username,String password);

    User findByEmail(String email);
}
