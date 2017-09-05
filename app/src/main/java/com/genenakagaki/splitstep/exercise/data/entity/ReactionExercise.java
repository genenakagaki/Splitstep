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
public class ReactionExercise extends BaseModel {

    public static final String TABLE_NAME = "reaction_exercise";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_REPS = "reps";
    public static final String COLUMN_SETS = "sets";
    public static final String COLUMN_CONES = "cones";
    public static final String COLUMN_REP_DURATION = "rep_duration";
    public static final String COLUMN_REST_DURATION = "rest_duration";

    @PrimaryKey
    @ForeignKey(tableClass = Exercise.class,
            references = {@ForeignKeyReference(columnName = "id", foreignKeyColumnName = "id")})
    public long id;

    @Column(defaultValue = "1") public int reps;
    @Column(defaultValue = "1") public int sets;
    @Column(defaultValue = "1") public int cones;
    @Column(defaultValue = "1") public int repDuration;
    @Column(defaultValue = "1") public int restDuration;

    public ReactionExercise() {}

    public ReactionExercise(long id, int reps, int sets, int cones, int repDuration, int restDuration) {
        this.id = id;
        this.reps = reps;
        this.sets = sets;
        this.cones = cones;
        this.repDuration = repDuration;
        this.restDuration = restDuration;
    }
}

