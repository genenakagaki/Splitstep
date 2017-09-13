package com.genenakagaki.splitstep.exercise.data.entity;

import com.genenakagaki.splitstep.exercise.data.ExerciseDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by gene on 7/25/17.
 */

@Table(database = ExerciseDatabase.class)
public class Exercise extends BaseModel {

    @PrimaryKey(autoincrement = true)
    public long id;

    @Column public int type;
    @Column public String name;
    @Column(defaultValue = "1") public int subType;
    @Column(defaultValue = "1") public int reps;
    @Column(defaultValue = "1") public int sets;
    @Column(defaultValue = "1") public int setDuration;
    @Column(defaultValue = "1") public int restDuration;
    @Column(defaultValue = "0") public boolean favorite;
    @Column public String notes;

    public Exercise() {}

    public Exercise(int type, String name) {
        this.type = type;
        this.name = name;
    }
}
