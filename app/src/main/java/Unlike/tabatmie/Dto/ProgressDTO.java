package Unlike.tabatmie.Dto;

public class ProgressDTO {

    int cnt;
    boolean center;

    public ProgressDTO(int cnt, boolean center) {
        this.cnt = cnt;
        this.center = center;
    }

    public int getCnt() {
        return cnt;
    }

    public boolean isCenter() {
        return center;
    }
}
