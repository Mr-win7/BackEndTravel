package net.hustonline.account.service;

import net.hustonline.account.domain.*;
import net.hustonline.account.mapper.PrivateBillMapper;
import net.hustonline.account.mapper.PublicBillMapper;
import net.hustonline.account.mapper.PublicBillParticipantMapper;
import net.hustonline.account.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BillService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PublicBillMapper publicBillMapper;

    @Autowired
    private PrivateBillMapper privateBillMapper;

    @Autowired
    private PublicBillParticipantMapper publicBillParticipantMapper;

    public List<PublicBill> getPublicBillsByAccountBook(AccountBook accountBook) {
        PublicBillExample publicBillExample = new PublicBillExample();
        publicBillExample.or().andAccountBookIdEqualTo(accountBook.getId());
        List<PublicBill> publicBills = publicBillMapper.selectByExample(publicBillExample);
        publicBills.forEach(publicBill -> {
            PublicBillParticipantExample publicBillParticipantExample = new PublicBillParticipantExample();
            publicBillParticipantExample.or().andPublicBillIdEqualTo(publicBill.getId());
            publicBill.setParticipants(publicBillParticipantMapper.selectByExample(publicBillParticipantExample).stream()
                    .map(publicBillParticipant -> {
                        User user = new User();
                        user.setId(publicBillParticipant.getUserId());
                        return user;
                    })
                    .collect(Collectors.toList())
            );
        });
        return publicBills;
    }

    public List<PrivateBill> getPrivateBillsByAccountBookAndUser(AccountBook accountBook, User user) {
        PrivateBillExample privateBillExample = new PrivateBillExample();
        privateBillExample.or().andAccountBookIdEqualTo(accountBook.getId()).andPayerIdEqualTo(user.getId());
        return privateBillMapper.selectByExample(privateBillExample);
    }

    public PublicBill getPublicBill(Integer id) {
        PublicBill publicBill = publicBillMapper.selectByPrimaryKey(id);
        PublicBillParticipantExample publicBillParticipantExample = new PublicBillParticipantExample();
        publicBillParticipantExample.or().andPublicBillIdEqualTo(id);
        publicBill.setParticipants(
                publicBillParticipantMapper.selectByExample(publicBillParticipantExample).stream()
                .map(x ->userMapper.selectByPrimaryKey(x.getUserId()))
                .collect(Collectors.toList())
        );
        return publicBill;
    }

    public void addPublicBill(PublicBill publicBill) {
        publicBillMapper.insertSelective(publicBill);
        List<User> users = publicBill.getParticipants();
        users.stream()
                .map(x -> new PublicBillParticipant()
                        .setUserId(x.getId())
                        .setPublicBillId(publicBill.getId()))
                .forEach(publicBillParticipantMapper::insertSelective);
    }

    public void updatePublicBill(PublicBill publicBill) {
        publicBillMapper.updateByPrimaryKeySelective(publicBill);
        if (publicBill.getParticipants() != null) {
            PublicBillParticipantExample publicBillParticipantExample = new PublicBillParticipantExample();
            publicBillParticipantExample.or().andPublicBillIdEqualTo(publicBill.getId());
            List<PublicBillParticipant> publicBillParticipantsOld = publicBillParticipantMapper.selectByExample(publicBillParticipantExample);
            List<PublicBillParticipant> publicBillParticipantsNew = publicBill.getParticipants().stream()
                    .map(x -> new PublicBillParticipant()
                            .setUserId(x.getId())
                            .setPublicBillId(publicBill.getId()))
                    .collect(Collectors.toList());
            publicBillParticipantsOld.stream()
                    .filter(x -> !publicBillParticipantsNew.contains(x))
                    .map(x -> x.getId())
                    .forEach(publicBillParticipantMapper::deleteByPrimaryKey);
            publicBillParticipantsNew.stream()
                    .filter(x -> !publicBillParticipantsOld.contains(x))
                    .forEach(publicBillParticipantMapper::insert);
        }

    }

    public void deletePublicBill(Integer id) {
        publicBillMapper.deleteByPrimaryKey(id);
    }

    public PrivateBill getPrivateBill(Integer id) {
        return privateBillMapper.selectByPrimaryKey(id);
    }

    public void addPrivateBill(PrivateBill privateBill) {
        privateBillMapper.insertSelective(privateBill);
    }

    public void updatePrivateBill(PrivateBill privateBill) {
        privateBillMapper.updateByPrimaryKeySelective(privateBill);
    }

    public void deletePrivateBill(Integer id) {
        privateBillMapper.deleteByPrimaryKey(id);
    }


}
