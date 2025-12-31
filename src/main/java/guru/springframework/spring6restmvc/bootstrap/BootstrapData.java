package guru.springframework.spring6restmvc.bootstrap;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.entities.Customer;
import guru.springframework.spring6restmvc.model.BeerStyle;
import guru.springframework.spring6restmvc.repositories.BeerRepository;
import guru.springframework.spring6restmvc.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BootstrapData implements CommandLineRunner {

    private final BeerRepository beerRepository;
    private final CustomerRepository customerRepository;

    // public BootstrapData(BeerRepository beerRepository, CustomerRepository customerRepository) {
    // // public BootstrapData(BeerRepository beerRepository) {
    //     this.beerRepository = beerRepository;
    //     this.customerRepository = customerRepository;
    // }


    @Override
    public void run(String... args) throws Exception {
        
        System.out.println("Loading some data into the database");
        loadBeerData();
        loadCustomerData();
        System.out.println("Stop loading some data into the database");
    }

    public void loadCustomerData() {
        if (customerRepository.count() == 0) {
            loadCustomerObjects();
        }
    }

    public void loadBeerData() {
        if (beerRepository.count() == 0) {
            loadBeerObjects();
        }
    }

    private void loadBeerObjects() {
        Beer beer1 = Beer.builder()
                .beerName("Galaxy Cat")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("12356")
                .price(new BigDecimal(12.50))
                .quantityOnHand(120)
                .createDateTime(LocalDateTime.now())
                .updateDateTime(LocalDateTime.now())
                .build();
        Beer beer2 = Beer.builder()
                .beerName("Modelo")
                .beerStyle(BeerStyle.LAGER)
                .upc("498724")
                .price(new BigDecimal(5.99))
                .quantityOnHand(200)
                .createDateTime(LocalDateTime.now())
                .updateDateTime(LocalDateTime.now())
                .build();
        Beer beer3 = Beer.builder()
                .beerName("Heineken")
                .beerStyle(BeerStyle.STOUT)
                .upc("987123")
                .price(new BigDecimal(8.31))
                .quantityOnHand(500)
                .createDateTime(LocalDateTime.now())
                .updateDateTime(LocalDateTime.now())
                .build();
        beerRepository.saveAll(Arrays.asList(beer1, beer2, beer3));
    }

    public void loadCustomerObjects() {
        Customer customer1 = Customer.builder()
                            .customerName("Customer 1")
                            .createdDate(LocalDateTime.now())
                            .lastModifiedDate(LocalDateTime.now())
                            .build();
        Customer customer2 = Customer.builder()
                            .customerName("Customer 2")
                            .createdDate(LocalDateTime.now())
                            .lastModifiedDate(LocalDateTime.now())
                            .build();
        Customer customer3 = Customer.builder()
                            .customerName("Customer 3")
                            .createdDate(LocalDateTime.now())
                            .lastModifiedDate(LocalDateTime.now())
                            .build();
        customerRepository.saveAll(Arrays.asList(customer1, customer2, customer3));
        
    }

}



