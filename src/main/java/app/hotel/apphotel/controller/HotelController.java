package app.hotel.apphotel.controller;

import app.hotel.apphotel.entity.Hotel;
import app.hotel.apphotel.payload.ApiResponse;
import app.hotel.apphotel.service.HotelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hotel")
public class HotelController {

    private final HotelService hotelService;

    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createHotel(@RequestBody Hotel hotel){
        return ResponseEntity.ok(hotelService.create(hotel));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id){
        return ResponseEntity.ok(hotelService.delete(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id){
        return ResponseEntity.ok(hotelService.getById(id));
    }

    @GetMapping()
    public ResponseEntity<?> get(){
        return ResponseEntity.ok(hotelService.getAll());
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id,@RequestBody Hotel hotel){
       return ResponseEntity.ok( hotelService.update(id,hotel));
    }

}
