package com.xiaoke.common.core.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * 容器云：work
 * 类名称：FileUtils
 * 类描述：  上传文件工具类
 * 创建时间：2017年7月16日 上午11:01:34
 *
 * @author xiaoke
 * @version V1.0
 */
@Slf4j
public class FileUtils extends FileUtil {
    private static String dot = ".";

    private static String suffixs = "jpeg,jpg,png,rar,zip,mp3,mp4,gif";

    /**
     * 校验文件扩展名
     * @param ext
     * @return
     */
    public static boolean checkExt(String ext,String suffixs) {
        String[] suffixsList = suffixs.split(",");
        boolean flag = false;
        for(String s:suffixsList){
            if(s.equals(ext)){
                flag = true;
                break;
            }
        }
        return flag;
    }


    /**
     * 复制单个文件，如果目标文件存在，则不覆盖
     *
     * @param srcFileName  待复制的文件名
     * @param descFileName 目标文件名
     * @return 如果复制成功，则返回true，否则返回false
     */
    public static boolean copyFile(String srcFileName, String descFileName) {
        return FileUtils.copyFileCover(srcFileName, descFileName, false);
    }

    /**
     * 复制单个文件
     *
     * @param srcFileName  待复制的文件名
     * @param descFileName 目标文件名
     * @param coverlay     如果目标文件已存在，是否覆盖
     * @return 如果复制成功，则返回true，否则返回false
     */
    public static boolean copyFileCover(String srcFileName,
                                        String descFileName, boolean coverlay) {
        File srcFile = new File(srcFileName);
        // 判断源文件是否存在
        if (!srcFile.exists()) {
            log.debug("复制文件失败，源文件 " + srcFileName + " 不存在!");
            return false;
        }
        // 判断源文件是否是合法的文件
        else if (!srcFile.isFile()) {
            log.debug("复制文件失败，" + srcFileName + " 不是一个文件!");
            return false;
        }
        File descFile = new File(descFileName);
        // 判断目标文件是否存在
        if (descFile.exists()) {
            // 如果目标文件存在，并且允许覆盖
            if (coverlay) {
                log.debug("目标文件已存在，准备删除!");
                if (!FileUtils.delFile(descFileName)) {
                    log.debug("删除目标文件 " + descFileName + " 失败!");
                    return false;
                }
            } else {
                log.debug("复制文件失败，目标文件 " + descFileName + " 已存在!");
                return false;
            }
        } else {
            if (!descFile.getParentFile().exists()) {
                // 如果目标文件所在的目录不存在，则创建目录
                log.debug("目标文件所在的目录不存在，创建目录!");
                // 创建目标文件所在的目录
                if (!descFile.getParentFile().mkdirs()) {
                    log.debug("创建目标文件所在的目录失败!");
                    return false;
                }
            }
        }

        // 准备复制文件
        // 读取的位数
        int readByte = 0;
        InputStream ins = null;
        OutputStream outs = null;
        try {
            // 打开源文件
            ins = new FileInputStream(srcFile);
            // 打开目标文件的输出流
            outs = new FileOutputStream(descFile);
            byte[] buf = new byte[1024];
            // 一次读取1024个字节，当readByte为-1时表示文件已经读取完毕
            while ((readByte = ins.read(buf)) != -1) {
                // 将读取的字节流写入到输出流
                outs.write(buf, 0, readByte);
            }
            log.debug("复制单个文件 " + srcFileName + " 到" + descFileName
                    + "成功!");
            return true;
        } catch (Exception e) {
            log.debug("复制文件失败：" + e.getMessage());
            return false;
        } finally {
            // 关闭输入输出流，首先关闭输出流，然后再关闭输入流
            if (outs != null) {
                try {
                    outs.close();
                } catch (IOException oute) {
                    oute.printStackTrace();
                }
            }
            if (ins != null) {
                try {
                    ins.close();
                } catch (IOException ine) {
                    ine.printStackTrace();
                }
            }
        }
    }

    /**
     * 复制整个目录的内容，如果目标目录存在，则不覆盖
     *
     * @param srcDirName  源目录名
     * @param descDirName 目标目录名
     * @return 如果复制成功返回true，否则返回false
     */
    public static boolean copyDirectory(String srcDirName, String descDirName) {
        return FileUtils.copyDirectoryCover(srcDirName, descDirName,
                false);
    }

