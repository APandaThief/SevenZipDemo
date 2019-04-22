package cn.chase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import org.sevenzip4j.SevenZipArchiveOutputStream;
import org.sevenzip4j.archive.SevenZipEntry;

public class SevenZip {
    private static final int BUFFEREDSIZE = 1024;
    /** * zip工具类的单例 */
    private static SevenZip zipInstance;
    /** * 压缩文件的字符编码 */
    public static String encoding = "GBK";

    /**
     * * * 将文件或者文件夹压缩为zip文件 * *
     *
     * @param inputFilename *
     *            压缩的文件或文件夹及详细路径 * *
     * @param zipFilename *
     *            输出文件名称及详细路径 * *
     * @throws IOException
     */
    public synchronized void zip(String inputFilename, String zipFilename) throws IOException {
        zip(new File(inputFilename), zipFilename);
    }

    /**
     * * * 将多个文件或者文件夹压缩成指定名称的zip压缩文件 * *
     *
     * @param inputFile *
     *            需压缩文件 * *
     * @param zipFilename *
     *            输出文件及详细路径 * *
     * @throws IOException
     */
    public synchronized void zip(String[] inputFile, String zipFilename) throws IOException {
        SevenZipArchiveOutputStream out = new SevenZipArchiveOutputStream(new FileOutputStream(zipFilename));
        try {
            for (String f : inputFile) {
                File file = new File(f);
                zip(file, out, file.isDirectory() ? file.getName() : "");
            }
        } catch (IOException e) {
            throw e;
        } finally {
            out.finish();
            out.close();
        }
    }

    /**
     * * * 将多个文件或者文件夹压缩成指定名称的zip压缩文件 * *
     *
     * @param inputFile *
     *            需压缩文件 * *
     * @param zipFilename *
     *            输出文件及详细路径 * *
     * @throws IOException
     */
    public synchronized void zip(List<String> inputFile, String zipFilename) throws IOException {
        SevenZipArchiveOutputStream out = new SevenZipArchiveOutputStream(new FileOutputStream(zipFilename));
        try {
            for (String f : inputFile) {
                File file = new File(f);
                zip(file, out, file.isDirectory() ? file.getName() : "");
            }
        } catch (IOException e) {
            throw e;
        } finally {
            out.finish();
            out.close();
        }
    }

    /**
     * * * 将多个文件或者文件夹压缩成指定名称的zip压缩文件 * *
     *
     * @param inputFile *
     *            需要压缩的文件 * *
     * @param zipFilename *
     *            压缩文件路径 * *
     * @throws IOException
     */
    public synchronized void zip(File[] inputFile, String zipFilename) throws IOException {
        SevenZipArchiveOutputStream out = new SevenZipArchiveOutputStream(new FileOutputStream(zipFilename));
        try {
            for (File f : inputFile)
                zip(f, out, f.isDirectory() ? f.getName() : "");
        } catch (IOException e) {
            throw e;
        } finally {
            out.finish();
            out.close();
        }
    }

    /**
     * * * 将单个文件或者文件夹压为ZIP文件 * *
     *
     * @param inputFile *
     *            文件夹或者文件 * *
     * @param zipFilename *
     *            压缩文件名称 * *
     * @throws IOException
     */
    public synchronized void zip(File inputFile, String zipFilename) throws IOException {
        SevenZipArchiveOutputStream out = new SevenZipArchiveOutputStream(new FileOutputStream(zipFilename));
        try {
            zip(inputFile, out, inputFile.isDirectory() ? inputFile.getName() : "");
        } catch (IOException e) {
            throw e;
        } finally {
            out.finish();
            out.close();
        }
    }

    /**
     * * * 压缩zip格式的压缩文件 * *
     *
     * @param inputFile *
     *            需压缩文件 * *
     * @param out *
     *            输出压缩文件 * *
     * @param base *
     *            结束标识 * *
     * @throws IOException
     */
    public synchronized void zip(File inputFile, SevenZipArchiveOutputStream out, String base) throws IOException {
        if (inputFile.isDirectory()) {
            if (inputFile.list().length == 0) {
                SevenZipEntry sevenEntry = new SevenZipEntry();
                setSevenZipEntryAttributes(inputFile, sevenEntry);
                sevenEntry.setName(base);
                sevenEntry.setSize(0);
                out.putNextEntry(sevenEntry);
            } else {
                base = base.length() == 0 ? "" : base + "/";
                File[] inputFiles = inputFile.listFiles();
                for (int i = 0; i < inputFiles.length; i++) {
                    zip(inputFiles[i], out, base + inputFiles[i].getName());
                }
            }
        } else {
            if (base.length() > 0) {
                SevenZipEntry sevenEntry = new SevenZipEntry();
                setSevenZipEntryAttributes(inputFile, sevenEntry);
                sevenEntry.setName(base);
                out.putNextEntry(sevenEntry);
                copy(out, new FileInputStream(inputFile));
            } else {
                SevenZipEntry sevenEntry = new SevenZipEntry();
                setSevenZipEntryAttributes(inputFile, sevenEntry);
                out.putNextEntry(sevenEntry);
                copy(out, new FileInputStream(inputFile));
            }
        }
    }

    public static void setSevenZipEntryAttributes(File file, SevenZipEntry sevenEntry) {
        sevenEntry.setName(file.getName());
        sevenEntry.setSize(file.length());
        sevenEntry.setLastWriteTime(file.lastModified());
        sevenEntry.setReadonly(!file.canWrite());
        sevenEntry.setHidden(file.isHidden());
        sevenEntry.setDirectory(file.isDirectory());
        sevenEntry.setArchive(true);
        sevenEntry.setSystem(false);
    }

    public static void copy(OutputStream out, InputStream in) throws IOException {
        byte[] buf = new byte[1024];
        int i;
        while ((i = in.read(buf)) != -1) {
            out.write(buf, 0, i);
        }
    }

    public SevenZip() {
    }

    /**
     * * 取得zip的单例 *
     *
     * @return
     */
    public static SevenZip getZip() {
        if (zipInstance == null) {
            zipInstance = new SevenZip();
        }
        return zipInstance;
    }

    public static void main(String[] args) throws Exception {
        SevenZip zip = new SevenZip();
        zip.zip(new String[] { "d:\\a.txt", "d:\\b.txt", "d:\\c.txt" }, "d:\\m.7z");
    }
}
