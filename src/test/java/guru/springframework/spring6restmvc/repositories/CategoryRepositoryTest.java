package guru.springframework.spring6restmvc.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.entities.Category;
import jakarta.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class CategoryRepositoryTest {

    @Autowired
    BeerRepository beerRepository;

    @Autowired
    CategoryRepository categoryRepository;

    private Beer beerTest; 

    
    @BeforeEach
    void setUp() {
        beerTest = beerRepository.findAll().get(0);
    }

    @Transactional
    @Test
    void testSaveCategory() {
        Category savedCategory = categoryRepository.save(
            Category.builder()
                .description("Test Category")
                .build()
        );

        beerTest.addCategory(savedCategory);

        Beer savedBeer = beerRepository.save(beerTest);
        System.out.println(savedBeer.getCategories().size());
        assertThat(savedBeer.getCategories().size()).isEqualTo(1);

    }

    @Transactional
    @Test
    void testDeleteCategory() {
        Category savedCategory = categoryRepository.save(
            Category.builder()
                .description("Test Category")
                .build()
        );

        beerTest.addCategory(savedCategory);

        Beer savedBeer = beerRepository.save(beerTest);
        System.out.println(savedBeer.getCategories().size());
        assertThat(savedBeer.getCategories().size()).isEqualTo(1);

        savedBeer.removeCategory(savedCategory);
        Beer savedBeer2 = beerRepository.save(savedBeer);

        assertThat(savedBeer2.getCategories().size()).isEqualTo(0);

        System.out.println("passing test....");
    }


}
