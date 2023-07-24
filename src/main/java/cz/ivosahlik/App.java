package cz.ivosahlik;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

@SpringBootApplication
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Bean
    ProtobufHttpMessageConverter protobufHttpMessageConverter() {
        return new ProtobufHttpMessageConverter();
    }

    private CustomerProtos.Customer customer(int id, String firstname, String lastname, Collection<String> emails) {

        Collection<CustomerProtos.Customer.EmailAddress> emailAddresses =
                emails.stream().map(e -> CustomerProtos.Customer.EmailAddress.newBuilder()
                                .setType(CustomerProtos.Customer.EmailType.PROFESSIONAL)
                                .setEmail(e).build())
                        .collect(Collectors.toList());

        return CustomerProtos.Customer.newBuilder()
                .setFirstName(firstname)
                .setLastName(lastname)
                .setId(id)
                .addAllEmail(emailAddresses)
                .build();
    }

    @Bean
    CustomerRepository customerRepository() {

        Map<Integer, CustomerProtos.Customer> customers = new ConcurrentHashMap<>();
        // populate with some dummy data
        asList(
                customer(1, "Java 8", "Temurin", asList("java8@email.com")),
                customer(2, "Java 11", "Temurin", asList("java11@email.com")),
                customer(3, "Java 15", "Temurin", asList("java15@email.com")),
                customer(4, "Java 17", "Temurin", asList("java17@email.com"))
        ).forEach(c -> customers.put(c.getId(), c));

        return new CustomerRepository() {

            @Override
            public CustomerProtos.Customer findById(int id) {
                return customers.get(id);
            }

            @Override
            public CustomerProtos.CustomerList findAll() {
                return CustomerProtos.CustomerList.newBuilder() //
                        .addAllCustomer(customers.values()) //
                        .build();
            }
        };
    }
}

