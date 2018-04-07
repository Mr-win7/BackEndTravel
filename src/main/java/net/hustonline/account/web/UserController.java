package net.hustonline.account.web;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import net.hustonline.account.domain.AccountBookParticipant;
import net.hustonline.account.domain.Session;
import net.hustonline.account.domain.User;
import net.hustonline.account.service.UserService;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Decoder;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RequestMapping("/api/v1/users")
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Value("${appid}")
    private String appid;

    @Value("${appsecret}")
    private String appsecret;

    @Value("${base-path}")
    private String bathPath;

    @ResponseBody
    @GetMapping("/{userId}/")
    public User getUser(@PathVariable Integer userId) {
        return userService.getUser(userId);
    }

    @ResponseBody
    @PutMapping("/")
    public User updateUser(@RequestBody Map<String, String> parameters, @RequestHeader("3rd-session") String thirdSession) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        User user = userService.getUserByOpenId(userService.getSessionByThirdSession(thirdSession).getOpenid());
        if (parameters.get("iv") != null && parameters.get("encryptedData") != null) {
            byte[] iv = new BASE64Decoder().decodeBuffer(parameters.get("iv"));
            byte[] encryptedData = new BASE64Decoder().decodeBuffer(parameters.get("encryptedData"));
            byte[] sessionKey = new BASE64Decoder().decodeBuffer(userService.getSessionByThirdSession(thirdSession).getSessionKey());

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKey secretKey = new SecretKeySpec(sessionKey, "AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));
            String result = new String(cipher.doFinal(encryptedData));

            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            user = mapper.readValue(result, User.class);

            HttpClient client = HttpClients.custom().build();
            HttpGet get = new HttpGet(user.getAvatarUrl());
            HttpResponse response = client.execute(get);
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                String prefix = entity.getContentType().getValue().split("/")[1];
                String newName = UUID.randomUUID().toString() + "." + prefix;
                File dest = new File("/static/image/avatar/" + newName);
                // 检测是否存在目录
                if (!dest.getParentFile().exists()) {
                    dest.getParentFile().mkdirs();
                }
                FileOutputStream outputStream = new FileOutputStream(dest);
                IOUtils.copy(response.getEntity().getContent(), outputStream);
                outputStream.flush();
                user.setAvatarUrl(bathPath + "avatar/" + newName);
            }
            User oldUser = userService.getUserByOpenId(user.getOpenId());
            user.setId(oldUser.getId());
            user.setQrCode(oldUser.getQrCode());
        }

        String qrCode = parameters.get("qrCode");
        if (qrCode != null) {
            user.setQrCode(qrCode);
        }
        userService.updateUser(user);
        return user;
    }

    @PostMapping("/")
    public ResponseEntity<Map<String, Object>> addUser(@RequestBody Map<String, String> parameters) throws IOException, NoSuchAlgorithmException {
        HashOperations<String, Object, Object> hashOperations = stringRedisTemplate.opsForHash();

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        CloseableHttpClient client = HttpClients.custom().build();
        HttpGet httpGet = new HttpGet("https://api.weixin.qq.com/sns/jscode2session?appid=" + appid + "&secret=" + appsecret + "&js_code=" + parameters.get("code") + "&grant_type=authorization_code");
        InputStream stream = client.execute(httpGet).getEntity().getContent();
        String result = IOUtils.toString(stream, "UTF-8");
        Session session = mapper.readValue(result, Session.class);

        if (session.getOpenid() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        String thirdSession = userService.generateThirdSession();
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("session", thirdSession);

        hashOperations.put("account_session_openid", thirdSession, session.getOpenid());
        hashOperations.put("account_session_sessionkey", thirdSession, session.getSessionKey());
        User user = userService.getUserByOpenId(session.getOpenid());

        client.close();
        if (user == null) {
            user = new User();
            user.setOpenId(session.getOpenid());
            userService.addUser(user);
            resultMap.put("userId", user.getId());
            return new ResponseEntity<>(resultMap, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(resultMap, HttpStatus.OK);
        }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    @PostMapping("/account_book/{accountBookId}/")
    public void addUserAccountBook(@RequestHeader("3rd-session") String thirdSession, @PathVariable Integer accountBookId) {
        User user = userService.getUserByThirdSession(thirdSession);
        AccountBookParticipant accountBookParticipant = new AccountBookParticipant();
        accountBookParticipant.setAccountBookId(accountBookId);
        accountBookParticipant.setUserId(user.getId());
        userService.addAccountBookParticipant(accountBookParticipant);
    }

}
