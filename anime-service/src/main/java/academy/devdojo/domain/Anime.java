package academy.devdojo.domain;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Anime {
    @EqualsAndHashCode.Include
    private Long id;
    private String name;
    @Getter
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
}
