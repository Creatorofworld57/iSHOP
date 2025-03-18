package org.local.meeting.Services.KafkaServices;

import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProducerOfPassCodes {


    private final KafkaTemplate<String, String> producer;

    public void send ( String email, String passcode) {
        producer.send("top_for_pass_cods", email, passcode);

        System.out.println("Sent message: " + passcode);

    }
}
