package com.person.pojo.dto;

/**
 * @Description 请求头信息数据传输类
 * @Author Xutong Li
 * @Date 2021/5/7
 */
public class HeaderDTO {

    private String alg;

    private String typ;

    public HeaderDTO(String alg, String typ) {
        this.alg = alg;
        this.typ = typ;
    }

    public String getAlg() {
        return alg;
    }

    public void setAlg(String alg) {
        this.alg = alg;
    }

    public String getTyp() {
        return typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }
}
