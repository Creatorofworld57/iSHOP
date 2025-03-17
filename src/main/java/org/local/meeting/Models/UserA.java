package org.local.meeting.Models;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.geo.Point;

@Entity
@Data
public class UserA {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;
    String email;
   // @Column(columnDefinition = "geometry(Point,4326)")
    //private Point location;
    String password;
}
