package app.hotel.apphotel.repository;

import app.hotel.apphotel.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order,Integer> {

    Order findByRoomNumberAndHotel_Id(Integer roomNum,Integer hotelId);

}
