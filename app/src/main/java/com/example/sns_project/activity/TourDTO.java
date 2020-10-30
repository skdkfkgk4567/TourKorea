package com.example.sns_project.activity;

public class TourDTO {

    private double xpos;
    private double ypos;
    private String name;
    private String tel;
    private String Image1;
    private String Image2;
    private String Addr;
    public double getXpos() {
        return xpos;
    }
    public void setXpos(double xpos) {
        this.xpos = xpos;
    }
    public double getYpos() {
        return ypos;
    }
    public void setYpos(double ypos) {
        this.ypos = ypos;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setTel(String tel) {
        this.tel = tel;
    }
    public String getTel() {
        return tel;
    }
    public void setImage1(String Image1) {
        this.Image1 = Image1;
    }
    public String getImage1() {
        return Image1;
    }
    public void setImage2(String Image2) {
        this.Image2 = Image2;
    }
    public String getImage2() {
        return Image2;
    }
    public String getAddr() {
        return Addr;
    }
    public void setAddr(String Addr) {
        this.Addr = Addr;
    }
}
