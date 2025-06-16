package guru.springframework.spring6restmvc.controllers;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import guru.springframework.spring6restmvc.services.CustomerService;
import guru.springframework.spring6restmvc.services.CustomerServiceImpl;
import static org.hamcrest.core.Is.is;

// This is to indicate that the controller needs to be spied 
@WebMvcTest(CustomerController.class)

public class CustomerControllerTest {

    @Autowired
    MockMvc mockMvc;
 
    @MockitoBean
    CustomerService customerService;

    CustomerServiceImpl customerServiceImpl = new CustomerServiceImpl();


    @Test
    void testGetcustomerById() throws Exception {


        var testCustomer = customerServiceImpl.listCustomers().get(0);

        given(customerService.getCustomerById(testCustomer.getId())).willReturn(testCustomer);


        mockMvc.perform(get("/api/v1/customer/" + testCustomer.getId())
               .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.id", is(testCustomer.getId().toString())))
               .andExpect(jsonPath("$.customerName", is(testCustomer.getCustomerName()))); 
        }
    

    @Test
    void testListCustomers() throws Exception {
        var testListOfCustomers = customerServiceImpl.listCustomers();
        given(customerService.listCustomers()).willReturn(testListOfCustomers);

        mockMvc.perform(get("/api/v1/customer")
               .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.length()", is(3)));
    }
}
