package org.example.black_sea_walnut.service.history;

import org.example.black_sea_walnut.dto.admin.historyMedia.HistoryMediaRequestForAdd;
import org.example.black_sea_walnut.entity.History;
import org.example.black_sea_walnut.service.Uploadable;
import org.example.black_sea_walnut.service.user.Saveable;

import java.util.List;

public interface HistoryFileRequest<M> extends Saveable<History,M>, Uploadable {
    List<HistoryMediaRequestForAdd> getFiles();
}