package by.bsuir.spp.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Users", schema = "Tasks")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "Login")
    private String login;

    @Column(name = "Password")
    private String password;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks;
}
