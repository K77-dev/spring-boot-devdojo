package academy.devdojo.controller;

import academy.devdojo.commons.AnimeUtils;
import academy.devdojo.commons.FileUtils;
import academy.devdojo.domain.Anime;
import academy.devdojo.repository.AnimeData;
import academy.devdojo.repository.AnimeHardCodedRepository;
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
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

@WebMvcTest(controllers = AnimeController.class)

@ComponentScan(basePackages = "academy.devdojo")
//@Import({AnimeMapperImpl.class, AnimeService.class, AnimeHardCodedRepository.class, AnimeData.class})
class AnimeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AnimeData animeData;

    @SpyBean
    private AnimeHardCodedRepository repository;

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private AnimeUtils animeUtils;
    
    @Autowired
    FileUtils fileUtils;

    private List<Anime> animesList;
    private final String URL_BASE = "/v1/animes";

    @BeforeEach
    void init() {
        animesList = new ArrayList<>(animeUtils.createAnimeList());
    }

    @Test
    @DisplayName("GET v1/animes - Find all animes")
    void findAll() throws Exception {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animesList);
        var response = fileUtils.readResourceFile("anime/anime-get-response-list.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL_BASE))
            .andDo(MockMvcResultHandlers.print()) // show the request and response in the console. Not necessary, but useful for debugging.
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().json(response));

    }

    @Test
    @DisplayName("GET v1/animes/filter?name=Mashle - Find all animes")
    void findByName() throws Exception {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animesList);
        var response = fileUtils.readResourceFile("anime/anime-get-filter-name-mashle-response.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL_BASE+"/filter").param("name", "Mashle"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/animes/filter?name=X - Return empty list when anime not found")
    void findByName_EmptyList() throws Exception {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animesList);
        var response = fileUtils.readResourceFile("anime/anime-get-filter-name-x-response.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL_BASE+"/filter").param("name", "X"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/animes/{id} - Find anime by id")
    void findById() throws Exception {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animesList);
        var response = fileUtils.readResourceFile("anime/anime-get-by-id-response.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL_BASE+"/{id}", 1L))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/animes/99 - Return 404 when anime not found")
    void findById_404() throws Exception {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animesList);

        mockMvc.perform(MockMvcRequestBuilders.get(URL_BASE+"/{id}", 99L))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("Anime not found!"));
    }

    @Test
    @DisplayName("POST v1/animes/ - Save anime")
    void save() throws Exception {
        var request = fileUtils.readResourceFile("anime/anime-post-save-request.json");
        var response = fileUtils.readResourceFile("anime/anime-post-save-response.json");

        var animeToSave = animeUtils.newAnimeToSave();
        BDDMockito.when(repository.save(ArgumentMatchers.any())).thenReturn(animeToSave);

        mockMvc.perform(MockMvcRequestBuilders
                    .post(URL_BASE)
                    .content(request)
                    .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("PUT v1/animes/ - Update anime")
    void update() throws Exception {
        var request = fileUtils.readResourceFile("anime/anime-put-update-request.json");

        BDDMockito.when(animeData.getAnimes()).thenReturn(animesList);

        mockMvc.perform(MockMvcRequestBuilders
                        .put(URL_BASE)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("PUT v1/animes/ - Update anime where anime is not found")
    void updateWhenAnimeIsNotFound() throws Exception {
        var request = fileUtils.readResourceFile("anime/anime-put-update-request-404.json");

        BDDMockito.when(animeData.getAnimes()).thenReturn(animesList);

        mockMvc.perform(MockMvcRequestBuilders
                        .put(URL_BASE)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("Anime not found!"));
    }

    @Test
    @DisplayName("\"PUT v1/animes/1 - Delete anime")
    void delete() throws Exception {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animesList);

        var id = animesList.getFirst().getId();
        mockMvc.perform(MockMvcRequestBuilders
                        .delete(URL_BASE+"/{id}", id)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("\"PUT v1/animes/1 - Delete anime where anime is not found")
    void delete_WhenAnimeIsNotFound() throws Exception {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animesList);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete(URL_BASE+"/{id}", 999L)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("Anime not found!"));
    }
}