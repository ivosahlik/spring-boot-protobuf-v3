package cz.ivosahlik;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AppTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void contextLoaded() {

        ResponseEntity<CustomerProtos.Customer> customerResponse = restTemplate.getForEntity(
                "/customers/2", CustomerProtos.Customer.class);

        System.out.println("customer retrieved: " + customerResponse.toString());
        assertThat(Objects.requireNonNull(customerResponse.getBody()).getFirstName()).isEqualTo("Java 11");
    }
}
