package org.example.black_sea_walnut.controller;

import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.dto.order.OrderResponseForStatsProducts;
import org.example.black_sea_walnut.dto.stats.UserResponseForStats;
import org.example.black_sea_walnut.entity.User;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.example.black_sea_walnut.service.OrderService;
import org.example.black_sea_walnut.service.UserService;
import org.example.black_sea_walnut.util.DateUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class StatsController {
    private final OrderService orderService;
    private final UserService userService;

    @GetMapping("/statistic")
    public ModelAndView showStats() {
        return new ModelAndView("admin/stats/stats");
    }

    @GetMapping("/orders/getByDate")
    public ResponseEntity<?> getOrdersByDate(@RequestParam("date") String date) {
        YearMonth yearMonth = YearMonth.from(DateUtil.toFormatDateToDB(date, "dd.MM.yyyy"));
        LocalDate firstDay = yearMonth.atDay(1);
        LocalDate lastDay = yearMonth.atEndOfMonth();
        List<Map<String, Object>> ordersCountByDate = orderService.getOrdersCountByDate(firstDay, lastDay);
        return new ResponseEntity<>(ordersCountByDate, HttpStatus.OK);
    }

    @GetMapping("/stats/products/get")
    public ResponseEntity<?> getProductBySizeAndDate(@RequestParam("date") String date,
                                                     @RequestParam("size") String size,
                                                     @RequestParam("languageCode") LanguageCode code) {
        YearMonth yearMonth = YearMonth.from(DateUtil.toFormatDateToDB(date, "dd.MM.yyyy"));
        LocalDate firstDay = yearMonth.atDay(1);
        LocalDate lastDay = yearMonth.atEndOfMonth();
        List<OrderResponseForStatsProducts> data = orderService.getTopProductBySalesInMonth(firstDay, lastDay, Integer.parseInt(size), code);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @GetMapping("/stats/users/get")
    public ResponseEntity<?> getUsersByDate(@RequestParam("date") String date) {
        YearMonth yearMonth = YearMonth.from(DateUtil.toFormatDateToDB(date, "dd.MM.yyyy"));
        LocalDate firstDay = yearMonth.atDay(1);
        LocalDate lastDay = yearMonth.atEndOfMonth();
        List<UserResponseForStats> response = userService.getUsersByDate(firstDay, lastDay);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
