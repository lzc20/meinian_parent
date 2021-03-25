package com.company.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.company.constant.MessageConstant;
import com.company.constant.RedisConstant;
import com.company.entity.PageResult;
import com.company.entity.QueryPageBean;
import com.company.entity.Result;
import com.company.pojo.Setmeal;
import com.company.service.SetmealService;
import com.company.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Reference
    SetmealService setmealService;

    @Autowired
    JedisPool jedisPool;

    @RequestMapping("/upload.do")
    //文件上传就是获取客户端传过来的图片
    public Result upload(MultipartFile imgFile){
        try {
            //获取原始文件名
            String originalFilename = imgFile.getOriginalFilename();
            // 找到.最后出现的位置
            int lastIndexOf = originalFilename.lastIndexOf(".");
            //获取文件后缀
            String suffix = originalFilename.substring(lastIndexOf);
            //使用UUID随机产生文件名称，防止同名文件覆盖
            String fileName = UUID.randomUUID().toString() + suffix;
            QiniuUtils.upload2Qiniu(imgFile.getBytes(),fileName);
//            TengXunUtils.upload(imgFile.getInputStream(), fileName);
            //图片上传成功
            //把图片的名字上传到redis一份
            Jedis jedis = jedisPool.getResource();
                //和七牛云是同步的
            jedis.sadd(RedisConstant.SETMEAL_PIC_RESOURCES,fileName);
            Result result = new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS, fileName);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Result(false,MessageConstant.PIC_UPLOAD_FAIL);

    }


    @RequestMapping("/add.do")
    public Result add(Integer[] travelgroupIds,@RequestBody Setmeal setmeal){
        try {
            setmealService.add(travelgroupIds,setmeal);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.ADD_SETMEAL_FAIL);
        }
        return new Result(true,MessageConstant.ADD_SETMEAL_SUCCESS);

    }

    @RequestMapping("/findPage.do")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){

        System.out.println("=================="+queryPageBean);
      PageResult pageResult =  setmealService.findPage(queryPageBean);
      return pageResult;
    }
}
