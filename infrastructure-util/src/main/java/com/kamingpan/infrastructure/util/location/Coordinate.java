package com.kamingpan.infrastructure.util.location;

import lombok.extern.slf4j.Slf4j;

/**
 * 坐标系
 * 1.（地球坐标）美国GPS使用的是WGS84的坐标系统，以经纬度的形式来表示地球平面上的某一个位置。
 * 2.（火星坐标）我国出于国家安全考虑，国内所有导航电子地图必须使用国家测绘局制定的加密坐标系统，即将一个真实的经纬度坐标加密成一个不正确的经纬度坐标。
 * <p>
 * 坐标关系
 * 高德MapABC地图API【火星坐标】
 * 百度地图API【百度坐标】
 * 腾讯搜搜地图API【火星坐标】
 * 搜狐搜狗地图API【搜狗坐标】
 * 阿里云地图API【火星坐标】
 * 图吧MapBar地图API【图吧坐标】
 * 灵图51ditu地图API【火星坐标】
 *
 * @author kamingpan
 * @since 2019-04-17
 */
@Slf4j
public class Coordinate {

    /**
     * 地球半径
     */
    public static final double R = 6378137;

    /**
     * 圆周率
     */
    public static final double PI = 3.1415926535897932384626;

    /**
     * 圆周率转换量
     */
    public static final double X_PI = Coordinate.PI * 3000.0 / 180.0;

    /**
     * 卫星椭圆球坐标投影到平面地图坐标系的投影因子
     */
    public static final double A = 6378245.0;

    /**
     * 椭球的偏心率
     */
    public static final double EE = 0.00669342162296594323;

    /**
     * 计算地球上任意两点(经纬度)距离
     *
     * @param latitude1  第一点纬度
     * @param longitude1 第一点经度
     * @param latitude2  第二点纬度
     * @param longitude2 第二点经度
     * @return 返回距离 单位：米
     */
    public static double distance(double latitude1, double longitude1, double latitude2, double longitude2) {
        latitude1 = latitude1 * Math.PI / 180.0;
        latitude2 = latitude2 * Math.PI / 180.0;

        double a = latitude1 - latitude2;
        double b = (longitude1 - longitude2) * Math.PI / 180.0;

        double sa2 = Math.sin(a / 2.0);
        double sb2 = Math.sin(b / 2.0);

        return 2 * R * Math.asin(Math.sqrt(sa2 * sa2 + Math.cos(latitude1) * Math.cos(latitude2) * sb2 * sb2));
    }

    /**
     * GPS（WGS－84） 转换成 火星坐标系 (GCJ-02) World Geodetic System ==> Mars Geodetic System
     *
     * @param latitude  纬度（横坐标）
     * @param longitude 经度（纵坐标）
     * @return 火星坐标系的纬经度（横纵坐标）
     */
    public static double[] gpsToGcj02(double latitude, double longitude) {
        double[] dev = calDev(latitude, longitude);
        double retLat = latitude + dev[0];
        double retLon = longitude + dev[1];

        return new double[]{retLat, retLon};
    }

    /**
     * 火星坐标系 (GCJ-02) 转换成 GPS（WGS－84） Mars Geodetic System ==> World Geodetic System
     *
     * @param latitude  纬度（横坐标）
     * @param longitude 经度（纵坐标）
     * @return gps坐标系的纬经度（横纵坐标）
     */
    public static double[] gcj02ToGps(double latitude, double longitude) {
        double[] dev = calDev(latitude, longitude);
        double retLat = latitude - dev[0];
        double retLon = longitude - dev[1];

        dev = calDev(retLat, retLon);
        retLat = latitude - dev[0];
        retLon = longitude - dev[1];

        return new double[]{retLat, retLon};
    }

    /**
     * GPS（WGS－84） 转换成 百度坐标系 (BD-09)
     *
     * @param latitude  纬度（横坐标）
     * @param longitude 经度（纵坐标）
     * @return 百度坐标系的纬经度（横纵坐标）
     */
    public static double[] gpsToBaiDu(double latitude, double longitude) {
        // 先转换成火星坐标系
        double[] gcj02 = gpsToGcj02(latitude, longitude);

        // 再从火星坐标系转为百度坐标系
        return gcj02ToBaiDu(gcj02[0], gcj02[1]);
    }

