package app.hotel.apphotel.service;

import app.hotel.apphotel.entity.Order;
import app.hotel.apphotel.entity.Payment;
import app.hotel.apphotel.entity.Room;
import app.hotel.apphotel.entity.enums.PAYMENT_STATUS;
import app.hotel.apphotel.payload.ApiResponse;
import app.hotel.apphotel.repository.OrderRepository;
import app.hotel.apphotel.repository.PaymentRepository;
import app.hotel.apphotel.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    private final RoomRepository repository;
    private final OrderRepository orderRepository;

    public PaymentService(PaymentRepository paymentRepository, RoomRepository repository, OrderRepository orderRepository) {
        this.paymentRepository = paymentRepository;
        this.repository = repository;
        this.orderRepository = orderRepository;
    }

   public ApiResponse pay(Payment payment){
        Payment pay=new Payment();
       Optional<Order> byId = orderRepository.findById(payment.getOrder().getId());
       if (byId.isPresent()){
           Optional<Room> byNumber = repository.findByNumber(byId.get().getRoomNumber());
           if (byNumber.get().getHotel().equals(byId.get().getHotel())){
               if (byNumber.get().getPrice()<= payment.getAmount()) {
                   pay.setOrder(byId.get());
                   pay.setAmount(payment.getAmount());
                   pay.setPayment_status(PAYMENT_STATUS.PAID);
                   pay.setPaymentDate(LocalDate.now());
                   paymentRepository.save(pay);
                   return new ApiResponse("Successfully paid",200);
               }
               return new ApiResponse("Money doesn't enough for this order",500);
           }
       }
       return new ApiResponse("order not found",404);
   }
}
