package com.example;

import com.github.tomakehurst.wiremock.client.WireMock;
import io.cucumber.java.en.Given;
import io.cucumber.junit.platform.engine.Cucumber;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.web.servlet.MockMvc;

import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Cucumber
@CucumberContextConfiguration
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureWireMock(port = 0)
public class CucumberTest {

    @Autowired
    private MockMvc mvc;

    @Given("the application says hello")
    public void getLocalHello() throws Exception {
        mvc.perform(get("/local").accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("Greetings from Local!")));
    }

    @Given("the stub says hello")
    public void getRemoteHello() throws Exception {
        stubFor(WireMock.get("/").willReturn(okJson("Greetings from Stub!")));

        mvc.perform(get("/remote").accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("Greetings from Stub!")));
    }

}
