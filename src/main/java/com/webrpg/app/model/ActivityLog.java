package com.webrpg.app.model;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "activity_log", schema = "gameworld", catalog = "game_test")
public class ActivityLog {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;
    @Basic
    @Column(name = "message", nullable = true, length = -1)
    private String message;
    @Basic
    @Column(name = "sys_time", nullable = true)
    private Timestamp sysTime;
    @Basic
    @Column(name = "game_time", nullable = true)
    private Long gameTime;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public Timestamp getSysTime() {
        return sysTime;
    }
    public void setSysTime(Timestamp sysTime) {
        this.sysTime = sysTime;
    }

    public Long getGameTime() {
        return gameTime;
    }
    public void setGameTime(Long gameTime) {
        this.gameTime = gameTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ActivityLog that = (ActivityLog) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (message != null ? !message.equals(that.message) : that.message != null) return false;
        if (sysTime != null ? !sysTime.equals(that.sysTime) : that.sysTime != null) return false;
        if (gameTime != null ? !gameTime.equals(that.gameTime) : that.gameTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (sysTime != null ? sysTime.hashCode() : 0);
        result = 31 * result + (gameTime != null ? gameTime.hashCode() : 0);
        return result;
    }
}
