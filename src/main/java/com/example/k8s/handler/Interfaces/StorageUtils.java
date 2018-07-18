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
package com.example.k8s.handler.Interfaces;

import java.io.InputStream;
import java.util.List;

public abstract class StorageUtils {

    private String prefix;

    private String ip;

    /**
     * List all files & dirs under the given path
     * 
     * @param path
     * @return A list of file names
     */
    public abstract List<String> getList(String path);

    /**
     * Save file to the specified location
     * 
     * @param file
     * @param pathAndFileName
     * @return return true if successfully saved.
     */
    public abstract boolean save(InputStream file, String pathAndFileName);

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    private StringBuffer getUrl() {
        return new StringBuffer("nfs://" + ip + "/" + prefix);
    }

    public StorageUtils() {

    }

    public StorageUtils(String prefix, String ip) {
        setPrefix(prefix);
        setIp(ip);
    }
}