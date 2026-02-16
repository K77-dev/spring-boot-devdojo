package academy.devdojo.service;

import academy.devdojo.domain.Anime;
import academy.devdojo.repository.AnimeHardCodedRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class AnimeServiceTest {

    @InjectMocks
    private AnimeService service;

    @Mock
    private AnimeHardCodedRepository repository;

    private final List<Anime> animeList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        animeList.addAll(
            List.of(
                Anime.builder().id(1L).name("Naruto").build(),
                Anime.builder().id(2L).name("One Piece").build(),
                Anime.builder().id(3L).name("Attack on Titan").build()
        ));
    }

    @Test
    @DisplayName("Find all animes")
    void findAll() {
        BDDMockito.when(repository.findAll()).thenReturn(animeList);
        var animes = service.findAll();
        Assertions.assertThat(animes).isNotNull().hasSameElementsAs(animeList);
    }

    @Test
    @DisplayName("Find anime by id")
    void findById() {
        var anime = animeList.getFirst();
        BDDMockito.when(repository.findById(anime.getId())).thenReturn(Optional.of(anime));
        var foundAnime = service.findById(anime.getId());
        Assertions.assertThat(foundAnime).isNotNull().isEqualTo(anime);
    }

    @Test
    @DisplayName("Find anime by name")
    void findByName() {
        var anime = animeList.getFirst();
        BDDMockito.when(repository.findByName(anime.getName())).thenReturn(List.of(anime));
        var foundAnime = service.findByName(anime.getName());
        Assertions.assertThat(foundAnime).isNotNull().isEqualTo(List.of(anime));
    }

    @Test
    @DisplayName("Save anime")
    void save() {
        var animeToBeSaved = Anime.builder().name("Dragon Ball Z").build();
        var savedAnime = Anime.builder().id(99L).name("Dragon Ball Z").build();
        BDDMockito.when(repository.save(animeToBeSaved)).thenReturn(savedAnime);
        var anime = service.save(animeToBeSaved);
        Assertions.assertThat(anime).isNotNull().isEqualTo(savedAnime);
    }

    @Test
    @DisplayName("Delete anime")
    void delete() {
        var anime = animeList.getFirst();
        BDDMockito.when(repository.findById(anime.getId())).thenReturn(Optional.of(anime));
        service.delete(anime.getId());
        BDDMockito.verify(repository).delete(anime);
    }

    @Test
    @DisplayName("Update anime")
    void update() {
        var anime = animeList.getFirst();
        var animeToUpdate = Anime.builder().id(anime.getId()).name("Naruto Shippuden").build();
        BDDMockito.when(repository.findById(anime.getId())).thenReturn(Optional.of(anime));
        service.update(animeToUpdate);
        BDDMockito.verify(repository).update(animeToUpdate);
    }
}