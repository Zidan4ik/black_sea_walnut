package org.example.black_sea_walnut.util;

import org.example.black_sea_walnut.entity.Product;
import org.example.black_sea_walnut.enums.MediaType;
import org.example.black_sea_walnut.service.ImageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Field;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImageUtilTest {
    @Mock
    private ImageService imageService;

    @InjectMocks
    private ImageUtil imageUtil;

    @Test
    void shouldReturnImageMediaTypeWhenFileIsImage() {
        MultipartFile file = new MockMultipartFile("file", "image.png", "image/png", new byte[0]);
        MediaType mediaType = ImageUtil.getMediaType(file);
        assert mediaType == MediaType.image;
    }

    @Test
    void shouldReturnImageMediaTypeWhenFileIsImage_WhenFileIsNull() {
        ImageUtil.getMediaType(null);
    }

    @Test
    void shouldReturnVideoMediaTypeWhenFileIsVideo() {
        MultipartFile file = new MockMultipartFile("file", "video.mp4", "video/mp4", new byte[0]);
        MediaType mediaType = ImageUtil.getMediaType(file);
        assert mediaType == MediaType.video;
    }

    @Test
    void shouldReturnNullWhenFileIsNotImageOrVideo() {
        MultipartFile file = new MockMultipartFile("file", "document.pdf", "application/pdf", new byte[0]);
        MediaType mediaType = ImageUtil.getMediaType(file);
        assert mediaType == null;
    }

    @Test
    void shouldDeleteImageIfEmpty() throws NoSuchFieldException, IllegalAccessException, IOException {
        Product product = new Product();
        product.setPathToImage1("path/to/image.png");
        Field field = Product.class.getDeclaredField("pathToImage1");
        field.setAccessible(true);
        field.set(product, "path/to/image.png");
        ImageUtil.deleteImageIfEmpty(product, "pathToImage1", imageService);
        verify(imageService, times(1)).deleteByPath("path/to/image.png");
    }

    @Test
    void shouldNotDeleteImageIfPathIsNull() throws NoSuchFieldException, IllegalAccessException, IOException {
        Product product = new Product();
        Field field = Product.class.getDeclaredField("pathToImage1");
        field.setAccessible(true);
        field.set(product, null);
        ImageUtil.deleteImageIfEmpty(product, "pathToImage1", imageService);
        verify(imageService, never()).deleteByPath(anyString());
    }

    @Test
    void shouldHandleExceptionWhenDeletingImage() throws NoSuchFieldException, IllegalAccessException, IOException {
        Product product = new Product();
        product.setPathToImage1("path/to/image.png");
        try {
            ImageUtil.deleteImageIfEmpty(product, "imagePath", imageService);
        } catch (RuntimeException e) {
            assert e.getMessage().equals("Error accessing image path field: imagePath");
        }
    }
}