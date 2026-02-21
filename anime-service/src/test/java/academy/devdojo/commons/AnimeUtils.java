package academy.devdojo.commons;

import academy.devdojo.domain.Anime;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AnimeUtils {

    public List<Anime> createAnimeList() {
        return List.of(
                Anime.builder().id(1L).name("Full Metal Brotherhood").build(),
                Anime.builder().id(2L).name("Steins Gate").build(),
                Anime.builder().id(3L).name("Mashle").build()
        );
    }

    public Anime newAnimeToSave() {
        return Anime.builder().id(99L).name("Dragon Ball Z").build();
    }
}
