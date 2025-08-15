package com.pl.premier_zone.player;
//The controller is the entry point for a client (browser, Postman, mobile app).
//It listens for specific HTTP requests (GET, POST, PUT, DELETE) on specific URLs
//The controller doesn’t do the business logic itself.
//Instead, it calls the service (e.g., PlayerService) to do the real work.
//This keeps the controller simple and focused on HTTP concerns.
//@Controller → marks this class as a Spring MVC controller.
//@ResponseBody → tells Spring to return data directly as JSON or XML, instead of rendering an HTML page.
//Whatever I return from this method, send it straight back to the client as the HTTP response body — don’t try to find an HTML view.”
//REST = Representational State Transfer
//It’s a way of designing web APIs so that:
//You use HTTP verbs to represent actions:
//GET → read data
//POST → create data
//PUT → update data
//DELETE → delete data
//You work with resources (like players, teams, etc.).
//Each resource has its own URL, like /players/5.
//No HTML rendering — just raw data in the HTTP response.
//When you add @ResponseBody, you’re telling Spring:
//“Don’t try to interpret my return value as a view name — just take it and put it straight into the response body that will be sent to the client.”

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
//shorthand for @controller and @responsebody combined 
@RestController
@RequestMapping(path = "api/v1/player")
public class PlayerController {

    public final PlayerService playerService;

    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;}


       //requestparam : Look in the query string for this key, and give me its value so I can use it in my code.”
       //not required means that the user does not have to have this in their URL the code will still run
    
    
    @GetMapping
    public List<Player>  getPlayers(@RequestParam(required = false) String name,
    @RequestParam(required = false) String team,
    @RequestParam(required = false) String position,
    @RequestParam(required = false) String nation) {


        //his now we want to write the logic which is the method is going to return a different player list based on the
        //presence of these query parameters so so if no parameters are provided it's just going to return all the players
        if(team != null && position != null) {
            return playerService.getPlayersByTeamAndPosition(team, position);
        }

        else if(team != null){
            return playerService.getPlayersFromTeam(team);
        }

        else if(name != null){ 
            return playerService.getPlayerByName(name);
        }
        
        else{
            return playerService.getPlayers();
    }


       //<Player> tells ResponseEntity what type of object its body will contain, so the compiler can keep you safe and you don’t need manual type checks or casts later.
        }

        
    

    //<Player> tells ResponseEntity what type of object its body will contain, so the compiler can keep you safe and you don’t need manual type checks or casts later.

    @PostMapping
    public ResponseEntity<Player> addPlayer(@RequestBody Player player){

        Player createdPlayer = playerService.addPlayer(player);
        return new ResponseEntity<Player>(createdPlayer, HttpStatus.CREATED);

       //HttpStatus in Spring is an enum — and enums in Java can also use dot notation to access their predefined constants.
}


    @PutMapping
    public  ResponseEntity<Player> updatePlayer(@RequestBody Player player) {

        Player updatedPlayer = playerService.updatePlayer(player);
        if(updatedPlayer != null) {
            return new ResponseEntity<Player>(updatedPlayer, HttpStatus.OK);
        }

        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }}

    @DeleteMapping("/{playerName}")
    public ResponseEntity<String > deletePlayer(@PathVariable String playerName) {
        
        playerService.deletePlayer(playerName);

        return new ResponseEntity<>("Player deleted succesfully", HttpStatus.OK);
    }

    


    
}
