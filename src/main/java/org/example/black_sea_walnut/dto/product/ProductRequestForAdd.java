package org.example.black_sea_walnut.dto.product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.example.black_sea_walnut.dto.HistoryRequestPricesForProduct;
import org.example.black_sea_walnut.validator.IsExistProductValidation;
import org.example.black_sea_walnut.validator.MediaValidation;
import org.example.black_sea_walnut.validator.NumberNullValidation;
import org.springframework.web.multipart.MultipartFile;


@Builder
@Getter
@Setter
@IsExistProductValidation
public class ProductRequestForAdd {
    private Long id;
    @NumberNullValidation(message = "The article id should be present!")
    @Min(value = 0, message = "{error.field.valid.min.value}")
    private Long articleId;
    private Boolean isActive;
    @Min(value = 0, message = "{error.field.valid.min.value}")
    private Long amount;
    @NotBlank(message = "{error.field.empty}")
    private String nameUk;
    @NotBlank(message = "{error.field.empty}")
    private String nameEn;
    @NumberNullValidation
    @Min(value = 0, message = "{error.field.valid.min.value}")
    private Long energyMass;
    @NumberNullValidation
    @Min(value = 0, message = "{error.field.valid.min.value}")
    private Long mass;
    @Size(max = 500, message = "{error.field.valid.size}")
    private String recipeUk;
    @Size(max = 500, message = "{error.field.valid.size}")
    private String recipeEn;
    @Size(max = 500, message = "{error.field.valid.size}")
    private String conditionExploitationUk;
    @Size(max = 500, message = "{error.field.valid.size}")
    private String conditionExploitationEn;
    @Size(max = 2000, message = "{error.field.valid.size}")
    private String descriptionProductUk;
    @Size(max = 2000, message = "{error.field.valid.size}")
    private String descriptionProductEn;
    @Size(max = 2000, message = "{error.field.valid.size}")
    private String descriptionPackingUk;
    @Size(max = 2000, message = "{error.field.valid.size}")
    private String descriptionPackingEn;
    @Size(max = 2000, message = "{error.field.valid.size}")
    private String descriptionPaymentUk;
    @Size(max = 2000, message = "{error.field.valid.size}")
    private String descriptionPaymentEn;
    @Size(max = 2000, message = "{error.field.valid.size}")
    private String descriptionDeliveryUk;
    @Size(max = 2000, message = "{error.field.valid.size}")
    private String descriptionDeliveryEn;
    private String pathToImage1;
    private String pathToImage2;
    private String pathToImage3;
    private String pathToImage4;
    private String pathToImageDescription;
    private String pathToImagePacking;
    private String pathToImagePayment;
    private String pathToImageDelivery;
    @MediaValidation(allowedTypes = {"image/png", "image/jpg", "image/jpeg"})
    private MultipartFile image1;
    @MediaValidation(allowedTypes = {"image/png", "image/jpg", "image/jpeg"})
    private MultipartFile image2;
    @MediaValidation(allowedTypes = {"image/png", "image/jpg", "image/jpeg"})
    private MultipartFile image3;
    @MediaValidation(allowedTypes = {"image/png", "image/jpg", "image/jpeg"})
    private MultipartFile image4;
    @MediaValidation(allowedTypes = {"image/png", "image/jpg", "image/jpeg"})
    private MultipartFile imageDescription;
    @MediaValidation(allowedTypes = {"image/png", "image/jpg", "image/jpeg"})
    private MultipartFile imagePayment;
    @MediaValidation(allowedTypes = {"image/png", "image/jpg", "image/jpeg"})
    private MultipartFile imagePacking;
    @MediaValidation(allowedTypes = {"image/png", "image/jpg", "image/jpeg"})
    private MultipartFile imageDelivery;
    private String prices;
    private Long tasteId;
    private Long discountId;
}