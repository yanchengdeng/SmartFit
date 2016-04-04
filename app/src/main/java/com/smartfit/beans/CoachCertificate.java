package com.smartfit.beans;

import java.util.List;

/**
 * Created by dengyancheng on 16/4/2.
 * <p/>
 * 申请教练认证  资料
 */
public class CoachCertificate {


    private String Id;
    private String CoachRealName;
    private CoachCertificateItem coachCertificateCard;//身份证
    private CoachCertificateItem coachCertificateWord;//工作证


    private List<CoachCertificateItem> coachCertificateAuths;//证书

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getCoachRealName() {
        return CoachRealName;
    }

    public void setCoachRealName(String coachRealName) {
        CoachRealName = coachRealName;
    }

    public List<CoachCertificateItem> getCoachCertificateAuths() {
        return coachCertificateAuths;
    }

    public void setCoachCertificateAuths(List<CoachCertificateItem> coachCertificateAuths) {
        this.coachCertificateAuths = coachCertificateAuths;
    }

    public CoachCertificateItem getCoachCertificateCard() {
        return coachCertificateCard;
    }

    public void setCoachCertificateCard(CoachCertificateItem coachCertificateCard) {
        this.coachCertificateCard = coachCertificateCard;
    }

    public CoachCertificateItem getCoachCertificateWord() {
        return coachCertificateWord;
    }

    public void setCoachCertificateWord(CoachCertificateItem coachCertificateWord) {
        this.coachCertificateWord = coachCertificateWord;
    }
}
