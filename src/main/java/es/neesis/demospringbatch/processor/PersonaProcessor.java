package es.neesis.demospringbatch.processor;

import es.neesis.demospringbatch.dto.User;
import es.neesis.demospringbatch.model.Persona;
import es.neesis.demospringbatch.model.PersonaBuilder;
import es.neesis.demospringbatch.model.UserEntity;
import org.springframework.batch.item.ItemProcessor;

import java.time.LocalDateTime;

public class PersonaProcessor implements ItemProcessor<User, Persona> {

    @Override
    public Persona process(User user) {

        return new PersonaBuilder()
                .setIdPersona(Integer.parseInt(user.getId()))
                .setNombre(user.getName())
                .setApellido(user.getSurname())
                .setDni("DNI" + user.getId())
                .build();
    }
}

