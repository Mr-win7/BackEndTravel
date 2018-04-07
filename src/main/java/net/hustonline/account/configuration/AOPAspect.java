package net.hustonline.account.configuration;

import net.hustonline.account.domain.AccountBook;
import net.hustonline.account.domain.History;
import net.hustonline.account.domain.PublicBill;
import net.hustonline.account.domain.User;
import net.hustonline.account.service.BillService;
import net.hustonline.account.service.HistoryService;
import net.hustonline.account.service.UserService;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Date;

@Aspect
@Configuration
public class AOPAspect {

    @Autowired
    private HistoryService historyService;

    @Autowired
    private UserService userService;

    @Autowired
    private BillService billService;

    @After("execution(public * net.hustonline.account.web.AccountBookController.addPublicBill(..)) && " +
            "args(publicBill, .., thirdSession)")
    public void addPublicBillHistory(PublicBill publicBill, String thirdSession) {
        History history = new History();
        User user = userService.getUserByThirdSession(thirdSession);
        history.setAccountBookId(publicBill.getAccountBookId());
        history.setCategory(publicBill.getCategory());
        history.setOperation("新添加了一笔账单");
        history.setOperatorId(user.getId());
        history.setSum(publicBill.getSum());
        history.setTime(new Date());
        historyService.addHistory(history);
    }

    @Before("execution(public * net.hustonline.account.web.BillController.updatePublicBill(..)) && " +
            "args(billId, .., thirdSession)")
    public void updatePublicBillHistory(Integer billId, String thirdSession) {
        History history = new History();
        PublicBill publicBill = billService.getPublicBill(billId);
        User user = userService.getUserByThirdSession(thirdSession);
        history.setAccountBookId(publicBill.getAccountBookId());
        history.setCategory(publicBill.getCategory());
        history.setOperation("修改了一笔账单");
        history.setOperatorId(user.getId());
        history.setSum(publicBill.getSum());
        history.setTime(new Date());
        historyService.addHistory(history);
    }

    @Before("execution(public * net.hustonline.account.web.BillController.deletePublicBill(..)) && " +
            "args(.., billId, thirdSession)")
    public void deletePublicBillHistory(Integer billId, String thirdSession) {
        PublicBill publicBill = billService.getPublicBill(billId);
        History history = new History();
        User user = userService.getUserByThirdSession(thirdSession);
        history.setAccountBookId(publicBill.getAccountBookId());
        history.setCategory(publicBill.getCategory());
        history.setOperation("删除了一笔账单");
        history.setOperatorId(user.getId());
        history.setSum(publicBill.getSum());
        history.setTime(new Date());
        historyService.addHistory(history);
    }

    @Before("execution(public * net.hustonline.account.web.AccountBookController.updateAccountBook(..)) && " +
            "args(.., accountBookId, thirdSession)")
    public void updateAccountBookHistory(Integer accountBookId, String thirdSession) {
        History history = new History();
        User user = userService.getUserByThirdSession(thirdSession);
        history.setAccountBookId(accountBookId);
        history.setOperatorId(user.getId());
        history.setOperation("修改了账本");
        history.setTime(new Date());
        historyService.addHistory(history);
    }
}
