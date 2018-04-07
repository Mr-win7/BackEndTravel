package net.hustonline.account.web;

import com.fasterxml.jackson.annotation.JsonView;
import net.hustonline.account.domain.*;
import net.hustonline.account.service.AccountBookService;
import net.hustonline.account.service.BillService;
import net.hustonline.account.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@PropertySource(value = {"classpath:application.properties"},encoding="utf-8")
@RequestMapping("/api/v1/account_books")
@RestController
public class AccountBookController {

    @Autowired
    private UserService userService;

    @Autowired
    private AccountBookService accountBookService;

    @Autowired
    private BillService billService;

    @Value("#{'${categories}'.split(',')}")
    private List<String> categories;

    @JsonView(View.Summary.class)
    @GetMapping("/")
    public List<AccountBook> getAccountBooks(@RequestHeader("3rd-session") String thirdSession) {
        User user = userService.getUserByThirdSession(thirdSession);
        return accountBookService.getAccountBooksByUser(user);
    }

    @JsonView(View.AccountBook.class)
    @GetMapping("/{accountBookId}/")
    public AccountBook getAccountBook(@PathVariable Integer accountBookId, @RequestHeader("3rd-session") String thirdSession) {
        User user = userService.getUserByThirdSession(thirdSession);
        AccountBook accountBook = accountBookService.getAccountBook(accountBookId);
        accountBook.setPublicBills(billService.getPublicBillsByAccountBook(accountBook));
        accountBook.setPrivateBills(billService.getPrivateBillsByAccountBookAndUser(accountBook, user));
        accountBook.setParticipants(userService.getAccountBookParticipants(accountBook));
        return accountBook;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/")
    public AccountBook addAccountBook(@RequestBody AccountBook accountBook) {
        accountBookService.addAccountBook(accountBook);
        return accountBook;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{accountBookId}/bills/public/")
    public PublicBill addPublicBill(@RequestBody PublicBill publicBill, @PathVariable Integer accountBookId, @RequestHeader("3rd-session") String thirdSession) {
        publicBill.setAccountBookId(accountBookId);
        billService.addPublicBill(publicBill);
        return publicBill;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{accountBookId}/bills/private/")
    public PrivateBill addPrivateBill(@RequestBody PrivateBill privateBill, @PathVariable Integer accountBookId,  @RequestHeader("3rd-session") String thirdSession) {
        User user = userService.getUserByThirdSession(thirdSession);
        privateBill.setPayerId(user.getId());
        privateBill.setAccountBookId(accountBookId);
        billService.addPrivateBill(privateBill);
        return privateBill;
    }

    @PutMapping("/{accountBookId}/")
    public void updateAccountBook(@RequestBody AccountBook accountBook, @PathVariable Integer accountBookId, @RequestHeader("3rd-session") String thirdSession) {
        accountBook.setId(accountBookId);
        accountBookService.updateAccountBook(accountBook);
    }

    @DeleteMapping("/{accountBookId}/")
    public void deleteAccountBook(@PathVariable Integer accountBookId) {
        accountBookService.deleteAccountBook(accountBookId);
    }

    @GetMapping("/{accountBookId}/summary/")
    public Map<String, Integer> getSummary(@PathVariable Integer accountBookId, @RequestHeader("3rd-session") String thirdSession){
        AccountBook accountBook = accountBookService.getAccountBook(accountBookId);
        User user = userService.getUserByThirdSession(thirdSession);
        Map<String, Integer> privateBillSummary = billService.getPrivateBillsByAccountBookAndUser(accountBook, user).stream()
                .collect(Collectors.groupingBy(PrivateBill::getCategory, Collectors.summingInt(PrivateBill::getSum)));
        Map<String, Integer> publicBillSummary = billService.getPublicBillsByAccountBook(accountBook).stream()
                .collect(Collectors.groupingBy(
                        PublicBill::getCategory,
                        Collectors.summingInt((publicBill) -> publicBill.getSum() / publicBill.getParticipants().size())
                ));
        return Arrays.stream("餐饮,交通,住宿,购物,娱乐,订票,其他".split(",")).collect(Collectors.toMap(Function.identity(), (category) -> privateBillSummary.getOrDefault(category, 0) + publicBillSummary.getOrDefault(category, 0), (s, a) -> s + a));
    }

}
