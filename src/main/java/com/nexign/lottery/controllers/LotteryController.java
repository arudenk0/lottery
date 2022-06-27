package com.nexign.lottery.controllers;

import com.nexign.lottery.dtos.ParticipantDto;
import com.nexign.lottery.entities.Participant;
import com.nexign.lottery.entities.Winner;
import com.nexign.lottery.services.ParticipantService;
import com.nexign.lottery.services.WinnerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/lottery")
public class LotteryController {

    private final ParticipantService participantService;
    private final WinnerService winnerService;

    public LotteryController(ParticipantService participantService, WinnerService winnerService) {
        this.participantService = participantService;
        this.winnerService = winnerService;
    }

    /**
     * Add new participant
     * @param participantDto participant json
     * @return Participant that was just added in database
     */
    @PostMapping(value = "participant")
    public ResponseEntity<Participant> participant(@Valid @RequestBody ParticipantDto participantDto) {
        var participant = participantService.addParticipant(participantDto);
        return new ResponseEntity<>(participant, HttpStatus.CREATED);
    }

    /**
     * Get all participants
     * @return all participants from database
     */
    @GetMapping(value = "participant")
    public ResponseEntity<Iterable<Participant>> participant() {
        var participants = participantService.getAll();
        return new ResponseEntity<>(participants, HttpStatus.OK);
    }

    /**
     * Start lottery and get winner
     * @return winner
     */
    @GetMapping(value = "start")
    public ResponseEntity<Winner> start() {
        var participants = participantService.getAll();
        var list = StreamSupport.stream(participants.spliterator(), true).toList();
        int participantsCount = list.size();
        if (participantsCount < 2) {
            return new ResponseEntity("Not enough participants. Should be at least 2, but now only " + list.size() + ".", HttpStatus.BAD_REQUEST);
        }
        int amount = generateAmount();
        int winnerNumber = new Random().nextInt(participantsCount);
        var winnerParticipant = list.get(winnerNumber);
        var winner = winnerService.addWinner(new Winner(winnerParticipant, amount));
        participantService.CleanDb();
        return new ResponseEntity<>(winner, HttpStatus.OK);
    }

    /**
     * Generate winner amount
     * @return amount from 1 to 1000
     */
    private int generateAmount() {
        try {
            return getRnd();
        } catch (Exception ex) {
            return 1 + new Random().nextInt(1000);
        }
    }

    /**
     * Trying to get random integer from http request
     * @return random integer
     * @throws URISyntaxException
     * @throws IOException
     * @throws InterruptedException
     */
    private int getRnd() throws URISyntaxException, IOException, InterruptedException {
        var request = HttpRequest.newBuilder()
                .uri(new URI("https://www.random.org/integers/?num=1&min=1&max=1000&col=1&base=10&format=plain&rnd=new"))
                .GET()
                .timeout(Duration.ofSeconds(2))
                .build();

        var response = HttpClient
                .newBuilder()
                .build()
                .send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == HttpStatus.OK.value()) {
            return Integer.parseInt(response.body().trim());
        } else {
            throw new ResponseStatusException(HttpStatus.valueOf(response.statusCode()));
        }
    }

    /**
     * Get all winners
     * @return all winners from database
     */
    @GetMapping(value = "winners")
    public ResponseEntity<Iterable<Winner>> winners() {
        var winners = winnerService.getAll();
        return new ResponseEntity<>(winners, HttpStatus.OK);
    }

    /**
     * Catch validation errors
     * @param ex MethodArgumentNotValidException
     * @return validation errors
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    /**
     * JSON parse error
     * @return error description
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public String handleHttpMessageNotReadableException() {
        return "JSON parse error";
    }

}
