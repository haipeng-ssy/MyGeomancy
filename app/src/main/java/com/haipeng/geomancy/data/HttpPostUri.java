package com.haipeng.geomancy.data;

/**
 * Created by Administrator on 2015/2/28.
 */
public class HttpPostUri {
    //阿里云
    static String Data_Ip = "http://115.28.65.6:8081";

//    static String Data_Ip = "http://58.241.11.114:8080";
    public static final String home_chose_uri=Data_Ip+"/Geo/choose";
    public static final String home_xialingshi_uri =Data_Ip+"/Geo/isDayLight";
    public static final String register_uri =Data_Ip+"/Geo/register";
    public static final String home_chose_question_uri=Data_Ip+"/Geo/getAnswer";
    public static final String total_evaluate =Data_Ip+"/Geo/simpleCalculate";
    public static final String eight_towards =Data_Ip+"/Geo/calculate";
    public static final String user_eva_app =Data_Ip+"/Geo/evaluation";

    static String PAY_Ip ="http://sj.layl.cn/plus";
    //static String PAY_Ip="http://layl.kaydong.com/plus";
//    付款链接地址 XXX更换成相应的数据即可
//    http://layl.kaydong.com/plus/pay.aspx?UserId=XXX&payTitle=XXX&payMoney=XXX
//
//    获取状态：
//    http://layl.kaydong.com/plus/GetResult.aspx
//    UserId 用户ID
//    返回json格式说明
//
//    {"_adddate":"\/Date(1397232000000+0800)\/","_id":1,"_ordeno":"251425788596321","_paymoney":"200","_paytitle":"还好的吧","_tradeno":"25478541256325","_userid":"12"}
//
//    _adddate:付款时间
//    _id:ID
//    _paymoney:金额
//    _paytitle:商品标题
//    _tradeno:交易号（支付宝交易，如为空则没有支付成功）
//    _orderno:订单号
//    _userid:用户ID




    public static final String pay_uri =PAY_Ip+"/pay.aspx?";
    public static final String get_pay_uri = PAY_Ip+"/GetResult.aspx";
}
