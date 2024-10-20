package app.hotel.apphotel.entity;

import app.hotel.apphotel.entity.enums.PAYMENT_STATUS;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    private Order order;

    private Double amount;

    private LocalDate paymentDate;

    @Enumerated(EnumType.STRING)
    private PAYMENT_STATUS payment_status;


}
