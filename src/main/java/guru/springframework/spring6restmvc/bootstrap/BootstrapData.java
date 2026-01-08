package guru.springframework.spring6restmvc.bootstrap;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.entities.Customer;
import guru.springframework.spring6restmvc.model.BeerCSVRecord;
import guru.springframework.spring6restmvc.model.BeerStyle;
import guru.springframework.spring6restmvc.repositories.BeerRepository;
import guru.springframework.spring6restmvc.repositories.CustomerRepository;
import guru.springframework.spring6restmvc.services.BeerCSVService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BootstrapData implements CommandLineRunner {

    private final BeerRepository beerRepository;
    private final CustomerRepository customerRepository;
    private final BeerCSVService beerCsvService;


    @Transactional
    @Override
    public void run(String... args) throws Exception {
        
        System.out.println("Loading some data into the database");
        loadBeerData();
        loadCustomerData();
        System.out.println("Stop loading some data into the database");
    }

    private void loadCsvData() throws FileNotFoundException {
        if (beerRepository.count() < 10){
            File file = ResourceUtils.getFile("classpath:csvdata/beers.csv");

            List<BeerCSVRecord> recs = beerCsvService.convertCSV(file);

            recs.forEach(beerCSVRecord -> {
                BeerStyle beerStyle = switch (beerCSVRecord.getStyle()) {
                    case "American Pale Lager" -> BeerStyle.LAGER;
                    case "American Pale Ale (APA)", "American Black Ale", "Belgian Dark Ale", "American Blonde Ale" ->
                            BeerStyle.ALE;
                    case "American IPA", "American Double / Imperial IPA", "Belgian IPA" -> BeerStyle.IPA;
                    case "American Porter" -> BeerStyle.PORTER;
                    case "Oatmeal Stout", "American Stout" -> BeerStyle.STOUT;
                    case "Saison / Farmhouse Ale" -> BeerStyle.SAISON;
                    case "Fruit / Vegetable Beer", "Winter Warmer", "Berliner Weissbier" -> BeerStyle.WHEAT;
                    case "English Pale Ale" -> BeerStyle.PALE_ALE;
                    default -> BeerStyle.PILSNER;
                };

                beerRepository.save(Beer.builder()
                                .beerName(StringUtils.abbreviate(beerCSVRecord.getBeer(), 50))
                                .beerStyle(beerStyle)
                                .price(BigDecimal.TEN)
                                .upc(beerCSVRecord.getRow().toString())
                                .quantityOnHand(beerCSVRecord.getCount())
                        .build());
            });
        }
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
                            .createDateTime(LocalDateTime.now())
                            .updateDateTime(LocalDateTime.now())
                            .build();
        Customer customer2 = Customer.builder()
                            .customerName("Customer 2")
                            .createDateTime(LocalDateTime.now())
                            .updateDateTime(LocalDateTime.now())
                            .build();
        Customer customer3 = Customer.builder()
                            .customerName("Customer 3")
                            .createDateTime(LocalDateTime.now())
                            .updateDateTime(LocalDateTime.now())
                            .build();
        customerRepository.saveAll(Arrays.asList(customer1, customer2, customer3));
        
    }

}



