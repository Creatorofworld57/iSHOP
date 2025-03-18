package org.local.meeting.Utils.JwtUtils;





import lombok.RequiredArgsConstructor;

import org.local.meeting.Models.Dao.UserA;
import org.local.meeting.Models.Dto.UserPrincipal;
import org.local.meeting.Repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {


    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserA> user = userRepository.findByLogin(username);
        if (user.isEmpty()) {
            System.out.println("User Not Found");
            throw new UsernameNotFoundException("user not found");
        }

        return new UserPrincipal(user.get());
    }
}
