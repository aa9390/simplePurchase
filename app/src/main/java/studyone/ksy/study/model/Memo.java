package studyone.ksy.study.model;

import java.util.Date;

public class Memo {
    private String txt, title;
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

    // 첫 개행문자가 나올 때까지를 제목으로 정함.
    public String getTitle() {
        if(!txt.isEmpty()) {
            if(txt.indexOf( "\n" ) > -1) {
                return txt.substring( 0, txt.indexOf( "\n" ) );
            }
            else {
                return txt;
            }
        }
        return title;
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

    public void setTitle(String title) {
        this.title = title;
    }
}
