package app.hotel.apphotel.service;

import app.hotel.apphotel.entity.Hotel;
import app.hotel.apphotel.entity.Room;
import app.hotel.apphotel.entity.enums.STATUS;
import app.hotel.apphotel.payload.ApiResponse;
import app.hotel.apphotel.repository.HotelRepository;
import app.hotel.apphotel.repository.RoomRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RoomService {

    private final RoomRepository repository;

    private  final HotelRepository hotelRepository;

    public RoomService(RoomRepository repository, HotelRepository hotelRepository) {
        this.repository = repository;
        this.hotelRepository = hotelRepository;
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse addRoom(Room room){
        Room roomSave=new Room();
        Optional<Hotel> byId = hotelRepository.findById(room.getHotel().getId());
        if (byId.isPresent()){
            Optional<Room> roomRepo = repository.findByNumber(room.getNumber());
            if (roomRepo.isPresent()){
                if (!roomRepo.get().getHotel().getId().equals(room.getHotel().getId())){
                    if (repository.findByNumber(room.getNumber()).isEmpty()){
                        roomSave.setNumber(room.getNumber());
                        roomSave.setRoom_type(room.getRoom_type());
                        roomSave.setHotel(room.getHotel());
                        roomSave.setStatus(STATUS.EMPTY);
                        roomSave.setPrice(roomSave.getPrice());
                        repository.save(roomSave);
                        return new ApiResponse("ok",200);
                    }
                    return new ApiResponse("In this hotel has room with this number",500);
                }else {
                    return new ApiResponse("This room number already exist!",500);
                }
            }else {
                roomSave.setNumber(room.getNumber());
                roomSave.setRoom_type(room.getRoom_type());
                roomSave.setHotel(room.getHotel());
                roomSave.setStatus(STATUS.EMPTY);
                roomSave.setPrice(roomSave.getPrice());
                repository.save(roomSave);
                return new ApiResponse("ok", 200);
            }
        }
        return new ApiResponse("Hotel not found!",404);
    }


    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse deleteRoom(Integer hotelId,Integer roomNumber){
        Optional<Room> byNumber = repository.findByNumber(roomNumber);
        if (byNumber.isPresent()){
            if (byNumber.get().getHotel().getId().equals(hotelId)){
                repository.delete(byNumber.get());
                return new ApiResponse("Deleted!",200);
            }
            return new ApiResponse("In this hotel doesn't have room by this number!",404);
        }
        return new ApiResponse("Room isn't exist!",404);
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse getAllRoomsByHotel(Integer hotelId){
        List<Room> hotelRooms=new ArrayList<>();
        List<Room> all = repository.findAll();
        for (Room room : all) {
            if (room.getHotel().getId().equals(hotelId)){
                hotelRooms.add(room);
            }
        }
        return new ApiResponse(200,hotelRooms);
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse getRoomById(Integer roomNumber){
        Optional<Room> byId = repository.findByNumber(roomNumber);
        List<Room> rooms= (List<Room>) byId.get();
        return new ApiResponse(200,rooms);
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse updateRoom(Integer roomNumber, Integer hotelId, Room room){
        Room save=new Room();
        Optional<Room> byNumber = repository.findByNumber(roomNumber);
        if (byNumber.isPresent() && byNumber.get().getHotel().getId().equals(hotelId)){
            Room oldRoom=byNumber.get();
            save.setId(oldRoom.getId());
            save.setRoom_type(room.getRoom_type());
            save.setHotel(room.getHotel());
            if (repository.findByNumber(room.getNumber()).isEmpty()){
                save.setNumber(room.getNumber());
                save.setPrice(room.getPrice());
                save.setStatus(room.getStatus());
                repository.delete(oldRoom);
                repository.save(save);
                return new ApiResponse("Success",200);
            }
            return new ApiResponse("In this hotel has room with this number",500);
        }
        return new ApiResponse("Room isn't exist",500);
    }
}
