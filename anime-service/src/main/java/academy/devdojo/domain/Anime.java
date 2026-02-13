package academy.devdojo.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class Anime {
    private Long id;
    private String name;
    private static List<Anime> animes = new ArrayList<>();

    static {
        animes.addAll(
            List.of(
                new Anime(1L, "Naturo"),
                new Anime(2L, "Goku"),
                new Anime(3L, "One Piece")
            )
        );
    }

    public static List<Anime> getAnimes() {
        return animes;
    }
}
