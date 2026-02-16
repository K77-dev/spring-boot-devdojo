package academy.devdojo.repository;

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

    @Mock
    private AnimeData animeData;
    private final List<Anime> animesDataList = new ArrayList<>();

    @BeforeEach
    void init(){
        animesDataList.addAll(
                List.of(
                        Anime.builder().id(1L).name("Naruto").build(),
                        Anime.builder().id(2L).name("Goku").build(),
                        Anime.builder().id(3L).name("One Piece").build()
                )
        );
        BDDMockito.when(animeData.getAnimes()).thenReturn(animesDataList);
    }

    @Test
    @DisplayName("findAll return a list with all animes")
    void findAll(){
        var animes = repository.findAll();

        Assertions.assertThat(animesDataList).isNotNull().hasSameElementsAs(animes);
    }

    @Test
    @DisplayName("findById returns anime with a given Id")
    void findById(){
        var exprectedAnime  = animesDataList.getFirst();
        var animes = repository.findById(exprectedAnime.getId());

        Assertions.assertThat(animes).contains(exprectedAnime);
    }

    @Test
    @DisplayName("findByName returns anime with a given Name")
    void findByName(){
        var exprectedAnime  = animesDataList.getFirst();
        var animes = repository.findByName(exprectedAnime.getName());

        Assertions.assertThat(animes).contains(exprectedAnime);
    }

    @Test
    @DisplayName("findByName returns anime with a given Name")
    void save(){
        var animeToSave = Anime.builder().id(99L).name("DIFFERS").build();
        var anime = repository.save(animeToSave);
        Assertions.assertThat(anime).isEqualTo(animeToSave).hasNoNullFieldsOrProperties();

        var animeSaveOptional = repository.findById(animeToSave.getId());
        Assertions.assertThat(animeSaveOptional).isPresent().contains(animeToSave);
    }

    @Test
    @DisplayName("delete removes a anime")
    void delete(){
         var animeToDelete  = animesDataList.getFirst();
        repository.delete(animeToDelete);

        Assertions.assertThat(animesDataList).doesNotContain(animeToDelete);
    }

    @Test
    @DisplayName("update a anime")
    void update(){
        var animeToUpdate = animesDataList.getFirst();
        animeToUpdate.setName("Driffers");
        repository.update(animeToUpdate );

        Assertions.assertThat(animesDataList).contains(animeToUpdate);

        var animeUpdateOptional = repository.findById(animeToUpdate.getId());
        Assertions.assertThat(animeUpdateOptional).isPresent();
        Assertions.assertThat(animeUpdateOptional.get().getName()).isEqualTo(animeToUpdate.getName());
    }
}