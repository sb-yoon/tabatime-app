package Unlike.tabatmie.Dto;

public class ProgressDTO {

    String tv_cnt;
    boolean center;

    public ProgressDTO(String tv_cnt, boolean center) {
        this.tv_cnt = tv_cnt;
        this.center = center;
    }

    public ProgressDTO(boolean center) {
        this.center = center;
    }

    public String getTv_cnt() {
        return tv_cnt;
    }

    public void setTv_cnt(String tv_cnt) {
        this.tv_cnt = tv_cnt;
    }

    public boolean isCenter() {
        return center;
    }

    public void setCenter(boolean center) {
        this.center = center;
    }
}
