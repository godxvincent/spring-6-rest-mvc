package guru.springframework.spring6restmvc.repositories;

// import guru.springframework.spring7restmvc.entities.Beer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
// import org.springframework.test.context.DynamicPropertyRegistry;
// import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import guru.springframework.spring6restmvc.entities.Beer;

// import javax.sql.DataSource;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by jt, Spring Framework Guru.
 */
@Testcontainers
@SpringBootTest
@ActiveProfiles("localmysql")
public class MySQLIT {

    @Container
    @ServiceConnection
    static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.0.41");

    // The easier way of doing this is with @ServiceConnection, but in case there's anything not supported we can still use DynamicPropertySource
    // @DynamicPropertySource
    // static void mySqlProperties(DynamicPropertyRegistry registry) {
    //     registry.add("spring.datasource.username", mySQLContainer::getUsername);
    //     registry.add("spring.datasource.password", mySQLContainer::getPassword);
    //     registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
    // }

    // We brought here the datasource just to inspect the content of that variable.
    // @Autowired
    // DataSource dataSource;

    @Autowired
    BeerRepository beerRepository;

    @Test
    void testListBeers() {
        List<Beer> beers = beerRepository.findAll();

        assertThat(beers.size()).isGreaterThan(0);
    }
}