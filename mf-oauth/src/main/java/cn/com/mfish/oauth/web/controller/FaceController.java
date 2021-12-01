package cn.com.mfish.oauth.web.controller;


import cn.com.mfish.oauth.cache.redis.RedisSessionDAO;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.io.ResourceUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author ：qiufeng
 * @description：TODO
 * @date ：2021/10/28 20:15
 */
@Controller
@RequestMapping("/face")
public class FaceController {
    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Resource
    RedisSessionDAO redisSessionDAO;

    @ApiOperation("比对")
    @GetMapping("/compare")
    public Object compare(Model model, HttpServletRequest request) {
        String id = UUID.randomUUID().toString();
        request.getSession().setAttribute("compareId",id);
        stringRedisTemplate.opsForValue().set(id, id, 300, TimeUnit.SECONDS);
        InputStream file1 = null;
        InputStream file2 = null;
        try {
            file1 = ResourceUtils.getInputStreamForPath("classpath:static/img/IMG_20211028_180509.jpg");
            file2 = ResourceUtils.getInputStreamForPath("classpath:static/img/IMG_20211028_180506.jpg");
            String img1 = imgToString(file1);
            String img2 = imgToString(file2);
            model.addAttribute("img1", img1);
            model.addAttribute("img2", img2);
            model.addAttribute("compareId", id);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "face";
    }

    @ApiOperation("比对")
    @PostMapping("/compare")
    public Object compareSubmit(HttpServletRequest request) {
        String id = request.getParameter("compareId");
        String compareId = stringRedisTemplate.opsForValue().get(id);
        if(StringUtils.isEmpty(compareId)){
            return new ResponseEntity<>("错误:session认证失败", HttpStatus.UNAUTHORIZED);
        }
        if (!compareId.equals(request.getSession().getAttribute("compareId"))) {
            return new ResponseEntity<>("错误:session认证失败", HttpStatus.UNAUTHORIZED);
        }
        Object result = request.getParameter("compareResult");
        return new ResponseEntity<>(result.toString(), HttpStatus.OK);
    }

    @GetMapping("/{key:.+}")
    public Object getModel(@PathVariable String key) {
        InputStream stream = null;
        BufferedReader reader = null;
        StringBuffer sb = new StringBuffer();
        try {
            stream = ResourceUtils.getInputStreamForPath("classpath:static/weights/" + key);
            reader = new BufferedReader(new InputStreamReader(stream));
            String line = reader.readLine(); // 读取第一行
            while (line != null) { // 如果 line 为空说明读完了
                sb.append(line); // 将读到的内容添加到 buffer 中
                sb.append("\n"); // 添加换行符
                line = reader.readLine(); // 读取下一行
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return new ResponseEntity<>(sb.toString(), HttpStatus.OK);
    }

    public String imgToString(InputStream img) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            BufferedImage bi;
            bi = ImageIO.read(img);
            ImageIO.write(bi, "jpg", baos);
            byte[] bytes = baos.toByteArray();
            String base64str = Base64.encodeBase64String(bytes);
            return "data:image/jpeg;base64," + base64str;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }
}
