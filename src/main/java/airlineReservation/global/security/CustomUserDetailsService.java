package airlineReservation.global.security;

import airlineReservation.infra.entity.User;
import airlineReservation.infra.entity.UserExample;
import airlineReservation.infra.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String email) {
        UserExample example = new UserExample();
        example.createCriteria()
                .andEmailEqualTo(email)
                .andIsDeletedEqualTo(false);

        List<User> users = userMapper.selectByExample(example);
        if (users.isEmpty()) {
            throw new UsernameNotFoundException("존재하지 않는 회원입니다.");
        }

        return new CustomUserDetails(users.get(0));
    }
}
