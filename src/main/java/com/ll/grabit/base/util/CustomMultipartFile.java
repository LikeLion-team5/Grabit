package com.ll.grabit.base.util;

import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

public class CustomMultipartFile implements MultipartFile {
    private final File file;

    public CustomMultipartFile(File file) {
        this.file = file;
    }

    @Override
    public String getName() {
        // TODO: 파일의 이름을 반환합니다.
        return file.getName();
    }

    @Override
    public String getOriginalFilename() {
        // TODO: 원래 파일의 이름을 반환합니다.
        return file.getName();
    }

    @Override
    public String getContentType() {
        // TODO: 파일의 컨텐츠 타입을 반환합니다.
        return "image/png";
    }

    @Override
    public boolean isEmpty() {
        return file.length() == 0;
    }

    @Override
    public long getSize() {
        return file.length();
    }

    @Override
    public byte[] getBytes() throws IOException {
        return FileCopyUtils.copyToByteArray(new FileInputStream(file));
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return null;
    }

    @Override
    public Resource getResource() {
        return MultipartFile.super.getResource();
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {

    }

    @Override
    public void transferTo(Path dest) throws IOException, IllegalStateException {
        MultipartFile.super.transferTo(dest);
    }
}
