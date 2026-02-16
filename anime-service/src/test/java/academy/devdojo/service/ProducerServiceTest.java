package academy.devdojo.service;

import academy.devdojo.domain.Producer;
import academy.devdojo.repository.ProducerHardCodedRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ProducerServiceTest {

    @InjectMocks
    private ProducerService service;

    @Mock
    private ProducerHardCodedRepository repository;

    private final List<Producer> producerList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        producerList.addAll(
                List.of(
                        Producer.builder().id(1L).name("Mappa").createdAt(LocalDateTime.now()).build(),
                        Producer.builder().id(2L).name("Kyoto Animation").createdAt(LocalDateTime.now()).build(),
                        Producer.builder().id(3L).name("Madhouse").createdAt(LocalDateTime.now()).build()
                )
        );
    }

    @Test
    @DisplayName("Find all producers")
    void findAll() {
        BDDMockito.when(repository.findAll()).thenReturn(producerList);
        var producers = service.findAll();
        Assertions.assertThat(producers).isNotNull().hasSameElementsAs(producerList);
    }

    @Test
    @DisplayName("Find producer by id")
    void findById() {
        var producer = producerList.getFirst();
        BDDMockito.when(repository.findById(producer.getId())).thenReturn(Optional.of(producer));
        var foundProducer = service.findById(producer.getId());
        Assertions.assertThat(foundProducer).isNotNull().isEqualTo(producer);
    }

    @Test
    @DisplayName("Find producer by name")
    void findByName() {
        var producer = producerList.getFirst();
        BDDMockito.when(repository.findByName(producer.getName())).thenReturn(List.of(producer));
        var foundProducer = service.findByName(producer.getName());
        Assertions.assertThat(foundProducer).isNotNull().hasSameElementsAs(List.of(producer));
    }

    @Test
    @DisplayName("Save producer")
    void save() {
        var producerToSave = Producer.builder().id(99L).name("Bones").createdAt(LocalDateTime.now()).build();
        BDDMockito.when(repository.save(producerToSave)).thenReturn(producerToSave);
        var savedProducer = service.save(producerToSave);
        Assertions.assertThat(savedProducer).isNotNull().isEqualTo(producerToSave).hasNoNullFieldsOrProperties();
    }

    @Test
    @DisplayName("Delete producer")
    void delete() {
        var producerToDelete = producerList.getFirst();
        BDDMockito.when(repository.findById(producerToDelete.getId())).thenReturn(Optional.of(producerToDelete));
        service.delete(producerToDelete.getId());
        BDDMockito.verify(repository).delete(producerToDelete);
    }

    @Test
    @DisplayName("Update producer")
    void update() {
        var producer = producerList.getFirst();
        var producerToUpdate = Producer.builder().id(producer.getId()).name("Bones").createdAt(LocalDateTime.now()).build();
        BDDMockito.when(repository.findById(producer.getId())).thenReturn(Optional.of(producer));
        service.update(producerToUpdate);
        BDDMockito.verify(repository).update(producerToUpdate);
    }
}