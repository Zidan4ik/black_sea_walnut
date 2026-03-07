package org.example.black_sea_walnut.service.history;

import org.example.black_sea_walnut.dto.admin.historyMedia.HistoryMediaRequestForAdd;


import java.util.List;

public interface HistoryFileRequest {
    List<HistoryMediaRequestForAdd> getFiles();
}