package net.hustonline.account.web;

import net.hustonline.account.domain.PrivateBill;
import net.hustonline.account.domain.PublicBill;
import net.hustonline.account.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/bills")
@RestController
public class BillController {

    @Autowired
    private BillService billService;

    @GetMapping("/public/{billId}/")
    public PublicBill getPublicBill(@PathVariable Integer billId) {
        return billService.getPublicBill(billId);
    }

    @GetMapping("/private/{billId}/")
    public PrivateBill getPrivateBill(@PathVariable Integer billId) {
        return billService.getPrivateBill(billId);
    }

    @PutMapping("/public/{billId}/")
    public void updatePublicBill(@PathVariable Integer billId, @RequestBody PublicBill publicBill, @RequestHeader("3rd-session") String thirdSession) {
        publicBill.setId(billId);
        billService.updatePublicBill(publicBill);
    }

    @PutMapping("/private/{billId}/")
    public void updatePrivateBill(@PathVariable Integer billId, @RequestBody PrivateBill privateBill) {
        privateBill.setId(billId);
        billService.updatePrivateBill(privateBill);
    }

    @DeleteMapping("/public/{billId}/")
    public void deletePublicBill(@PathVariable Integer billId, @RequestHeader("3rd-session") String thirdSession) {
        billService.deletePublicBill(billId);
    }

    @DeleteMapping("/private/{billId}/")
    public void deletePrivateBill(@PathVariable Integer billId) {
        billService.deletePrivateBill(billId);
    }

}
