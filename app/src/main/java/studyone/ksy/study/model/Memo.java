package studyone.ksy.study.model;

public class Memo {
    private String txt, title, key;
    private long createDate, updateDate;    // DB 설정에 따라 추후 long 타입으로 바꿔야 할수도

    public String getTxt() {
        return txt;
    }

    public long getCreateDate() {
        return createDate;
    }

    public long getUpdateDate() {
        return updateDate;
    }

    // 첫 개행문자가 나올 때까지를 제목으로 정함.
    public String getTitle() {
        if(txt != null) {
            if(txt.indexOf( "\n" ) > -1) {
                return txt.substring( 0, txt.indexOf( "\n" ) );
            }
            else {
                return txt;
            }
        }
        return title;
    }

    public String getKey() {
        return key;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public void setUpdateDate(long updateDate) {
        this.updateDate = updateDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
