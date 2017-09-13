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
    @Column public int subType;
    @Column public int sets;
    @Column public int reps;
    @Column public int setDuration;
    @Column public int restDuration;
    @Column public boolean favorite;
    @Column public String notes;

    public Exercise() {}

    public Exercise(int type, String name) {
        this.type = type;
        this.name = name;
        subType = 1;
        sets = 3;
        reps = 10;
        setDuration = 30;
        restDuration = 60;
        favorite = false;
    }

    public Exercise(int type, int subType, String name) {
        this.type = type;
        this.subType = subType;
        this.name = name;
        sets = 3;
        reps = 10;
        setDuration = 30;
        restDuration = 60;
        favorite = false;
    }
}
