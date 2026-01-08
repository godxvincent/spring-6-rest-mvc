package guru.springframework.spring6restmvc.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

// import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.model.BeerCSVRecord;

public class BeerCSVServiceImplTest {

    BeerCSVService beerCSVService = new BeerCSVServiceImpl();

    @Test
    void testConvertCSV() throws FileNotFoundException {
        
        File file = ResourceUtils.getFile("classpath:csvdata/beers.csv");

        List<BeerCSVRecord> records = new BeerCSVServiceImpl().convertCSV(file);

        System.out.println(records.size());

        assertThat(records.size()).isGreaterThan(0);

        //

    }
}
