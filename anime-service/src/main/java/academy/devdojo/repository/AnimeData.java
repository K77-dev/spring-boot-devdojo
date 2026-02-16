package academy.devdojo.repository;

import academy.devdojo.domain.Anime;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AnimeData {
    private final List<Anime> animes = new ArrayList<>();

    {
        animes.addAll(
                List.of(
                        Anime.builder().id(1L).name("Naruto").build(),
                        Anime.builder().id(2L).name("Goku").build(),
                        Anime.builder().id(3L).name("One Piece").build()
                )
        );
    }

    public List<Anime> getAnimes() {
        return animes;
    }
}
