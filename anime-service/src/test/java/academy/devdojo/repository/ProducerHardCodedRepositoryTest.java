package academy.devdojo.repository;

import academy.devdojo.commons.ProducerUtils;
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

    @InjectMocks
    private ProducerUtils producerUtils;

    @Mock
    private ProducerData producerData;
    private List<Producer> producersList;

    @BeforeEach
    void init(){
        producersList = new ArrayList<>(producerUtils.createProducerList());
        BDDMockito.when(producerData.getProducers()).thenReturn(producersList);
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
        var exprectedProducer  = producersList.getFirst();
        var producers = repository.findById(exprectedProducer.getId());

        Assertions.assertThat(producers).contains(exprectedProducer);
    }

    @Test
    @DisplayName("findByName returns producer with a given Name")
    void findByName(){
        var exprectedProducer  = producersList.getFirst();
        var producers = repository.findByName(exprectedProducer.getName());

        Assertions.assertThat(producers).contains(exprectedProducer);
    }

    @Test
    @DisplayName("findByName returns producer with a given Name")
    void save(){
        var producerToSave = producerUtils.newProducerToSave();
        var producer = repository.save(producerToSave);
        Assertions.assertThat(producer).isEqualTo(producerToSave).hasNoNullFieldsOrProperties();

        var producerSaveOptional = repository.findById(producerToSave.getId());
        Assertions.assertThat(producerSaveOptional).isPresent().contains(producerToSave);
    }

    @Test
    @DisplayName("delete removes a producer")
    void delete(){
         var producerToDelete  = producersList.getFirst();
        repository.delete(producerToDelete);

        Assertions.assertThat(producersList).doesNotContain(producerToDelete);
    }

    @Test
    @DisplayName("update a producer")
    void update(){
        var producerToUpdate = producersList.getFirst();
        producerToUpdate.setName("Aniplex");
        repository.update(producerToUpdate );

        Assertions.assertThat(producersList).contains(producerToUpdate);

        var producerUpdateOptional = repository.findById(producerToUpdate.getId());
        Assertions.assertThat(producerUpdateOptional).isPresent();
        Assertions.assertThat(producerUpdateOptional.get().getName()).isEqualTo(producerToUpdate.getName());
    }
}