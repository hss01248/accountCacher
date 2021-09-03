package com.hss01248.accountcache;

import androidx.annotation.Keep;



/**
 * Created by huangshuisheng on 2018/10/26.
 */
@Keep
public class DebugAccount {

    public Long id;
    public long updateTime;

    public String countryCode;

    public String account;
    public String pw;
    public int hostType;
    public int usedNum;
    public int position;


    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public long getUpdateTime() {
        return this.updateTime;
    }
    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }
    public String getCountryCode() {
        return this.countryCode;
    }
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
    public String getAccount() {
        return this.account;
    }
    public void setAccount(String account) {
        this.account = account;
    }
    public String getPw() {
        return this.pw;
    }
    public void setPw(String pw) {
        this.pw = pw;
    }
    public int getHostType() {
        return this.hostType;
    }
    public void setHostType(int hostType) {
        this.hostType = hostType;
    }
    public int getUsedNum() {
        return this.usedNum;
    }
    public void setUsedNum(int usedNum) {
        this.usedNum = usedNum;
    }
    public int getPosition() {
        return this.position;
    }
    public void setPosition(int position) {
        this.position = position;
    }




}
