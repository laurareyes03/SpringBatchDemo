package es.neesis.demospringbatch.processor;

import es.neesis.demospringbatch.dto.User;
import es.neesis.demospringbatch.model.UserEntity;
import org.springframework.batch.item.ItemProcessor;

import java.time.LocalDateTime;

public class UserProcessor implements ItemProcessor<User, UserEntity> {

    @Override
    public UserEntity process(User user) {

        return UserEntity.builder()
                .id(Integer.parseInt(user.getId()))
                .username(user.getUsername())
                .password(user.getPassword())
                .email(user.getEmail())
                .fullname(user.getName() + " " + user.getSurname())
                .timestamp(LocalDateTime.now().toString())
                .build();
    }
}
