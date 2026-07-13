package org.example.black_sea_walnut.service.document.excel;

import org.example.black_sea_walnut.entity.Taste;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.List;

@Service
public class TasteExcelExporter implements ExcelExporter<Taste> {
    public ByteArrayInputStream exportTastes(List<Taste> tastes) {
        return exportToExcel("Tastes List", Taste.class, tastes, null);
    }
}
