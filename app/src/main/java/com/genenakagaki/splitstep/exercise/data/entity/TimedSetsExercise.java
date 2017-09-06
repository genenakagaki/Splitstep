package com.genenakagaki.splitstep.exercise.data.entity;

import com.genenakagaki.splitstep.exercise.data.ExerciseDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.ForeignKeyReference;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by gene on 4/18/17.
 */


@Table(database = ExerciseDatabase.class)
public class TimedSetsExercise extends BaseModel {

    public static final String TABLE_NAME = "timed_sets_exercise";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_SETS = "sets";
    public static final String COLUMN_SET_DURATION = "set_duration";
    public static final String COLUMN_REST_DURATION = "rest_duration";

    @PrimaryKey
    @ForeignKey(tableClass = Exercise.class,
            references = {@ForeignKeyReference(columnName = "id", foreignKeyColumnName = "id")})
    public long id;

    @Column(defaultValue = "1") public int sets;
    @Column(defaultValue = "1") public int setDuration;
    @Column(defaultValue = "1") public int restDuration;

    public TimedSetsExercise() {}

    public TimedSetsExercise(long id) {
        this.id = id;
    }

    public TimedSetsExercise(long id, int sets, int setDuration, int restDuration) {
        this.id = id;
        this.sets = sets;
        this.setDuration = setDuration;
        this.restDuration = restDuration;
    }
}

