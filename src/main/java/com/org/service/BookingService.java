package com.org.service;

import com.org.Models.*;
import com.org.dto.*;
import com.org.exception.ResourceNotFoundException;
import com.org.exception.SeatUnavailableException;
import com.org.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BookingService {

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private ShowSeatRepository showSeatRepository;

    @Transactional
    public BookingDto createBooking(BookingRequestDto bookingDto){

        User user = userRepository.findById(bookingDto.getUserId())
                .orElseThrow(()->new ResourceNotFoundException("User Not Found...."));
        Show show = showRepository.findById(bookingDto.getShowId())
                .orElseThrow(()->new ResourceNotFoundException("Show Not Found..."));

        List<ShowSeats> selectedSeat = showSeatRepository.findAllById(bookingDto.getSeatIDs());
        for(ShowSeats seat : selectedSeat){
            if(!"AVAILABLE".equals(seat.getStatus())){
                throw new SeatUnavailableException("Seat NO : "+seat.getSeat().getSeatNumber()
                +" Unavailable");
            }
            seat.setStatus("LOCKED");
        }
        showSeatRepository.saveAll(selectedSeat);

        Double totalAmount = selectedSeat.stream().mapToDouble(ShowSeats::getPrice).sum();
        Payment payment = new Payment();
        payment.setAmount(totalAmount);
        payment.setPaymentMethod(bookingDto.getPaymentMethod());
        payment.setStatus("SUCCESS");
        payment.setPaymentTime(LocalDateTime.now());
        payment.setTransactionId(UUID.randomUUID().toString());

        Booking myBooking = new Booking();
        myBooking.setBookingNumber(UUID.randomUUID().toString());
        myBooking.setBookingTime(LocalDateTime.now());
        myBooking.setStatus("CONFIRMED");
        myBooking.setShow(show);
        myBooking.setUser(user);
        myBooking.setTotalAmount(totalAmount);
        myBooking.setPayment(payment);
        Booking savedBooking = bookingRepository.save(myBooking);

        selectedSeat.forEach(
                seat->{
                    seat.setStatus("BOOKED");
                    seat.setBooking(savedBooking);
                }
        );

        showSeatRepository.saveAll(selectedSeat);
        return mappingToBookingDto(savedBooking,selectedSeat);
    }

    public BookingDto getBookingById(Long id){
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Booking is not found..."));

        List<ShowSeats>showSeatsList = showSeatRepository.findAll()
                .stream()
                .filter(seat->
                    seat.getBooking()!=null && seat.getBooking().getId().equals(booking.getId())
                ).collect(Collectors.toList());

        return mappingToBookingDto(booking,showSeatsList);

    }

    public BookingDto getBookingByNumber(String number){
        Booking booking = bookingRepository.findByBookingNumber(number)
                .orElseThrow(()-> new ResourceNotFoundException("Booking is not found..."));

        List<ShowSeats>showSeatsList = showSeatRepository.findAll()
                .stream()
                .filter(seat->
                        seat.getBooking()!=null && seat.getBooking().getId().equals(booking.getId())
                ).collect(Collectors.toList());

        return mappingToBookingDto(booking,showSeatsList);

    }

    public List<BookingDto> getBookingByUserId(Long id){
        List<Booking> bookingList = bookingRepository.findByUserId(id);



        return bookingList
                .stream()
                .map(booking -> {
                    List<ShowSeats>showSeatsList = showSeatRepository.findAll()
                    .stream()
                    .filter(seat->
                            seat.getBooking()!=null && seat.getBooking().getId().equals(booking.getId())
                    ).collect(Collectors.toList());
                    return mappingToBookingDto(booking,showSeatsList);
        }).collect(Collectors.toList());

    }

    @Transactional
    public BookingDto cancelBooking(Long id){
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Booking not found...."));
        booking.setStatus("CANCELLED");

        List<ShowSeats>showSeatsList = showSeatRepository.findAll()
                .stream()
                .filter(seat->
                        seat.getBooking()!=null && seat.getBooking().getId().equals(booking.getId())
                ).collect(Collectors.toList());

        showSeatsList.forEach(showSeat -> {
                showSeat.setStatus("AVAILABLE");
                showSeat.setBooking(null);
        });

        if(booking.getPayment()!=null){
            booking.getPayment().setStatus("REFUNDED");
        }
        Booking updatedBooking = bookingRepository.save(booking);
        showSeatRepository.saveAll(showSeatsList);
        return mappingToBookingDto(updatedBooking,showSeatsList);
    }


    private BookingDto mappingToBookingDto(Booking booking,List<ShowSeats>seats){

        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(booking.getId());
        bookingDto.setBookingNumber(booking.getBookingNumber());
        bookingDto.setBookingTime(booking.getBookingTime());
        bookingDto.setStatus(booking.getStatus());
        bookingDto.setTotalAmount(booking.getTotalAmount());

        UserDto userDto = new UserDto();
        userDto.setId(booking.getUser().getId());
        userDto.setName(booking.getUser().getName());
        userDto.setEmail(booking.getUser().getEmail());
        userDto.setPhoneNumber(booking.getUser().getPhoneNumber());

        bookingDto.setUserDto(userDto);

        ShowDto showDto = new ShowDto();
        showDto.setId(booking.getShow().getId());
        showDto.setStartTime(booking.getShow().getStartTime());
        showDto.setEndTime(booking.getShow().getEndTime());

        MovieDto movieDto = new MovieDto();
        movieDto.setId(booking.getShow().getMovie().getId());
        movieDto.setDuration(booking.getShow().getMovie().getDurationMin());
        movieDto.setDescription(booking.getShow().getMovie().getDescription());
        movieDto.setGenre(booking.getShow().getMovie().getGenre());
        movieDto.setLanguage(booking.getShow().getMovie().getLanguage());
        movieDto.setTitle(booking.getShow().getMovie().getTitle());
        movieDto.setPosterUrl(booking.getShow().getMovie().getPosterUrl());
        movieDto.setReleaseDate(booking.getShow().getMovie().getReleaseDate());

        showDto.setMovieDto(movieDto);

        ScreenDto screenDto = new ScreenDto();
        screenDto.setId(booking.getShow().getScreen().getId());
        screenDto.setName(booking.getShow().getScreen().getName());
        screenDto.setTotalSeat(booking.getShow().getScreen().getTotalSeats());

        TheaterDto theaterDto = new TheaterDto();
        theaterDto.setId(booking.getShow().getScreen().getTheater().getId());
        theaterDto.setName(booking.getShow().getScreen().getTheater().getName());
        theaterDto.setCity(booking.getShow().getScreen().getTheater().getCity());
        theaterDto.setAddress(booking.getShow().getScreen().getTheater().getAddress());
        theaterDto.setTotalScreens(booking.getShow().getScreen().getTheater().getTotalScreen());

        screenDto.setTheaterDto(theaterDto);

        showDto.setScreenDto(screenDto);

        bookingDto.setShowDto(showDto);

        List<ShowSeatDto> showSeatDtoList =seats.stream().map(
                seat->{
                    ShowSeatDto showSeatDto = new ShowSeatDto();
                    showSeatDto.setId(seat.getId());
                    showSeatDto.setStatus(seat.getStatus());
                    showSeatDto.setPrice(seat.getPrice());
                        // seat dto in seat itself
                    SeatDto baseSeatDto = new SeatDto();
                    baseSeatDto.setId(seat.getSeat().getId());
                    baseSeatDto.setBasePrice(seat.getSeat().getBasePrice());
                    baseSeatDto.setSeatNumber(seat.getSeat().getSeatNumber());
                    baseSeatDto.setSeatType(seat.getSeat().getSeatType());
                    showSeatDto.setSeatDto(baseSeatDto);
                    return showSeatDto;
                })
                .collect(Collectors.toList());

        bookingDto.setShowSeatDtos(showSeatDtoList);

        if(booking.getPayment()!=null){
            PaymentDto paymentDto = new PaymentDto();
            paymentDto.setId(booking.getPayment().getId());
            paymentDto.setStatus(booking.getPayment().getStatus());
            paymentDto.setPaymentMethod(booking.getPayment().getPaymentMethod());
            paymentDto.setPaymentTime(booking.getPayment().getPaymentTime());
            paymentDto.setAmount(booking.getPayment().getAmount());
            paymentDto.setTransactionID(booking.getPayment().getTransactionId());
            bookingDto.setPaymentDto(paymentDto);
        }

        return bookingDto;
    }
}
