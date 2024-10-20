package app.hotel.apphotel.service;

import app.hotel.apphotel.entity.Hotel;
import app.hotel.apphotel.payload.ApiResponse;
import app.hotel.apphotel.repository.HotelRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HotelService {

    private final HotelRepository hotelRepository;

    public HotelService(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }


    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse create(Hotel hotel){
        Hotel save = hotelRepository.save(hotel);
        return new ApiResponse("ok",200,save.getName());
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse delete(Integer id){
    hotelRepository.deleteById(id);
    return new ApiResponse("Deleted",200);
    }


    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse update(Integer id,Hotel hotel){
        Optional<Hotel> byId = hotelRepository.findById(id);
        if (byId.isPresent()){
            hotel.setId(byId.get().getId());
            hotelRepository.delete(byId.get());
            hotelRepository.save(hotel);
            return new ApiResponse("Updated!",200,hotel);
        }
        return new ApiResponse("Not found",404);
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse getById(Integer id){
        Optional<Hotel> byId = hotelRepository.findById(id);
        return byId.map(hotel -> new ApiResponse(200, hotel)).orElseGet(() -> new ApiResponse("Not found", 404));
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse getAll(){
        List<Hotel> all = hotelRepository.findAll();
        return new ApiResponse(200,all);
    }

}
