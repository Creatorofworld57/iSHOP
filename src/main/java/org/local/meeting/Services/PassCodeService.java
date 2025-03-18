package org.local.meeting.Services;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PassCodeService {

    private final RedisTemplate<String, Object> redisTemplate;
    private Duration duration_of_expiration_pass_code = Duration.ofMinutes(5);

    public String create_code(String email) {
        SecureRandom random = new SecureRandom();
        int random_pass = random.nextInt(1000000, 9999999);
        //сохраняем код после его создания
        save_code(String.valueOf(random_pass), email);
        return Integer.toString(random_pass);
    }

    public void save_code(String code, String email) {
        Map<String, Object> passcode = new HashMap<>();
        passcode.put("code", code);
        passcode.put("time", new Date(System.currentTimeMillis() + duration_of_expiration_pass_code.toMillis()));
        redisTemplate.opsForHash().putAll("pass_code " + email, passcode);
        redisTemplate.expire("pass_code " + email, duration_of_expiration_pass_code);
    }

    public boolean verify_code(String code, String email) {
        String true_pass_code = (String) redisTemplate.opsForHash().get("pass_code " + email, "code");
        //могут быть проблемы с date
        Date expired_date = (Date) redisTemplate.opsForHash().get("pass_code " + email, "time");
    System.out.println("был вытащен code" + true_pass_code +"  "+ expired_date);
        if (true_pass_code != null && true_pass_code.equals(code)) {
            System.out.println("first_check");
            if (expired_date != null && expired_date.after(new Date(System.currentTimeMillis()))) {
                delete_code(email);
                return true;
            }

        }
        return false;
    }

    public void delete_code(String email) {
        redisTemplate.delete("pass_code " + email);
    }


}
