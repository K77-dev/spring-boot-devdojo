package academy.devdojo.repository;

import academy.devdojo.domain.Producer;
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

@ExtendWith(MockitoExtension.class)
class ProducerHardCodedRepositoryTest  {

    @InjectMocks
    private ProducerHardCodedRepository repository;

    @Mock
    private ProducerData producerData;
    private final List<Producer> producersDataList = new ArrayList<>();

    @BeforeEach
    void init(){
        producersDataList.addAll(
                List.of(
                        Producer.builder().id(1L).name("Ufotable").createdAt(LocalDateTime.now()).build(),
                        Producer.builder(  ).id(2L).name("Wit Studio").createdAt(LocalDateTime.now()).build(),
                        Producer.builder().id(3L).name("Studio Guibli ").createdAt(LocalDateTime.now()).build()
                )
        );
        BDDMockito.when(producerData.getProducers()).thenReturn(producersDataList);
    }

    @Test
    @DisplayName("findAll return a list with all producers")
    void findAll(){
        var producers = repository.findAll();

        Assertions.assertThat(producers).isNotNull().hasSameElementsAs(producers);
    }

    @Test
    @DisplayName("findById returns producer with a given Id")
    void findById(){
        var exprectedProducer  = producersDataList.getFirst();
        var producers = repository.findById(exprectedProducer.getId());

        Assertions.assertThat(producers).contains(exprectedProducer);
    }

    @Test
    @DisplayName("findByName returns producer with a given Name")
    void findByName(){
        var exprectedProducer  = producersDataList.getFirst();
        var producers = repository.findByName(exprectedProducer.getName());

        Assertions.assertThat(producers).contains(exprectedProducer);
    }

    @Test
    @DisplayName("findByName returns producer with a given Name")
    void save(){
        var producerToSave = Producer.builder().id(99L).name("MAPA").createdAt(LocalDateTime.now()).build();
        var producer = repository.save(producerToSave);
        Assertions.assertThat(producer).isEqualTo(producerToSave).hasNoNullFieldsOrProperties();

        var producerSaveOptional = repository.findById(producerToSave.getId());
        Assertions.assertThat(producerSaveOptional).isPresent().contains(producerToSave);
    }

    @Test
    @DisplayName("delete removes a producer")
    void delete(){
         var producerToDelete  = producersDataList.getFirst();
        repository.delete(producerToDelete);

        Assertions.assertThat(producersDataList).doesNotContain(producerToDelete);
    }

    @Test
    @DisplayName("update a producer")
    void update(){
        var producerToUpdate = producersDataList.getFirst();
        producerToUpdate.setName("Aniplex");
        repository.update(producerToUpdate );

        Assertions.assertThat(producersDataList).contains(producerToUpdate);

        var producerUpdateOptional = repository.findById(producerToUpdate.getId());
        Assertions.assertThat(producerUpdateOptional).isPresent();
        Assertions.assertThat(producerUpdateOptional.get().getName()).isEqualTo(producerToUpdate.getName());
    }
}