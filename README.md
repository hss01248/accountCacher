# account cacher

测试账号加密后保存到sd卡(.yuv/databases/名字+testaccount2.db), 便于下次直接选择,不用再记账号.

默认只存储dev和test账号,不存储release账号(可配置存储).

提供no-op功能,便于上线.







# gradle

[![](https://jitpack.io/v/hss01248/accountCacher.svg)](https://jitpack.io/#hss01248/accountCacher)

Add it in your root build.gradle at the end of repositories:

```css
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```



## 普通工程

**Step 2.** Add the dependency

```css
	dependencies {
	        implementation 'com.github.hss01248:accountCacher:1.0.0'
	}
```

## 组件化工程

```groovy
dependencies {
	        implementation 'com.github.hss01248:accountCacher:1.0.0'
	}
```











# api



![image-20210903101416745](https://gitee.com/hss012489/picbed/raw/master/picgo/1630635264025-image-20210903101416745.jpg)



```java
    AccountCacher.storeReleaseAccount = true;//配置可存储正式环境账号
/*
    * @param dbName                可以为空. 为空则存储于默认数据库
     * @param hasAdaptScopedStorage 是否已经适配Android11的分区存储
     */
    public static void init(@Nullable String dbName, boolean hasAdaptScopedStorage) {

public static void configHostType(int dev,int test,int release){
        TYPE_RELEASE = release;
        TYPE_DEV = dev;
        TYPE_TEST = test;
    }



/**
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

# 示例代码

```java
public class BaseApp extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        AccountCacher.init("",true);
        AccountCacher.storeReleaseAccount = true;
        AccountCacher.configHostType(1,3,0);
    }
}


登录页面:
AccountCacher.selectAccount(环境类型, this, "国家码", new AccountCallback() {
            @Override
            public void onSuccess(DebugAccount account) {
                //将DebugAccount里的用户名密码 设置给登录页面的用户名,密码输入框
            }

            @Override
            public void onError(Throwable error) {
                error.printStackTrace();
            }
        });

//注册/登录成功后:
 AccountCacher.saveAccount(this,环境类型,"国家码","用户名","明文密码");
```



