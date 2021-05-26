package by.bsuir.spp.model;

import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.time.ZonedDateTime;

@Data
public class TaskDto {
    private int id;
    private String task;
    private String status;
    private ZonedDateTime EndDate;
}
