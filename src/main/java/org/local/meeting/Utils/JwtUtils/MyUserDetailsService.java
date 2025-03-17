package org.local.meeting.Utils.JwtUtils;





import lombok.RequiredArgsConstructor;

import org.local.meeting.Models.UserA;
import org.local.meeting.Models.UserPrincipal;
import org.local.meeting.Repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;





@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {


    private final UserRepository userRepository;



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserA user = userRepository.findByUsername(username);
        if (user == null) {
            System.out.println("User Not Found");
            throw new UsernameNotFoundException("user not found");
        }

        return new UserPrincipal(user);
    }
}
