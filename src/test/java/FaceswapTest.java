package com.example.unimovie.features;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.mock.web.MockMultipartFile;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import java.nio.file.Files;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import static org.mockito.Mockito.when;
import org.mockito.Mockito;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import org.springframework.boot.test.mock.mockito.SpyBean;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;



//@ExtendWith(MockitoExtension.class)
//@SpringBootTest
//@AutoConfigureMockMvc
@WebMvcTest(Faceswap.class)
public class FaceswapTest {

    @Autowired
    private MockMvc mockMvc;

    private MockMultipartFile file1;
    private MockMultipartFile file2;

    private Path path1;
    private Path path2;

    @SpyBean
    private Faceswap faceswap;


    @BeforeEach
    public void setUp() throws IOException {
        path1 = Paths.get("/root/project/milestone2/api/faceswap/images/elon_musk.jpg");
        path2 = Paths.get("/root/project/milestone2/api/faceswap/images/mark.jpg");

        byte[] content1 = Files.readAllBytes(path1);
        byte[] content2 = Files.readAllBytes(path2);

        file1 = new MockMultipartFile("file1", "elon_musk.jpg", "image/jpg", content1);
        file2 = new MockMultipartFile("file2", "mark.jpg", "image/jpg", content2);
    }

    @Test
    public void testHandleFileUpload_NonEmptyFiles() throws Exception {
        mockMvc.perform(multipart("/features/upload")
                        .file(file1)
                        .file(file2))
                .andExpect(result -> {
                    if (result.getResolvedException() != null) {
                        System.out.println("Exception during test: " + result.getResolvedException().getMessage());
                        result.getResolvedException().printStackTrace();
                    }
                })
                .andExpect(status().isOk());
    }


    @Test
    public void testHandleFileUpload1() throws Exception {
        MockMultipartFile emptyFile1 = new MockMultipartFile("file1", "empty1.jpg", "image/jpeg", new byte[0]);
        MockMultipartFile emptyFile2 = new MockMultipartFile("file2", "empty2.jpg", "image/jpeg", new byte[0]);

        mockMvc.perform(multipart("/features/upload")
                        .file(emptyFile1)
                        .file(emptyFile2))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("Please select two files to upload."));
    }


    @Test
    public void testHandleFileUpload2() throws Exception {
        MockMultipartFile mockFile1 = new MockMultipartFile("file1", "filename1.jpg", "image/jpeg", "Some content".getBytes());
        MockMultipartFile mockFile2 = new MockMultipartFile("file2", "filename2.jpg", "image/jpeg", "Some content".getBytes());
        doThrow(new RuntimeException("Simulated Exception")).when(faceswap).runPythonScript(any(Path.class), any(Path.class));

        mockMvc.perform(multipart("/features/upload")
                        .file("file1", mockFile1.getBytes())
                        .file("file2", mockFile2.getBytes()))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testFilesException() throws Exception {
        try (MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.exists(any(Path.class))).thenReturn(true);
            mockedFiles.when(() -> Files.delete(any(Path.class))).thenThrow(new IOException("Simulated IOException"));
            faceswap.cleanUpFiles(Paths.get("/path/to/test/file.jpg"));
        }
    }

    @Test
    public void testHandleFileUpload3() throws Exception {
        MockMultipartFile nonEmptyFile = new MockMultipartFile("file1", "filename.jpg", "image/jpeg", "some content".getBytes());
        MockMultipartFile emptyFile = new MockMultipartFile("file2", "filename2.jpg", "image/jpeg", new byte[0]);

        mockMvc.perform(multipart("/features/upload")
                        .file(nonEmptyFile)
                        .file(emptyFile))
                .andExpect(status().isBadRequest());
    }



    @Test
    public void testFilesExist() throws Exception {
        try (MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.exists(any(Path.class))).thenReturn(false);
            faceswap.cleanUpFiles(Paths.get("/non/existing/file.jpg"));
        }
    }

}