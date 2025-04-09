package es.neesis.demospringbatch.tasklet;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class insertPreloadDataTasklet implements Tasklet {

    private JdbcTemplate jdbcTemplate;

    public insertPreloadDataTasklet(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {

        String sql = "INSERT INTO users ( id, username, password, email, fullname, timestamp) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        List<Object[]> userData = List.of(
            new Object[]{"1", "user1", "defaultPassword", "user1@email.com", "User One", "2025-04-09"},
            new Object[]{"2", "user2", "defaultPassword", "user2@email.com", "User Two", "2025-04-09"},
            new Object[]{"3", "user3", "defaultPassword", "user3@email.com", "User Three", "2025-04-09"}
        );

        jdbcTemplate.batchUpdate(sql, userData);
        return RepeatStatus.FINISHED;
    }
}
