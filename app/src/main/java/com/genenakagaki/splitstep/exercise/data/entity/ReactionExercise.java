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

    @PrimaryKey
    @ForeignKey(tableClass = Exercise.class,
            references = {@ForeignKeyReference(columnName = "id", foreignKeyColumnName = "id")})
    public long id;

    @Column(defaultValue = "1") public int cones;
    @Column(defaultValue = "1") public int repDuration;

    public ReactionExercise() {}

    public ReactionExercise(long id) {
        this.id = id;
    }

    public ReactionExercise(long id, int cones, int repDuration) {
        this.id = id;
        this.cones = cones;
        this.repDuration = repDuration;
    }
}

