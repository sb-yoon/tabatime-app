package Unlike.tabatmie.listener;

public interface ExerciseListener {

    void setReady(int ready);

    void setExercise(int set, int round, int exercise);

    void setRest(int set, int round, int rest);

    void setProgressTime(int time);
}
