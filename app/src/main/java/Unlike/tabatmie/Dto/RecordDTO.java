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

    public RecordDTO(int id, int exercise_time, int exercise, int rest, int set, int round, int round_reset, String regDate) {
        this.id = id;
        this.exercise_time = exercise_time;
        this.exercise = exercise;
        this.rest = rest;
        this.set = set;
        this.round = round;
        this.round_reset = round_reset;
        this.regDate = regDate;
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

    public int getExercise() {
        return exercise;
    }

    public int getRest() {
        return rest;
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

    public int getRound_reset() {
        return round_reset;
    }

    public String getRegDate() {
        return regDate;
    }
}
