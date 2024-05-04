package com.example.unimovie.features;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.util.MultiValueMap;
import org.springframework.util.LinkedMultiValueMap;
import java.util.Map;

import java.util.Base64;


@RestController
@RequestMapping("/features")
public class Faceswap {
    Path tempDir = null;
    Path tempFile1 = null;
    Path tempFile2 = null;
    Path resultFile = null;

    private static final String TEMP_DIR = "uploads"; // Directory to store temporary files

    @PostMapping("/upload")
    public ResponseEntity<?> handleFileUpload(
            @RequestParam("file1") MultipartFile file1,
            @RequestParam("file2") MultipartFile file2) {

        HttpHeaders headers = new HttpHeaders();

        if (file1.isEmpty() || file2.isEmpty()) {
            headers.setContentType(MediaType.APPLICATION_JSON);
            return ResponseEntity.badRequest().headers(headers).body(Map.of("error", "Please select two files to upload."));
        }

        try {
            tempDir = Files.createTempDirectory("uploads-");
            tempFile1 = Files.createTempFile(tempDir, "upload1-", ".jpg");
            tempFile2 = Files.createTempFile(tempDir, "upload2-", ".jpg");
            file1.transferTo(tempFile1);
            file2.transferTo(tempFile2);

            resultFile = runPythonScript(tempFile1, tempFile2);


            byte[] imageBytes = Files.readAllBytes(resultFile);
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);
            headers.setContentType(MediaType.APPLICATION_JSON);
            return ResponseEntity.ok().headers(headers).body(Map.of("image", base64Image, "message", "Faces swapped successfully."));
        } catch (Exception e) {
            headers.setContentType(MediaType.APPLICATION_JSON);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(headers).body(Map.of("error", "An error occurred during file processing."));
        } finally {
            cleanUpFiles(tempFile1, tempFile2, tempDir);
        }
    }


    public Path runPythonScript(Path sourcePath, Path targetPath) throws IOException, InterruptedException {
        // Directory of Python file
        String scriptPath = "/root/project/milestone2/api/faceswap/main.py";

        // Build the command to run the script with arguments
        ProcessBuilder builder = new ProcessBuilder(
                "python", scriptPath,
                "--source_img", sourcePath.toString(),
                "--target_img", targetPath.toString()
        );

        // Set the working directory for the script
        builder.directory(new File("/root/project/milestone2/api/faceswap"));

        builder.redirectErrorStream(true);

        // Start the process
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
        /*
        if (exitCode != 0) {
            System.err.println("Script output:\n" + output.toString());
            throw new RuntimeException("Python script execution failed with exit code: " + exitCode);
        }
        */


        resultFile = Paths.get("/root/project/milestone2/api/faceswap/outputs/result.jpeg");



        return resultFile;
    }

    public void cleanUpFiles(Path... paths) {
        for (Path path : paths) {
            try {
                if (Files.exists(path)) {
                    Files.delete(path);
                }
            } catch (IOException e) {
                System.err.println("Failed to delete " + path + ": " + e.getMessage());
            }
        }
    }


}