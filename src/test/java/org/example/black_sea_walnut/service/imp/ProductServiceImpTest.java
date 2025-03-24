package org.example.black_sea_walnut.service.imp;

import jakarta.persistence.EntityNotFoundException;
import lombok.SneakyThrows;
import org.example.black_sea_walnut.dto.PageResponse;
import org.example.black_sea_walnut.dto.admin.historyPrice.HistoryResponsePricesForProduct;
import org.example.black_sea_walnut.dto.admin.product.ProductRequestForAdd;
import org.example.black_sea_walnut.dto.admin.product.ProductResponseForAdd;
import org.example.black_sea_walnut.dto.admin.product.ProductResponseForShopPage;
import org.example.black_sea_walnut.dto.admin.product.ProductResponseForViewInProducts;
import org.example.black_sea_walnut.dto.web.ProductResponseForViewInTable;
import org.example.black_sea_walnut.entity.HistoryPrices;
import org.example.black_sea_walnut.entity.Product;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.example.black_sea_walnut.mapper.ProductMapper;
import org.example.black_sea_walnut.repository.ProductRepository;
import org.example.black_sea_walnut.service.DiscountService;
import org.example.black_sea_walnut.service.HistoryPricesService;
import org.example.black_sea_walnut.service.TasteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImpTest {
    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductMapper mapper;
    @Mock
    private TasteService tasteService;
    @Mock
    private DiscountService discountService;
    @Mock
    private ImageServiceImp imageServiceImp;
    @Mock
    private HistoryPricesService historyPricesService;
    @Mock
    private MultipartFile multipartFile;

    @InjectMocks
    private ProductServiceImp productService;

    private Product product;
    private ProductRequestForAdd productRequest;
    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1L);
        product.setPathToImage1("/path/to/image1");
        product.setPathToImage2("/path/to/image2");
        product.setPathToImage3("/path/to/image3");
        product.setPathToImage4("/path/to/image4");
        product.setPathToImagePayment("/path/to/imagePayment");
        product.setPathToImageDelivery("/path/to/imageDelivery");
        product.setPathToImageDescription("/path/to/imageDescription");
        product.setPathToImagePacking("/path/to/imagePacking");

        productRequest = ProductRequestForAdd.builder().id(1L).build();
        productRequest.setPathToImage1("/path/to/image1");
        productRequest.setPathToImage2("/path/to/image2");
        productRequest.setPathToImage3("/path/to/image3");
        productRequest.setPathToImage4("/path/to/image4");
        productRequest.setPathToImagePayment("/path/to/imagePayment");
        productRequest.setPathToImageDelivery("/path/to/imageDelivery");
        productRequest.setPathToImageDescription("/path/to/imageDescription");
        productRequest.setPathToImagePacking("/path/to/imagePacking");
    }

    @Test
    void testGetAll() {
        when(productRepository.findAll()).thenReturn(List.of(product));
        List<Product> products = productService.getAll();
        assertEquals(1, products.size());
    }

    @Test
    void testGetById_ProductFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        Product foundProduct = productService.getById(1L);
        assertEquals(1L, foundProduct.getId());
    }

    @Test
    void testGetById_ProductNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> productService.getById(1L));
    }

    @Test
    void testSave_ProductEntity() {
        when(productRepository.save(any(Product.class))).thenReturn(product);
        Product savedProduct = productService.save(product);
        assertEquals(1L, savedProduct.getId());
    }

    @Test
    void testSave_ProductEntity_WhenException() {
        when(productRepository.findById(1L)).thenThrow(new RuntimeException("Exception"));
        assertThrows(RuntimeException.class, () -> productService.save(productRequest));
    }

    @Test
    void testSave_ProductEntity_WhereNewPriceIsNoNull() {
        productRequest.setId(1L);
        productRequest.setNewPrice(1);
        product.setId(1L);
        product.setHistoryPrices(new ArrayList<>(List.of(new HistoryPrices())));
        when(productService.save(product)).thenReturn(product);
        when(productRepository.findById(1L)).thenReturn(Optional.ofNullable(product));

        Product savedProduct = productService.save(productRequest);
        assertEquals(1L, savedProduct.getId());
    }

    @Test
    void testSave_ProductEntity_WhereIdIsNull() {
        productRequest.setId(null);
        when(mapper.toEntityForRequestAdd(productRequest)).thenReturn(product);
        when(productService.save(product)).thenReturn(product);
        Product savedProduct = productService.save(productRequest);
        assertEquals(1L, savedProduct.getId());
    }

    @Test
    void testDeleteById() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        doNothing().when(productRepository).deleteById(1L);
        assertDoesNotThrow(() -> productService.deleteById(1L));
        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteById_WhereFilesAreNull() {
        product.setPathToImage1(null);
        product.setPathToImage2(null);
        product.setPathToImage3(null);
        product.setPathToImage4(null);
        product.setPathToImagePayment(null);
        product.setPathToImageDelivery(null);
        product.setPathToImageDescription(null);
        product.setPathToImagePacking(null);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        doNothing().when(productRepository).deleteById(1L);
        assertDoesNotThrow(() -> productService.deleteById(1L));
        verify(productRepository, times(1)).deleteById(1L);
    }
    @Test
    void testDeleteById_WhereHappenedDeletingFiles() {
        product.setPathToImage1("");
        product.setPathToImage2("");
        product.setPathToImage3("");
        product.setPathToImage4("");
        product.setPathToImagePayment("");
        product.setPathToImageDelivery("");
        product.setPathToImageDescription("");
        product.setPathToImagePacking("");
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        doNothing().when(productRepository).deleteById(1L);
        assertDoesNotThrow(() -> productService.deleteById(1L));
        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    void testGetAllPaged() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> page = new PageImpl<>(List.of(product));
        Specification<Product> specification = Specification.where(null);
        when(productRepository.findAll(specification, pageable)).thenReturn(page);
        ProductResponseForViewInProducts dto = ProductResponseForViewInProducts.builder().build();
        when(mapper.toDTOForView(any(), any())).thenReturn(dto);
        PageResponse<ProductResponseForViewInProducts> response = productService.getAll(dto, pageable, LanguageCode.en);
        assertEquals(1, response.getContent().size());
    }

    @Test
    void testIncreaseCountItems() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        product.setTotalCount(5L);
        productService.increaseCountItems(1L);
        assertEquals(6, product.getTotalCount());
    }

    @Test
    void testDecreaseCountItems() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        product.setTotalCount(5L);
        productService.decreaseCountItems(1L);
        assertEquals(4, product.getTotalCount());
    }

    @Test
    void testDecreaseCountItems_WhereCountIs0() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        product.setTotalCount(0L);
        productService.decreaseCountItems(1L);
        assertEquals(0, product.getTotalCount());
    }

    @Test
    void getAllMasses() {
        productService.getAllMasses();
    }

    @Test
    void testGetAllProductsForShopPage() {
        Pageable pageable = PageRequest.of(0, 10);
        ProductResponseForShopPage response = ProductResponseForShopPage.builder().build();
        LanguageCode code = LanguageCode.en;
        Page<Product> page = new PageImpl<>(List.of(new Product()));
        Specification<Product> specification = Specification.where(null);
        when(productRepository.findAll(specification, pageable)).thenReturn(page);
        ProductResponseForViewInTable dto = ProductResponseForViewInTable.builder().build();
        when(mapper.toResponseForViewInProduction(any(), eq(code))).thenReturn(dto);

        PageResponse<ProductResponseForViewInTable> result = productService.getAll(response, pageable, code);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(productRepository).findAll(specification, pageable);
    }

    @Test
    void testSaveProductFromDto() {
        ProductRequestForAdd dto = ProductRequestForAdd.builder().build();
        dto.setId(1L);
        dto.setCurrentPrice(100);
        dto.setPathToImage1("/path/to/image1");
        dto.setPathToImage2("/path/to/image2");
        dto.setPathToImage3("/path/to/image3");
        dto.setPathToImage4("/path/to/image4");
        dto.setPathToImageDelivery("/path/to/imageDelivery");
        dto.setPathToImageDescription("/path/to/imageDescription");
        dto.setPathToImagePacking("/path/to/imagePacking");
        dto.setPathToImagePayment("/path/to/imagePayment");
        Product product = new Product();
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any())).thenReturn(product);

        Product result = productService.save(dto);

        assertNotNull(result);
        verify(productRepository).save(any());
    }

    @Test
    void testGetByArticleId() {
        Long articleId = 1L;
        Product product = new Product();
        when(productRepository.getByArticleId(articleId)).thenReturn(Optional.of(product));

        Product result = productService.getByArticleId(articleId);

        assertNotNull(result);
        verify(productRepository).getByArticleId(articleId);
    }

    @Test
    void testGetByArticleId_ProductNotFound() {
        Long articleId = 1L;

        when(productRepository.getByArticleId(articleId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                productService.getByArticleId(articleId)
        );

        assertEquals("Product with article id:" + articleId + " was not found!", exception.getMessage());
        verify(productRepository, times(1)).getByArticleId(articleId);
    }

    @Test
    void testGetByIdLikeDTOAdd() {
        Long id = 1L;
        Product product = new Product();
        product.setId(id);
        ProductResponseForAdd responseForAdd = ProductResponseForAdd.builder().build();
        when(productRepository.findById(id)).thenReturn(Optional.of(product));
        when(mapper.toResponseForAdd(product)).thenReturn(responseForAdd);
        when(historyPricesService.getLatestPriceByProductIdInDtoForProduct(id)).thenReturn(HistoryResponsePricesForProduct.builder().build());

        ProductResponseForAdd result = productService.getByIdLikeDTOAdd(id);

        assertNotNull(result);
        verify(productRepository).findById(id);
    }

    @Test
    void testIsExistByArticleId() {
        Long id = 1L;
        when(productRepository.existsByArticleId(id)).thenReturn(true);

        boolean result = productService.isExistByArticleId(id);

        assertTrue(result);
        verify(productRepository).existsByArticleId(id);
    }

    @Test
    void testIsExistById() {
        Long id = 1L;
        when(productRepository.existsById(id)).thenReturn(true);

        boolean result = productService.isExistById(id);

        assertTrue(result);
        verify(productRepository).existsById(id);
    }


    @Test
    void getRandomProductsBySize_ShouldReturnMappedProducts() {
        int size = 2;
        LanguageCode code = LanguageCode.en;
        Product product1 = new Product();
        Product product2 = new Product();
        List<Product> mockProducts = List.of(product1, product2);
        ProductResponseForViewInTable response1 = ProductResponseForViewInTable.builder().build();
        ProductResponseForViewInTable response2 = ProductResponseForViewInTable.builder().build();
        List<ProductResponseForViewInTable> mockResponses = List.of(response1, response2);

        when(productRepository.findRandomProducts(size)).thenReturn(mockProducts);
        when(historyPricesService.getLastTwoDataByProduct(any(Product.class))).thenReturn(List.of());
        when(mapper.toResponseForViewInMain(any(Product.class), eq(code)))
                .thenReturn(mockResponses.get(0), mockResponses.get(1));

        List<ProductResponseForViewInTable> result = productService.getRandomProductsBySize(size, code);

        assertEquals(mockResponses.size(), result.size());
        verify(productRepository, times(1)).findRandomProducts(size);
        verify(mapper, times(2)).toResponseForViewInMain(any(Product.class), eq(code));
    }

    @Test
    void testProcessImage_WithValidImage() {
        String imagePath = "";
        String fieldName = "pathToImage1";
        Consumer<String> pathSetter = mock(Consumer.class);
        when(multipartFile.isEmpty()).thenReturn(false);
        when(imageServiceImp.generateFileName(multipartFile)).thenReturn("generatedFileName.jpg");

        productService.processImage(multipartFile, imagePath, pathSetter, product, fieldName);

        verify(pathSetter).accept(contains("/products/image/generatedFileName.jpg"));
    }

    @Test
    void testProcessImage_WithEmptyImage() {
        String imagePath = "";
        String fieldName = "pathToImage1";
        Consumer<String> pathSetter = mock(Consumer.class);
        when(multipartFile.isEmpty()).thenReturn(true);

        productService.processImage(multipartFile, imagePath, pathSetter, product, fieldName);

        verify(pathSetter, never()).accept(anyString());
    }

    @Test
    void testProcessImage_WithExistingImagePath() {
        String imagePath = "existing/path.jpg";
        String fieldName = "pathToImage1";
        Consumer<String> pathSetter = mock(Consumer.class);
        when(multipartFile.isEmpty()).thenReturn(false);

        productService.processImage(multipartFile, imagePath, pathSetter, product, fieldName);
    }

    @Test
    void testProcessImage_WithEmptyImagePathAndExistingProduct() {
        String imagePath = "";
        String fieldName = "pathToImage1";
        Consumer<String> pathSetter = mock(Consumer.class);
        when(multipartFile.isEmpty()).thenReturn(false);

        productService.processImage(multipartFile, imagePath, pathSetter, product, fieldName);

        verify(imageServiceImp, times(1)).generateFileName(multipartFile);
        verify(pathSetter, times(1)).accept(contains("/products/image/"));
    }

    @Test
    void testProcessImage_WithEmptyImagePathAndNoExistingProduct() {
        String imagePath = "";
        String fieldName = "pathToImage1";
        Consumer<String> pathSetter = mock(Consumer.class);
        when(multipartFile.isEmpty()).thenReturn(false);

        productService.processImage(multipartFile, imagePath, pathSetter, null, fieldName);

        verify(imageServiceImp, times(1)).generateFileName(multipartFile);
        verify(pathSetter, times(1)).accept(contains("/products/image/"));
    }
}