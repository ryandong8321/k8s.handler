/*
 * COPYRIGHT
 * ---------
 * Copyright (C) 2017 by
 * Ericsson AB
 * All rights reserved.
 * REVISION HISTORY
 * ----------------
 * Created By: Alex Zhang, Jul 18, 2018
 * Revised By:
 * Change:
 */
package com.example.k8s.handler.service;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.stereotype.Component;

import com.example.k8s.handler.Interfaces.StorageUtils;
import com.sun.xfile.XFile;
import com.sun.xfile.XFileOutputStream;

import groovy.util.logging.Slf4j;
import net.sf.jftp.system.logging.Log;

@Component
@Slf4j
public class NFSutils extends StorageUtils {

    public static void main(String[] args) {
        StorageUtils stor = new NFSutils("home/bidteso/nfs", "100.98.42.254");
        File file = new File("/home/alex/project/pull.sh");
        try {
            FileInputStream fis = new FileInputStream(file);
            stor.save(fis, "/abc", "testpat1h.sh");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Override
    public List<String> getList(String path) {
        // TODO Auto-generated method stub
        return null;
    }

    public NFSutils() {
        super();
    }

    public NFSutils(String prefix, String ip) {
        super(prefix, ip);
    }

    @Override
    public boolean save(InputStream file, String path, String fileName) {
        System.out.println("build dirs: " + this.getUrl().append(path).toString());
        buildPath(path);
        System.out.println("write: " + this.getUrl().append(path).append("/" + fileName).toString());
        return writeFile(file, this.getUrl().append(path).append("/" + fileName).toString());
    }

    private void buildPath(String path) {
        XFile xfile = new XFile(this.getUrl().append(path).toString());
        xfile.mkdirs();
    }

    private boolean writeFile(InputStream file, String pathAndFileName) {
        try {

            XFileOutputStream outputStream = new XFileOutputStream(pathAndFileName);
            byte[] buffer = new byte[10000000];
            file.read(buffer);
            outputStream.write(buffer);
            outputStream.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;

        }
        return true;
    }
}
