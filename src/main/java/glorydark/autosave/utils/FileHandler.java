package glorydark.autosave.utils;

import cn.nukkit.utils.TextFormat;
import glorydark.autosave.MainClass;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileHandler {

    private static final int  BUFFER_SIZE = 2 * 1024;

    public FileHandler() {
    }

    public static void copyDir(String fromDir,String toDir) throws IOException{
        MainClass.logger.info(TextFormat.YELLOW+"复制目录"+fromDir+"成功!");
        //创建目录的File对象
        File dirSource = new File(fromDir);
        //判断源目录是不是一个目录
        if (!dirSource.isDirectory()) {
            //如果不是目录那就不复制
            return;
        }
        //创建目标目录的File对象
        File destDir = new File(toDir);
        //如果目的目录不存在
        if(!destDir.exists()){
            //创建目的目录
            destDir.mkdir();
        }
        //获取源目录下的File对象列表
        File[] files = dirSource.listFiles();
        for (File file : files) {
            //拼接新的fromDir(fromFile)和toDir(toFile)的路径
            String strFrom = fromDir + File.separator + file.getName();
            String strTo = toDir + File.separator + file.getName();
            //判断File对象是目录还是文件
            //判断是否是目录
            if (file.isDirectory()) {
                //递归调用复制目录的方法
                copyDir(strFrom,strTo);
            }
            //判断是否是文件
            if (file.isFile()) {
                //递归调用复制文件的方法
                copyFile(strFrom,strTo);
            }
        }
    }

    public static void copyFile(String fromFilePath,String toFilePath) throws IOException{
        //字节输入流——读取文件
        FileInputStream in = new FileInputStream(fromFilePath);
        //字节输出流——写入文件
        FileOutputStream out = new FileOutputStream(toFilePath);
        File fromFile = new File(fromFilePath);
        File toFile = new File(fromFilePath);
        //把读取到的内容写入新文件
        //把字节数组设置大一些   1*1024*1024=1M
        byte[] bs = new byte[1*1024*1024];
        int count = 0;
        while((count = in.read(bs))!=-1){
            out.write(bs,0,count);
        }
        //关闭流
        in.close();
        out.flush();
        out.close();
    }

    public static void delete(File dir) {
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            for (File file : files) {
                delete(file);
            }
        }
        dir.delete();
    }

    public static void generateFile(String path, String format) throws Exception {

        File file = new File(path);
        // 压缩文件的路径不存在
        if (!file.exists()) {
            throw new Exception("路径 " + path + " 不存在文件，无法进行压缩...");
        }
        // 用于存放压缩文件的文件夹
        String generateFile = file.getParent() + File.separator +"out";
        File compress = new File(generateFile);
        // 如果文件夹不存在，进行创建
        if( !compress.exists() ){
            compress.mkdirs();
        }

        // 目的压缩文件
        String generateFileName = compress.getAbsolutePath() + File.separator + file.getName() + "." + format;

        // 输入流 表示从一个源读取数据
        // 输出流 表示向一个目标写入数据

        // 输出流
        FileOutputStream outputStream = new FileOutputStream(generateFileName);

        // 压缩输出流
        ZipOutputStream zipOutputStream = new ZipOutputStream(new BufferedOutputStream(outputStream));

        generateFile(zipOutputStream,file,"");

        MainClass.logger.info(TextFormat.YELLOW+"源文件位置：" + file.getAbsolutePath() + "，目的压缩文件生成位置：" + generateFileName);
        // 关闭 输出流
        zipOutputStream.close();
    }

    private static void generateFile(ZipOutputStream out, File file, String dir) throws Exception {

        // 当前的是文件夹，则进行一步处理
        if (file.isDirectory()) {
            //得到文件列表信息
            File[] files = file.listFiles();
            //将文件夹添加到下一级打包目录
            out.putNextEntry(new ZipEntry(dir + "/"));
            dir = dir.length() == 0 ? "" : dir + "/";
            //循环将文件夹中的文件打包
            for (int i = 0; i < files.length; i++) {
                generateFile(out, files[i], dir + files[i].getName());
            }
        } else { // 当前是文件

            // 输入流
            FileInputStream inputStream = new FileInputStream(file);
            // 标记要打包的条目
            out.putNextEntry(new ZipEntry(dir));
            // 进行写操作
            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = inputStream.read(bytes)) > 0) {
                out.write(bytes, 0, len);
            }
            // 关闭输入流
            inputStream.close();
        }

    }
}
