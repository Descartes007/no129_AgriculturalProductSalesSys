package com.cow.horse.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cow.horse.commcn.Result;
import com.cow.horse.entity.Files;
import com.cow.horse.service.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 文件处理接口
 */
@RestController
@RequestMapping("/file")
public class FileController {

    @Value("${files.upload.path}")
    String fileUploadPath;

    @Resource
    private FileService fileService;

    @GetMapping("/reception/all")
    public Result receptionAll() {
        return Result.success(fileService.receptionAll());
    }


    /**
     * 文件上传接口
     *
     * @param file
     * @return String
     * @throws
     */
    @PostMapping("/upload")
    public String upload(@RequestParam MultipartFile file) throws IOException {
//        response.setHeader("Accept-Ranges", "bytes");
//        response.setHeader("Content-Length", String.valueOf(file));
        return fileService.upload(file);
    }

    @GetMapping("/{fileUUID}")
    public void download(@PathVariable String fileUUID, HttpServletResponse response) throws IOException {
        fileService.download(fileUUID, response);

    }


    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String name) {
        Page<Files> filesPage = fileService.findPage(pageNum, pageSize, name);
        return Result.success(filesPage);
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        return Result.success(fileService.delete(id));
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        return Result.success(fileService.deleteBatch(ids));

    }

    @PostMapping("/update")
    public Result update(@RequestBody Files file) {
        return Result.success(fileService.updateByFile(file));
    }

}
