package org.local.meeting.Repositories;

import org.local.meeting.Models.UserA;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserA,Long> {
    UserA findByUsername(String username);
}
