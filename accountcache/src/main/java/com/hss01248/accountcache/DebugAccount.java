package com.hss01248.accountcache;

import androidx.annotation.Keep;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.Arrays;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Index;

/**
 * Created by huangshuisheng on 2018/10/26.
 */
@Keep
@Entity
public class DebugAccount {
    @Id
    public Long id;
    public long updateTime;

    public String countryCode;

    public String account;
    public String pw;
    public int hostType;
    public int usedNum;
    public int position;

    @Override
    public String toString() {
        return "DebugAccount{" +
                "id=" + id +
                ", updateTime=" + updateTime +
                ", countryCode='" + countryCode + '\'' +
                ", account='" + account + '\'' +
                ", pw='" + pw + '\'' +
                ", hostType=" + hostType +
                ", usedNum=" + usedNum +
                ", position=" + position +
                '}';
    }

    @Generated(hash = 142474538)
    public DebugAccount(Long id, long updateTime, String countryCode,
            String account, String pw, int hostType, int usedNum, int position) {
        this.id = id;
        this.updateTime = updateTime;
        this.countryCode = countryCode;
        this.account = account;
        this.pw = pw;
        this.hostType = hostType;
        this.usedNum = usedNum;
        this.position = position;
    }
    @Generated(hash = 65329523)
    public DebugAccount() {
    }
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
