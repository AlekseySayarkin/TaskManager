package by.bsuir.spp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "Statuses", schema = "Tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Status {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "Status")
    @Enumerated(EnumType.STRING)
    private StatusType status;

    public enum StatusType {
        ACTIVE, FINISHED, POSTPONED
    }
}
