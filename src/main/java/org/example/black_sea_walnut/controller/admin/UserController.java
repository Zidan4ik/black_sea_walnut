package org.example.black_sea_walnut.controller.admin;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.language.bm.Lang;
import org.example.black_sea_walnut.dto.PageResponse;

import org.example.black_sea_walnut.dto.admin.order.OrderUserResponseForView;
import org.example.black_sea_walnut.dto.admin.order.ResponseOrderForView;
import org.example.black_sea_walnut.dto.admin.user.UserResponseForView;
import org.example.black_sea_walnut.dto.admin.user.request.UserFopRequestForView;
import org.example.black_sea_walnut.dto.admin.user.request.UserIndividualRequestForAdd;
import org.example.black_sea_walnut.dto.admin.user.request.UserLegalRequestForView;
import org.example.black_sea_walnut.dto.admin.user.response.UserFopResponseForAdd;
import org.example.black_sea_walnut.dto.admin.user.response.UserIndividualResponseForAdd;
import org.example.black_sea_walnut.dto.admin.user.response.UserLegalResponseForView;
import org.example.black_sea_walnut.entity.User;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.example.black_sea_walnut.enums.RegisterType;
import org.example.black_sea_walnut.enums.UserStatus;
import org.example.black_sea_walnut.mapper.UserMapper;
import org.example.black_sea_walnut.service.OrderService;
import org.example.black_sea_walnut.service.UserService;
import org.example.black_sea_walnut.validator.groupValidation.EmailValidGroups;
import org.example.black_sea_walnut.validator.groupValidation.OrderedEmailValidation;
import org.example.black_sea_walnut.validator.groupValidation.OrderedPhoneValidation;
import org.example.black_sea_walnut.validator.groupValidation.PhoneValidGroups;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class UserController {
    private final UserService userService;
    private final OrderService orderService;
    private final UserMapper userMapper;

    @GetMapping("/users")
    public ModelAndView getUsers() {
        return new ModelAndView("admin/users/users");
    }

    @GetMapping("/users/table/load")
    public ModelAndView loadTable(@ModelAttribute UserResponseForView userResponseForView,
                                  @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "5") int size,
                                  @RequestParam String languageCode) {
        ModelAndView model = new ModelAndView("admin/fragments/table-users");
        PageRequest pageable = PageRequest.of(page, size);
        PageResponse<UserResponseForView> pageResponse = userService.getAll(userResponseForView, pageable);
        model.addObject("data", pageResponse.getContent());
        return model;
    }

    @GetMapping("/users/pagination/load")
    public ModelAndView loadPagination(@ModelAttribute UserResponseForView userResponseForView,
                                       @RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "5") int size,
                                       @RequestParam String languageCode) {
        ModelAndView model = new ModelAndView("admin/fragments/pagination");
        PageRequest pageable = PageRequest.of(page, size);
        PageResponse<UserResponseForView> pageResponse = userService.getAll(userResponseForView, pageable);
        model.addObject("pageData", pageResponse.getMetadata());
        return model;
    }

    @GetMapping("/user/{id}/edit")
    public ModelAndView viewUser(@PathVariable Long id) {
        ModelAndView model = new ModelAndView("admin/users/user-edit");
        User userById = userService.getById(id);
        model.addObject("id", id);
        model.addObject("userStatus", userById.getStatus().toString());
        return model;
    }

    @GetMapping("/user/{id}/orders/table/load")
    public ModelAndView loadOrdersTable(@PathVariable Long id,
                                        @ModelAttribute OrderUserResponseForView responseOrder,
                                        @RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "5") int size,
                                        @RequestParam String languageCode) {
        ModelAndView model = new ModelAndView("admin/fragments/table-user-orders");
        PageRequest pageRequest = PageRequest.of(page, size);
        PageResponse<OrderUserResponseForView> pageResponse = orderService.getAllByUser(id, responseOrder, pageRequest, LanguageCode.fromString(languageCode));
        model.addObject("data", pageResponse.getContent());
        return model;
    }

    @GetMapping("/user/{id}/pagination/load")
    public ModelAndView loadPagination(@PathVariable Long id,
                                       @ModelAttribute OrderUserResponseForView responseOrder,
                                       @RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "5") int size,
                                       @RequestParam String languageCode) {
        ModelAndView model = new ModelAndView("admin/fragments/pagination");
        PageRequest pageable = PageRequest.of(page, size);
        PageResponse<OrderUserResponseForView> pageResponse = orderService.getAllByUser(id, responseOrder, pageable, LanguageCode.fromString(languageCode));
        model.addObject("pageData", pageResponse.getMetadata());
        return model;
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        User userById = userService.getById(id);
        if (userById.getRegisterType().equals(RegisterType.fop)) {
            UserFopResponseForAdd responseForUserFopAdd = userMapper.toResponseForUserFopAdd(userById);
            return new ResponseEntity(responseForUserFopAdd, HttpStatus.OK);
        } else if (userById.getRegisterType().equals(RegisterType.individual)) {
            UserIndividualResponseForAdd responseForUserIndividualAdd = userMapper.toResponseForUserIndividualAdd(userById);
            return new ResponseEntity(responseForUserIndividualAdd, HttpStatus.OK);
        } else if (userById.getRegisterType().equals(RegisterType.legal)) {
            UserLegalResponseForView responseForUserLegalAdd = userMapper.toResponseForUserLegalAdd(userById);
            return new ResponseEntity<>(responseForUserLegalAdd, HttpStatus.OK);
        }
        return new ResponseEntity<>("User with id:" + id + " doesn't have responsive type of registration!", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/user-individual/save")
    public ResponseEntity<?> saveUserIndividual(@Validated({OrderedPhoneValidation.class, OrderedEmailValidation.class})
                                                UserIndividualRequestForAdd dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

            return ResponseEntity
                    .status(HttpStatus.valueOf(400))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(errors);
        }
        userService.save(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/user-fop/save")
    public ResponseEntity<?> saveUserFop(@Validated({OrderedPhoneValidation.class, OrderedEmailValidation.class})
                                         UserFopRequestForView dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

            return ResponseEntity
                    .status(HttpStatus.valueOf(400))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(errors);
        }
        userService.save(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/user-legal/save")
    public ResponseEntity<?> saveUserLegal(@Validated({OrderedPhoneValidation.class, OrderedEmailValidation.class})
                                           UserLegalRequestForView dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity
                    .status(HttpStatus.valueOf(400))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(errors);
        }
        userService.save(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/user/{id}/change")
    public ResponseEntity<?> changeStatusUser(@PathVariable Long id) {
        User userById = userService.getById(id);
        UserStatus status = userById.getStatus();
        if (status.equals(UserStatus.isActive))
            userById.setStatus(UserStatus.isDeleted);
        else if (status.equals(UserStatus.isDeleted))
            userById.setStatus(UserStatus.isActive);
        userService.save(userById);
        return new ResponseEntity<>("User with id:" + id + " was changed!", HttpStatus.OK);
    }

    @DeleteMapping("/user/delete")
    public ResponseEntity<?> deleteUserModal(@RequestBody Long id) {
        try {
            userService.deleteUserById(id);
            return ResponseEntity.ok().body("User with id: " + id + " was deleted!");
        } catch (SecurityException ex){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
        } catch (EntityNotFoundException ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @ModelAttribute("isActiveUsers")
    public boolean toActiveSidebarButton() {
        return true;
    }
}
