package com.smartfit.beans;

/**
 * Created by dengyancheng on 16/4/2.
 */
public class CoachCertificateItem {

    private String CertificateName;
    private String CertificateImg;
    private String Type;

    public CoachCertificateItem(String certificateName, String certificateImg, String type) {
        CertificateName = certificateName;
        CertificateImg = certificateImg;
        Type = type;
    }

    public String getCertificateName() {
        return CertificateName;
    }

    public void setCertificateName(String certificateName) {
        CertificateName = certificateName;
    }

    public String getCertificateImg() {
        return CertificateImg;
    }

    public void setCertificateImg(String certificateImg) {
        CertificateImg = certificateImg;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }
}
