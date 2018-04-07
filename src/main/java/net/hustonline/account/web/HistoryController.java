package net.hustonline.account.web;

import net.hustonline.account.domain.History;
import net.hustonline.account.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/histories")
@RestController
public class HistoryController {

    @Autowired
    private HistoryService historyService;

    @GetMapping("/account_book/{accountBookId}")
    public List<History> getHistories(@PathVariable Integer accountBookId) {
        return historyService.getHistoriesByAccountBook(accountBookId);
    }

}
