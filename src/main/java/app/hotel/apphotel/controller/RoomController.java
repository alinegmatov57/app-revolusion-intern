package app.hotel.apphotel.controller;

import app.hotel.apphotel.entity.Room;

import app.hotel.apphotel.service.RoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/room")
public class RoomController {
    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addRoom(@RequestBody Room room){
        return ResponseEntity.ok(roomService.addRoom(room));
    }

    @DeleteMapping()
    public ResponseEntity<?> delete(@RequestBody Integer hotelId,Integer roomNumber){
        return ResponseEntity.ok(roomService.deleteRoom(hotelId,roomNumber));
    }

    @GetMapping("/{number}")
    public ResponseEntity<?> getById(@PathVariable Integer number){
        return ResponseEntity.ok(roomService.getRoomById(number));
    }

    @GetMapping("/{hotelId}")
    public ResponseEntity<?> getByHotelId(@PathVariable Integer hotelId){
        return ResponseEntity.ok(roomService.getAllRoomsByHotel(hotelId));
    }

    @PutMapping("/update/{number}")
    public ResponseEntity<?> update(@PathVariable Integer number,@RequestParam Integer hotelId,@RequestBody Room room){
        return ResponseEntity.ok(roomService.updateRoom(number,hotelId,room));
    }
}
