package com.example.examenfinal.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity
public class SyncInfo extends Auditable{
    @PrimaryKey
    @NonNull
    private String entity;
    private boolean isDirty;
    public SyncInfo(){
       super();
    }

    public @NonNull String getEntity() {
        return entity;
    }

    public void setEntity(@NonNull String entity) {
        this.entity = entity;
    }

    public boolean isDirty() {
        return isDirty;
    }

    public void setDirty(boolean dirty) {
        isDirty = dirty;
    }

    public boolean isOf(Class<?> classR){
        return Objects.equals(classR.getSimpleName().toLowerCase(), entity);
    }
}
