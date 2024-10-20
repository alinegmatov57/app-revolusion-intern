package app.hotel.apphotel.entity;


import app.hotel.apphotel.entity.enums.ROOM_TYPE;
import app.hotel.apphotel.entity.enums.STATUS;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private Hotel hotel;

    @Enumerated(EnumType.STRING)
    private ROOM_TYPE room_type;


    @Column(nullable = false)
    private Integer number;

    @Column(nullable = false)
    private Double price;

    @Enumerated(EnumType.STRING)
    private STATUS status;
}
