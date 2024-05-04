package com.example.unimovie.rec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;


@Configuration
class LoadDatabase {

	private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);
	
	@Bean
	CommandLineRunner initDatabase(MovieRepository mvRepository) {

		return args -> {
			
			
			String filePath = "data/out.csv";
        	try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            	String line;
            	while ((line = br.readLine()) != null) {
                	String[] parts = line.split(",");
                	if (parts.length >= 3) {
                    	String season = parts[5];
                    	String title = parts[1];
                    	String genres = parts[2].replace("|", ", ");
						String rating = parts[3];
                    
                    	Movie movie = new Movie(season, title, genres, rating);
                    	mvRepository.save(movie);
                	}
            	}
        	} catch (IOException e) {
            	System.err.println("Error while reading the file: " + e.getMessage());
        	}
		};
	}

}