    /**
     * 火星坐标系 (GCJ-02) 转换成 百度坐标系 (BD-09)
     *
     * @param latitude  纬度（横坐标）
     * @param longitude 经度（纵坐标）
     * @return 百度坐标系的纬经度（横纵坐标）
     */
    public static double[] gcj02ToBaiDu(double latitude, double longitude) {
        double x = longitude;
        double y = latitude;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * X_PI);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * X_PI);

        double lon = z * Math.cos(theta) + 0.0065;
        double lat = z * Math.sin(theta) + 0.006;

        return new double[]{lat, lon};
    }

    /**
     * 百度坐标系 (BD-09) 转换成 火星坐标系 (GCJ-02)
     *
     * @param latitude  纬度（横坐标）
     * @param longitude 经度（纵坐标）
     * @return 火星坐标系的纬经度（横纵坐标）
     */
    public static double[] baiDuToGcj02(double latitude, double longitude) {
        double x = longitude - 0.0065;
        double y = latitude - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * X_PI);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * X_PI);

        double lon = z * Math.cos(theta);
        double lat = z * Math.sin(theta);

        return new double[]{lat, lon};
    }


    //**************************************** 算法分隔行 ********************************************//

    /**
     * 方法作用未知
     *
     * @param latitude  纬度（横坐标）
     * @param longitude 经度（纵坐标）
     * @return {double[]}
     */
    private static double[] calDev(double latitude, double longitude) {
        if (isOutOfChina(latitude, longitude)) {
            return new double[]{0, 0};
        }

        double dLat = toLatitude(longitude - 105.0, latitude - 35.0);
        double dLon = toLongitude(longitude - 105.0, latitude - 35.0);

        double radLat = latitude / 180.0 * PI;
        double magic = Math.sin(radLat);
        magic = 1 - EE * magic * magic;

        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((A * (1 - EE)) / (magic * sqrtMagic) * PI);
        dLon = (dLon * 180.0) / (A / sqrtMagic * Math.cos(radLat) * PI);

        return new double[]{dLat, dLon};
    }

    /**
     * 是否中国境外
     *
     * @param latitude  纬度（横坐标）
     * @param longitude 经度（纵坐标）
     * @return 是否中国境外
     */
    private static boolean isOutOfChina(double latitude, double longitude) {
        return longitude < 72.004 || longitude > 137.8347 || latitude < 0.8293 || latitude > 55.8271;

        /*if (longitude < 72.004 || longitude > 137.8347) {
            return true;
        }
        if (latitude < 0.8293 || latitude > 55.8271) {
            return true;
        }
        return false;*/
    }

    /**
     * 转换为纬度（横坐标）
     *
     * @param latitude  纬度（横坐标）
     * @param longitude 经度（纵坐标）
     * @return 转换后的纬度（横坐标）
     */
    private static double toLatitude(double latitude, double longitude) {
        double ret = -100.0 + 2.0 * latitude + 3.0 * longitude + 0.2 * longitude * longitude
                + 0.1 * latitude * longitude + 0.2 * Math.sqrt(Math.abs(latitude));
        ret += (20.0 * Math.sin(6.0 * latitude * PI) + 20.0 * Math.sin(2.0 * latitude * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(longitude * PI) + 40.0 * Math.sin(longitude / 3.0 * PI)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(longitude / 12.0 * PI) + 320 * Math.sin(longitude * PI / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    /**
     * 转换为经度（纵坐标）
     *
     * @param latitude  纬度（横坐标）
     * @param longitude 经度（纵坐标）
     * @return 转换后的经度（纵坐标）
     */
    private static double toLongitude(double latitude, double longitude) {
        double ret = 300.0 + latitude + 2.0 * longitude + 0.1 * latitude * latitude
                + 0.1 * latitude * longitude + 0.1 * Math.sqrt(Math.abs(latitude));
        ret += (20.0 * Math.sin(6.0 * latitude * PI) + 20.0 * Math.sin(2.0 * latitude * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(latitude * PI) + 40.0 * Math.sin(latitude / 3.0 * PI)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(latitude / 12.0 * PI) + 300.0 * Math.sin(latitude / 30.0 * PI)) * 2.0 / 3.0;
        return ret;
    }

}
