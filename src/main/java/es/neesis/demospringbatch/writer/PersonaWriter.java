package es.neesis.demospringbatch.writer;

import es.neesis.demospringbatch.model.Persona;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.core.io.FileSystemResource;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class PersonaWriter implements ItemWriter<Persona> {

    private ExecutionContext executionContext;
    private boolean headerWritten = false;

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        this.executionContext = stepExecution.getJobExecution().getExecutionContext();

        File outputFile = new File("output_personas.csv");
        if (outputFile.exists()) {
            outputFile.delete();
        }

        headerWritten = false;
    }

    @Override
    public void write(List<? extends Persona> items) throws Exception {
        File outputFile = new File("output_personas.csv");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile, true))) {

            if (!headerWritten) {
                writer.write("idPersona,nombre,apellido,dni");
                writer.newLine();
                headerWritten = true;
            }

            for (Persona persona : items) {
                String line = String.format("%s,%s,%s,%s",
                        persona.getIdPersona(),
                        persona.getNombre(),
                        persona.getApellido(),
                        persona.getDni());
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error escribiendo el archivo", e);
        }
    }
}
