package com.cow.horse.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cow.horse.commcn.Result;
import com.cow.horse.entity.Files;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface FileService extends IService<Files> {

    Result receptionAll();

    String upload(MultipartFile file) throws IOException;

    void download(String fileUUID, HttpServletResponse response) throws IOException;


    Page<Files> findPage(Integer pageNum, Integer pageSize, String name);

    Result delete(Integer id);

    Result deleteBatch(List<Integer> ids);

    Result updateByFile(Files file);
}
