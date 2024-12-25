package org.example.black_sea_walnut.service;

import org.example.black_sea_walnut.dto.ResponseOrderDetailForView;

import java.util.List;

public interface OrderDetailService {
        List<ResponseOrderDetailForView> getById(Long id);
}
