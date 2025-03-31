package org.example.black_sea_walnut.service.imp;

import lombok.SneakyThrows;
import org.example.black_sea_walnut.util.LogUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImageServiceImpTest {
    @InjectMocks
    private ImageServiceImp imageService;

    @TempDir
    private Path tempDir;
    private static final String UPLOAD_DIR = "./test-files";
    private Path uploadPath;
    private Path tempDirectory;
    private Path tempFolder;
    private Path tempFile;
    private Path tempFileLock;
    private MockMultipartFile mockFile;
    @Mock
    private LogUtil logUtil;
    @BeforeEach
    void setUp() throws IOException {
        uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        mockFile = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test content".getBytes());
        tempDirectory = Files.createTempDirectory(uploadPath, "testDir");
        tempFolder = Files.createDirectory(tempDirectory.resolve("testFolder1"));
        tempFile = Files.createFile(tempDirectory.resolve("testFolder1").resolve("testFile.txt"));
        tempFileLock = Files.createFile(tempDirectory.resolve("testFolder1").resolve("testFile.txt.lock"));
    }

    @AfterEach
    public void tearDown() throws IOException {
        Files.walkFileTree(tempDirectory, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    @Test
    void ImageService_Init_Failure() {
        Path mockPath = mock(Path.class);
        try (MockedStatic<LogUtil> logUtilMock = mockStatic(LogUtil.class);
             MockedStatic<Files> filesMock = mockStatic(Files.class)) {
            IOException testIoException = new IOException("Test IOException");
            filesMock.when(() -> Files.createDirectories(mockPath)).thenThrow(testIoException);
            assertThrows(RuntimeException.class, () -> imageService.init(mockPath));
            logUtilMock.verify(() -> LogUtil.logError("Failed to initialize folder for upload at " + mockPath, testIoException));
        }
    }

    @Test
    void ImageService_Save_SuccessfulSave() {
        imageService.save(mockFile, tempFile.toString());
        assertTrue(Files.exists(tempFile), "File should be saved successfully");
    }

    @Test
    void ImageService_Save_DirectoryNotExists() {
        String path = "./test-files/testDir/testFolder1/testFile.txt";
        imageService.save(mockFile, path);
        Path pathFile = Path.of(path);
    }

    @Test
    void ImageService_Save_FileIsNull() {
        imageService.save(null, tempFile.toString());
    }

    @Test
    void ImageService_Save_PathIsNull() {
        imageService.save(mockFile, null);
    }

    @Test
    void ImageService_Save_PathIsEmpty() {
        imageService.save(mockFile, "");
    }

    @Test
    void ImageService_Save_WhenExceptionIsIOException() {
        try (MockedStatic<LogUtil> logUtilMock = mockStatic(LogUtil.class);
             MockedStatic<Files> filesMock = mockStatic(Files.class)) {
            IOException testIoException = new IOException("Test IOException");
            filesMock.when(() -> Files.copy(any(InputStream.class), any(Path.class))).thenThrow(testIoException);
            imageService.save(mockFile, "/uploads/test.txt");
            logUtilMock.verify(() -> LogUtil.logError("Error occurred while saving file to /uploads/test.txt", testIoException));
        }
    }


//    @Test
//    void deleteByPath_withExistingFile_shouldNotDeleteFile() throws IOException {
//        Path filePath = tempFolder.resolve("testFile.txt");
//        String relativePath = filePath.toString().replace(tempDir.toString(), "");
//        imageService.deleteByPath(relativePath);
//    }


    @Test
    void testGenerateFileName() {
        String generatedName = imageService.generateFileName(mockFile);
        assertNotNull(generatedName, "Generated file name should not be null");
        assertTrue(generatedName.matches("^[\\w-]+\\.test\\.jpg$"), "Generated file name should match expected pattern");
    }

    @Test
    void deleteByPath_withExistingFile_shouldDeleteFileAndLogSuccess() throws IOException {
        Path filePath = tempFolder.resolve("testFileToDelete.txt");
        Files.createFile(filePath);
        String relativePath = filePath.toString().replace(tempDir.toString(), "");

        try (MockedStatic<LogUtil> logUtilMock = mockStatic(LogUtil.class)) {
            imageService.deleteByPath(relativePath.substring(1));
            assertFalse(Files.exists(filePath), "Файл повинен бути видалений");

            logUtilMock.verify(() -> LogUtil.logInfo(contains("Deleted file")), times(1));
        }
    }

    @Test
    void deleteByPath_withExistingFile_shouldNotFoundPath() throws IOException {
        Path filePath = tempFolder.resolve("testFileToDelete.txt");
        try (MockedStatic<LogUtil> logUtilMock = mockStatic(LogUtil.class)) {
            imageService.deleteByPath("");
            assertFalse(Files.exists(filePath), "Файл повинен бути видалений");
        }
    }
    @Test
    void deleteByPath_withExistingFile_WhenPathIsNull() throws IOException {
        Path filePath = tempFolder.resolve("testFileToDelete.txt");
        try (MockedStatic<LogUtil> logUtilMock = mockStatic(LogUtil.class)) {
            imageService.deleteByPath(null);
            assertFalse(Files.exists(filePath), "Файл повинен бути видалений");
        }
    }

    @Test
    void deleteByPath_withExistingFolder_shouldDeleteFolderAndContents() throws IOException {
        Path folderPath = tempFolder.resolve("testFolderToDelete");
        Files.createDirectory(folderPath);
        Path fileInFolder = Files.createFile(folderPath.resolve("file.txt"));

        String relativePath = folderPath.toString().replace(tempDir.toString(), "");

        imageService.deleteByPath(relativePath.substring(1));
        assertFalse(Files.exists(folderPath), "Каталог повинен бути видалений");
        assertFalse(Files.exists(fileInFolder), "Файл у каталозі повинен бути видалений");
    }

    @Test
    void deleteByPath_withLockFile_shouldNotDelete() throws IOException {
        try (MockedStatic<LogUtil> logUtilMock = mockStatic(LogUtil.class)) {
            imageService.deleteByPath(tempFile.toString().substring(1));
            logUtilMock.verify(() -> LogUtil.logWarning(contains("Deletion blocked")), times(1));
        }
    }

    @SneakyThrows
    @Test
    void deleteByPath_withNonExistingFile_shouldLogWarning() {
        String nonExistentPath = "nonexistent.txt";
        try (MockedStatic<LogUtil> logUtilMock = mockStatic(LogUtil.class)) {
            imageService.deleteByPath(nonExistentPath);
            logUtilMock.verify(() -> LogUtil.logWarning(contains("Path not found")), times(1));
        }
    }

//    @Test
//    void deleteByPath_ShouldDeleteFileSuccessfully(@TempDir Path tempDir) throws IOException {
//        Path tempFile = tempDir.resolve("testFile.txt");
//        Files.createFile(tempFile);
//
//        try (MockedStatic<LogUtil> logUtilMock = mockStatic(LogUtil.class)) {
//            imageService.deleteByPath(tempFile.toString().replace(tempDir.toString(), ""));
////            logUtilMock.verify(() -> LogUtil.logInfo(contains("Attempting to delete file")), times(1));
////            logUtilMock.verify(() -> LogUtil.logInfo(contains("File deleted successfully")), times(1));
//        }
//    }
//
//    @Test
//    void deleteByPath_ShouldLogWarning_WhenFileDoesNotExist() throws IOException {
//        Path fakePath = Path.of("./non_existent_file.txt");
//
//        try (MockedStatic<LogUtil> logUtilMock = mockStatic(LogUtil.class)) {
//            imageService.deleteByPath(fakePath.toString().replace(".", ""));
//
//            assertFalse(Files.exists(fakePath)); // Файл не повинен існувати
//
//            // Логування має спрацювати
//            logUtilMock.verify(() -> LogUtil.logInfo(contains("Attempting to delete file")), times(1));
//            logUtilMock.verify(() -> LogUtil.logWarning(contains("File not found")), times(1));
//        }
//    }
}