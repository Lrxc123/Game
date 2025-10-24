package package2;

public class Test {
    public static void main(String[] args) {
        //获得Runtime对象
//        Runtime r1=Runtime.getRuntime();
        System.out.println(Runtime.getRuntime().freeMemory()/1024/1024);
        //运行cmd命令
        //shutdown 关机
        //-s 默认在一分钟之后关机
        //-s -t 指定关机时间 秒
        //-a 取消关机
        //-r 关机并重启
        Runtime.getRuntime().exec("")
    }
}
