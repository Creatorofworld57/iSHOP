package org.local.meeting.Repositories;

import org.local.meeting.Models.Dao.UserA;
import org.local.meeting.Models.Dto.AuthRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<UserA,Long> {
    UserA findByLogin(String username);

    @Query("SELECT NEW org.local.meeting.Models.Dto.AuthRequest(u.login, u.password) FROM UserA u WHERE u.email = :email")
    AuthRequest findByEmail(String email);
}
