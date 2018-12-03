package studyone.ksy.study.model;

import java.util.Date;

public class Memo {
    private String txt;
    private Date createDate, updateDate;

    public String getTxt() {
        return txt;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
