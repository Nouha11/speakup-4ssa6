package tn.esprit.spring.contentservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
        "eureka.client.enabled=false",
        "spring.cloud.config.enabled=false"
})
class ContentServiceApplicationTests {

    @Test
    void contextLoads() {
    }
}
