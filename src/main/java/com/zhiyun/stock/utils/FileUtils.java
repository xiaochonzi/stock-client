package com.zhiyun.stock.utils;

import cn.hutool.core.io.FileUtil;

/**
 * @program: FileUtils
 * @description:
 * @author: Chonzi.xiao
 * @create: 2021/7/5 13:52
 **/
public class FileUtils extends FileUtil {

    public static String getProjectPath() {
        return FileUtils.class.getResource("/").getPath();
    }

}
