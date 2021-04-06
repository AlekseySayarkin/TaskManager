package by.bsuir.spp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "Tasks", schema = "Tasks")
@Data
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "Task")
    private String task;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "StatusId", referencedColumnName = "Id")
    private Status status;

    @Column(name = "EndDate")
    private ZonedDateTime EndDate;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "FileId", referencedColumnName = "Id")
    private File file;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "UserId")
    @JsonIgnore
    private User user;
}
