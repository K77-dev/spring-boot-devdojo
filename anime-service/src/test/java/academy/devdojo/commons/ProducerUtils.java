package academy.devdojo.commons;

import academy.devdojo.domain.Producer;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ProducerUtils {

    public List<Producer> createProducerList() {
        var dateTime = "2026-02-16T15:09:48.083599";
        var formatter = java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        var localDateTime = java.time.LocalDateTime.parse(dateTime, formatter);
        return List.of(
                Producer.builder().id(1L).name("Ufotable").createdAt(localDateTime).build(),
                Producer.builder().id(2L).name("Wit Studio").createdAt(localDateTime).build(),
                Producer.builder().id(3L).name("Studio Guibli").createdAt(localDateTime).build()
        );
    }

    public Producer newProducerToSave() {
        return Producer.builder().id(99L).name("MAPPA").createdAt(LocalDateTime.now()).build();
    }
}
