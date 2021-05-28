# account cacher

测试账号加密后保存到sd卡,便于下次直接选择

会申请存储权限. 暂未适配Android api 30.



![image-20210528093344350](/Users/hss/Library/Application Support/typora-user-images/image-20210528093344350.png)



```java
public static void configHostType(int dev,int test,int release){
        TYPE_RELEASE = release;
        TYPE_DEV = dev;
        TYPE_TEST = test;
    }

/**
     * 不会在正式环境弹出
     *
     * @param activity
     * @param countryCode
     * @param callback
     */
    public static void selectAccount(int hostType, FragmentActivity activity, String countryCode, AccountCallback callback)
      
      
       /**
     * 登录成功后,保存账号密码
     * 不会保存正式服的账号密码
     *
     * @param countryCode
     * @param account
     * @param pw
     */
    public static void saveAccount(Activity activity, int currentHostType, final String countryCode, String account, String pw)
```





![image-20210528103805555](https://gitee.com/hss012489/picbed/raw/master/picgo/1622169485620-image-20210528103805555.jpg)

填充到登录信息:

![image-20210528103848856](https://gitee.com/hss012489/picbed/raw/master/picgo/1622169528892-image-20210528103848856.jpg)

登录成功后调用:

saveAccount(Activity activity, int currentHostType, final String countryCode, String account, String pw)