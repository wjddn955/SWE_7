package com.example.unimovie.pre;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.io.IOException;
import java.io.File;
import java.nio.file.Paths;
import java.io.InputStream; // Import for InputStream
import java.io.BufferedReader; // Import for BufferedReader
import java.io.InputStreamReader; // Import for InputStreamReader

import java.util.regex.Pattern;

@RestController
@RequestMapping("/features")
public class MovieController_predict {

    private static final String SCRIPT_PATH = "main.py";
    private static final Pattern FACTOR_PATTERN = Pattern.compile("^([^,]+,){21}[^,]+$");

    @PostMapping("/movie_predict")
    public ResponseEntity<?> predictMovieSuccess(@RequestParam("factor") String factor) {
        

        if (!isValidFactor(factor)) {
            return new ResponseEntity<>("Invalid input factors.", HttpStatus.BAD_REQUEST);
        }

        try {
            String result = runPythonScript(factor);

            return ResponseEntity.ok().body(result);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing result: " + e.getMessage());
        }
    }

    private boolean isValidFactor(String factor) {
        return FACTOR_PATTERN.matcher(factor).matches();
    }

    private String runPythonScript(String factor) throws IOException, InterruptedException {
        String scriptPath = "main.py";

        ProcessBuilder builder = new ProcessBuilder("python", scriptPath, "--factors", factor);

        builder.directory(new File("/root/project/milestone2/api/movie_predict"));

        builder.redirectErrorStream(true);

        Process process = builder.start();
        InputStream is = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        String line;
        StringBuilder output = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            output.append(line + "\n");
            System.out.println(line);
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            System.err.println("Script output:\n" + output.toString());
            throw new RuntimeException("Python script execution failed with exit code: " + exitCode);
        }

        String[] lines = output.toString().split("\n");
        return lines[lines.length - 1];
    }
}