    /**
     * 复制整个目录的内容
     *
     * @param srcDirName  源目录名
     * @param descDirName 目标目录名
     * @param coverlay    如果目标目录存在，是否覆盖
     * @return 如果复制成功返回true，否则返回false
     */
    public static boolean copyDirectoryCover(String srcDirName, String descDirName, boolean coverlay) {
        File srcDir = new File(srcDirName);
        // 判断源目录是否存在
        if (!srcDir.exists()) {
            log.debug("复制目录失败，源目录 " + srcDirName + " 不存在!");
            return false;
        }
        // 判断源目录是否是目录
        else if (!srcDir.isDirectory()) {
            log.debug("复制目录失败，" + srcDirName + " 不是一个目录!");
            return false;
        }
        // 如果目标文件夹名不以文件分隔符结尾，自动添加文件分隔符
        String descDirNames = descDirName;
        if (!descDirNames.endsWith(File.separator)) {
            descDirNames = descDirNames + File.separator;
        }
        File descDir = new File(descDirNames);
        // 如果目标文件夹存在
        if (descDir.exists()) {
            if (coverlay) {
                // 允许覆盖目标目录
                log.debug("目标目录已存在，准备删除!");
                if (!FileUtils.delFile(descDirNames)) {
                    log.debug("删除目录 " + descDirNames + " 失败!");
                    return false;
                }
            } else {
                log.debug("目标目录复制失败，目标目录 " + descDirNames + " 已存在!");
                return false;
            }
        } else {
            // 创建目标目录
            log.debug("目标目录不存在，准备创建!");
            if (!descDir.mkdirs()) {
                log.debug("创建目标目录失败!");
                return false;
            }

        }

        boolean flag = true;
        // 列出源目录下的所有文件名和子目录名
        File[] files = srcDir.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 如果是一个单个文件，则直接复制
            if (files[i].isFile()) {
                flag = FileUtils.copyFile(files[i].getAbsolutePath(),
                        descDirName + files[i].getName());
                // 如果拷贝文件失败，则退出循环
                if (!flag) {
                    break;
                }
            }
            // 如果是子目录，则继续复制目录
            if (files[i].isDirectory()) {
                flag = FileUtils.copyDirectory(files[i]
                        .getAbsolutePath(), descDirName + files[i].getName());
                // 如果拷贝目录失败，则退出循环
                if (!flag) {
                    break;
                }
            }
        }

