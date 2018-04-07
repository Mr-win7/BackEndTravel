package net.hustonline.account.service;

import net.hustonline.account.domain.*;
import net.hustonline.account.mapper.AccountBookMapper;
import net.hustonline.account.mapper.AccountBookParticipantMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountBookService {

    @Autowired
    private AccountBookParticipantMapper accountBookParticipantMapper;

    @Autowired
    private AccountBookMapper accountBookMapper;

    public List<AccountBook> getAccountBooksByUser(User user) {
        AccountBookParticipantExample accountBookParticipantExample = new AccountBookParticipantExample();
        accountBookParticipantExample.or().andUserIdEqualTo(user.getId());
        List<AccountBookParticipant> accountBookParticipants = accountBookParticipantMapper.selectByExample(accountBookParticipantExample);
        return accountBookParticipants.stream()
                .map(x -> x.getAccountBookId())
                .map(accountBookMapper::selectByPrimaryKey)
                .sorted(Comparator.comparing(AccountBook::getTime))
                .collect(Collectors.toList());
    }

    public AccountBook getAccountBook(Integer id) {
        return  accountBookMapper.selectByPrimaryKey(id);
    }

    public void addAccountBook(AccountBook accountBook) {
        accountBookMapper.insertSelective(accountBook);
    }

    public void updateAccountBook(AccountBook accountBook) {
        accountBookMapper.updateByPrimaryKeySelective(accountBook);
    }

    public void deleteAccountBook(Integer id) {
        accountBookMapper.deleteByPrimaryKey(id);
    }

}
