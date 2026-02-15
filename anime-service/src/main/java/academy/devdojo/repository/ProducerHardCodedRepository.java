package academy.devdojo.repository;

import academy.devdojo.domain.Producer;
import external.dependency.Connection;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Log4j2
public class ProducerHardCodedRepository {
    private static final List<Producer> PRODUCERS = new ArrayList<>();

    //private final Connection connectionMySql;
    //@Qualifier(value = "connectionMongoDB")
    private final Connection connection;

    static {
        PRODUCERS.addAll(
                List.of(
                        Producer.builder().id(1L).name("Mappa").createdAt(LocalDateTime.now()).build(),
                        Producer.builder().id(2L).name("Kyoto Animation").createdAt(LocalDateTime.now()).build(),
                        Producer.builder().id(3L).name("Madhouse").createdAt(LocalDateTime.now()).build()
                )
        );
    }

    public List<Producer> findAll(){
        log.debug(connection);
        return PRODUCERS;
    }

    public Optional<Producer> findById(Long id){
        return PRODUCERS.stream()
                .filter( n -> n.getId().equals(id))
                .findFirst();
    }

    public List<Producer> findByName(String name){
        return PRODUCERS.stream()
                .filter(n -> n.getName().equalsIgnoreCase(name))
                .toList();
    }

    public Producer save(Producer producer){
        PRODUCERS.add(producer);
        return producer;
    }

    public void delete(Producer producer){
        PRODUCERS.remove(producer);
    }

    public void update(Producer producer){
        delete(producer);
        save(producer);
    }
}
