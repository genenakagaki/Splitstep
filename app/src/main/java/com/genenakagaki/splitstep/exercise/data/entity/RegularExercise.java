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
public class RegularExercise extends BaseModel {

    @PrimaryKey
    @ForeignKey(tableClass = Exercise.class,
            references = {@ForeignKeyReference(columnName = "id", foreignKeyColumnName = "id")})
    public long id;

    @Column(defaultValue = "1") public int subType;
    @Column(defaultValue = "1") public int reps;
    @Column(defaultValue = "1") public int sets;
    @Column(defaultValue = "1") public int restDuration;

    public RegularExercise() {}

    public RegularExercise(long id) {
        this.id = id;
    }

    public RegularExercise(long id, int subType, int reps, int sets, int restDuration) {
        this.id = id;
        this.subType = subType;
        this.reps = reps;
        this.sets = sets;
        this.restDuration = restDuration;
    }
}
