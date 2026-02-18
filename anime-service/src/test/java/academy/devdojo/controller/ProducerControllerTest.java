package academy.devdojo.controller;

import academy.devdojo.domain.Producer;
import academy.devdojo.repository.ProducerData;
import academy.devdojo.repository.ProducerHardCodedRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@WebMvcTest(controllers = ProducerController.class)

@ComponentScan(basePackages = "academy.devdojo")
//@Import({ProducerMapperImpl.class, ProducerService.class, ProducerHardCodedRepository.class, ProducerData.class})
class ProducerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProducerData producerData;

    @SpyBean
    private ProducerHardCodedRepository repository;

    @Autowired
    private ResourceLoader resourceLoader;

    private final List<Producer> producersList = new java.util.ArrayList<>();

    @BeforeEach
    void init() {
        var dateTime = "2026-02-16T15:09:48.083599";
        var formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        var localDateTime = LocalDateTime.parse(dateTime, formatter);
        producersList.addAll(
                List.of(
                        Producer.builder().id(1L).name("Ufotable").createdAt(localDateTime).build(),
                        Producer.builder().id(2L).name("Wit Studio").createdAt(localDateTime).build(),
                        Producer.builder().id(3L).name("Studio Guibli").createdAt(localDateTime).build()
                )
        );
    }

    @Test
    @DisplayName("GET v1/producers - Find all producers")
    void findAll() throws Exception {
        BDDMockito.when(producerData.getProducers()).thenReturn(producersList);
        var response = readResourceFile("producer/producer-get-response-list.json");

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/producers"))
            .andDo(MockMvcResultHandlers.print()) // show the request and response in the console. Not necessary, but useful for debugging.
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().json(response));

    }

    @Test
    @DisplayName("GET v1/producers/filter?name=Ufotable - Find all producers")
    void findByName() throws Exception {
        BDDMockito.when(producerData.getProducers()).thenReturn(producersList);
        var response = readResourceFile("producer/producer-get-filter-name-ufotable-response.json");

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/producers/filter").param("name", "Ufotable"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/producers/filter?name=Ufotable - Return empty list when producer not found")
    void findByName_EmptyList() throws Exception {
        BDDMockito.when(producerData.getProducers()).thenReturn(producersList);
        var response = readResourceFile("producer/producer-get-filter-name-x-response.json");

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/producers/filter").param("name", "X"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/producers/{id} - Return empty list when producer not found")
    void findById() throws Exception {
        BDDMockito.when(producerData.getProducers()).thenReturn(producersList);
        var response = readResourceFile("producer/producer-get-by-id-response.json");

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/producers/{id}", 1L))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/producers/99 - Return 404 when producer not found")
    void findById_404() throws Exception {
        BDDMockito.when(producerData.getProducers()).thenReturn(producersList);

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/producers/{id}", 99L))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("Producer not found!"));
    }

    @Test
    @DisplayName("POST v1/producers/ - Save producer")
    void save() throws Exception {
        var request = readResourceFile("producer/producer-post-save-request.json");
        var response = readResourceFile("producer/producer-post-save-response.json");

        var producerToSave = Producer.builder().id(99L).name("MAPPA").createdAt(LocalDateTime.now()).build();
        BDDMockito.when(repository.save(ArgumentMatchers.any())).thenReturn(producerToSave);

        mockMvc.perform(MockMvcRequestBuilders
                    .post("/v1/producers")
                    .content(request)
                    .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }


    // =================================
    private String readResourceFile(String fileName) throws IOException {
        var file = resourceLoader.getResource("classpath:%s".formatted(fileName)).getFile();
        return new String(Files.readAllBytes(file.toPath()));
    }

}