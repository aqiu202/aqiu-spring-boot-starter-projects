package com.github.aqiu202.util.http;

import com.github.aqiu202.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public abstract class HttpHeaderUtils {
    private static final Map<String, String> ContentMap = new HashMap<>();

    static {
        ContentMap.put("001", "application/x-001");
        ContentMap.put("301", "application/x-301");
        ContentMap.put("ez", "application/andrew-inset");
        ContentMap.put("hqx", "application/mac-binhex40");
        ContentMap.put("cpt", "application/mac-compactpro");
        ContentMap.put("doc", "application/msword");
        ContentMap.put("docx", "application/msword");
        ContentMap.put("bin", "application/octet-stream");
        ContentMap.put("dms", "application/octet-stream");
        ContentMap.put("lha", "application/octet-stream");
        ContentMap.put("lzh", "application/octet-stream");
        ContentMap.put("exe", "application/octet-stream");
        ContentMap.put("class", "application/octet-stream");
        ContentMap.put("so", "application/octet-stream");
        ContentMap.put("dll", "application/octet-stream");
        ContentMap.put("oda", "application/oda");
        ContentMap.put("pdf", "application/pdf");
        ContentMap.put("ai", "application/postscript");
        ContentMap.put("eps", "application/postscript");
        ContentMap.put("ps", "application/postscript");
        ContentMap.put("smi", "application/smil");
        ContentMap.put("smil", "application/smil");
        ContentMap.put("mif", "application/vnd.mif");

        ContentMap.put("xls", "application/vnd.ms-excel");
        ContentMap.put("ppt", "application/vnd.ms-powerpoint");

        ContentMap.put("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        ContentMap.put("pptx", "application/vnd.ms-powerpoint");

        ContentMap.put("wbxml", "application/vnd.wap.wbxml");
        ContentMap.put("wmlc", "application/vnd.wap.wmlc");
        ContentMap.put("wmlsc", "application/vnd.wap.wmlscriptc");
        ContentMap.put("bcpio", "application/x-bcpio");
        ContentMap.put("vcd", "application/x-cdlink");
        ContentMap.put("pgn", "application/x-chess-pgn");
        ContentMap.put("cpio", "application/x-cpio");
        ContentMap.put("csh", "application/x-csh");
        ContentMap.put("dcr", "application/x-director");
        ContentMap.put("dir", "application/x-director");
        ContentMap.put("dxr", "application/x-director");
        ContentMap.put("dvi", "application/x-dvi");
        ContentMap.put("spl", "application/x-futuresplash");
        ContentMap.put("gtar", "application/x-gtar");
        ContentMap.put("hdf", "application/x-hdf");
        ContentMap.put("js", "application/x-javascript");
        ContentMap.put("skp", "application/x-koan");
        ContentMap.put("skd", "application/x-koan");
        ContentMap.put("skt", "application/x-koan");
        ContentMap.put("skm", "application/x-koan");
        ContentMap.put("latex", "application/x-latex");
        ContentMap.put("globalConn", "application/x-netcdf");
        ContentMap.put("cdf", "application/x-netcdf");
        ContentMap.put("sh", "application/x-sh");
        ContentMap.put("shar", "application/x-shar");
        ContentMap.put("swf", "application/x-shockwave-flash");
        ContentMap.put("sit", "application/x-stuffit");
        ContentMap.put("sv4cpio", "application/x-sv4cpio");
        ContentMap.put("sv4crc", "application/x-sv4crc");
        ContentMap.put("tar", "application/x-tar");
        ContentMap.put("tcl", "application/x-tcl");
        ContentMap.put("tex", "application/x-tex");
        ContentMap.put("texinfo", "application/x-texinfo");
        ContentMap.put("texi", "application/x-texinfo");
        ContentMap.put("t", "application/x-troff");
        ContentMap.put("tr", "application/x-troff");
        ContentMap.put("roff", "application/x-troff");
        ContentMap.put("man", "application/x-troff-man");
        ContentMap.put("me", "application/x-troff-me");
        ContentMap.put("ms", "application/x-troff-ms");
        ContentMap.put("ustar", "application/x-ustar");
        ContentMap.put("src", "application/x-wais-source");
        ContentMap.put("xhtml", "application/xhtml+xml");
        ContentMap.put("xht", "application/xhtml+xml");
        ContentMap.put("zip", "application/zip");
        ContentMap.put("au", "audio/basic");
        ContentMap.put("snd", "audio/basic");
        ContentMap.put("mid", "audio/midi");
        ContentMap.put("midi", "audio/midi");
        ContentMap.put("kar", "audio/midi");
        ContentMap.put("mpga", "audio/mpeg");
        ContentMap.put("mp2", "audio/mpeg");
        ContentMap.put("mp3", "audio/mpeg");
        ContentMap.put("aif", "audio/x-aiff");
        ContentMap.put("aiff", "audio/x-aiff");
        ContentMap.put("aifc", "audio/x-aiff");
        ContentMap.put("m3u", "audio/x-mpegurl");
        ContentMap.put("ram", "audio/x-pn-realaudio");
        ContentMap.put("rm", "audio/x-pn-realaudio");
        ContentMap.put("rpm", "audio/x-pn-realaudio-plugin");
        ContentMap.put("ra", "audio/x-realaudio");
        ContentMap.put("wav", "audio/x-wav");
        ContentMap.put("pdb", "chemical/x-pdb");
        ContentMap.put("xyz", "chemical/x-xyz");
        ContentMap.put("bmp", "image/bmp");
        ContentMap.put("gif", "image/gif");
        ContentMap.put("ief", "image/ief");
        ContentMap.put("jpeg", "image/jpeg");
        ContentMap.put("jpg", "image/jpeg");
        ContentMap.put("jpe", "image/jpeg");
        ContentMap.put("png", "image/png");
        ContentMap.put("tiff", "image/tiff");
        ContentMap.put("tif", "image/tiff");
        ContentMap.put("djvu", "image/vnd.djvu");
        ContentMap.put("djv", "image/vnd.djvu");
        ContentMap.put("wbmp", "image/vnd.wap.wbmp");
        ContentMap.put("ras", "image/x-cmu-raster");
        ContentMap.put("pnm", "image/x-portable-anymap");
        ContentMap.put("pbm", "image/x-portable-bitmap");
        ContentMap.put("pgm", "image/x-portable-graymap");
        ContentMap.put("ppm", "image/x-portable-pixmap");
        ContentMap.put("rgb", "image/x-rgb");
        ContentMap.put("xbm", "image/x-xbitmap");
        ContentMap.put("xpm", "image/x-xpixmap");
        ContentMap.put("xwd", "image/x-xwindowdump");
        ContentMap.put("igs", "model/iges");
        ContentMap.put("iges", "model/iges");
        ContentMap.put("msh", "model/mesh");
        ContentMap.put("mesh", "model/mesh");
        ContentMap.put("silo", "model/mesh");
        ContentMap.put("wrl", "model/vrml");
        ContentMap.put("vrml", "model/vrml");
        ContentMap.put("css", "text/css");
        ContentMap.put("html", "text/html;charset=UTF-8");
        ContentMap.put("htm", "text/html;charset=UTF-8");
        ContentMap.put("asc", "text/plain");
        ContentMap.put("txt", "text/plain");
        ContentMap.put("java", "text/plain");
        ContentMap.put("rtx", "text/richtext");
        ContentMap.put("rtf", "text/rtf");
        ContentMap.put("sgml", "text/sgml");
        ContentMap.put("sgm", "text/sgml");
        ContentMap.put("tsv", "text/tab-separated-values");
        ContentMap.put("wml", "text/vnd.wap.wml");
        ContentMap.put("wmls", "text/vnd.wap.wmlscript");
        ContentMap.put("etx", "text/x-setext");
        ContentMap.put("xsl", "text/xml");
        ContentMap.put("xml", "text/xml");
        ContentMap.put("mpeg", "video/mpeg");
        ContentMap.put("mpg", "video/mpeg");
        ContentMap.put("mpe", "video/mpeg");
        ContentMap.put("qt", "video/quicktime");
        ContentMap.put("mov", "video/quicktime");
        ContentMap.put("mxu", "video/vnd.mpegurl");
        ContentMap.put("avi", "video/x-msvideo");
        ContentMap.put("movie", "video/x-sgi-movie");
        ContentMap.put("ice", "x-conference/x-cooltalk");
        //自定义类型
        ContentMap.put("apf", "application/x-apf");
    }

    /**
     * 得到需要写入的 html http 类型
     *
     * @param typeName 类型名，一般以后缀名呈现
     */
    public static String getContentType(String typeName) {
        if (StringUtils.isBlank(typeName)) {
            return "application/octet-stream";
        }
        typeName = typeName.toLowerCase();
        String v = ContentMap.get(typeName);
        return v == null ? "application/octet-stream" : v;
    }

    /**
     * 设置返回头
     *
     * @param response 响应对象
     * @param fileName 文件名
     * @param typeName 类型名
     */
    public static void setHeaderForDownload(HttpServletResponse response, String typeName, String fileName) {
        if (response == null) {
            return;
        }
        String contentType = getContentType(typeName);
        response.setContentType(contentType);

        if (StringUtils.isNotBlank(fileName)) {
            response.setHeader("Content-Disposition", "attachment;filename=" + formatAttachmentFileName(fileName));
            //需要暴露给前端js
            response.addHeader("Access-Control-Expose-Headers", "Content-Disposition");
        }
    }

    public static void setContentType(HttpServletResponse response, String fileName) {
        setHeaderForDownload(response, getFileSuffix(fileName), null);
    }

    /**
     * 设置返回头
     *
     * @param response 响应对象
     * @param fileName 文件名
     */
    public static void setHeaderForDownload(HttpServletResponse response, String fileName) {
        if (StringUtils.isBlank(fileName)) {
            setHeaderForDownload(response, "", "");
            return;
        }
        String suffixName = getFileSuffix(fileName);
        //设置响应头
        setHeaderForDownload(response, suffixName, fileName);
    }

    private static String getFileSuffix(String fileName) {
        String suffixName = "";
        if (fileName.lastIndexOf('.') > 0 && fileName.lastIndexOf('.') < (fileName.length() - 1)) {
            suffixName = fileName.substring(fileName.lastIndexOf('.') + 1);
        }
        return suffixName;
    }

    public static String formatAttachmentFileName(String str) {
        if (StringUtils.isBlank(str)) {
            return "";
        }
        try {
            //new String(fileName.getBytes(), "ISO8859-1")
            //return new String(str.getBytes("UTF-8"), "iso8859-1");
            return URLEncoder.encode(str, StandardCharsets.UTF_8.name());
        } catch (Exception e) {
            return str;
        }
        //return str;
    }

    public static void main(String[] args) {
        String fileName = "测试.txt";
        System.out.println(formatAttachmentFileName(fileName));
        System.out.println(new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));
    }
}
