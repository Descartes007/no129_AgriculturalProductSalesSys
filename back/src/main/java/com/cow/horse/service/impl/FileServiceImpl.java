package com.cow.horse.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cow.horse.commcn.Result;
import com.cow.horse.entity.Files;
import com.cow.horse.mapper.FileMapper;
import com.cow.horse.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, Files> implements FileService {

    @Value("${files.upload.path}")
    String fileUploadPath;

    @Autowired
    private FileMapper fileMapper;

    @Override
    public Result receptionAll() {
        return Result.success(fileMapper.selectList(null));
    }

    @Override
    public String upload(MultipartFile file) throws IOException {

        String originalFilename = file.getOriginalFilename(); // 原始名称
        String type = FileUtil.extName(originalFilename); // 根据hutoole工具类获取文件类型
        long size = file.getSize(); // 获取文件大小
        // 存储到磁盘
        File uploadParentFile = new File(fileUploadPath);
        // 判断配置文件目录是否存在，若不存在则创建一个新的目录
        if (!uploadParentFile.exists()) {
            uploadParentFile.mkdirs();
        }
        // 创建文件唯一标识码
        String uuid = IdUtil.fastSimpleUUID();

        String fileUUID = uuid + StrUtil.DOT + type;
        // uploadFile 路径+文件名
        File uploadFile = new File(fileUploadPath + fileUUID);
        String url;
        String md5;
        // 获取文件的md5
        md5 = SecureUtil.md5(file.getInputStream());
        Files dbFiles = getFileByMd5(md5);
        if (dbFiles != null) { // 文件已存在
            url = dbFiles.getUrl();
        } else {
            // 上传文件到磁盘
            file.transferTo(uploadFile);
            url = "http://localhost:9090/file/" + fileUUID;
        }
        // 存储数据库
        Files saveFile = new Files();
        saveFile.setName(originalFilename);
        saveFile.setType(type);
        saveFile.setSize(size / 1024);
        saveFile.setUrl(url);
        saveFile.setMd5(md5);
        fileMapper.insert(saveFile);

        return url;
    }

    @Override
    public void download(String fileUUID, HttpServletResponse response) throws IOException {
        // 根据文件的唯一标识码获取文件
        File uploadFile = new File(fileUploadPath + fileUUID);
        if (!uploadFile.exists()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // 根据扩展名设置正确的 Content-Type，图片内联显示，其他类型作为附件下载
        String ext = fileUUID.contains(".") ? fileUUID.substring(fileUUID.lastIndexOf('.') + 1).toLowerCase() : "";
        String contentType = "application/octet-stream";
        if ("png".equals(ext))
            contentType = "image/png";
        else if ("jpg".equals(ext) || "jpeg".equals(ext))
            contentType = "image/jpeg";
        else if ("gif".equals(ext))
            contentType = "image/gif";
        else if ("bmp".equals(ext))
            contentType = "image/bmp";

        if (contentType.startsWith("image/")) {
            response.setContentType(contentType);
            response.setHeader("Content-Disposition", "inline;filename=" + URLEncoder.encode(fileUUID, "UTF-8"));
        } else {
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileUUID, "UTF-8"));
        }

        response.setContentLengthLong(uploadFile.length());

        // 写出文件字节流
        ServletOutputStream os = response.getOutputStream();
        os.write(FileUtil.readBytes(uploadFile));
        os.flush();
        os.close();
    }

    @Override
    public Page<Files> findPage(Integer pageNum, Integer pageSize, String name) {
        Page<Files> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();

        if (!"".equals(name)) {
            queryWrapper.like("name", name);
        }
        queryWrapper.eq("is_delete", false);
        queryWrapper.orderByDesc("id");

        return fileMapper.selectPage(page, queryWrapper);
    }

    @Override
    public Result delete(Integer id) {
        Files files = fileMapper.selectById(id);
        files.setIsDelete(true);
        return Result.success(fileMapper.updateById(files));
    }

    @Override
    public Result deleteBatch(List<Integer> ids) {
        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", ids);
        List<Files> filesList = fileMapper.selectList(queryWrapper);
        for (Files file : filesList) {
            file.setIsDelete(true);
            fileMapper.updateById(file);
        }
        return Result.success(true);
    }

    @Override
    public Result updateByFile(Files file) {
        return Result.success(fileMapper.updateById(file));
    }

    // 查数据库有没有
    private Files getFileByMd5(String md5) {
        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("md5", md5);
        List<Files> filesList = fileMapper.selectList(queryWrapper);
        return filesList.size() == 0 ? null : filesList.get(0);
    }

}
