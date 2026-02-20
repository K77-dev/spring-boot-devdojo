package academy.devdojo.repository;

import academy.devdojo.commons.AnimeUtils;
import academy.devdojo.domain.Anime;
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

@ExtendWith(MockitoExtension.class)
class AnimeHardCodedRepositoryTest {

    @InjectMocks
    private AnimeHardCodedRepository repository;

    @InjectMocks
    private AnimeUtils animeUtils;

    @Mock
    private AnimeData animeData;

    private List<Anime> animeList;

    @BeforeEach
    void init(){
        animeList = new ArrayList<>(animeUtils.createAnimeList());
        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);
    }

    @Test
    @DisplayName("findAll return a list with all animes")
    void findAll(){
        var animes = repository.findAll();

        Assertions.assertThat(animeList).isNotNull().hasSameElementsAs(animes);
    }

    @Test
    @DisplayName("findById returns anime with a given Id")
    void findById(){
        var exprectedAnime  = animeList.getFirst();
        var animes = repository.findById(exprectedAnime.getId());

        Assertions.assertThat(animes).contains(exprectedAnime);
    }

    @Test
    @DisplayName("findByName returns anime with a given Name")
    void findByName(){
        var exprectedAnime  = animeList.getFirst();
        var animes = repository.findByName(exprectedAnime.getName());

        Assertions.assertThat(animes).contains(exprectedAnime);
    }

    @Test
    @DisplayName("findByName returns anime with a given Name")
    void save(){
        var animeToSave = animeUtils.newAnimeToSave();
        var anime = repository.save(animeToSave);
        Assertions.assertThat(anime).isEqualTo(animeToSave).hasNoNullFieldsOrProperties();

        var animeSaveOptional = repository.findById(animeToSave.getId());
        Assertions.assertThat(animeSaveOptional).isPresent().contains(animeToSave);
    }

    @Test
    @DisplayName("delete removes a anime")
    void delete(){
         var animeToDelete  = animeList.getFirst();
        repository.delete(animeToDelete);

        Assertions.assertThat(animeList).doesNotContain(animeToDelete);
    }

    @Test
    @DisplayName("update a anime")
    void update(){
        var animeToUpdate = animeList.getFirst();
        animeToUpdate.setName("Driffers");
        repository.update(animeToUpdate );

        Assertions.assertThat(animeList).contains(animeToUpdate);

        var animeUpdateOptional = repository.findById(animeToUpdate.getId());
        Assertions.assertThat(animeUpdateOptional).isPresent();
        Assertions.assertThat(animeUpdateOptional.get().getName()).isEqualTo(animeToUpdate.getName());
    }
}