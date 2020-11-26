package com.webrpg.app.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Activity {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;
    @Basic
    @Column(name = "name", nullable = true, length = 60)
    private String name;
    /*
    Instead of dealing with date fields, we will be saving time in the game
    as elapses time from a past time that starts at 0.. The time will increment at some
    small time interval, most likely in seconds since most games don't track actions
    that fall below this amount of time
     */
    @Basic
    @Column(name = "start_date", nullable = true)
    private Long startDate;
    @Basic
    @Column(name = "end_date", nullable = true)
    private Long endDate;
    /*
    Start and end time will be the same as for start date.  However, this will
    correspond to the 24 hour clock,
     */
    @Basic
    @Column(name = "start_time", nullable = true)
    private Long startTime;
    @Basic
    @Column(name = "end_time", nullable = true)
    private Long endTime;
    @Basic
    @Column(name = "recurrence", nullable = true)
    private Long recurrence;


    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Long getStartDate() {
        return startDate;
    }
    public void setStartDate(Long startDate) {
        this.startDate = startDate;
    }

    public Long getEndDate() {
        return endDate;
    }
    public void setEndDate(Long endDate) {
        this.endDate = endDate;
    }

    public Long getStartTime() {
        return startTime;
    }
    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }
    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }


    public Long getRecurrence() {
        return recurrence;
    }
    public void setRecurrence(Long recurrence) {
        this.recurrence = recurrence;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Activity activity = (Activity) o;

        if (id != null ? !id.equals(activity.id) : activity.id != null) return false;
        if (name != null ? !name.equals(activity.name) : activity.name != null) return false;
        if (startDate != null ? !startDate.equals(activity.startDate) : activity.startDate != null) return false;
        if (endDate != null ? !endDate.equals(activity.endDate) : activity.endDate != null) return false;
        if (startTime != null ? !startTime.equals(activity.startTime) : activity.startTime != null) return false;
        if (endTime != null ? !endTime.equals(activity.endTime) : activity.endTime != null) return false;
        if (recurrence != null ? !recurrence.equals(activity.recurrence) : activity.recurrence != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
        result = 31 * result + (startTime != null ? startTime.hashCode() : 0);
        result = 31 * result + (endTime != null ? endTime.hashCode() : 0);
        result = 31 * result + (recurrence != null ? recurrence.hashCode() : 0);
        return result;
    }
}