        if (!flag) {
            log.debug("复制目录 " + srcDirName + " 到 " + descDirName + " 失败!");
            return false;
        }
        log.debug("复制目录 " + srcDirName + " 到 " + descDirName + " 成功!");
        return true;

    }

    /**
     * 删除文件，可以删除单个文件或文件夹
     *
     * @param fileName 被删除的文件名
     * @return 如果删除成功，则返回true，否是返回false
     */
    public static boolean delFile(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            log.debug(fileName + " 文件不存在!");
            return true;
        } else {
            if (file.isFile()) {
                return FileUtils.deleteFile(fileName);
            } else {
                return FileUtils.deleteDirectory(fileName);
            }
        }
    }

    /**
     * 删除单个文件
     *
     * @param fileName 被删除的文件名
     * @return 如果删除成功，则返回true，否则返回false
     */
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            log.debug(fileName + "文件不存在!");
            return true;
        }
        if (!file.isFile()) {
            log.debug(fileName + "不是文件!");
            return false;
        }
        if (file.delete()) {
            log.debug("删除文件" + fileName + "成功!");
            return true;
        } else {
            log.debug("删除文件" + fileName + "失败!");
            return false;
        }
    }

    /**
     * 删除目录及目录下的文件
     *
     * @param dirName 被删除的目录所在的文件路径
     * @return 如果目录删除成功，则返回true，否则返回false
     */
    public static boolean deleteDirectory(String dirName) {
        String dirNames = dirName;
        if (!dirNames.endsWith(File.separator)) {
            dirNames = dirNames + File.separator;
        }
        File dirFile = new File(dirNames);
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            log.debug(dirNames + " 目录不存在!");
            return true;
        }
        boolean flag = true;
        // 列出全部文件及子目录
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 删除子文件
            if (files[i].isFile()) {
                flag = FileUtils.deleteFile(files[i].getAbsolutePath());
                // 如果删除文件失败，则退出循环
                if (!flag) {
                    break;
                }
            }
            // 删除子目录
            else if (files[i].isDirectory()) {
                flag = FileUtils.deleteDirectory(files[i]
                        .getAbsolutePath());
                // 如果删除子目录失败，则退出循环
                if (!flag) {
                    break;
                }
            }
        }

        if (!flag) {
            log.debug("删除目录失败!");
            return false;
        }
        // 删除当前目录
        if (dirFile.delete()) {
            log.debug("删除目录 " + dirName + " 成功!");
            return true;
        } else {
            log.debug("删除目录 " + dirName + " 失败!");
            return false;
        }

    }

    /**
     * 创建单个文件
     *
     * @param descFileName 文件名，包含路径
     * @return 如果创建成功，则返回true，否则返回false
     */
    public static boolean createFile(String descFileName) {
        File file = new File(descFileName);
        if (file.exists()) {
            log.debug("文件 " + descFileName + " 已存在!");
            return false;
        }
        if (descFileName.endsWith(File.separator)) {
            log.debug(descFileName + " 为目录，不能创建目录!");
            return false;
        }
        if (!file.getParentFile().exists()) {
            // 如果文件所在的目录不存在，则创建目录
            if (!file.getParentFile().mkdirs()) {
                log.debug("创建文件所在的目录失败!");
                return false;
            }
        }

        // 创建文件
        try {
            if (file.createNewFile()) {
                log.debug(descFileName + " 文件创建成功!");
                return true;
            } else {
                log.debug(descFileName + " 文件创建失败!");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.debug(descFileName + " 文件创建失败!");
            return false;
        }

    }

    /**
     * 创建目录
     *
     * @param descDirName 目录名,包含路径
     * @return 如果创建成功，则返回true，否则返回false
     */
    public static boolean createDirectory(String descDirName) {
        String descDirNames = descDirName;
        if (!descDirNames.endsWith(File.separator)) {
            descDirNames = descDirNames + File.separator;
        }
        File descDir = new File(descDirNames);
        if (descDir.exists()) {
            log.debug("目录 " + descDirNames + " 已存在!");
            return false;
        }
        // 创建目录
        if (descDir.mkdirs()) {
            log.debug("目录 " + descDirNames + " 创建成功!");
            return true;
        } else {
            log.debug("目录 " + descDirNames + " 创建失败!");
            return false;
        }

    }

    /**
     * 获取待压缩文件在ZIP文件中entry的名字，即相对于跟目录的相对路径名
     *
     * @param dirPath 目录名
     * @param file    entry文件名
     * @return
     */
    private static String getEntryName(String dirPath, File file) {
        String dirPaths = dirPath;
        if (!dirPaths.endsWith(File.separator)) {
            dirPaths = dirPaths + File.separator;
        }
        String filePath = file.getAbsolutePath();
        // 对于目录，必须在entry名字后面加上"/"，表示它将以目录项存储
        if (file.isDirectory()) {
            filePath += "/";
        }
        int index = filePath.indexOf(dirPaths);

        return filePath.substring(index + dirPaths.length());
    }

    /**
     * 根据“文件名的后缀”获取文件内容类型（而非根据File.getContentType()读取的文件类型）
     *
     * @param returnFileName 带验证的文件名
     * @return 返回文件类型
     */
    private static String html = "shtml";
    private static String htm = "htm";

    public static String getContentType(String returnFileName) {
        String contentType = "application/octet-stream";
        if (returnFileName.lastIndexOf(StrUtil.DOT) < 0) {
            return contentType;
        }
        returnFileName = returnFileName.toLowerCase();
        returnFileName = returnFileName.substring(returnFileName.lastIndexOf(StrUtil.DOT) + 1);
        switch (returnFileName) {
            case "html":
                contentType = "text/html";
                break;
            case "htm":
                contentType = "text/html";
                break;
            case "apk":
                contentType = "application/vnd.android.package-archive";
                break;
            case "sis":
                contentType = "application/vnd.symbian.install";
                break;
            case "exe":
                contentType = "application/x-msdownload";
                break;
            case "msi":
                contentType = "application/x-msdownload";
                break;
            case "css":
                contentType = "text/css";
                break;
            case "xml":
                contentType = "text/xml";
                break;
            case "gif":
                contentType = "image/gif";
                break;
            case "sisx":
                contentType = "application/vnd.symbian.install";
                break;
            case "jpeg":
                contentType = "image/jpeg";
                break;
            case "jpg":
                contentType = "image/jpeg";
                break;
            case "js":
                contentType = "application/x-javascript";
                break;
            case "atom":
                contentType = "application/atom+xml";
                break;
            case "rss":
                contentType = "application/rss+xml";
                break;
            case "mml":
                contentType = "text/mathml";
                break;
            case "txt":
                contentType = "text/plain";
                break;
            case "jad":
                contentType = "text/vnd.sun.j2me.app-descriptor";
                break;
            case "wml":
                contentType = "text/vnd.wap.wml";
                break;
            case "htc":
                contentType = "text/x-component";
                break;
            case "png":
                contentType = "image/png";
                break;
            case "tif":
                contentType = "image/tiff";
                break;
            case "tiff":
                contentType = "image/tiff";
                break;
            case "wbmp":
                contentType = "image/vnd.wap.wbmp";
                break;
            case "ico":
                contentType = "image/x-icon";
                break;
            case "jng":
                contentType = "image/x-jng";
                break;
            case "bmp":
                contentType = "image/x-ms-bmp";
                break;
            case "svg":
                contentType = "image/svg+xml";
                break;
            case "jar":
                contentType = "application/java-archive";
                break;
            case "var":
                contentType = "application/java-archive";
                break;
            case "ear":
                contentType = "application/java-archive";
                break;
            case "doc":
                contentType = "application/msword";
                break;
            case "pdf":
                contentType = "application/pdf";
                break;
            case "rtf":
                contentType = "application/rtf";
                break;
            case "xls":
                contentType = "application/vnd.ms-excel";
                break;
            case "ppt":
                contentType = "application/vnd.ms-powerpoint";
                break;
            case "7z":
                contentType = "application/x-7z-compressed";
                break;
            case "rar":
                contentType = "application/x-rar-compressed";
                break;
            case "swf":
                contentType = "application/x-shockwave-flash";
                break;
            case "rpm":
                contentType = "application/x-redhat-package-manager";
                break;
            case "der":
                contentType = "application/x-x509-ca-cert";
                break;
            case "pem":
                contentType = "application/x-x509-ca-cert";
                break;
            case "crt":
                contentType = "application/x-x509-ca-cert";
                break;
            case "xhtml":
                contentType = "application/xhtml+xml";
                break;
            case "zip":
                contentType = "application/zip";
                break;
            case "mid":
                contentType = "audio/midi";
                break;
            case "midi":
                contentType = "audio/midi";
                break;
            case "kar":
                contentType = "audio/midi";
                break;
            case "mp3":
                contentType = "audio/mpeg";
                break;
            case "ogg":
                contentType = "audio/ogg";
                break;
            case "m4a":
                contentType = "audio/x-m4a";
                break;
            case "ra":
                contentType = "audio/x-realaudio";
                break;
            case "3gpp":
                contentType = "video/3gpp";
                break;
            case "3gp":
                contentType = "video/3gpp";
                break;
            case "mp4":
                contentType = "audio/ogg";
                break;
            case "mpeg":
                contentType = "video/mpeg";
                break;
            case "mpg":
                contentType = "video/mpeg";
                break;
            case "mov":
                contentType = "video/quicktime";
                break;
            case "flv":
                contentType = "video/x-flv";
                break;
            case "m4v":
                contentType = "video/x-m4v";
                break;
            case "mng":
                contentType = "video/x-mng";
                break;
            case "asf":
                contentType = "video/x-mng";
                break;
            case "asx":
                contentType = "video/x-ms-asf";
                break;
            case "wmv":
                contentType = "video/x-ms-wmv";
                break;
            case "avi":
                contentType = "video/x-msvideo";
                break;
            default:
                break;
        }
        return contentType;
    }

    /**
     * 向浏览器发送文件下载，支持断点续传
     *
     * @param file 要下载的文件
     * @param request 请求对象
     * @param response 响应对象
     * @param fileName 指定下载的文件名
     * @return 返回错误信息，无错误信息返回null
     */
    private static String range = "Range";


    /**
     * 获目录下的文件列表
     *
     * @param dir        搜索目录
     * @param searchDirs 是否是搜索目录
     * @return 文件列表
     */
    public static List<String> findChildrenList(File dir, boolean searchDirs) {
        createDirectory(dir.getPath());
        List<String> files = new ArrayList<>();
        for (String subFiles : dir.list()) {
            File file = new File(dir + "/" + subFiles);
            Boolean bool = (((searchDirs) && (file.isDirectory())) || ((!searchDirs) && (!file.isDirectory())));
            if (bool) {
                files.add(file.getName());
            }
        }
        return files;
    }

    //=========================== 关于上传文件的校验相关 begin ==========================

    /**
     * 获取文件名，不包含扩展名
     *
     * @param fileName 文件名
     * @return 例如：d:\files\test.jpg  返回：d:\files\test
     */
    public static String getFileNameWithoutExtension(String fileName) {
        if ((fileName == null) || (fileName.lastIndexOf(dot) == -1)) {
            return null;
        }
        return fileName.substring(0, fileName.lastIndexOf(dot));
    }


    //=========================== 关于上传文件的校验相关 end ==========================

    /**
     * 获取文件大小 自动转换 B KB MB GB
     *
     * @param size
     * @return
     */
    private static double conversion = 1024;

    public static String getPrintSize(long size) {
        // 如果字节数少于1024，则直接以B为单位，否则先除于1024，后3位因太少无意义
        double value = (double) size;
        if (value < conversion) {
            return String.valueOf(value) + "B";
        } else {
            value = new BigDecimal(value / conversion).setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
        }
        // 如果原字节数除于1024之后，少于1024，则可以直接以KB作为单位
        // 因为还没有到达要使用另一个单位的时候
        // 接下去以此类推
        if (value < conversion) {
            return String.valueOf(value) + "KB";
        } else {
            value = new BigDecimal(value / conversion).setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
        }
        if (value < conversion) {
            return String.valueOf(value) + "MB";
        } else {
            // 否则如果要以GB为单位的，先除于1024再作同样的处理
            value = new BigDecimal(value / conversion).setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
            return String.valueOf(value) + "GB";
        }
    }

    public static double getFileSizeGb(long size) {
        // 如果字节数少于1024，则直接返回0GB，否则先除于1024，后3位因太少无意义
        double value = (double) size;
        if (value < conversion) {
            return 0;
        } else {
            //换算成KB
            value = new BigDecimal(value / conversion).setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
        }
        // 如果原字节数除于1024之后，少于1024，则可以直接返回0GB
        if (value < conversion) {
            return 0;
        } else {
            //换算成MB
            value = new BigDecimal(value / conversion).setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
        }

        // 换算成GB
        value = new BigDecimal(value / conversion).setScale(5, BigDecimal.ROUND_DOWN).doubleValue();
        return value;
    }

    /**
     * 读取文件内容
     *
     * @param path
     * @return
     */
    public static String readFile(String path) {
        try {
            //定义一个file对象，用来初始化FileReader
            File file = new File(path);
            //定义一个fileReader对象，用来初始化BufferedReader
            FileReader reader = new FileReader(file);
            //new一个BufferedReader对象，将文件内容读取到缓存
            BufferedReader bReader = new BufferedReader(reader);
            //定义一个字符串缓存，将字符串存放缓存中
            StringBuilder sb = new StringBuilder();
            String s = "";
            //逐行读取文件内容，不读取换行符和末尾的空格
            while ((s = bReader.readLine()) != null) {
                //将读取的字符串添加换行符后累加存放在缓存中
                sb.append(s + "\n");
            }
            bReader.close();
            String str = sb.toString();
            return str;
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }


    public static MultipartFile base64MutipartFile(String imgStr) {
        try {
            log.info("============> base64转文件开始 <============");
            String[] baseStr = imgStr.split(",");
            byte[] b = Base64.getDecoder().decode(baseStr[1]);
            return new BASE64DecodedMultipartFile(b, baseStr[0]);
        } catch (Exception e) {
            log.info("base64转文件异常");
            e.printStackTrace();
            return null;
        }
    }
}
