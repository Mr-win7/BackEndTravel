package net.hustonline.account.service;

import net.hustonline.account.domain.AccountBook;
import net.hustonline.account.domain.History;
import net.hustonline.account.domain.HistoryExample;
import net.hustonline.account.mapper.HistoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistoryService {

    @Autowired
    private HistoryMapper historyMapper;

    public List<History> getHistoriesByAccountBook(Integer accountBookId) {
        HistoryExample historyExample = new HistoryExample();
        historyExample.or().andAccountBookIdEqualTo(accountBookId);
        return historyMapper.selectByExample(historyExample);
    }

    public void addHistory(History history) {
        historyMapper.insertSelective(history);
    }

}
