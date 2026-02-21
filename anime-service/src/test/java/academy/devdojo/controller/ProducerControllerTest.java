package academy.devdojo.controller;

import academy.devdojo.commons.FileUtils;
import academy.devdojo.commons.ProducerUtils;
import academy.devdojo.domain.Producer;
import academy.devdojo.repository.ProducerData;
import academy.devdojo.repository.ProducerHardCodedRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

@WebMvcTest(controllers = ProducerController.class)
@ComponentScan(basePackages = {"academy.devdojo"})
//@ActiveProfiles("test") // Add in pom.xml plugin
//@Import({ProducerMapperImpl.class, ProducerService.class, ProducerHardCodedRepository.class, ProducerData.class})
class ProducerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProducerData producerData;

    @SpyBean
    private ProducerHardCodedRepository repository;

    @Autowired
    private FileUtils fileUtils;

    @Autowired
    private ProducerUtils producerUtils;

    private List<Producer> producersList;
    private static final String BASE_URL = "/v1/producers";

    @BeforeEach
    void init() {
        producersList = new ArrayList<>(producerUtils.createProducerList());
    }

    @Test
    @DisplayName("GET v1/producers - Find all producers")
    void findAll() throws Exception {
        BDDMockito.when(producerData.getProducers()).thenReturn(producersList);
        var response = fileUtils.readResourceFile("producer/producer-get-response-list.json");

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL))
            .andDo(MockMvcResultHandlers.print()) // show the request and response in the console. Not necessary, but useful for debugging.
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().json(response));

    }

    @Test
    @DisplayName("GET v1/producers/filter?name=Ufotable - Find all producers")
    void findByName() throws Exception {
        BDDMockito.when(producerData.getProducers()).thenReturn(producersList);
        var response = fileUtils.readResourceFile("producer/producer-get-filter-name-ufotable-response.json");

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL+"/filter").param("name", "Ufotable"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/producers/filter?name=Ufotable - Return empty list when producer not found")
    void findByName_EmptyList() throws Exception {
        BDDMockito.when(producerData.getProducers()).thenReturn(producersList);
        var response = fileUtils.readResourceFile("producer/producer-get-filter-name-x-response.json");

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL+"/filter").param("name", "X"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/producers/{id} - Find producer by id")
    void findById() throws Exception {
        BDDMockito.when(producerData.getProducers()).thenReturn(producersList);
        var response = fileUtils.readResourceFile("producer/producer-get-by-id-response.json");

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL+"/{id}", 1L))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/producers/99 - Return 404 when producer not found")
    void findById_404() throws Exception {
        BDDMockito.when(producerData.getProducers()).thenReturn(producersList);

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL+"/{id}", 99L))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("Producer not found!"));
    }

    @Test
    @DisplayName("POST v1/producers/ - Save producer")
    void save() throws Exception {
        var request = fileUtils.readResourceFile("producer/producer-post-save-request.json");
        var response = fileUtils.readResourceFile("producer/producer-post-save-response.json");

        var producerToSave = producerUtils.newProducerToSave();
        BDDMockito.when(repository.save(ArgumentMatchers.any())).thenReturn(producerToSave);

        mockMvc.perform(MockMvcRequestBuilders
                    .post(BASE_URL)
                    .content(request)
                    .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("PUT v1/producers/ - Update producer")
    void update() throws Exception {
        var request = fileUtils.readResourceFile("producer/producer-put-update-request.json");

        BDDMockito.when(producerData.getProducers()).thenReturn(producersList);

        mockMvc.perform(MockMvcRequestBuilders
                        .put(BASE_URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("PUT v1/producers/ - Update producer where producer is not found")
    void updateWhenProducerIsNotFound() throws Exception {
        var request = fileUtils.readResourceFile("producer/producer-put-update-request-404.json");

        BDDMockito.when(producerData.getProducers()).thenReturn(producersList);

        mockMvc.perform(MockMvcRequestBuilders
                        .put(BASE_URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("Producer not found!"));
    }

    @Test
    @DisplayName("\"PUT v1/producers/1 - Delete producer")
    void delete() throws Exception {
        BDDMockito.when(producerData.getProducers()).thenReturn(producersList);

        var id = producersList.getFirst().getId();
        mockMvc.perform(MockMvcRequestBuilders
                        .delete(BASE_URL+"/{id}", id)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("\"PUT v1/producers/1 - Delete producer where producer is not found")
    void delete_WhenProducerIsNotFound() throws Exception {
        BDDMockito.when(producerData.getProducers()).thenReturn(producersList);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete(BASE_URL+"/{id}", 999L)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("Producer not found!"));
    }

}