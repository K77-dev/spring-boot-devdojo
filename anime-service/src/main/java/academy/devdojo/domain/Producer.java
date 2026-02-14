package academy.devdojo.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class Producer {
    private Long id;
    private String name;
    @Getter
    private static List<Producer> producers = new ArrayList<>();

    static {
        producers.addAll(
            List.of(
                new Producer(1L, "Mappa"),
                new Producer(2L, "Kyoto Animation"),
                new Producer(3L, "Madhouse")
            )
        );
    }
}
