package com.genenakagaki.splitstep.exercise.data.entity;

import com.genenakagaki.splitstep.exercise.data.ExerciseDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.ForeignKeyReference;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by gene on 7/25/17.
 */

@Table(database = ExerciseDatabase.class)
public class RepsExercise extends BaseModel {

    public static final String TABLE_NAME = "reps_exercise";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_REPS = "reps";
    public static final String COLUMN_SETS = "sets";
    public static final String COLUMN_REST_DURATION = "rest_duration";

    @PrimaryKey
    @ForeignKey(tableClass = Exercise.class,
            references = {@ForeignKeyReference(columnName = "id", foreignKeyColumnName = "id")})
    public long id;

    @Column(defaultValue = "1") public int reps;
    @Column(defaultValue = "1") public int sets;
    @Column(defaultValue = "1") public int restDuration;

    public RepsExercise() {}

    public RepsExercise(long id) {
        this.id = id;
    }

    public RepsExercise(long id, int reps, int sets, int restDuration) {
        this.id = id;
        this.reps = reps;
        this.sets = sets;
        this.restDuration = restDuration;
    }
}
