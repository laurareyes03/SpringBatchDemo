package es.neesis.demospringbatch.processor;

import es.neesis.demospringbatch.dto.User;
import es.neesis.demospringbatch.model.UserEntity;
import org.springframework.batch.item.ItemProcessor;

import java.time.LocalDateTime;

public class UserEditProcessor implements ItemProcessor<User, UserEntity> {

    @Override
    public UserEntity process(User user) {



        return UserEntity.builder()
                .id(Integer.parseInt(user.getId()))
                .username(user.getUsername() + "EDITED")
                .password(user.getPassword() + "EDITED")
                .email(user.getEmail() + "EDITED")
                .fullname(user.getName() + " " + user.getSurname() + "EDITED")
                .timestamp(LocalDateTime.now().toString())
                .build();
    }
}
