package Unlike.tabatmie.Dto;

public class RecordDTO {

    int id;
    int exercise_time;
    int exercise;
    int rest;
    int set;
    int round;
    int round_reset;
    String regDate;
    boolean delete;

    public RecordDTO(int id, int exercise_time, int exercise, int rest, int set, int round, int round_reset, String regDate, boolean delete) {
        this.id = id;
        this.exercise_time = exercise_time;
        this.exercise = exercise;
        this.rest = rest;
        this.set = set;
        this.round = round;
        this.round_reset = round_reset;
        this.regDate = regDate;
        this.delete = delete;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getExercise_time() {
        return exercise_time;
    }

    public void setExercise_time(int exercise_time) {
        this.exercise_time = exercise_time;
    }

    public int getExercise() {
        return exercise;
    }

    public void setExercise(int exercise) {
        this.exercise = exercise;
    }

    public int getRest() {
        return rest;
    }

    public void setRest(int rest) {
        this.rest = rest;
    }

    public int getSet() {
        return set;
    }

    public void setSet(int set) {
        this.set = set;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public int getRound_reset() {
        return round_reset;
    }

    public void setRound_reset(int round_reset) {
        this.round_reset = round_reset;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }
}
