package flow.fileattach;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class FileAttachApplication {

	public static void main(String[] args) {
		SpringApplication.run(FileAttachApplication.class, args);
	}

}
