package net.hustonline.account.service;

import net.hustonline.account.domain.*;
import net.hustonline.account.mapper.AccountBookParticipantMapper;
import net.hustonline.account.mapper.UserMapper;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AccountBookParticipantMapper accountBookParticipantMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public void addUser(User user) {
        userMapper.insertSelective(user);
    }

    public void updateUser(User user) {
        userMapper.updateByPrimaryKeySelective(user);
    }

    public User getUser(Integer id) {
        return userMapper.selectByPrimaryKey(id);
    }

    public User getUserByOpenId(String userId) {
        UserExample userExample = new UserExample();
        userExample.or().andOpenIdEqualTo(userId);
        List<User> users = userMapper.selectByExample(userExample);
        return users.isEmpty() ? null : users.get(0);
    }

    public String generateThirdSession() throws NoSuchAlgorithmException {
        byte[] bytes = new byte[16];
        SecureRandom.getInstance("SHA1PRNG").nextBytes(bytes);
        return Hex.encodeHexString(bytes);
    }

    public Session getSessionByThirdSession(String thirdSession) {
        HashOperations<String, Object, Object> hashOperations = stringRedisTemplate.opsForHash();
        Session session = new Session();
        session.setOpenid((String) hashOperations.get("account_session_openid", thirdSession));
        session.setSessionKey((String) hashOperations.get("account_session_sessionkey", thirdSession));
        return session;
    }

    public List<User> getAccountBookParticipants(AccountBook accountBook) {
        AccountBookParticipantExample accountBookParticipantExample = new AccountBookParticipantExample();
        accountBookParticipantExample.or().andAccountBookIdEqualTo(accountBook.getId());
        List<AccountBookParticipant> accountBookParticipants = accountBookParticipantMapper.selectByExample(accountBookParticipantExample);
        return accountBookParticipants.stream()
                .map(x -> x.getUserId())
                .map(userMapper::selectByPrimaryKey)
                .collect(Collectors.toList());
    }

    public void addAccountBookParticipant(AccountBookParticipant accountBookParticipant) {
        accountBookParticipantMapper.insertSelective(accountBookParticipant);
    }

    public User getUserByThirdSession(String thirdSession) {
        Session session = getSessionByThirdSession(thirdSession);
        return getUserByOpenId(session.getOpenid());
    }
}
