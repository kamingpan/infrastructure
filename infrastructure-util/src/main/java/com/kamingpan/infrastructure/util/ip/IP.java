package com.kamingpan.infrastructure.util.ip;

import com.fasterxml.jackson.databind.JsonNode;
import com.kamingpan.infrastructure.util.conversion.JsonConversion;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * IP工具
 *
 * @author kamingpan
 * @since 2016-03-08
 */
@Slf4j
public class IP {

    // 匹配ip所在地的请求地址
    private static final String REQUEST_URL = "http://ip.taobao.com/service/getIpInfo.php";

    // 服务器端请求编码格式。如GBK,UTF-8等
    private static final String ENCODING = "UTF-8";

    /**
     * 获取发起请求的网络ip
     *
     * @param request 请求
     * @return ip
     */
    public static String getIP(HttpServletRequest request) {
        if (null == request) {
            return null;
        }
        String ip = request.getHeader("x-forwarded-for");
        if (null == ip || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (null == ip || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (null == ip || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if (ip.equals("127.0.0.1") || ip.equals("0:0:0:0:0:0:0:1")) {
                // 根据网卡取本机配置的IP
                InetAddress inetAddress;
                try {
                    inetAddress = InetAddress.getLocalHost();
                    ip = inetAddress.getHostAddress();
                } catch (UnknownHostException exception) {
                    log.warn("获取机器ip失败", exception);
                }
            }
        }
        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (null != ip && ip.length() > 15) { //"***.***.***.***".length() = 15
            if (ip.indexOf(",") > 0) {
                ip = ip.substring(0, ip.indexOf(","));
            }
        }
        return ip;
    }

    /**
     * 匹配ip所在地址
     *
     * @param ip ip
     * @return address 所在地址
     */
    public static String getAddress(String ip) {
        String address;
        if (null == ip || ip.isEmpty()) {
            return null;
        }

        return getAddresses("ip=" + ip, ENCODING);
    }

    /**
     * 获取指定ip的mac地址
     *
     * @param ip ip
     * @return mac
     */
    public static String getMac(String ip) {
        String mac = getMacInLinux(ip).trim();
        if (mac.isEmpty()) {
            mac = getMacInWindows(ip).trim();
        }
        return mac;
    }


    // *******************************************************************************


    /**
     * 从外部库中匹配ip相应的所在地址
     *
     * @param content  请求的参数 格式为：ip=xxx&???=xxx
     * @param encoding 服务器端请求编码。如GBK,UTF-8等
     * @return ip所在地址
     * @throws UnsupportedEncodingException 不支持字符编码异常
     */
    private static String getAddresses(String content, String encoding) {
        // 调用pconline的接口
        // 从requestUrl取得IP所在的省市区信息
        String returnString = IP.getResult(REQUEST_URL, content, encoding);

        // 输出地区json数组字符编码
        // log.debug(returnString);

        try {
            // 处理返回的省市区信息（转换为对象处理）
            JsonNode jsonNode = JsonConversion.OBJECT_MAPPER.readTree(returnString);
            IPResponse ipResponse = JsonConversion.convertToObject(jsonNode.get("data").toString(), IPResponse.class);
            return ipResponse.getDetail();
        } catch (IOException exception) {
            return returnString;
        }
    }

    /**
     * 发送请求获取地址信息
     *
     * @param requestUrl 请求的地址
     * @param content    请求的参数 格式为：ip=xxx&???=xxx
     * @param encoding   服务器端请求编码。如GBK,UTF-8等
     * @return ip所在地址信息
     */
    private static String getResult(String requestUrl, String content, String encoding) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(requestUrl);
            connection = (HttpURLConnection) url.openConnection(); // 新建连接实例
            connection.setConnectTimeout(2000); // 设置连接超时时间，单位毫秒
            connection.setReadTimeout(2000); // 设置读取数据超时时间，单位毫秒
            connection.setDoOutput(true); // 是否打开输出流 true|false
            connection.setDoInput(true); // 是否打开输入流true|false
            connection.setRequestMethod("POST"); // 提交方法POST|GET
            connection.setUseCaches(false); // 是否缓存true|false
            connection.connect(); // 打开连接端口
            DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream()); // 打开输出流往对端服务器写数据
            dataOutputStream.writeBytes(content); // 提交表单 name=xxx&pwd=xxx
            dataOutputStream.flush(); // 刷新
            dataOutputStream.close(); // 关闭输出流
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), encoding)); // 往对端写完数据对端服务器返回数据 ,以BufferedReader流来读取
            StringBuilder buffer = new StringBuilder();
            String line;
            while (null != (line = reader.readLine())) {
                buffer.append(line);
            }
            reader.close();
            return buffer.toString();
        } catch (IOException exception) {
            log.warn("IP地址匹配失败", exception);
            return "IP地址匹配失败：" + exception.getMessage();
        } finally {
            if (null != connection) {
                connection.disconnect(); // 关闭连接
            }
        }
    }

    /**
     * decodeUnicode 转换字符串为中文
     *
     * @param text 转换的文本
     * @return 转换后的字符串
     */
    private static String decodeUnicode(String text) {
        char aChar;
        int length = text.length();
        StringBuilder stringBuilder = new StringBuilder(length);
        for (int x = 0; x < length; ) {
            aChar = text.charAt(x++);
            if (aChar == '\\') {
                aChar = text.charAt(x++);
                if (aChar == 'u') {
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = text.charAt(x++);
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException("Malformed encoding.");
                        }
                    }
                    stringBuilder.append((char) value);
                } else {
                    if (aChar == 't') {
                        aChar = '\t';
                    } else if (aChar == 'r') {
                        aChar = '\r';
                    } else if (aChar == 'n') {
                        aChar = '\n';
                    } else if (aChar == 'f') {
                        aChar = '\f';
                    }
                    stringBuilder.append(aChar);
                }
            } else {
                stringBuilder.append(aChar);
            }
        }
        return stringBuilder.toString();
    }

    /**
     * window系统获取指定ip的mac地址
     *
     * @param ip 目标ip
     * @return Mac Address
     */
    private static String getMacInWindows(String ip) {
        String[] cmd = {"cmd", "/c", "ping " + ip};
        String[] another = {"cmd", "/c", "arp -a"};

        String cmdResult = sendCmdRequest(cmd, another);
        return macFilter(ip, cmdResult, "-");
    }

    /**
     * linux系统获取指定ip的mac地址
     *
     * @param ip 目标ip
     * @return Mac Address
     */
    private static String getMacInLinux(String ip) {
        String[] cmd = {"/bin/sh", "-c", "ping " + ip + " -c 2 && arp -a"};
        String cmdResult = sendCmdRequest(cmd);
        return macFilter(ip, cmdResult, ":");
    }

    /**
     * 发送命令
     *
     * @param cmd 命令
     * @return 执行结果
     */
    private static String sendCmdRequest(String[] cmd) {
        StringBuilder result = new StringBuilder("");
        String line;
        try {
            Process proc = Runtime.getRuntime().exec(cmd);
            InputStreamReader inputStreamReader = new InputStreamReader(proc.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            while (null != (line = bufferedReader.readLine())) {
                result.append(line);
            }
            bufferedReader.close();
            inputStreamReader.close();
        } catch (Exception exception) {
            log.warn("发送cmd命令失败", exception);
        }
        return result.toString();
    }

    /**
     * 发送命令
     *
     * @param cmd     第一个命令
     * @param another 第二个命令
     * @return 第二个命令的执行结果
     */
    private static String sendCmdRequest(String[] cmd, String[] another) {
        StringBuilder result = new StringBuilder("");
        String line;
        try {
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec(cmd);
            proc.waitFor(); //已经执行完第一个命令，准备执行第二个命令
            proc = rt.exec(another);
            InputStreamReader inputStreamReader = new InputStreamReader(proc.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            while (null != (line = bufferedReader.readLine())) {
                result.append(line);
            }
            bufferedReader.close();
            inputStreamReader.close();
        } catch (Exception exception) {
            log.warn("发送cmd命令失败", exception);
        }
        return result.toString();
    }

    /**
     * 将返回的信息过滤，获取mac地址
     *
     * @param ip           目标ip,一般在局域网内
     * @param sourceString 命令处理的结果字符串
     * @param macSeparator mac分隔符号
     * @return mac地址，用上面的分隔符号表示
     */
    private static String macFilter(String ip, String sourceString, String macSeparator) {
        String result = "";
        String regExp = "((([0-9,A-F,a-f]{1,2}" + macSeparator + "){1,5})[0-9,A-F,a-f]{1,2})";
        Pattern pattern = Pattern.compile(regExp);
        Matcher matcher = pattern.matcher(sourceString);
        while (matcher.find()) {
            result = matcher.group(1);
            // 如果有多个IP,只匹配本IP对应的Mac.
            if (sourceString.indexOf(ip) <= sourceString.lastIndexOf(matcher.group(1))) {
                break;
            }
        }
        return result;
    }

    /**
     * 获取服务器ip
     *
     * @return 服务器ip
     */
    public static String getServerIP() {
        String serverIP = null;

        Enumeration<NetworkInterface> netInterfaces;
        try {
            netInterfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException exception) {
            log.warn("无法获取服务器ip", exception);
            return null;
        }

        while (netInterfaces.hasMoreElements()) {
            NetworkInterface networkInterface = netInterfaces.nextElement();
            Enumeration<InetAddress> netAddresses = networkInterface.getInetAddresses();
            while (netAddresses.hasMoreElements()) {
                InetAddress inetAddress = netAddresses.nextElement();
                // serverIP = inetAddress.getHostAddress();

                // 获取符合条件的ip
                if (null != inetAddress
                        && !inetAddress.isSiteLocalAddress() && !inetAddress.isLoopbackAddress()
                        && inetAddress instanceof Inet4Address
                        && inetAddress.getHostAddress().contains(".")) {
                    serverIP = inetAddress.getHostAddress();
                } else {
                    serverIP = null;
                }
            }
        }

        return serverIP;
    }

    /**
     * 获取本地ip
     *
     * @return 本地ip
     * @throws UnknownHostException 未知host异常
     */
    public static String getLocalIP() throws UnknownHostException {
        InetAddress inetAddress = InetAddress.getLocalHost();

        byte[] ips = inetAddress.getAddress();
        StringBuilder localIP = new StringBuilder();
        for (int i = 0; i < ips.length; i++) {
            if (i > 0) {
                localIP.append(".");
            }
            localIP.append(ips[i] & 0xFF);
        }
        return localIP.toString();
    }

    public static void main(String[] args) throws IOException {
        log.debug(IP.getAddress("183.6.174.133"));
    }

}
