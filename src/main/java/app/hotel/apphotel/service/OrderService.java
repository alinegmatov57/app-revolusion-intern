package app.hotel.apphotel.service;

import app.hotel.apphotel.entity.Hotel;
import app.hotel.apphotel.entity.Order;
import app.hotel.apphotel.entity.Room;
import app.hotel.apphotel.entity.enums.ORDER_STATUS;
import app.hotel.apphotel.entity.enums.STATUS;
import app.hotel.apphotel.payload.ApiResponse;
import app.hotel.apphotel.repository.HotelRepository;
import app.hotel.apphotel.repository.OrderRepository;
import app.hotel.apphotel.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final HotelRepository hotelRepository;

    private final OrderRepository orderRepository;

    private final RoomRepository roomRepository;

    public OrderService(HotelRepository hotelRepository, OrderRepository orderRepository, RoomRepository roomRepository) {
        this.hotelRepository = hotelRepository;
        this.orderRepository = orderRepository;
        this.roomRepository = roomRepository;
    }

    public ApiResponse createOrder(Order order){
        Optional<Room> byNumber = roomRepository.findByNumber(order.getRoomNumber());
        Optional<Hotel> hotel = hotelRepository.findById(order.getHotel().getId());
        List<Room> rooms= (List<Room>) byNumber.get();
        for (Room room : rooms) {
            if (hotel.isPresent() && room.getHotel().getId().equals(hotel.get().getId()) ){
                Order hotelId = orderRepository.findByRoomNumberAndHotel_Id(order.getRoomNumber(), order.getHotel().getId());
                    if (checkDate(order.getCheckIn(),order.getCheckOut(),hotelId.getCheckIn(),hotelId.getCheckOut())) {
                        saveOrder(order, room,STATUS.EMPTY);
                        return new ApiResponse("Success!",200);
                    }
                    else if (order.getCheckIn().equals(LocalDate.now())){
                        saveOrder(order,room,STATUS.BOOKED);
                        return new ApiResponse("Success",200);
                    }
                    return new ApiResponse("You can not order this time",500);
                }
                return new ApiResponse("Room is not empty this time",500);
            }
            return new ApiResponse("hotel is not present or in this hotel doesnt have room by this number",500);
    }

    private void saveOrder(Order reqOrder, Room room,STATUS status) {
        Order mainOrder=new Order();
        Room room1=new Room();
        mainOrder.setHotel(reqOrder.getHotel());
        mainOrder.setUser(reqOrder.getUser());
        mainOrder.setRoomNumber(room.getNumber());
        mainOrder.setCheckIn(reqOrder.getCheckIn());
        mainOrder.setCheckOut(reqOrder.getCheckOut());
        room1.setNumber(room.getId());
        room1.setRoom_type(room.getRoom_type());
        room1.setPrice(room.getPrice());
        room1.setHotel(room.getHotel());
        room1.setStatus(status);
        mainOrder.setOrder_status(ORDER_STATUS.PENDING);
        roomRepository.delete(room);
        roomRepository.save(room1);
        orderRepository.save(mainOrder);
    }
    public boolean checkDate(LocalDate newCheck,LocalDate newCheckOut,LocalDate checkIn,LocalDate checkOut) {
        return (newCheck.isAfter(LocalDate.now()) && newCheckOut.isBefore(checkIn)) || newCheck.isAfter(checkOut);
    }
}
