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

    public static final String TABLE_NAME = "exercise";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_FAVORITE = "favorite";

    @PrimaryKey(autoincrement = true)
    public long id;

    @Column public int type;
    @Column public String name;

    @Column(defaultValue = "0")
    public boolean favorite;

    public Exercise() {}

    public Exercise(int type, String name, boolean favorite) {
        this.type = type;
        this.name = name;
        this.favorite = favorite;
    }
}
