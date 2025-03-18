package org.local.meeting.Models.Dao;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "Users")
public class UserA {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String login;
    String email;
   // @Column(columnDefinition = "geometry(Point,4326)")
    //private Point location;
    String password;
}
