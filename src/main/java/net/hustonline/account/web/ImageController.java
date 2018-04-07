package net.hustonline.account.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RequestMapping("/api/v1/images")
@Controller
public class ImageController {

    @Value("${base-path}")
    private String bathPath;

    @PostMapping("/qrcodes/")
    public ResponseEntity<Map<String, String>> uploadQRCode(@RequestParam MultipartFile qrcode) throws IOException {
        String fileName = qrcode.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        String newName = UUID.randomUUID().toString() + suffix;
        File dest = new File("qrcode/" + newName);
        // 检测是否存在目录
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        qrcode.transferTo(dest);
        Map<String, String> data = new HashMap<>();
        data.put("path", bathPath + dest.getPath());
        return new ResponseEntity(data, HttpStatus.OK);
    }


    @PostMapping("/bills/")
    public ResponseEntity<Map<String, String>> uploadImage(@RequestParam MultipartFile bill) throws IOException {
        String fileName = bill.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        String newName = UUID.randomUUID().toString() + suffix;
        File dest = new File("bill/" + newName);
        // 检测是否存在目录
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        bill.transferTo(dest);
        Map<String, String> data = new HashMap<>();
        data.put("path", bathPath + dest.getPath());
        return new ResponseEntity(data, HttpStatus.OK);
    }
}
