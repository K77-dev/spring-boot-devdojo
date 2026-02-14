package academy.devdojo.domain;

import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Producer {
    @EqualsAndHashCode.Include
    private Long id;
    private String name;
    private LocalDateTime createdAt;
    @Getter
    private static List<Producer> producers = new ArrayList<>();

    static {
        producers.addAll(
            List.of(
                Producer.builder().id(1L).name("Mappa").createdAt(LocalDateTime.now()).build(),
                Producer.builder().id(2L).name("Kyoto Animation").createdAt(LocalDateTime.now()).build(),
                Producer.builder().id(3L).name("Madhouse").createdAt(LocalDateTime.now()).build()
            )
        );
    }
}
